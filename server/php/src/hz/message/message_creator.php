<?php

require_once('mysql_holder.php');
require_once('mysql_post.php');
require_once('message.php');

/**
 * 创建mysql类message
 *
 * @param string $user mysql用户名
 * @param string $password mysql用户密码
 * @param string $database mysql数据库名
 * @return Message message对象
 */
function createMessageByMysql($user, $password, $database) {
    $holder = new MysqlHolder($user, $password, $database);
    $post = new MysqlPost($holder->inst());
    return new Message($holder, $post);
}

?>
