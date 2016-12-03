package hz.message;

import org.json.JSONObject;

/**
 * 信息类接口
 */
public interface Post {
	/**
	 * 获取post个数
	 * 
	 * @return {"status": "<状态>", "message": "<错误描述>", "data": {"count": <post个数>}}
	 * @throws Exception 错误异常
	 */
	public JSONObject getCount() throws Exception;

	/**
	 * 获取所有的post
	 * 
	 * @return {"status": "<状态>", "message": "<错误描述>", "data": [{"id": <post ID>, "timestamp": <post时间戳，单位：s>, "content": "<post内容>"}, ...]}
	 * @throws Exception 错误异常
	 */
	public JSONObject getAll() throws Exception;

	/**
	 * 根据ID获取post
	 * 
	 * @param param {"id": <post ID>}
	 * @return {"status": "<状态>", "message": "<错误描述>", "data": {"id": <post ID>, "timestamp": <post时间戳，单位：s>, "content": "<post内容>"}}
	 * @throws Exception 错误异常
	 */
	public JSONObject getById(JSONObject param) throws Exception;

	/**
	 * 添加post
	 * 
	 * @param param {"content": "<post内容>"}
	 * @return {"status": "<状态>", "message": "<错误描述>", "data": {"id": <post ID>, "timestamp": <post时间戳，单位：s>, "content": "<post内容>"}}
	 * @throws Exception 错误异常
	 */
	public JSONObject add(JSONObject param) throws Exception;

	/**
	 * 修改post
	 * 
	 * @param param {"id": <post ID>, "content": "<post内容>"}
	 * @return {"status": "<状态>", "message": "<错误描述>", "data": {"id": <post ID>, "timestamp": <post时间戳，单位：s>, "content": "<post内容>"}}
	 * @throws Exception 错误异常
	 */
	public JSONObject modify(JSONObject param) throws Exception;

	/**
	 * 删除post
	 * 
	 * @param param {"id": <post ID>}
	 * @return {"status": "<状态>", "message": "<错误描述>", "data": {"id": <post ID>}}
	 * @throws Exception 错误异常
	 */
	public JSONObject remove(JSONObject param) throws Exception;
}
