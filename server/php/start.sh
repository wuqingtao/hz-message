#!/usr/bin/env sh

target=`which php-cgi`
spawn-fcgi -a 127.0.0.1 -p 8401 -F 1 -- $target
