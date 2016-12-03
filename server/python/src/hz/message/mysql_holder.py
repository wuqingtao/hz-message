#!/usr/bin/env python
#-*-coding: utf-8-*-

'''mysql实体类'''

import mysql.connector

class MysqlHolder:
    '''mysql实体类
    
    mysql实体类实现mysql连接和关闭，及数据库创建和销毁
    '''
    def __init__(self, user, password, database):
        '''
        构造函数
        
        Args:
            user: mysql用户名
            password: mysql用户密码
            database: mysql数据库名
        '''
        # 保存数据库名
        self.__database = database
        
        # 创建数据库连接
        self.__conn = mysql.connector.connect(host='localhost', port=3306, user=user, password=password)
        
        # 创建数据库
        cur = self.__conn.cursor()
        cur.execute('CREATE DATABASE IF NOT EXISTS `%s` default character set utf8 COLLATE utf8_general_ci' % (self.__database))
        cur.execute('USE %s' % (self.__database))
        self.__conn.commit()
        cur.close()

    def inst(self):
        '''获取数据库连接
        
        Returns:
            数据库连接
        '''
        return self.__conn

    def destroy(self):
        '''销毁数据库'''
        cur = self.__conn.cursor()
        cur.execute('DROP DATABASE IF EXISTS `%s`' % (self.__database))
        self.__conn.commit()
        cur.close()

    def close(self):
        '''关闭数据库连接'''
        self.__conn.close()
