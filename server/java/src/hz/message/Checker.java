package hz.message;

import org.json.JSONObject;

/**
 * 校验请求参数是否合法
 */
public class Checker {
	/**
	 * 校验type参数是否合法
	 * 
	 * @param param {"type": "<type值>", ...}
	 * @return {"<type值>", {"status": "<错误状态>", "message", "<错误描述>:}}
	 */
	public static Object[] checkParamType(JSONObject param) {
		if (!param.has("type")) {
			return new Object[] { null, new JSONObject("{\"status\": \"lost_parameter\", \"message\": \"`type` is necessary.\"}") };
		}

		Object typeObj = param.get("type");
		if (typeObj == null || !typeObj.getClass().equals(java.lang.String.class)) {
			return new Object[] { null, new JSONObject("{\"status\": \"invalid_parameter\", \"message\": \"`type` should be string.\"}") };
		}

		String type = typeObj.toString();
		if (type.isEmpty()) {
			return new Object[] { null, new JSONObject("{\"status\": \"invalid_parameter\", \"message\": \"`type` should not be empty.\"}") };
		}

		return new Object[] {type, null};
	}

	/**
	 * 校验id参数是否合法
	 * 
	 * @param param {"id": <id值>, ...}
	 * @return {<id值>, {"status": "<错误状态>", "message", "<错误描述>:}}
	 */ 
	public static Object[] checkParamId(JSONObject param) {
		if (!param.has("id")) {
			return new Object[] { null, new JSONObject("{\"status\": \"lost_parameter\", \"message\": \"`id` is necessary.\"}") };
		}

		Object idObj = param.get("id");
		if (idObj == null || !idObj.getClass().equals(java.lang.Integer.class)) {
			return new Object[] { null, new JSONObject("{\"status\": \"invalid_parameter\", \"message\": \"`id` should be int.\"}") };
		}

		int id = (Integer) idObj;
		return new Object[] { id, null };
	}

	/**
	 * 校验content参数是否合法
	 * 
	 * @param param {"content": "<content值>", ...}
	 * @return {"<content值>", {"status": "<错误状态>", "message", "<错误描述>:}}
	 */ 
	public static Object[] checkParamContent(JSONObject param) {
		if (!param.has("content")) {
			return new Object[] { null, new JSONObject("{\"status\": \"lost_parameter\", \"message\": \"`content` is necessary.\"}") };
		}

		Object contentObj = param.get("content");
		if (contentObj == null || !contentObj.getClass().equals(java.lang.String.class)) {
			return new Object[] { null, new JSONObject("{\"status\": \"invalid_parameter\", \"message\": \"`content` should be string.\"}") };
		}

		String content = contentObj.toString();
		if (content.isEmpty()) {
			return new Object[] { null, new JSONObject("{\"status\": \"invalid_parameter\", \"message\": \"`content` should not be empty.\"}") };
		}

		// 返回`content`值
		return new Object[] { content, null };
	}
}
