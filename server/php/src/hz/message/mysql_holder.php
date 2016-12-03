<?php

/**
 * mysql实体类
 * <p>
 * mysql实体类实现mysql连接和关闭，及数据库创建和销毁
 */
class MysqlHolder {
	/** 数据库连接 */
	private $conn;
	/** 数据库名称 */
	private $database;
	
	/**
	 * 构造函数
	 *
	 * @param string $user mysql用户名
	 * @param string $password mysql用户密码
	 * @param string $database mysql数据库名
	 */
	public function __construct($user, $password, $database) {
		// 创建数据库连接
		$this->conn = new mysqli('localhost', $user, $password);
		
		// 保存数据库名
		$this->database = $database;
		
		// 创建数据库
		$this->conn->query(sprintf('CREATE DATABASE IF NOT EXISTS `%s` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci', $this->database));
		$this->conn->query(sprintf('USE `%s`', $this->database));
	}
	
	/**
	 * 获取数据库连接
	 */
	public function inst() {
		return $this->conn;
	}
	
	/**
	 * 销毁数据库
	 */
	public function destroy() {
		$this->conn->query(sprintf('DROP DATABASE IF EXISTS `%s`', $this->database));
	}
	
	/**
	 * 关闭数据库连接
	 */
	public function close() {
		$this->conn->close();
	}
}

?>
