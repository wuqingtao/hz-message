#!/usr/bin/env sh

local_path=`dirname $0`
cd $local_path
npm install
rm -r build
webpack
