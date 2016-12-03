#!/usr/bin/env node

'use strict';

const assert = require('assert');
const mysql_holder = require('../../../src/hz/message/mysql_holder.js');

/** mysql用户名 */
const user = 'root';
/** mysql用户密码 */
const password = 'Ningning~1';
/** mysql数据库名 */
const database = 'test_message';

describe('mysql_holder.MysqlHolder', function() {
	beforeEach(function(done) {
		this._hdr = new mysql_holder.MysqlHolder(user, password, database, function(err) {
			assert(!err);
			
			done();
		});
	});

	afterEach(function(done) {
		this._hdr.destroy(function() {});
        this._hdr.close(function() {
			done();
		});
	});
	
	it('mysql_holder.MysqlHolder.inst()', function() {
		this.timeout(0);
		
		// 测试返回的对象为数据库连接对象
		const conn = this._hdr.inst();
		assert('query' in conn);
	});

	it('mysql_holder.MysqlHolder.destroy()', function(done) {
		this.timeout(0);
		
		const conn = this._hdr.inst();

		// 测试数据库没有销毁
		conn.query('SHOW TABLES', function(err) {
			assert(!err);
		});
		
		// 执行destroy
		this._hdr.destroy(function(err) {
			assert(!err);
		});

		// 测试数据库已经销毁
		conn.query('SHOW TABLES', function(err) {
			assert(err);
			
			done();
		});
	});

	it('mysql_holder.MysqlHolder.close()', function(done) {
		this.timeout(0);
		
		// 测试数据库连接没有关闭
		// TODO:

		// 执行close
		this._hdr.close(function(err){
			assert(!err);

			// 测试数据库连接已经关闭
			// TODO:
			
			done();
		});
	});
});
