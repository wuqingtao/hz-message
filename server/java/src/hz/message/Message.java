package hz.message;

import org.json.JSONObject;

/**
 * 消息类
 * <p>
 * 消息类用于处理用户的消息请求，包括添加、查询、修改和删除操作
 */
public class Message {
	/** holder对象 */
	private Holder _holder;
	/** post对象 */
	private Post _post;

	/**
	 * 构造函数
	 * 
	 * @param holder holder对象
	 * @param post post对象
	 */
	public Message(Holder holder, Post post) {
		_holder = holder;
		_post = post;
	}

	/**
	 * 处理请求
	 * 
	 * @param param {"type", "<type值>", ...}
	 * @return {"status": "<状态>", "message": "<错误描述>", "data": <结果>}
	 * @throws Exception 错误异常
	 */
    public JSONObject request(JSONObject param) throws Exception {
    	// 校验type参数
		Object[] typeErr = Checker.checkParamType(param);
		String type = (String) typeErr[0];
		JSONObject err = (JSONObject) typeErr[1];
		if (err != null) {
			return err;
		}
		
		// 根据不同type值调用post对象处理
		// 如果type值不支持，返回错误
        if (type.equals("get_post_count")) {
            return _post.getCount();
        } else if (type.equals("get_all_post")) {
            return _post.getAll();
        } else if (type.equals("get_post_by_id")) {
            return _post.getById(param);
        } else if (type.equals("add_post")) {
            return _post.add(param);
        } else if (type.equals("modify_post")) {
            return _post.modify(param);
        } else if (type.equals("remove_post")) {
            return _post.remove(param);
        } else {
            return new JSONObject("{\"status\": \"invalid_parameter\", \"message\": \"`type` is invalid.\"}");
		}
	}

    /**
     * 关闭操作
     * 
     * @throws Exception 错误异常
     */
    public void close() throws Exception {
        _holder.close();
	}
}
