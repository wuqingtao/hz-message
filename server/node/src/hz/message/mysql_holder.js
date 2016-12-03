#!/usr/bin/env node

'use strict';

exports.MysqlHolder = MysqlHolder;

const mysql = require('mysql');

/**
 * mysql实体类
 * <p>
 * mysql实体类实现mysql连接和关闭，及数据库创建和销毁
 *
 * @param {string} user mysql用户名
 * @param {string} password mysql用户密码
 * @param {string} database mysql数据库名
 * @param {function} callback 回调函数 ({Error} err 错误)
 */
function MysqlHolder(user, password, database, callback) {
	let _this = this;

	// 保存数据库名
	_this._database = database;

	// 创建数据库连接
	_this._conn = mysql.createConnection({user: user, password: password});	
	_this._conn.connect(function(err) {
		if (err) {
			callback(err);
			return;
		}

		// 创建数据库
		_this._conn.query('CREATE DATABASE IF NOT EXISTS ?? DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci', [_this._database], function(cerr) {
			if (cerr) {
				callback(cerr);
				return;
			}

			// 使用数据库
			_this._conn.query('USE ??', [_this._database], function(uerr) {
				callback(uerr);
			});
		});
	});
}

/**
 * 获取数据库连接
 */
MysqlHolder.prototype.inst = function() {
	return this._conn;
}

/**
 * 销毁数据库
 *
 * @param {function} callback 回调函数 ({Error} err 错误)
 */
MysqlHolder.prototype.destroy = function(callback) {
	this._conn.query('DROP DATABASE IF EXISTS ??', [this._database], function(err) {
		callback(err);
	});
}

/**
 * 关闭数据库连接
 *
 * @param {function} callback 回调函数 ({Error} err 错误)
 */
MysqlHolder.prototype.close = function(callback) {
	this._conn.end(function(err) {
		callback(err);
	});
}
