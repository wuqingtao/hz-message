#!/usr/bin/env python
#-*-coding: utf-8-*-

'''测试message_creator'''

import os, sys
sys.path.append(os.path.join(os.path.dirname(os.path.realpath(__file__)), '../../../src/'))
from hz.message.message_creator import *
import unittest

# mysql用户名
_user = 'root'
# mysql用户密码
_password = 'Ningning~1'
# mysql数据库名
_database = 'test_message'

class test_message_creator(unittest.TestCase):
    '''测试message_creator'''
    def test_createMessageByMysql(self):
        '''测试message_creator.createMessageByMysql()'''
        # 执行createMessageByMysql
        message = createMessageByMysql(_user, _password, _database);

        # 测试创建对象为message
        self.assertTrue(hasattr(message, 'request'));

if __name__ == '__main__':
    unittest.main()
