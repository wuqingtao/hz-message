#!/usr/bin/env sh

local_path=`dirname $0`
target_path=$HOME/bin/hz-message/server/java/
rm -rf $target_path
mkdir -p $target_path
cp $local_path/start.sh $target_path
cp $local_path/stop.sh $target_path
cp $local_path/obj/*.jar $target_path
cp -r $local_path/obj/lib $target_path
