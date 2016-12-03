#!/usr/bin/env python
#-*-coding: utf-8-*-

'''FCGI服务'''

import os
import utils.dict_parser as dict_parser
import message_creator
from flup.server.fcgi import WSGIServer
import logging, logging.handlers
import traceback

# 数据库用户
_user = 'root'
# 数据库密码
_password = 'Ningning~1'
# mysql数据库名
_database = "message";

# 日志对象
_logger = None

def application(environ, start_response):
    '''WSGIServer回调函数
    
    Args:
        environ: FCGI请求环境变量
        start_response: 响应回调函数
    '''
    # 仅支持POST请求，否则返回HTTP 501
    requestmethod = environ['REQUEST_METHOD']
    if requestmethod != 'POST':
        start_response('501 Not Implemented', [])
        return []
    
    # 获取请求参数
    paramLen = int(environ.get('CONTENT_LENGTH', 0))
    paramStr = environ['wsgi.input'].read(paramLen)
    
    try:
        # 将请求参数从str转换为dict
        param = dict_parser.decode(paramStr)
        
        # 创建message对象
        message = message_creator.createMessageByMysql(_user, _password, _database)
        
        # 处理请求
        result = message.request(param)
        
        # 关闭message对象
        message.close()
        
        # 将返回结果从dict转换为str
        resultStr = dict_parser.encode(result)
    
        # 记录处理日志
        _logger.info('req:%s res:%s' % (paramStr, resultStr))

        # 返回处理结果
        status = '200 OK'
        headers = [('Content-type', 'text/plain;charset=UTF-8')]
        start_response(status, headers)
        return [resultStr]
    except Exception, e:
        # 如果处理结果出现异常，记录异常日志，返回HTTP 500
        _logger.exception(e)
        start_response('500 Internal Server Error', [])
        return []

if __name__  == '__main__':
    # 创建日志对象
    logPath = os.path.join(os.path.dirname(os.path.realpath(__file__)), '../../logs/')
    if not os.path.exists(logPath):
        os.makedirs(logPath)
    logFile = os.path.join(logPath, 'message.log')
    handler = logging.handlers.RotatingFileHandler(logFile, maxBytes=8*024*1024)
    formatter = logging.Formatter('[%(asctime)s] [%(levelname)s] %(name)s - %(message)s', '%Y-%b-%d %H:%M:%S')
    handler.setFormatter(formatter)
    _logger = logging.getLogger('message')
    _logger.addHandler(handler)
    _logger.setLevel(logging.DEBUG)

    # # Run directly. ONLY FOR TEST.
    # WSGIServer(application, multithreaded = True, multiprocess = False, bindAddress = ('127.0.0.1', 8401)).run()

    # Run the server by fcgi wrapper.
    WSGIServer(application).run()
