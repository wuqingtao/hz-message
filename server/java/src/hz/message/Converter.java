package hz.message;

import org.json.JSONObject;

/**
 * 处理字符串和json之间的转换
 */
public class Converter {
	/**
	 * 字符串转json
	 * 
	 * @param str 字符串
	 * @return {json对象, 错误状态}
	 */
	public static Object[] str2json(String str) {
		try {
			return new Object[] { new JSONObject(str), null };
		} catch (Exception e) {
			return new Object[] { null, new JSONObject("{\"status\": \"invalid_parameter\", \"message\": \"The param should be a JSON string.\"}") };
		}
	}

	/**
	 * json转字符串
	 * 
	 * @param json json对象
	 * @return 字符串
	 */
	public static String json2str(JSONObject obj) {
		return obj.toString();
	}
}
