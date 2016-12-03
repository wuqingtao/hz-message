#!/usr/bin/env python
#-*-coding: utf-8-*-

'''测试mysql_Post'''

import os, sys
sys.path.append(os.path.join(os.path.dirname(os.path.realpath(__file__)), '../../../src/'))
from hz.message.mysql_holder import MysqlHolder
from hz.message.mysql_post import MysqlPost
import mysql.connector
import time
from datetime import datetime
import unittest

# 数据库用户
_user = 'root'
# 数据库密码
_password = 'Ningning~1'
# 数据库名称
_database = 'test_message'

class MysqlPostTest(unittest.TestCase):
    '''测试mysql_holder.MysqlPost'''
    # 测试post
    __testPosts = [
        {'content': 'abcdefghijklmnopqrstuvwxyz'},
        {'content': 'ABCDEFGHIJKLMNOPQRSTUVWXYZ'},
        {'content': '1234567890'},
        {'content': '红尘世界？一片雾茫茫！'},
        {'content': '<p>红尘世界？<em>一片雾茫茫！</em></p>'},
    ]

    def setUp(self):
        self.__holder = MysqlHolder(_user, _password, _database)
        self.__post = MysqlPost(self.__holder.inst())

    def tearDown(self):
        self.__holder.destroy()
        self.__holder.close()

    def test_getCount(self):
        '''测试mysql_post.MysqlPost.getCount()'''
        # 测试初始post个数为0
        res = self.__post.getCount()
        self.assertEqual(res, {'status': 'ok', 'data': {'count': 0}})

        # 添加测试post
        for post in self.__testPosts:
            self.__post.add(post)
            
        # 测试post个数为添加的post个数
        res = self.__post.getCount()
        self.assertEqual(res, {'status': 'ok', 'data': {'count': len(self.__testPosts)}})

    def test_getAll(self):
        '''测试mysql_post.MysqlPost.getAll()'''
        # 测试初始的post为空
        res = self.__post.getAll()
        self.assertEqual(res, {'status': 'ok', 'data': []})

        # 添加测试post
        for post in self.__testPosts:
            self.__post.add(post)

        # 测试获取的post时间递减，和添加的post顺序相反、content一致
        res = self.__post.getAll()
        self.assertEqual(res['status'], 'ok')
        posts = res['data']
        self.assertEqual(len(posts), len(self.__testPosts))
        savedTimestamp = sys.maxint
        for i in range(len(posts)):
            post = posts[i]
            timestamp = post['timestamp']
            self.assertTrue(timestamp <= savedTimestamp)
            savedTimestamp = timestamp;
            content = post['content']
            self.assertEqual(content, self.__testPosts[len(self.__testPosts) - i - 1]['content'])

    def test_getById(self):
        '''测试mysql_post.MysqlPost.getById()'''
        # 测试对空的post，通过id获取错误
        res = self.__post.getById({'id': sys.maxint})
        self.assertDictContainsSubset({'status': 'none_target'}, res)

        # 添加测试post
        for post in self.__testPosts:
            self.__post.add(post)

        # 获取所有的post，用于比对
        res = self.__post.getAll()
        posts = res['data']

        # 测试对每一个添加的post，和通过对应的id获取的post一致
        for post in posts:
            res = self.__post.getById({'id': post['id']})
            self.assertEqual(res, {'status': 'ok', 'data': post})

    def test_add(self):
        '''测试mysql_post.MysqlPost.add()'''
        # 添加post
        res = self.__post.add({'content': 'content'})
        
        # 测试添加状态ok
        self.assertDictContainsSubset({'status': 'ok'}, res)
        
        # 测试添加的id合法
        id = res['data']['id']
        self.assertTrue(id > 0)
        
        # 测试添加的timestamp合法
        timestamp = res['data']['timestamp']
        self.assertTrue(isinstance(timestamp, int) and timestamp > 0)
        
        # 测试添加的content正确
        content = res['data']['content']
        self.assertEqual(content, 'content')

    def test_modify(self):
        '''测试mysql_post.MysqlPost.modify()'''
        # 添加测试post
        self.__post.add({'content': 'content'})

        # 获取post
        res = self.__post.getAll()
        prePost = res['data'][0]

        # 修改post
        res = self.__post.modify({'id': prePost['id'], 'content': 'CONTENT'})
        
        # 测试修改状态ok
        self.assertDictContainsSubset({'status': 'ok'}, res)
        
        # 保存修改后的post
        modifiedPost = res['data']

        # 再次获取post
        res = self.__post.getAll()
        nowPost = res['data'][0]

        # 测试获取的id和修改后的id一致
        self.assertEqual(prePost['id'], modifiedPost['id'])
        
        # 测试获取的timestamp和修改后的timestamp一致
        self.assertEqual(prePost['timestamp'], modifiedPost['timestamp'])
        
        # 测试修改后的content正确
        self.assertEqual(modifiedPost['content'], 'CONTENT')
        
        # 测试修改后的post和再次获取的post一致
        self.assertEqual(modifiedPost, nowPost)

    def test_remove(self):
        '''测试mysql_post.MysqlPost.remove()'''
        # 添加post
        self.__post.add({'content': 'content'})

        # 获取post ID
        res = self.__post.getAll()
        id = res['data'][0]['id']

        # 删除post
        res = self.__post.remove({'id': id})
        
        # 测试删除结果
        self.assertEqual(res, {'status': 'ok', 'data': {'id': id}})

if __name__ == '__main__':
    unittest.main()
