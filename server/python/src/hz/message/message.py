#!/usr/bin/env python
#-*-coding: utf-8-*-

'''消息类'''

import checker

class Message:
    '''消息类
    
    消息类用于处理用户的消息请求，包括添加、查询、修改和删除操作
    '''
    def __init__(self, holder, post):
        '''构造函数
        
        Args:
            holder: holder对象
            post: post对象
        '''
        self.__holder = holder
        self.__post = post

    def request(self, param):
        '''处理请求
        
        Args:
            param: {'type', '<type值>', ...}
        
        Returns:
            {'status': '<状态>', 'message': '<错误描述>', 'data': <结果>}
        '''
        # 校验type参数
        type, err = checker.checkParamType(param)
        if err:
            return err
        
        # 根据不同type值调用post对象处理
        # 如果type值不支持，返回错误
        if type == 'get_post_count':
            return self.__post.getCount()
        elif type == 'get_all_post':
            return self.__post.getAll()
        elif type == 'get_post_by_id':
            return self.__post.getById(param)
        elif type == 'add_post':
            return self.__post.add(param)
        elif type == 'modify_post':
            return self.__post.modify(param)
        elif type == 'remove_post':
            return self.__post.remove(param)
        else:
            return {'status': 'invalid_parameter', 'message': '`type` is invalid.'}

    def close(self):
        '''关闭操作'''
        # 关闭holder对象
        self.__holder.close()
