#!/usr/bin/env node

'use strict';

exports.MysqlPost = MysqlPost;

const checker = require('./checker');

/**
 * mysql信息类
 * <p>
 * mysql信息类实现对数据库表post的创建、添加、查询、修改和删除
 *
 * @param {mysql.Connection} conn 数据库连接
 * @param {function} callback 回调函数 ({Error} err 错误)
 */
function MysqlPost(conn, callback) {
	// 数据库表名
	this._table = 'post';

	// 保存数据库连接
	this._conn = conn;

	// 创建数据库表
	this._conn.query('CREATE TABLE IF NOT EXISTS ?? (`id` INT NOT NULL PRIMARY KEY AUTO_INCREMENT, `timestamp` INT NOT NULL, `content` TEXT NOT NULL)', [this._table], function(err) {
		callback(err);
	});
}

/**
 * 获取post个数
 *
 * @param {function} callback 回调函数 ({Error} err 错误，{*} res {'status': '<状态>', 'message': '<错误描述>', 'data': {'count': <post个数>}})
 */
MysqlPost.prototype.getCount = function(callback) {
	// 查询数据库
	this._conn.query('SELECT COUNT(`id`) AS count FROM ??', [this._table], function(err, results) {
		if (err) {
			callback(err);
		} else {
			let res = {status: 'ok', data: results[0]};
			callback(undefined, res);
		}
	});
}

/**
 * 获取所有post
 *
 * @param {function} callback 回调函数 ({*} err 错误，{*} res {'status': '<状态>', 'message': '<错误描述>', 'data': [{'id': <post ID>, 'timestamp': <post时间戳，单位：s>, 'content': '<post内容>'}, ...]})
 */
MysqlPost.prototype.getAll = function(callback) {
	// 查询数据库
	this._conn.query('SELECT `id`, `timestamp`, `content` FROM ?? ORDER BY `id` DESC', [this._table], function(err, results) {
		if (err) {
			callback(err);
		} else {
			let posts = [];
			for (let i in results) {
				posts.push(results[i]);
			}
			let res = {status: 'ok', data: posts};
			callback(undefined, res);
		}
	});
}

/**
 * 根据ID获取post
 *
 * @param {*} param {'id': <post ID>}
 * @param {function} callback 回调函数 ({*} err 错误，{*} res {'status': '<状态>', 'message': '<错误描述>', 'data': {'id': <post ID>, 'timestamp': <post时间戳，单位：s>, 'content': '<post内容>'}})
 */
MysqlPost.prototype.getById = function(param, callback) {
	// 校验id参数
	let [id, idErr] = checker.checkParamId(param);
	if (idErr) {
		callback(undefined, idErr);
		return;
	}

	// 查询数据库，如果没有数据返回错误
	this._conn.query('SELECT `id`, `timestamp`, `content` FROM ?? WHERE `id`=? LIMIT 1', [this._table, id], function(err, results) {
		if (err) {
			callback(err);
		} else {
			let res = results.length != 0 ? {status: 'ok', data: results[0]} : {status: 'none_target', message: '`id` does not exist.'};
			callback(undefined, res);
		}
	});
}

/**
 * 添加post
 *
 * @param {*} param {'content': '<post内容>'}
 * @param {function} callback 回调函数 ({*} err 错误，{*} res {'status': '<状态>', 'message': '<错误描述>', 'data': {'id': <post ID>, 'timestamp': <post时间戳，单位：s>, 'content': '<post内容>'}})
 */
MysqlPost.prototype.add = function(param, callback) {
	// 校验content参数
	let [content, contentErr] = checker.checkParamContent(param);
	if (contentErr) {
		callback(undefined, contentErr);
		return;
	}

	// 获取当前系统时间
	let timestamp = Math.floor(new Date().getTime() / 1000);
	
	// 查询数据库
	this._conn.query('INSERT INTO ?? SET `timestamp`=?, `content`=?', [this._table, timestamp, content], function(err, result) {
		if (err) {
			callback(err);
		} else {
			let res = {'status': 'ok', 'data': {'id': result.insertId, 'timestamp': timestamp, 'content': content}};
			callback(undefined, res);
		}
	});
}

/**
 * 修改post
 *
 * @param {*} param {'id': <post ID>, 'content': '<post内容>'}
 * @param {function} callback 回调函数 ({*} err 错误，{*} res {'status': '<状态>', 'message': '<错误描述>', 'data': {'id': <post ID>, 'timestamp': <post时间戳，单位：s>, 'content': '<post内容>'}})
 */
MysqlPost.prototype.modify = function(param, callback) {
	// 校验id参数
	let [id, idErr] = checker.checkParamId(param);
	if (idErr) {
		callback(undefined, idErr);
		return;
	}
	
	// 校验content参数
	let [content, contentErr] = checker.checkParamContent(param);
	if (contentErr) {
		callback(undefined, contentErr);
		return;
	}

	// 查询数据库，如果没有数据修改返回错误
	let _this = this;
	this._conn.query('UPDATE ?? SET `content`=? WHERE `id`=?', [this._table, content, id], function(err, result) {
		if (err) {
			callback(err);
		} else {
			if (result.affectedRows == 1) {
				_this.getById({id: id}, function(gerr, gres) {
					callback(gerr, gres)
				});
			} else {
				let res = {status: 'none_target', message: '`id` does not exist.'};
				callback(undefined, res);
			}
		}
	});
}

/**
 * 删除post
 *
 * @param {*} param {'id': <post ID>}
 * @param {function} callback 回调函数 ({*} err 错误，{*} res {'status': '<状态>', 'message': '<错误描述>', 'data': {'id': <post ID>}})
 */
MysqlPost.prototype.remove = function(param, callback) {
	// 校验id参数
	let [id, idErr] = checker.checkParamId(param);
	if (idErr) {
		callback(undefined, idErr);
		return;
	}

	// 查询数据库，如果没有数据删除返回错误
	this._conn.query('DELETE FROM ?? WHERE `id`=?', [this._table, id], function(err, result) {
		if (err) {
			callback(err);
		} else {
			let res = result.affectedRows == 1 ? {status: 'ok', data: {id: id}} : {status: 'none_target', message: '`id` does not exist.'};
			callback(undefined, res);
		}
	});
}
