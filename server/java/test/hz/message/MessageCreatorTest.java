package hz.message;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * 测试MessageCreator
 */
public class MessageCreatorTest {
	/** mysql用户名 */
	private final String _user = "root";
	/** mysql用户密码 */
	private final String _password = "Ningning~1";
	/** mysql数据库名 */
	private final String _database = "test_message";
	
	/**
	 * 测试MessageCreator.createMessageByMysql()
	 * 
	 * @throws Exception 错误异常
	 */
	@Test
	public void test_createMessageByMysql() throws Exception {
		// 执行createMessageByMysql
		Message message = MessageCreator.createMessageByMysql(_user, _password, _database);

		// 测试创建对象不为空
		assertNotNull(message);
	}
}
