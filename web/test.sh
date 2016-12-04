#!/usr/bin/env sh

local_path=`dirname $0`
mocha $local_path/test/js
