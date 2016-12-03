#!/usr/bin/env python
#-*-coding: utf-8-*-

'''测试message'''

import os, sys
sys.path.append(os.path.join(os.path.dirname(os.path.realpath(__file__)), '../../../src/'))
from hz.message.message import Message
import unittest

class test_Message(unittest.TestCase):
    '''测试message.Message'''
    def setUp(self):
        holder = TestHolder()
        self.__post = TestPost()
        self.__message = Message(holder, self.__post)
        
    def tearDown(self):
        self.__message.close();
    
    def test_request(self):
        '''测试message.Message.request()'''
        # 测试{'type': 'get_post_count'}
        self.assertEqual(self.__message.request({'type': 'get_post_count'}), self.__post.getCount())
        
        # 测试{'type': 'get_all_post'}
        self.assertEqual(self.__message.request({'type': 'get_all_post'}), self.__post.getAll())
        
        # 测试{'type': 'get_post_by_id'}
        self.assertEqual(self.__message.request({'type': 'get_post_by_id'}), self.__post.getById({}))
        
        # 测试{'type': 'add_post'}
        self.assertEqual(self.__message.request({'type': 'add_post'}), self.__post.add({}))
        
        # 测试{'type': 'modify_post'}
        self.assertEqual(self.__message.request({'type': 'modify_post'}), self.__post.modify({}))
        
        # 测试{'type': 'remove_post'}
        self.assertEqual(self.__message.request({'type': 'remove_post'}), self.__post.remove({}))
        
        # 测试{'type': 'other'}
        self.assertEqual(self.__message.request({'type': 'other'})['status'], 'invalid_parameter')

class TestHolder:
    '''模拟holder类'''
    def inst(self):
        return None

    def close(self):
        return None

    def destroy(self):
        return None

class TestPost:
    '''模拟post类'''
    def getCount(self):
        return {'status': 'ok', 'data': {'count': 1234}}

    def getAll(self):
        return {'status': 'ok', 'data': [{'id': 1234, 'timestamp': 123456, 'content': 'abcd'}, {'id': 12345, 'timestamp': 1234567, 'content': 'abcde'}]}

    def getById(self, data):
        return {'status': 'ok', 'data': {'id': 1234, 'timestamp': 123456, 'content': 'abcd'}}

    def add(self, data):
        return {'status': 'ok', 'data': {'id': 1234, 'timestamp': 123456, 'content': 'abcd'}}

    def modify(self, data):
        return {'status': 'ok', 'data': {'id': 1234, 'timestamp': 123456, 'content': 'abcd'}}

    def remove(self, data):
        return {'status': 'ok', 'data': {'id': 1234}}

if __name__ == '__main__':
    unittest.main()
