#!/usr/bin/env node

'use strict';

// 导出模块
exports.createMessageByMysql = createMessageByMysql;

// 引入模块
const mysql_holder = require('./mysql_holder.js');
const mysql_post = require('./mysql_post.js');
const message = require('./message.js');

/**
 * 创建mysql类message
 *
 * @param {string} user mysql用户名
 * @param {string} password mysql用户密码
 * @param {string} database mysql数据库名
 * @param {function} callback 回调函数 ({Error} err 错误, {Message} msg Message对象)
 */
function createMessageByMysql(user, password, database, callback) {
    const hdr = new mysql_holder.MysqlHolder(user, password, database, function(err) {
		if (err) {
			callback(err);
			return;
		}
	
		const pst = new mysql_post.MysqlPost(hdr.inst(), function(perr) {
			if (perr) {
				callback(perr);
				return;
			}
			
			let msg = new message.Message(hdr, pst);
			callback(undefined, msg);
		});		
	});
}
