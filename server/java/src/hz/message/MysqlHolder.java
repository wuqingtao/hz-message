package hz.message;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

/**
 * mysql实体类
 * <p>
 * mysql实体类实现mysql连接和关闭，及数据库创建和销毁
 */
public class MysqlHolder implements Holder {
	/** 数据库连接 */
	private Connection _conn;
	/** 数据库名 */
	private String _database;
	
	/**
	 * 构造函数
	 * 
	 * @param user mysql用户名
	 * @param password mysql用户密码
	 * @param database mysql数据库名
	 * @throws Exception 错误异常
	 */
	public MysqlHolder(String user, String password, String database) throws Exception {
		// 定义数据库接入类和连接地址
		final String driver = "com.mysql.cj.jdbc.Driver";
		final String url = "jdbc:mysql://localhost:3306/?characterEncoding=utf8&useSSL=false";
		
		// 连接数据库
		Class.forName(driver);
		_conn = DriverManager.getConnection(url, user, password);
		
		// 保存数据库名
		_database = database;
		
		// 创建数据库
		Statement statement = _conn.createStatement();
		statement.execute(String.format("CREATE DATABASE IF NOT EXISTS `%s` default character set utf8 COLLATE utf8_general_ci", _database));
		statement.execute(String.format("USE `%s`", _database));
		statement.close();
	}
	
	@Override
	public Object inst() {
		return _conn;
	}
	
	@Override
	public void destroy() throws Exception {
		Statement statement = _conn.createStatement();
		statement.execute(String.format("DROP DATABASE IF EXISTS `%s`", _database));
		statement.close();
	}
	
	@Override
	public void close() throws Exception {
		_conn.close();
	}
}
