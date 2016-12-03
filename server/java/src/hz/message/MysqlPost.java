package hz.message;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;

import org.json.JSONObject;

/**
 * mysql信息类
 * <p>
 * mysql信息类实现对数据库表post的创建、添加、查询、修改和删除
 */
public class MysqlPost implements Post {
	/** 数据库表名 */
	private final String _table = "post";

	/** 数据库连接 */
	private Connection _conn;
	
	/**
	 * 构造函数
	 * 
	 * @param conn 数据库连接
	 * @throws Exception 错误异常
	 */
	public MysqlPost(Connection conn) throws Exception {
		// 保存数据库连接
		_conn = conn;
		
		// 创建数据库表
		Statement statement = _conn.createStatement();
		statement.execute(String.format("CREATE TABLE IF NOT EXISTS `%s` (`id` INT NOT NULL PRIMARY KEY AUTO_INCREMENT, `timestamp` INT NOT NULL, `content` TEXT NOT NULL)", _table));
		statement.close();
	}
	
	@Override
	public JSONObject getCount() throws Exception {
		// 查询数据库
		Statement statement = _conn.createStatement();
		ResultSet resultSet = statement.executeQuery(String.format("SELECT COUNT(`id`) AS count FROM `%s`", _table));
		resultSet.next();
		int count = resultSet.getInt("count");
		statement.close();
		String resultStr = String.format("{\"status\": \"ok\", \"data\": {\"count\": %d}}", count);
		return new JSONObject(resultStr);
	}
	
	@Override
	public JSONObject getAll() throws Exception {
		// 查询数据库
		Statement statement = _conn.createStatement();
		ResultSet resultSet = statement.executeQuery(String.format("SELECT `id`,`timestamp`,`content` FROM `%s` ORDER BY `id` DESC", _table));
		String cellStrs = "";
		while (resultSet.next()) {
			int id = resultSet.getInt("id");
			int timestamp = resultSet.getInt("timestamp");
			String content = resultSet.getString("content");
			String cellStr = String.format("{\"id\": %d, \"timestamp\": %d, \"content\": \"%s\"}", id, timestamp, content);
			cellStrs += (cellStrs.isEmpty() ? "" : ", ") + cellStr;
		}
		statement.close();
		String resultStr = String.format("{\"status\": \"ok\", \"data\": [%s]}", cellStrs);
		return new JSONObject(resultStr);
	}

	@Override
    public JSONObject getById(JSONObject param) throws Exception {
		// 校验id参数
		Object[] idErr = Checker.checkParamId(param);
		int id = (Integer) idErr[0];
		JSONObject err = (JSONObject) idErr[1];
		if (err != null) {
			return err;
		}
		
		// 查询数据库，如果没有数据返回错误
		Statement statement = _conn.createStatement();
		ResultSet resultSet = statement.executeQuery(String.format("SELECT `id`,`timestamp`,`content` FROM `%s` WHERE `id`=%d LIMIT 1", _table, id));
		if (!resultSet.next()) {
			statement.close();
			return new JSONObject("{\"status\": \"none_target\", \"message\": \"`id` does not exist.\"}");
		}
		int timestamp = resultSet.getInt("timestamp");
		String content = resultSet.getString("content");
		statement.close();
		String resultStr = String.format("{\"status\": \"ok\", \"data\": {\"id\": %d, \"timestamp\": %d, \"content\": \"%s\"}}", id, timestamp, content);
		return new JSONObject(resultStr);
	}

	@Override
    public JSONObject add(JSONObject param) throws Exception {
		// 校验content参数
		Object[] contentErr = Checker.checkParamContent(param);
		String content = (String) contentErr[0];
		JSONObject err = (JSONObject) contentErr[1];
		if (err != null) {
			return err;
		}

		// 获取当前系统时间
		int timestamp = (int) (new Date().getTime() / 1000);

		// 查询数据库
		Statement statement = _conn.createStatement();
		statement.executeUpdate(String.format("INSERT INTO `%s` SET `timestamp`=%d,`content`=\"%s\"", _table, timestamp, content), Statement.RETURN_GENERATED_KEYS);
		ResultSet resultSet = statement.getGeneratedKeys();
		resultSet.next();
		int id = resultSet.getInt(1);
		statement.close();
		String resultStr = String.format("{\"status\": \"ok\", \"data\": {\"id\": %d, \"timestamp\": %d, \"content\": \"%s\"}}", id, timestamp, content);
		return new JSONObject(resultStr);
	}

	@Override
    public JSONObject modify(JSONObject param) throws Exception {
		// 校验id参数
		Object[] idErr = Checker.checkParamId(param);
		int id = (Integer) idErr[0];
		JSONObject err = (JSONObject) idErr[1];
		if (err != null) {
			return err;
		}

		// 校验content参数
		Object[] contentErr = Checker.checkParamContent(param);
		String content = (String) contentErr[0];
		err = (JSONObject) contentErr[1];
		if (err != null) {
			return err;
		}
		
		// 查询数据库，如果没有数据修改返回错误
		Statement statement = _conn.createStatement();
		int rowCount = statement.executeUpdate(String.format("UPDATE `%s` SET `content`=\"%s\" WHERE `id`=%d", _table, content, id));
		statement.close();
		if (rowCount != 1) {
			return new JSONObject("{\"status\": \"none_target\", \"message\": \"`id` does not exist.\"}");
		}
		return getById(new JSONObject(String.format("{\"id\": %d}", id)));
	}

	@Override
    public JSONObject remove(JSONObject param) throws Exception {
		// 校验id参数
		Object[] idErr = Checker.checkParamId(param);
		int id = (Integer) idErr[0];
		JSONObject err = (JSONObject) idErr[1];
		if (err != null) {
			return err;
		}
		
		// 查询数据库，如果没有数据修改返回错误
		Statement statement = _conn.createStatement();
		int rowCount = statement.executeUpdate(String.format("DELETE FROM `%s` WHERE `id`=%d", _table, id));
		statement.close();
		if (rowCount != 1) {
			return new JSONObject("{\"status\": \"none_target\", \"message\": \"`id` does not exist.\"}");
		}
		String resultStr = String.format("{\"status\": \"ok\", \"data\": {\"id\": %d}}", id);
		return new JSONObject(resultStr);
	}
}
