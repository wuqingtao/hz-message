package hz.message;

import java.sql.Connection;

/**
 * message对象创建类
 */
public class MessageCreator {
	/**
	 * 创建mysql类message
	 * 
	 * @param user mysql用户名
	 * @param password mysql用户密码
	 * @param database mysql数据库名
	 * @return message对象
	 * @throws Exception 错误异常
	 */
	public static Message createMessageByMysql(String user, String password, String database) throws Exception {
		Holder holder = new MysqlHolder(user, password, database);
		Post post = new MysqlPost((Connection) holder.inst());
		return new Message(holder, post);
	}
}
