<?php

use PHPUnit\Framework\TestCase;
require_once('../../../src/hz/message/mysql_holder.php');

/**
 * 测试mysql_holder.MysqlHolder
 */
class test_MysqlHolder extends TestCase {
	/** mysql用户名 */
	const user = 'root';
	/** mysql用户密码 */
	const password = 'Ningning~1';
	/** mysql数据库名 */
	const database = 'test_message';
	
	/** holder对象 */
	private $holder;
 	
	/**
	 * @before
	 */
	public function before() {
        $this->holder = new MysqlHolder(self::user, self::password, self::database);
	}
	
	/**
	 * @after
	 */
	public function after() {
		try {
			$this->holder->destroy();
		} catch (Exception $e) {
		}
	}
	
	/**
	 * 测试mysql_holder.MysqlHolder.inst()
	 */
	public function test_inst() {
        // 测试返回的对象为mysql连接对象
        $conn = $this->holder->inst();
        $this->assertTrue(method_exists($conn, 'query'));
	}
	
	/**
	 * 测试mysql_holder.MysqlHolder.destroy()
	 */
	public function test_destroy() {
        $conn = $this->holder->inst();

        // 测试数据库没有销毁
        $res = $conn->query('SHOW TABLES');
        $this->assertNotEmpty($res);

        // 执行destroy
        $this->holder->destroy();

        // 测试数据库已经销毁
        $res = $conn->query('SHOW TABLES');
        $this->assertFalse($res);
	}
	
	/**
	 * 测试mysql_holder.MysqlHolder.close()
	 */
	public function test_close() {
        // 测试数据库连接没有关闭
        // TODO:

        // 执行close
        $this->holder->close();

        // 测试数据库连接已经关闭
        // TODO:
	}
}

?>
