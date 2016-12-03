package hz.message;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.Statement;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * 测试MysqlHolder
 */
public class MysqlHolderTest {
	/** mysql用户名 */
	private final String _user = "root";
	/** mysql用户密码 */
	private final String _password = "Ningning~1";
	/** mysql数据库名 */
	private final String _database = "test_message";
	
	/** holder对象 */
	private Holder _holder;
	
	@Before
	public void before() throws Exception {
		_holder = new MysqlHolder(_user, _password, _database);
	}
	
	@After
	public void after() {
		try {
			_holder.destroy();
			_holder.close();
		} catch (Exception e) {
		}
	}
	
	/**
	 * 测试MysqlHolder.inst()
	 * 
	 * @throws Exception 错误异常
	 */
	@Test
	public void test_inst() throws Exception {
		// 测试返回的对象为mysql连接对象
		Object inst = _holder.inst();
		assertTrue(inst instanceof Connection);
	}
	
	/**
	 * 测试MysqlHolder.destroy()
	 * 
	 * @throws Exception 错误异常
	 */
	@Test
	public void test_destroy() throws Exception {		
		Connection conn = (Connection) _holder.inst();

		// 测试数据库没有销毁
		try {
			Statement statement = conn.createStatement();
			statement.executeQuery("SHOW TABLES");
			statement.close();
		} catch (Exception e) {
			fail("No expect Exception.");
        }

		// 执行destroy
		_holder.destroy();
		
		// 测试数据库已经销毁
		try {
			Statement statement = conn.createStatement();
			statement.executeQuery("SHOW TABLES");
			statement.close();
			fail("Expect Exception.");
		} catch (Exception e) {
        }
	}
	
	/**
	 * 测试MysqlHolder.close()
	 * 
	 * @throws Exception 错误异常
	 */
	@Test
	public void test_close() throws Exception {
		// 测试数据库连接没有关闭
		assertFalse(((Connection) _holder.inst()).isClosed()); 
		
		// 执行close
		_holder.close();
		
		// 测试数据库连接已经关闭
		assertTrue(((Connection) _holder.inst()).isClosed()); 
	}
}
