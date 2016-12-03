#!/usr/bin/env sh

local_path=`dirname $0`
mocha $local_path/test/hz/message/checker_test.js
mocha $local_path/test/hz/message/message_test.js
mocha $local_path/test/hz/message/message_creator_test.js
mocha $local_path/test/hz/message/mysql_holder_test.js
mocha $local_path/test/hz/message/mysql_post_test.js
