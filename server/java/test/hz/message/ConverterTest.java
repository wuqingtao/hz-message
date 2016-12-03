package hz.message;

import static org.junit.Assert.*;

import org.junit.Test;
import org.json.JSONObject;

/**
 * 测试Converter
 */
public class ConverterTest {
	/**
	 * 测试Converter.str2json()
	 */
	@Test
	public void test_str2json() {
		// 测试空参数
		Object[] res = Converter.str2json(null);
		assertTrue(res[0] == null && ((JSONObject) res[1]).getString("status").equals("invalid_parameter"));
		res = Converter.str2json("");
		assertTrue(res[0] == null && ((JSONObject) res[1]).getString("status").equals("invalid_parameter"));
		
		// 测试非法参数
		res = Converter.str2json("1234");
		assertTrue(res[0] == null && ((JSONObject) res[1]).getString("status").equals("invalid_parameter"));
		
		// 测试合法参数
		res = Converter.str2json("{\"abcd\": \"1234\"}");
		assertTrue(((JSONObject) res[0]).toMap().equals(new JSONObject("{\"abcd\": \"1234\"}").toMap()) && res[1] == null);
	}
	
	/**
	 * 测试Converter.json2str()
	 */
	@Test
	public void test_json2str() {
		// 测试空参数
		try {
			Converter.json2str(null);
			fail("Expect Exception.");
		} catch (Exception e) {
		}

		// 测试合法参数
		String res = Converter.json2str(new JSONObject());
		assertEquals(res, "{}");
		res = Converter.json2str(new JSONObject("{\"abcd\": \"1234\"}"));
		assertEquals(res, "{\"abcd\":\"1234\"}");
	}
}
