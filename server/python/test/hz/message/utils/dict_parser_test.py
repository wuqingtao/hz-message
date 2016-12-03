#!/usr/bin/env python
#-*-coding: utf-8-*-

'''测试dict_parser'''

import os, sys
sys.path.append(os.path.join(os.path.dirname(os.path.realpath(__file__)), '../../../../src/'))
from hz.message.utils.dict_parser import decode, encode
import unittest

class test_dict_parser(unittest.TestCase):
    '''测试dict_parser'''
    def test_decode(self):
        '''测试dict_parser.decode()'''
        # 测试合法参数
        self.assertDictEqual(decode('{}'), {})
        self.assertDictEqual(decode('{"a": 1, "b": "2"}'), {'a': 1, 'b': '2'})
        self.assertDictEqual(decode('{"我": 1, "b": "你"}'), {'我': 1, 'b': '你'})
        
        # 测试非法参数
        with self.assertRaises(TypeError):
            decode(None)
            decode(12)
        with self.assertRaises(ValueError):
            decode('')
            decode('ab')

    def test_encode(self):
        '''测试dict_parser.encode()'''
        # 测试合法参数
        self.assertEqual(encode({}), '{}')
        self.assertEqual(encode({'a': 1, 'b': '2'}), '{"a": 1, "b": "2"}')
        self.assertEqual(encode({'我': 1, 'b': '你'}), '{"b": "你", "我": 1}')
        
        # 测试非法参数
        self.assertEqual(encode(None), 'null')
        self.assertEqual(encode(12), '12')
        self.assertEqual(encode('ab'), '"ab"')

if __name__ == '__main__':
    unittest.main()
