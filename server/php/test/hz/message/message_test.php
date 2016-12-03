<?php

use PHPUnit\Framework\TestCase;
require_once('../../../src/hz/message/message.php');

/**
 * 测试message.Message
 */
class test_Message extends TestCase {
	/** message对象 */
	private $message;
	/** post对象 */
	private $post;
	
	/**
	 * @before
	 */
	public function before() {
        $holder = new TestHolder();
        $this->post = new TestPost();
        $this->message = new Message($holder, $this->post);
	}
	
	/**
	 * @after
	 */
	public function after() {
        $this->message->close();
	}
	
	/**
	 * 测试message.Message.request()
	 */
	public function test_request() {
        // 测试['type'=>'get_post_count']
        $res = $this->message->request(['type'=>'get_post_count']);
        $this->assertEquals($res, $this->post->getCount());

        // 测试['type'=>'get_all_post']
        $res = $this->message->request(['type'=>'get_all_post']);
        $this->assertEquals($res, $this->post->getAll());

        // 测试['type'=>'get_post_by_id']
        $res = $this->message->request(['type'=>'get_post_by_id']);
        $this->assertEquals($res, $this->post->getById([]));

        // 测试['type'=>'add_post']
        $res = $this->message->request(['type'=>'add_post']);
        $this->assertEquals($res, $this->post->add([]));

        // 测试['type'=>'modify_post']
        $this->message->request(['type'=>'modify_post']);
        $this->assertEquals($res, $this->post->modify([]));

        // 测试['type'=>'remove_post']
        $res = $this->message->request(['type'=>'remove_post']);
        $this->assertEquals($res, $this->post->remove([]));

        // 测试['type'=>'other']
        $res = $this->message->request(['type'=>'other']);
        $this->assertEquals($res['status'], 'invalid_parameter');
	}
}

/**
 * 模拟holder类
 */
class TestHolder {
	public function destroy() {
	}
	
	public function close() {
	}
}

/**
 * 模拟post类
 */
class TestPost {
	public function getCount() {
        return ['status'=>'ok', 'data'=>['count'=>1234]];
	}

	public function getAll() {
        return ['status'=>'ok', 'data'=>[['id'=>1234, 'timestamp'=>123456, 'content'=>'abcd'], ['id'=>12345, 'timestamp'=>1234567, 'content'=>'abcde']]];
	}

	public function getById($data) {
        return ['status'=>'ok', 'data'=>['id'=>1234, 'timestamp'=>123456, 'content'=>'abcd']];
	}

	public function add($data) {
        return ['status'=>'ok', 'data'=>['id'=>1234, 'timestamp'=>123456, 'content'=>'abcd']];
	}

	public function modify($data) {
        return ['status'=>'ok', 'data'=>['id'=>1234, 'timestamp'=>123456, 'content'=>'abcd']];
	}

	public function remove($data) {
        return ['status'=>'ok', 'data'=>['id'=>1234]];
	}
}

?>
