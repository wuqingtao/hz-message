#!/usr/bin/env python
#-*-coding: utf-8-*-

'''message对象'''

from mysql_holder import MysqlHolder
from mysql_post import MysqlPost
from message import Message

def createMessageByMysql(user, password, database):
    '''创建mysql类message
    
    Args:
        user: mysql用户名
        password: mysql用户密码
        database: mysql数据库名
    
    Returns:
        message对象
    '''
    holder = MysqlHolder(user, password, database)
    post = MysqlPost(holder.inst())
    return Message(holder, post)
