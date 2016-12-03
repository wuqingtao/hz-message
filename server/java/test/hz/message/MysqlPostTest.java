package hz.message;

import static org.junit.Assert.*;

import java.sql.Connection;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * 测试MysqlPost类
 *
 */
public class MysqlPostTest {
	/** mysql用户名 */
	private final String _user = "root";
	/** mysql用户密码 */
	private final String _password = "Ningning~1";
	/** mysql数据库名 */
	private final String _database = "test_message";
	
	/** holder对象 */	
	private Holder _holder;
	/** post对象 */
	private Post _post;
	
	/** 测试post */
    private final JSONObject[] _testPosts = {
        new JSONObject("{\"content\": \"abcdefghijklmnopqrstuvwxyz\"}"),
        new JSONObject("{\"content\": \"ABCDEFGHIJKLMNOPQRSTUVWXYZ\"}"),
        new JSONObject("{\"content\": \"1234567890\"}"),
        new JSONObject("{\"content\": \"红尘世界？一片雾茫茫！\"}"),
        new JSONObject("{\"content\": \"<p>红尘世界？<em>一片雾茫茫！</em></p>\"}"),
    };
	
	@Before
	public void before() throws Exception {
		_holder = new MysqlHolder(_user, _password, _database);
		_post = new MysqlPost((Connection) _holder.inst());
	}
	
	@After
	public void after() throws Exception {
		_holder.destroy();
		_holder.close();
	}

	/**
	 * 测试MysqlPost.getCount()
	 * 
	 * @throws Exception 错误异常
	 */
	@Test
    public void test_getCount() throws Exception {
		// 测试初始post个数为0
		JSONObject res = _post.getCount();
		assertEquals(res.toMap(), new JSONObject("{\"status\": \"ok\", \"data\": {\"count\": 0}}").toMap());
		
		// 添加测试post
		for (JSONObject post : _testPosts) {
			_post.add(post);
		}
		
		// 测试post个数为添加的post个数
		res = _post.getCount();
		assertEquals(res.toMap(), new JSONObject(String.format("{\"status\": \"ok\", \"data\": {\"count\": %d}}", _testPosts.length)).toMap());
	}

	/**
	 * 测试MysqlPost.getAll()
	 * 
	 * @throws Exception 错误异常
	 */
	@Test
    public void test_getAll() throws Exception {
		// 测试初始的post为空
		JSONObject res = _post.getAll();
		assertEquals(res.toMap(), new JSONObject("{\"status\": \"ok\", \"data\": []}}").toMap());

		// 添加测试post
		for (JSONObject post : _testPosts) {
			_post.add(post);
		}

		// 测试获取的post时间递减，和添加的post顺序相反、content一致
		res = _post.getAll();
		assertEquals(res.getString("status"), "ok");
		JSONArray posts = res.getJSONArray("data");
		assertEquals(posts.length(), _testPosts.length);
		int savedTimestamp = Integer.MAX_VALUE;
		for (int i = 0; i < posts.length(); ++i) {
			JSONObject post = posts.getJSONObject(i);
			int timestamp = post.getInt("timestamp");
			assertTrue(timestamp <= savedTimestamp);
			savedTimestamp = timestamp;
			String content = post.getString("content");
			assertEquals(content, _testPosts[_testPosts.length - i - 1].getString("content"));
		}
	}

	/**
	 * 测试MysqlPost.getById()
	 * 
	 * @throws Exception 错误异常
	 */
	@Test
    public void test_getById() throws Exception {
        // 测试对空的post，通过id获取错误
		JSONObject res = _post.getById(new JSONObject(String.format("{\"id\": %d}", Integer.MAX_VALUE)));
        assertEquals(res.getString("status"), "none_target");
		
        // 添加测试post
		for (JSONObject post : _testPosts) {
			_post.add(post);
		}

        // 测试对每一个添加的post，和通过对应的id获取的post一致
        res = _post.getAll();
        JSONArray posts = res.getJSONArray("data");

        // 对每一个post数据，分别调用getById，测试返回的post数据是否一致
        for (int i = 0; i < posts.length(); ++i) {
        	JSONObject post = posts.getJSONObject(i);
			int id = post.getInt("id");
            res = _post.getById(new JSONObject(String.format("{\"id\": %d}", id)));
            assertEquals(res.getString("status"), "ok");
			assertEquals(res.getJSONObject("data").toMap(), post.toMap());
		}
	}
	
	/**
	 * 测试MysqlPost.add()
	 * 
	 * @throws Exception 错误异常
	 */
	@Test	
    public void test_add() throws Exception {
        // 添加post
		JSONObject res = _post.add(new JSONObject("{\"content\": \"content\"}"));
		
		// 测试添加状态ok
		assertEquals(res.getString("status"), "ok");
		
		// 测试添加的id合法
		JSONObject post = res.getJSONObject("data") ;
        int id = post.getInt("id");
        assertTrue(id > 0);

        // 测试添加的timestamp合法
        int timestamp = post.getInt("timestamp");
        assertTrue(timestamp > 0);
		
        // 测试添加的content正确
        String content = post.getString("content");
        assertEquals(content, "content");
	}
	
	/**
	 * 测试MysqlPost.modify()
	 * 
	 * @throws Exception 错误异常
	 */
	@Test
    public void test_modify() throws Exception {
        // 添加一个post
        _post.add(new JSONObject("{\"content\": \"content\"}"));

        // 获取post
        JSONObject res = _post.getAll();
        JSONObject prePost = res.getJSONArray("data").getJSONObject(0);

        // 修改post
        res = _post.modify(new JSONObject(String.format("{\"id\": %d, \"content\": \"CONTENT\"}", prePost.getInt("id"))));
        
        // 测试修改状态ok
        assertEquals(res.getString("status"), "ok");

        // 保存修改后的post
        JSONObject modifiedPost = res.getJSONObject("data");

        // 再次获取post
        res = _post.getAll();
        JSONObject nowPost = res.getJSONArray("data").getJSONObject(0);

        // 测试获取的id和修改后的id一致
        assertEquals(prePost.getInt("id"), modifiedPost.getInt("id"));
        
        // 测试获取的timestamp和修改后的timestamp一致
        assertEquals(prePost.getInt("timestamp"), modifiedPost.getInt("timestamp"));
        
        // 测试修改后的content正确
        assertEquals(modifiedPost.getString("content"), "CONTENT");
        
        // 测试修改后的post和再次获取的post一致
        assertEquals(modifiedPost.toMap(), nowPost.toMap());
	}

	/**
	 * 测试MysqlPost.remove()
	 * 
	 * @throws Exception 错误异常
	 */
	@Test
    public void test_remove() throws Exception {
        // 添加post
        _post.add(new JSONObject("{\"content\": \"content\"}"));

        // 获取post ID
        JSONObject res = _post.getAll();
		int id = res.getJSONArray("data").getJSONObject(0).getInt("id");

		// 删除post
        res = _post.remove(new JSONObject(String.format("{\"id\": %d}", id)));

        // 测试删除结果
        assertEquals(res.toMap(), new JSONObject(String.format("{\"status\": \"ok\", \"data\": {\"id\": %d}}", id)).toMap());
	}
}
