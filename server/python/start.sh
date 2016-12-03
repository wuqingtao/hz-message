#!/usr/bin/env sh

local_path=`dirname $0`
target=$local_path/hz/message/fcgi_server.py
spawn-fcgi -a 127.0.0.1 -p 8401 -F 1 -- $target
