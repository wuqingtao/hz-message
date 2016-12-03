#!/usr/bin/env node

'use strict';

// 创建日志对象
let logger;
(function() {
	const path = require('path');
	const fs = require('fs');
	const log4js = require('log4js');

	let logPath = path.resolve(__dirname, '../../logs/');
	if (!fs.existsSync(logPath)) {
		fs.mkdirSync(logPath);
	}
	let logFile = path.resolve(logPath, 'message.log');
	log4js.configure({
		appenders: [
			{type: 'file', absolute: true, filename: logFile, category: 'message', maxLogSize: 8*1024*1024},
		]
	});
	logger = log4js.getLogger('message');
})();

/** mysql用户名 */
const user = 'root';
/** mysql用户密码 */
const password = 'Ningning~1';
/** mysql数据库名 */
const database = 'message';

// 创建server
(function() {
	const fcgi = require('node-fastcgi');
	const message_creator = require('./message_creator.js');

	fcgi.createServer(function(req, res) {
		// 仅支持POST请求，否则返回HTTP 501
		if (req.method != 'POST') {
			res.writeHead(501);
			res.end();
			return;
		}

		// 请求参数
		let paramStr = "";

		req.on('data', function(data) {
            // 获取请求参数
			paramStr += data.toString();
		});

		// 处理请求
		req.on('end', function() {
			// 将请求参数从string转换为对象
			let param = JSON.parse(paramStr);

			// 创建Message对象
			message_creator.createMessageByMysql(user, password, database, function(err, msg) {
				// 如果有错误发生，记录错误日志，并返回HTTP 500
				if (err) {
					logger.error(err.message + '\n' + err.stack);
					res.writeHead(500);
					res.end();
					return;
				}
				
				// 处理请求
				msg.request(param, function(rerr, result) {
					// 如果有错误发生，记录错误日志，并返回HTTP 500
					if (rerr) {
						logger.error(rerr.message + '\n' + rerr.stack);
						res.writeHead(500);
						res.end();
						return;
					}
					
					// 将请求参数从对象转换为string
					let resultStr = JSON.stringify(result);
					
					// 记录处理日志
					logger.info('req:' + paramStr + ' ' + 'res:' + resultStr);
					
					// 返回处理结果
					res.writeHead(200, {'Content-Type': 'text/plain;charset=UTF-8'});
					res.end(resultStr);
				});
			});
		});
	}).listen();
})();
