<?php

require_once('checker.php');

/**
 * 消息类
 * <p>
 * 消息类用于处理用户的消息请求，包括添加、查询、修改和删除操作
 */
class Message {
	/** holder对象 */
	private $holder;
	/** post对象 */
	private $post;
	
	/**
	 * 构造函数
	 *
	 * @param mixed $holder holder对象
	 * @param mixed $post post对象
	 */
	public function __construct($holder, $post) {
		$this->holder = $holder;
		$this->post = $post;
	}
	
	/**
	 * 请求处理
	 *
	 * @param array $param ['type'=>'<type值>', ...]
	 * @return array ['status'=>'<状态>', 'message'=>'<错误描述>', 'data': <结果>]
	 */
	public function request($param) {
		// 校验type参数
		$typeerr = checkParamType($param);
		$type = $typeerr[0];
		$err = $typeerr[1];
		if ($err) {
			return $err;
		}
		
		// 根据不同type值调用post对象处理
		// 如果type值不支持，返回错误
		if ($type == 'get_post_count') {
			return $this->post->getCount();
		} else if ($type == 'get_all_post') {
			return $this->post->getAll();
		} else if ($type == 'get_post_by_id') {
			return $this->post->getById($param);
		} else if ($type == 'add_post') {
			return $this->post->add($param);
		} else if ($type == 'modify_post') {
			return $this->post->modify($param);
		} else if ($type == 'remove_post') {
			return $this->post->remove($param);
		} else {
			return ['status'=>'invalid_parameter', 'message'=>'`type` is invalid.'];
		}
	}
	
	/**
	 * 关闭操作
	 */
	public function close() {
		// 关闭holder对象
		$this->holder->close();
	}
}

?>
