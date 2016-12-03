#!/usr/bin/env python
#-*-coding: utf-8-*-

'''mysql信息类'''

import checker
import mysql.connector
import time
from datetime import datetime

class MysqlPost:
    '''mysql信息类
    
    mysql信息类实现对数据库表post的创建、添加、查询、修改和删除
    '''
    # 数据库表名
    __table = 'post'

    def __init__(self, conn):
        '''构造函数
        
        Args:
            conn: 数据库连接
        '''
        # 保存数据库连接
        self.__conn = conn
        
        # 创建数据库表
        cur = self.__conn.cursor()
        cur.execute('''
            CREATE TABLE IF NOT EXISTS `%s` (
                `id` INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
                `timestamp` INT NOT NULL,
                `content` TEXT NOT NULL
            )''' % (self.__table))
        self.__conn.commit()
        cur.close()

    def getCount(self):
        '''获取post个数
        
        Returns:
            ['status': '<状态>', 'message': '<错误描述>', 'data': ['count': <post个数>]]
        '''
        # 查询数据库
        cur = self.__conn.cursor()
        cur.execute('SELECT COUNT(`id`) AS count FROM `%s`' % (self.__table))
        res = cur.fetchone()
        cur.close()
        post = self.__convertCount(res)
        return {'status': 'ok', 'data': post}

    def getAll(self):
        '''获取所有post
        
        Returns:
            ['status': '<状态>', 'message': '<错误描述>', 'data': [['id': <post ID>, 'timestamp': <post时间戳，单位：s>, 'content': '<post内容>'], ...]]
        '''
        # 查询数据库
        cur = self.__conn.cursor()
        cur.execute('SELECT `id`,`timestamp`,`content` FROM `%s` ORDER BY `id` DESC' % (self.__table))
        res = cur.fetchall()
        cur.close()
        post = self.__convertMultiPost(res)
        return {'status': 'ok', 'data': post}

    def getById(self, param):
        '''根据ID获取post
        
        Args:
            param: ['id': <post ID>]
        
        Returns:
            ['status': '<状态>', 'message': '<错误描述>', 'data': ['id': <post ID>, 'timestamp': <post时间戳，单位：s>, 'content': '<post内容>']]
        '''
        # 校验id参数
        id, err = checker.checkParamId(param)
        if err:
            return err
        
        # 查询数据库，如果没有数据返回错误
        cur = self.__conn.cursor()
        cur.execute('SELECT `id`,`timestamp`,`content` FROM `%s` WHERE `id`=%d LIMIT 1' % (self.__table, id))
        res = cur.fetchone()
        cur.close()
        if not res:
            return {'status': 'none_target', 'message': '`id` does not exist'}
        post = self.__convertOnePost(res)
        return {'status': 'ok', 'data': post}

    def add(self, param):
        '''添加post
        
        Args:
            param: ['content': '<post内容>']
        
        Returns:
            ['status': '<状态>', 'message': '<错误描述>', 'data': ['id': <post ID>, 'timestamp': <post时间戳，单位：s>, 'content': '<post内容>']]
        '''
        # 校验content参数
        content, err = checker.checkParamContent(param)
        if err:
            return err
        
        # 获取当前系统时间
        timestamp = int(time.mktime(datetime.now().timetuple()))
        
        # 查询数据库
        cur = self.__conn.cursor()
        cur.execute('INSERT INTO `%s` SET `timestamp`=%d,`content`="%s"' % (self.__table, timestamp, content))
        self.__conn.commit()
        id = cur.lastrowid
        cur.close()
        return {'status': 'ok', 'data': {'id': id, 'timestamp': timestamp, 'content': content}}

    def modify(self, param):
        '''修改post
        
        Args:
            param: ['id': <post ID>]
        
        Returns:
            ['status': '<状态>', 'message': '<错误描述>', 'data': ['id': <post ID>, 'timestamp': <post时间戳，单位：s>, 'content': '<post内容>']]
        '''
        # 校验id参数
        id, err = checker.checkParamId(param)
        if err:
            return err
        
        # 校验content参数
        content, err = checker.checkParamContent(param)
        if err:
            return err
        
        # 查询数据库，如果没有数据修改返回错误
        values = []
        if content:
            values.append('`content`="' + content + '"')
        cmd = 'UPDATE `' + self.__table + '` SET ' + ','.join(values) + ' WHERE `id`=' + str(id)
        cur = self.__conn.cursor()
        cur.execute(cmd)
        self.__conn.commit()
        modified = cur.rowcount == 1
        cur.close()
        if not modified:
            return {'status': 'none_target', 'message': '`id` does not exist'}
        return self.getById({'id':id})

    def remove(self, param):
        '''删除post
        
        Args:
            param: ['id': <post ID>]
        
        Returns:
            ['status': '<状态>', 'message': '<错误描述>', 'data': ['id': <post ID>]]
        '''
        # 校验id参数
        id, err = checker.checkParamId(param)
        if err:
            return err
        
        # 查询数据库，如果没有数据删除返回错误
        cur = self.__conn.cursor()
        cur.execute('DELETE FROM `%s` WHERE `id`=%d' % (self.__table, id))
        self.__conn.commit()
        deleted = cur.rowcount == 1
        cur.close()
        if not deleted:
            return {'status': 'none_target', 'message': '`id` does not exist'}
        return {'status': 'ok', 'data': {'id': id}}

    def __convertMultiPost(self, res):
        '''转换多个post查询结果
        
        Args:
            res: [(<Post ID>, <Post时间戳>, '<Post内容>')]
        
        Returns:
            [{'id': <Post ID>, 'timestamp': <Post时间戳>, 'content': '<Post内容>'}, ...]
        '''
        data = []
        for id, timestamp, content in res:
            data.append({'id': id, 'timestamp': timestamp, 'content': content.encode('utf-8')})
        return data

    def __convertOnePost(self, res):
        '''转换单个post查询结果
        
        Args:
            res: (<Post ID>, <Post时间戳>, '<Post内容>')
        
        Returns:
            (dict) {'id': <Post ID>, 'timestamp': <Post时间戳>, 'content': '<Post内容>'}
        '''
        id, timestamp, content = res
        return {'id': id, 'timestamp': timestamp, 'content': content.encode('utf-8')}

    def __convertCount(self, res):
        '''转换ost个数查询结果
        
        Args:
            res: (<Post个数>, )
        
        Returns:
            {'count': <Post个数>}
        '''
        count, = res
        return {'count': count}
