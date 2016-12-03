<?php

require_once('../../libs/log4php/Logger.php');
require_once('message_creator.php');

// 创建日志对象
date_default_timezone_set('PRC');
Logger::configure(getcwd().'/../../conf/logger.properties');
$logger = Logger::getLogger('message');

/** mysql用户名 */
const user = 'root';
/** mysql用户密码 */
const password = 'Ningning~1';
/** mysql数据库名 */
const database = 'message';

// 仅支持POST请求，否则返回HTTP 501
$method = $_SERVER['REQUEST_METHOD'];
if ($method != 'POST') {
	http_response_code(501);
	return;
}

try {
	// 获取请求参数
	$paramStr = file_get_contents("php://input");

	// 将请求参数从string转换为array
	$param = json_decode($paramStr, true);
	
	// 创建message对象
	$message = createMessageByMysql(user, password, database);

	// 处理请求
	$result = $message->request($param);

	// 关闭message对象
	$message->close();
	
	// 将返回结果从array转换为string
	$resultStr = json_encode($result);
	
	// 记录处理日志
	$logger->info(sprintf('req:%s res:%s', $paramStr, $resultStr));
	
	// 设置返回HTTP head
	header('Content-Type: text/plain;charset=UTF-8'); 

	// 返回处理结果
	file_put_contents("php://output", $resultStr);
} catch (\Exception $e) {
	// 如果处理结果出现异常，记录异常日志，返回HTTP 500
	// TODO: 不能捕获异常，待fix
	$logger->error($e, $e);
	http_response_code(500);
}

?>
