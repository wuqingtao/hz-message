#!/usr/bin/env sh

local_path=`dirname $0`
rm -rf $local_path/logs
mkdir -p $local_path/logs
nohup java -jar $local_path/hz-message-1.0.0.jar -p 8401 >$local_path/logs/server.log 2>&1 &
