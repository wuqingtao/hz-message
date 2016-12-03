#!/usr/bin/env python
#-*-coding: utf-8-*-

'''测试mysql_holder'''

import os, sys
sys.path.append(os.path.join(os.path.dirname(os.path.realpath(__file__)), '../../../src/'))
from hz.message.mysql_holder import MysqlHolder
import mysql.connector
import unittest

# 数据库用户
_user = 'root'
# 数据库密码
_password = 'Ningning~1'
# 数据库名称
_database = 'test_message'

class MysqlHolderTest(unittest.TestCase):
    '''测试mysql_holder.MysqlHolder'''
    def setUp(self):
        self.__holder = MysqlHolder(_user, _password, _database)
    
    def tearDown(self):
        try:
            self.__holder.destroy()
            self.__holder.close();
        except:
            pass
    
    def test_inst(self):
        '''测试mysql_holder.MysqlHolder.inst()'''
        # 测试返回的对象为mysql连接对象
        inst = self.__holder.inst()
        self.assertTrue(isinstance(inst, mysql.connector.MySQLConnection))
    
    def test_destroy(self):
        '''测试mysql_holder.MysqlHolder.destroy()'''
        conn = self.__holder.inst()
        
        # 测试数据库没有销毁
        cur = conn.cursor()
        cur.execute('SHOW TABLES')
        result = cur.fetchall()
        cur.close()
        self.assertTrue(result != None)
        
        # 执行destroy
        self.__holder.destroy()
        
        # 测试数据库已经销毁
        with self.assertRaises(Exception):
            cur = conn.cursor()
            cur.execute('SHOW TABLES')
            cur.close()
    
    def test_close(self):
        '''测试mysql_holder.MysqlHolder.close()'''
        # 测试数据库连接没有关闭
        self.assertTrue(self.__holder.inst().is_connected())
        
        # 执行close
        self.__holder.close()
        
        # 测试数据库连接已经关闭
        self.assertFalse(self.__holder.inst().is_connected())

if __name__ == '__main__':
    unittest.main()
