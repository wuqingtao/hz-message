#!/usr/bin/env sh

local_path=`dirname $0`
target_path=$HOME/bin/hz-message/server/php/
rm -rf $target_path
mkdir -p $target_path
cp $local_path/start.sh $target_path
cp $local_path/stop.sh $target_path
cp -r $local_path/src/* $target_path
cp -r $local_path/libs $target_path
cp -r $local_path/conf $target_path
