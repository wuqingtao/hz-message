<?php

use PHPUnit\Framework\TestCase;
require_once('../../../src/hz/message/mysql_holder.php');
require_once('../../../src/hz/message/mysql_post.php');

/**
 * 测试mysql_post.MysqlPost
 */
class test_MysqlPost extends TestCase {
	/** mysql用户名 */
	const user = 'root';
	/** mysql用户密码 */
	const password = 'Ningning~1';
	/** mysql数据库名 */
	const database = 'test_message';

	/** 测试post */
	const testPosts = [
        ['content'=>'abcdefghijklmnopqrstuvwxyz'],
        ['content'=>'ABCDEFGHIJKLMNOPQRSTUVWXYZ'],
        ['content'=>'1234567890'],
        ['content'=>'红尘世界？一片雾茫茫！'],
        ['content'=>'<p>红尘世界？<em>一片雾茫茫！</em></p>'],
    ];
	
	/** holder对象 */
	private $holder;
	
	/** post对象 */
	private $post;

	/**
	 * @before
	 */
	public function before() {
		$this->holder = new MysqlHolder(self::user, self::password, self::database);
		$this->holder->inst()->query('TRUNCATE TABLE post');
		$this->post = new MysqlPost($this->holder->inst());
	}
	
	/**
	 * @after
	 */
	public function after() {
		$this->holder->destroy();
		$this->holder->close();
	}
	
	/**
	 * 测试mysql_post.MysqlPost.getCount()
	 */
	public function test_getCount() {
		// 测试初始post个数为0
		$res = $this->post->getCount();
		$this->assertEquals($res, ['status'=>'ok', 'data'=>['count'=>0]]);
		
		// 添加测试post
		foreach (self::testPosts as $post) {
			$this->post->add($post);
		}
		
		// 测试post个数为添加的post个数
		$res = $this->post->getCount();
		$this->assertEquals($res, ['status'=>'ok', 'data'=>['count'=>count(self::testPosts)]]);
	}
	
	/**
	 * 测试mysql_post.MysqlPost.getAll()
	 */
	public function test_getAll() {
		// 测试初始的post为空
		$res = $this->post->getAll();
		$this->assertEquals($res, ['status'=>'ok', 'data'=>[]]);
		
		// 添加测试post
		foreach (self::testPosts as $post) {
			$this->post->add($post);
		}
		
		// 测试获取的post时间递减，和添加的post顺序相反、content一致
		$res = $this->post->getAll();
		$this->assertEquals($res['status'], 'ok');
		$posts = $res['data'];
		$this->assertEquals(count($posts), count(self::testPosts));
		$savedTimestamp = PHP_INT_MAX;
		for ($i = 0; $i < count($posts); ++$i) {
			$post = $posts[$i];
			$timestamp = $post['timestamp'];
			$this->assertTrue($timestamp <= $savedTimestamp);
			$savedTimestamp = $timestamp;
			$content = $post['content'];
			$this->assertEquals($content, self::testPosts[count(self::testPosts) - $i - 1]['content']);
		}
	}

	/**
	 * 测试mysql_post.MysqlPost.getById()
	 */
	public function test_getById() {
		// 测试对空的post，通过id获取错误
		$res = $this->post->getById(['id'=>PHP_INT_MAX]);
		$this->assertEquals($res['status'], 'none_target');

		// 添加测试post
		foreach (self::testPosts as $post) {
			$this->post->add($post);
		}

		// 测试对每一个添加的post，和通过对应的id获取的post一致
		$res = $this->post->getAll();
		$posts = $res['data'];
		foreach ($posts as $post) {
			$res = $this->post->getById(['id'=>$post['id']]);
			$this->assertEquals($res, ['status'=>'ok', 'data'=>$post]);
		}
	}
	
	/**
	 * 测试mysql_post.MysqlPost.add()
	 */
	public function test_add() {
		// 添加post
		$res = $this->post->add(['content'=>'content']);
		
		// 测试添加状态ok
		$this->assertEquals($res['status'], 'ok');
		
		// 测试添加的id合法
		$post = $res['data'];
		$id = $post['id'];
		$this->assertTrue(is_integer($id) && $id > 0);
		
		// 测试添加的timestamp合法
		$timestamp = $post['timestamp'];
		$this->assertTrue(is_integer($timestamp) && $timestamp > 0);
		
		// 测试添加的content正确
		$content = $post['content'];
		$this->assertEquals($content, 'content');
	}
	
	/**
	 * 测试mysql_post.MysqlPost.modify()
	 */
	public function test_modify() {
		// 添加测试post
		$this->post->add(['content'=>'content']);
		
		// 获取post
		$res = $this->post->getAll();
		$prePost = $res['data'][0];
		
		// 修改post
		$res = $this->post->modify(['id'=>$prePost['id'], 'content'=>'CONTENT']);
		
		// 测试修改状态ok
		$this->assertEquals($res['status'], 'ok');
		
		// 保存修改后的post
		$modifiedPost = $res['data'];
		
		// 再次获取post
		$res = $this->post->getAll();
		$nowPost = $res['data'][0];
		
		// 测试获取的id和修改后的id一致
		$this->assertEquals($prePost['id'], $modifiedPost['id']);
		
		// 测试获取的timestamp和修改后的timestamp一致
		$this->assertEquals($prePost['timestamp'], $modifiedPost['timestamp']);
		
		// 测试修改后的content正确
		$this->assertEquals($modifiedPost['content'], 'CONTENT');
		
		// 测试修改后的post和再次获取的post一致
		$this->assertEquals($modifiedPost, $nowPost);
	}
	
	/**
	 * 测试mysql_post.MysqlPost.remove()
	 */
	public function test_remove() {
		// 添加post
		$this->post->add(['content'=>'content']);
		
		// 获取post ID
		$res = $this->post->getAll();
		$id = $res['data'][0]['id'];
		
		// 删除post
		$res = $this->post->remove(['id'=>$id]);
		
		// 测试删除结果
		$this->assertEquals($res, ['status'=>'ok', 'data'=>['id'=>$id]]);
	}
}

?>
