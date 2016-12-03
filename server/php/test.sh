#!/usr/bin/env sh

local_path=`dirname $0`
cd $local_path/test/hz/message
phpunit checker_test.php
phpunit message_test.php
phpunit message_creator_test.php
phpunit mysql_holder_test.php
phpunit mysql_post_test.php
