<?php

require_once('checker.php');

/**
 * mysql信息类
 * <p>
 * mysql信息类实现对数据库表post的创建、添加、查询、修改和删除
 */
class MysqlPost {
	/** 数据库表名 */
	const table = 'post';
	
	/** 数据库连接 */
	private $conn;
	
	/**
	 * 构造函数
	 *
	 * @param mysqli $conn 数据库连接
	 */
	public function __construct($conn) {
		// 保存数据库连接
		$this->conn = $conn;
		
		// 创建数据库表
		$this->conn->query(sprintf('CREATE TABLE IF NOT EXISTS `%s` (`id` INT NOT NULL PRIMARY KEY AUTO_INCREMENT, `timestamp` INT NOT NULL, `content` TEXT NOT NULL)', self::table));
	}
	
	/**
	 * 获取post个数
	 *
	 * @return array ['status'=>'<状态>', 'message'=>'<错误描述>', 'data'=>['count'=><post个数>]]
	 */
	public function getCount() {
		// 查询数据库
		$res = $this->conn->query(sprintf('SELECT COUNT(`id`) AS count FROM `%s`', self::table));
		$row = $res->fetch_assoc();
		$row['count'] = (int)$row['count'];
		return ['status'=>'ok', 'data'=>$row];
	}
	
	/**
	 * 获取所有post
	 *
	 * @return array ['status'=>'<状态>', 'message'=>'<错误描述>', 'data'=>[['id'=><post ID>, 'timestamp'=><post时间戳，单位：s>, 'content'=>'<post内容>'], ...]]
	 */
	public function getAll() {
		// 查询数据库
		$res = $this->conn->query(sprintf('SELECT `id`, `timestamp`, `content` FROM `%s` ORDER BY `id` DESC', self::table));
		$rows = $res->fetch_all(MYSQLI_ASSOC);
		for ($i = 0; $i < count($rows); ++$i) {
			$rows[$i]['id'] = (int)$rows[$i]['id'];
			$rows[$i]['timestamp'] = (int)$rows[$i]['timestamp'];
		}
		return ['status'=>'ok', 'data'=>$rows];
	}
	
	/**
	 * 根据ID获取post
	 *
	 * @param array $param ['id'=><post ID>]
	 * @return array ['status'=>'<状态>', 'message'=>'<错误描述>', 'data'=>['id'=><post ID>, 'timestamp'=><post时间戳，单位：s>, 'content'=>'<post内容>']]
	 */
	public function getById($data) {
		// 校验id参数
		$iderr = checkParamId($data);
		$id = $iderr[0];
		$err = $iderr[1];
		if ($err) {
			return $err;
		}
		
		// 查询数据库，如果没有数据返回错误
		$res = $this->conn->query(sprintf('SELECT `id`, `timestamp`, `content` FROM `%s` WHERE `id`=%d LIMIT 1', self::table, $id));
		$row = $res->fetch_assoc();
		if ($row) {
			$row['id'] = (int)$row['id'];
			$row['timestamp'] = (int)$row['timestamp'];
			return ['status'=>'ok', 'data'=>$row];
		} else {
			return ['status'=>'none_target', 'message'=>'`id` does not exist.'];
		}
	}
	
	/**
	 * 添加post
	 *
	 * @param array $param ['content'=>'<post内容>']
	 * @return array ['status'=>'<状态>', 'message'=>'<错误描述>', 'data'=>['id'=><post ID>, 'timestamp'=><post时间戳，单位：s>, 'content'=>'<post内容>']]
	 */
	public function add($data) {
		// 校验content参数
		$contenterr = checkParamContent($data);
		$content = $contenterr[0];
		$err = $contenterr[1];
		if ($err) {
			return $err;
		}
		
		// 获取当前系统时间
		$timestamp = time();
		
		// 查询数据库
		$this->conn->query(sprintf('INSERT INTO `%s` SET `timestamp`=%d, `content`="%s"', self::table, $timestamp, $content));
		return ['status'=>"ok", 'data'=>['id'=>$this->conn->insert_id, 'timestamp'=>$timestamp, 'content'=>$content]];
	}
	
	/**
	 * 修改post
	 *
	 * @param array $param ['id'=><post ID>]
	 * @return array ['status'=>'<状态>', 'message'=>'<错误描述>', 'data'=>['id'=><post ID>, 'timestamp'=><post时间戳，单位：s>, 'content'=>'<post内容>']]
	 */
	public function modify($data) {
		// 校验id参数
		$iderr = checkParamId($data);
		$id = $iderr[0];
		$err = $iderr[1];
		if ($err) {
			return $err;
		}
		
		// 校验content参数
		$contenterr = checkParamContent($data);
		$content = $contenterr[0];
		$err = $contenterr[1];
		if ($err) {
			return $err;
		}

		// 查询数据库，如果没有数据修改返回错误
		$this->conn->query(sprintf('UPDATE `%s` SET `content`="%s" WHERE `id`=%d', self::table, $content, $id));
		if ($this->conn->affected_rows) {
			return $this->getById(['id'=>$id]);
		} else {
			return ['status'=>'none_target', 'message'=>'`id` does not exist.'];
		}
	}

	/**
	 * 删除post
	 *
	 * @param array $param ['id'=><post ID>]
	 * @return array ['status'=>'<状态>', 'message'=>'<错误描述>', 'data'=>['id'=><post ID>]]
	 */
	public function remove($data) {
		// 校验id参数
		$iderr = checkParamId($data);
		$id = $iderr[0];
		$err = $iderr[1];
		if ($err) {
			return $err;
		}
		
		// 查询数据库，如果没有数据删除返回错误
		$this->conn->query(sprintf('DELETE FROM `%s` WHERE `id`=%d', self::table, $id));
		if ($this->conn->affected_rows) {
			return ['status'=>'ok', 'data'=>['id'=>$id]];
		} else {
			return ['status'=>'none_target', 'message'=>'`id` does not exist.'];
		}
	}
}

?>
