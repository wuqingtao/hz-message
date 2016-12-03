<?php

use PHPUnit\Framework\TestCase;
require_once('../../../src/hz/message/checker.php');

/**
 * 测试checker
 */
class test_checker extends TestCase {
	/**
	 * 测试checker.checkParamType()
	 */
	public function test_checkParamType() {
		// 测试空参数
		$res = checkParamType([]);
		$this->assertTrue($res[0] == null && $res[1]['status'] == 'lost_parameter');
		
		// 测试非法key参数
		$res = checkParamType(['abcd'=>'1234']);
        $this->assertTrue($res[0] == null && $res[1]['status'] == 'lost_parameter');
		$res = checkParamType(['Type'=>'1234']);
        $this->assertTrue($res[0] == null && $res[1]['status'] == 'lost_parameter');
		
		// 测试非法value参数
		$res = checkParamType(['type'=>1234]);
		$this->assertTrue($res[0] == null && $res[1]['status'] == 'invalid_parameter');
		$res = checkParamType(['type'=>'']);
		$this->assertTrue($res[0] == null && $res[1]['status'] == 'invalid_parameter');
		
		// 测试合法参数
		$res = checkParamType(['type'=>'1234']);
        $this->assertTrue($res[0] == '1234' && $res[1] == null);
	}
	
	/**
	 * 测试checker.checkParamId()
	 */
	public function test_checkParamId() {
		// 测试空参数
		$res = checkParamId([]);
		$this->assertTrue($res[0] == null && $res[1]['status'] == 'lost_parameter');
		
		// 测试非法key参数
		$res = checkParamId(['abcd'=>1234]);
		$this->assertTrue($res[0] == null && $res[1]['status'] == 'lost_parameter');
		$res = checkParamId(['Id'=>1234]);
		$this->assertTrue($res[0] == null && $res[1]['status'] == 'lost_parameter');
		
		// 测试非法value参数
		$res = checkParamId(['id'=>'1234']);
		$this->assertTrue($res[0] == null && $res[1]['status'] == 'invalid_parameter');
		
		// 测试合法参数
		$res = checkParamId(['id'=>1234]);
		$this->assertTrue($res[0] == 1234 && $res[1] == null);
	}
	
	/**
	 * 测试checker.checkParamContent()
	 */
	public function test_checkParamContent() {
		// 测试空参数
		$res = checkParamContent([]);
		$this->assertTrue($res[0] == null && $res[1]['status'] == 'lost_parameter');
		
		// 测试非法key参数
		$res = checkParamContent(['abcd'=>'1234']);
		$this->assertTrue($res[0] == null && $res[1]['status'] == 'lost_parameter');
		$res = checkParamContent(['Content'=>'1234']);
		$this->assertTrue($res[0] == null && $res[1]['status'] == 'lost_parameter');
		
		// 测试非法value参数
		$res = checkParamContent(['content'=>1234]);
		$this->assertTrue($res[0] == null && $res[1]['status'] == 'invalid_parameter');
		$res = checkParamContent(['content'=>'']);
		$this->assertTrue($res[0] == null && $res[1]['status'] == 'invalid_parameter');
		
		// 测试合法参数
		$res = checkParamContent(['content'=>'1234']);
		$this->assertTrue($res[0] == '1234' && $res[1] == null);
	}
}

?>
