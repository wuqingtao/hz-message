#!/usr/bin/env sh

local_path=`dirname $0`
python $local_path/test/hz/message/utils/dict_parser_test.py
python $local_path/test/hz/message/checker_test.py
python $local_path/test/hz/message/message_test.py
python $local_path/test/hz/message/message_creator_test.py
python $local_path/test/hz/message/mysql_holder_test.py
python $local_path/test/hz/message/mysql_post_test.py
