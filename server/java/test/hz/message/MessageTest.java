package hz.message;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.json.JSONObject;

/**
 * 测试Message
 */
public class MessageTest {
	/** message对象 */
	private Message _message;
	/** post对象 */
	private Post _post;
	
	@Before
	public void before() {
        Holder holder = new TestHolder();
        _post = new TestPost();
        _message = new Message(holder, _post);
	}
	
	@After
	public void after() throws Exception {
		_message.close();
	}

	/**
	 * 测试Message.request()
	 * 
	 * @throws Exception 错误异常
	 */
	@Test
    public void test_request() throws Exception {
        // 测试{"type": "get_post_count"}
        assertEquals(_message.request(new JSONObject("{\"type\": \"get_post_count\"}")).toMap(), _post.getCount().toMap());
        
        // 测试{"type": "get_all_post"}
        assertEquals(_message.request(new JSONObject("{\"type\": \"get_all_post\"}")).toMap(), _post.getAll().toMap());
        
        // 测试{"type": "get_post_by_id"}
        assertEquals(_message.request(new JSONObject("{\"type\": \"get_post_by_id\"}")).toMap(), _post.getById(new JSONObject("{}")).toMap());
        
        // 测试{"type": "add_post"}
        assertEquals(_message.request(new JSONObject("{\"type\": \"add_post\"}")).toMap(), _post.add(new JSONObject("{}")).toMap());
        
        // 测试{"type": "modify_post"}
        assertEquals(_message.request(new JSONObject("{\"type\": \"modify_post\"}")).toMap(), _post.modify(new JSONObject("{}")).toMap());
        
        // 测试{"type": "remove_post"}
        assertEquals(_message.request(new JSONObject("{\"type\": \"remove_post\"}")).toMap(), _post.remove(new JSONObject("{}")).toMap());
        
        // 测试{"type": "other"}
        assertEquals(_message.request(new JSONObject("{\"type\": \"other\"}")).getString("status"), "invalid_parameter");
	}

	/**
	 * 模拟holder类
	 */
	private static class TestHolder implements Holder {
		@Override
	    public Object inst() {
	        return null;
		}
	
		@Override
	    public void destroy() throws Exception {
		}
	
		@Override
	    public void close() throws Exception {
		}
	}

	/**
	 * 模拟post类
	 */
	private static class TestPost implements Post {
		@Override
	    public JSONObject getCount() throws Exception {
	        return new JSONObject("{\"status\": \"ok\", \"data\": {\"count\": 1234}}");
		}
	
		@Override
	    public JSONObject getAll() throws Exception {
	        return new JSONObject("{\"status\": \"ok\", \"data\": [{\"id\": 1234, \"timestamp\": 123456, \"content\": \"abcd\"}, {\"id\": 12345, \"timestamp\": 1234567, \"content\": \"abcde\"}]}");
		}
	
		@Override
	    public JSONObject getById(JSONObject data) throws Exception {
	        return new JSONObject("{\"status\": \"ok\", \"data\": {\"id\": 1234, \"timestamp\": 123456, \"content\": \"abcd\"}}");
		}
	
		@Override
	    public JSONObject add(JSONObject data) throws Exception {
	        return new JSONObject("{\"status\": \"ok\", \"data\": {\"id\": 1234, \"timestamp\": 123456, \"content\": \"abcd\"}}");
		}
	
		@Override
	    public JSONObject modify(JSONObject data) throws Exception {
	        return new JSONObject("{\"status\": \"ok\", \"data\": {\"id\": 1234, \"timestamp\": 123456, \"content\": \"abcd\"}}");
		}
	
		@Override
	    public JSONObject remove(JSONObject data) throws Exception {
	        return new JSONObject("{\"status\": \"ok\", \"data\": {\"id\": 1234}}");
		}
	}
}
