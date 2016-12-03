#!/usr/bin/env python
#-*-coding: utf-8-*-

'''该模块定义了str和dict之间的转换操作

将指定字符编码的字符串转换为dict
将dict转换为指定字符编码的字符串
'''

import json

def __decodeList(d):
    '''对list中的元素，将unicode转换为str
    
    Args:
        d: 待转换的list
    
    Returns:
        转换后的list
    '''
    rv = []
    for item in d:
        if isinstance(item, unicode):
            item = item.encode('utf-8')
        elif isinstance(item, list):
            item = __decodeList(item)
        elif isinstance(item, dict):
            item = __decodeDict(item)
        rv.append(item)
    return rv

def __decodeDict(d):
    '''对dict中的key和value，将unicode转换为str
    
    Args:
        d: 待转换的dict
    
    Returns:
        转换后的dict
    '''
    rv = {}
    for key, value in d.iteritems():
        if isinstance(key, unicode):
            key = key.encode('utf-8')
        if isinstance(value, unicode):
            value = value.encode('utf-8')
        elif isinstance(value, list):
            value = __decodeList(value)
        elif isinstance(value, dict):
            value = __decodeDict(value)
        rv[key] = value
    return rv

def decode(s, encoding='utf-8'):
    '''将指定字符编码的字符串转换为dict
    
    Args:
        s: 待转换的字符串
        encoding: 字符串编码
    
    Returns:
        转换后的dict
    '''
    return json.loads(s, encoding = encoding, object_hook = __decodeDict)

def encode(d, encoding='utf-8'):
    '''将dict转换为指定字符编码的字符串
    
    Args:
        d: 待转换的dict
        encoding: 字符串编码
    
    Returns:
        转换后的字符串
    '''
    return json.dumps(d, encoding = encoding, ensure_ascii = False)
