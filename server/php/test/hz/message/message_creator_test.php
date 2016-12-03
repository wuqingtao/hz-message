<?php

use PHPUnit\Framework\TestCase;
require_once('../../../src/hz/message/message_creator.php');

/**
 * 测试message_creator
 */
class test_message_creator extends TestCase {
	/** mysql用户名 */
	const user = 'root';
	/** mysql用户密码 */
	const password = 'Ningning~1';
	/** mysql数据库名 */
	const database = 'test_message';
	
	/**
	 * 测试message_creator.createMessageByMysql()
	 */
	public function test_createMessageByMysql() {
		// 执行createMessageByMysql
		$message = createMessageByMysql(self::user, self::password, self::database);

		// 测试创建对象为message
		$this->assertTrue(method_exists($message, 'request'));
	}
}

?>
