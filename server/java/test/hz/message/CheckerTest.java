package hz.message;

import static org.junit.Assert.*;

import org.junit.Test;
import org.json.JSONObject;

/**
 * 测试Checker
 */
public class CheckerTest {
	/**
	 * 测试Checker.checkParamType()
	 */
	@Test
	public void test_checkParamType() {
		// 测试空参数
		Object[] res = Checker.checkParamType(new JSONObject("{}"));
		assertTrue(res[0] == null && ((JSONObject) res[1]).getString("status").equals("lost_parameter"));
		
		// 测试非法key参数
		res = Checker.checkParamType(new JSONObject("{\"abcd\": \"1234\"}"));
		assertTrue(res[0] == null && ((JSONObject) res[1]).getString("status").equals("lost_parameter"));
		res = Checker.checkParamType(new JSONObject("{\"Type\": \"1234\"}"));
		assertTrue(res[0] == null && ((JSONObject) res[1]).getString("status").equals("lost_parameter"));
		
		// 测试非法value参数
		res = Checker.checkParamType(new JSONObject("{\"type\": 1234}"));
		assertTrue(res[0] == null && ((JSONObject) res[1]).getString("status").equals("invalid_parameter"));
		res = Checker.checkParamType(new JSONObject("{\"type\": \"\"}"));
		assertTrue(res[0] == null && ((JSONObject) res[1]).getString("status").equals("invalid_parameter"));
		
		// 测试合法参数
		res = Checker.checkParamType(new JSONObject("{\"type\": \"1234\"}"));
		assertTrue(res[0].equals("1234") && res[1] == null);
	}
	
	/**
	 * 测试Checker.checkParamId()
	 */
	@Test
	public void test_checkParamId() {
		// 测试空参数
		Object[] res = Checker.checkParamId(new JSONObject("{}"));
		assertTrue(res[0] == null && ((JSONObject) res[1]).getString("status").equals("lost_parameter"));
		
		// 测试非法key参数
		res = Checker.checkParamId(new JSONObject("{\"abcd\": 1234}"));
		assertTrue(res[0] == null && ((JSONObject) res[1]).getString("status").equals("lost_parameter"));
		res = Checker.checkParamId(new JSONObject("{\"Id\": 1234}"));
		assertTrue(res[0] == null && ((JSONObject) res[1]).getString("status").equals("lost_parameter"));

		// 测试非法value参数
		res = Checker.checkParamId(new JSONObject("{\"id\": \"1234\"}"));
		assertTrue(res[0] == null && ((JSONObject) res[1]).getString("status").equals("invalid_parameter"));
		
		// 测试合法参数
		res = Checker.checkParamId(new JSONObject("{\"id\": 1234}"));
		assertTrue(res[0].equals(1234) && res[1] == null);
	}
	
	/**
	 * 测试Checker.checkParamContent()
	 */
	@Test
	public void test_checkParamContent() {
		// 测试空参数
		Object[] res = Checker.checkParamContent(new JSONObject("{}"));
		assertTrue(res[0] == null && ((JSONObject) res[1]).getString("status").equals("lost_parameter"));

		// 测试非法key参数
		res = Checker.checkParamContent(new JSONObject("{\"abcd\": \"1234\"}"));
		assertTrue(res[0] == null && ((JSONObject) res[1]).getString("status").equals("lost_parameter"));
		res = Checker.checkParamContent(new JSONObject("{\"Content\": \"1234\"}"));
		assertTrue(res[0] == null && ((JSONObject) res[1]).getString("status").equals("lost_parameter"));

		// 测试非法value参数
		res = Checker.checkParamContent(new JSONObject("{\"content\": 1234}"));
		assertTrue(res[0] == null && ((JSONObject) res[1]).getString("status").equals("invalid_parameter"));
		res = Checker.checkParamContent(new JSONObject("{\"content\": \"\"}"));
		assertTrue(res[0] == null && ((JSONObject) res[1]).getString("status").equals("invalid_parameter"));

		// 测试合法参数
		res = Checker.checkParamContent(new JSONObject("{\"content\": \"1234\"}"));
		assertTrue(res[0].equals("1234") && res[1] == null);
	}
}
