#!/usr/bin/env python
#-*-coding: utf-8-*-

'''测试checker'''

import os, sys
sys.path.append(os.path.join(os.path.dirname(os.path.realpath(__file__)), '../../../src/'))
from hz.message.checker import *
import unittest

class test_checker(unittest.TestCase):
    '''测试checker'''
    def test_checkParamType(self):
        '''测试checker.checkParamType()'''
        # 测试空参数
        res = checkParamType({})
        self.assertTrue(res[0] == None and res[1]['status'] == 'lost_parameter')
        
        # 测试非法key参数
        res = checkParamType({'abcd': '1234'})
        self.assertTrue(res[0] == None and res[1]['status'] == 'lost_parameter')
        res = checkParamType({'Type': '1234'})
        self.assertTrue(res[0] == None and res[1]['status'] == 'lost_parameter')
        
        # 测试非法value参数
        res = checkParamType({'type': 1234})
        self.assertTrue(res[0] == None and res[1]['status'] == 'invalid_parameter')
        res = checkParamType({'type': ''})
        self.assertTrue(res[0] == None and res[1]['status'] == 'invalid_parameter')
        
        # 测试合法参数
        res = checkParamType({'type': '1234'})
        self.assertTrue(res[0] == '1234' and res[1] == None)

    def test_checkParamId(self):
        '''测试checker.checkParamId()'''
        # 测试空参数
        res = checkParamId({})
        self.assertTrue(res[0] == None and res[1]['status'] == 'lost_parameter')
        
        # 测试非法key参数
        res = checkParamId({'abcd': 1234})
        self.assertTrue(res[0] == None and res[1]['status'] == 'lost_parameter')
        res = checkParamId({'Id': 1234})
        self.assertTrue(res[0] == None and res[1]['status'] == 'lost_parameter')
        
        # 测试非法value参数
        res = checkParamId({'id': '1234'})
        self.assertTrue(res[0] == None and res[1]['status'] == 'invalid_parameter')

        # 测试合法参数
        res = checkParamId({'id': 1234})
        self.assertTrue(res[0] == 1234 and res[1] == None)

    def test_checkParamContent(self):
        '''测试checker.checkParamContent()'''
        # 测试空参数
        res = checkParamContent({})
        self.assertTrue(res[0] == None and res[1]['status'] == 'lost_parameter')
        
        # 测试非法key参数
        res = checkParamContent({'abcd': '1234'})
        self.assertTrue(res[0] == None and res[1]['status'] == 'lost_parameter')
        res = checkParamContent({'Content': '1234'})
        self.assertTrue(res[0] == None and res[1]['status'] == 'lost_parameter')
        
        # 测试非法value参数
        res = checkParamContent({'content': 1234})
        self.assertTrue(res[0] == None and res[1]['status'] == 'invalid_parameter')
        res = checkParamContent({'content': ''})
        self.assertTrue(res[0] == None and res[1]['status'] == 'invalid_parameter')
        
        # 测试合法参数
        res = checkParamContent({'content': '1234'})
        self.assertTrue(res[0] == '1234' and res[1] == None)

if __name__ == '__main__':
    unittest.main()
