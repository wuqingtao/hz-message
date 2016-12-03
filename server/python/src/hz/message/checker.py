#!/usr/bin/env python
#-*-coding: utf-8-*-

'''校验请求参数是否合法'''

def checkParamType(param):
    '''校验type参数是否合法

    Args:
        param: {'type': '<type值>', ...}

    Returns:
        ('<type值>', {'status': '<错误状态>', 'message': '<错误描述>'})
    '''
    if not param.has_key('type'):
        return (None, {'status': 'lost_parameter', 'message': '`type` is necessary.'})
        
    type = param['type']
    if type == None or not isinstance(type, str):
        return (None, {'status': 'invalid_parameter', 'message': '`type` should be string.'})
    
    if not type:
        return (None, {'status': 'invalid_parameter', 'message': '`type` should not be empty.'})
    
    return (type, None)

def checkParamId(param):
    '''校验id参数是否合法

    Args:
        param: {'id': <id值>, ...}

    Returns:
        (<id值>, {'status': '<错误状态>', 'message': '<错误描述>'})
    '''
    if not param.has_key('id'):
        return (None, {'status': 'lost_parameter', 'message': '`id` is necessary.'})
        
    id = param['id']
    if type == None or not isinstance(id, int):
        return (None, {'status': 'invalid_parameter', 'message': '`id` should be int.'})
    
    return (id, None)

def checkParamContent(param):
    '''校验content参数是否合法

    Args:
        param: {'content': '<content值>', ...}

    Returns:
        ('<content值>', {'status': '<错误状态>', 'message': '<错误描述>'})
    '''
    if not param.has_key('content'):
        return (None, {'status': 'lost_parameter', 'message': '`content` is necessary.'})
        
    content = param['content']
    if content == None or not isinstance(content, str):
        return (None, {'status': 'invalid_parameter', 'message': '`content` should be string.'})
    
    if not content:
        return (None, {'status': 'invalid_parameter', 'message': '`content` should not be empty.'})
    
    return (content, None)
