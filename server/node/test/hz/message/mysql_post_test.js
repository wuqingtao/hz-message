#!/usr/bin/env node

'use strict';

const assert = require('assert');
const mysql_holder = require('../../../src/hz/message/mysql_holder.js');
const mysql_post = require('../../../src/hz/message/mysql_post.js');

// 数据库用户
const user = 'root';
// 数据库密码
const password = 'Ningning~1';
// 数据库名称
const database = 'test_message';

// 测试Post内容
const testPosts = [
	{'content': 'abcdefghijklmnopqrstuvwxyz'},
	{'content': 'ABCDEFGHIJKLMNOPQRSTUVWXYZ'},
	{'content': '1234567890'},
	{'content': '红尘世界？一片雾茫茫！'},
	{'content': '<p>红尘世界？<em>一片雾茫茫！</em></p>'},
];

describe('mysql_post.MysqlPostTest', function() {
	beforeEach(function(done) {
		let _this = this;
		
		_this._hdr = new mysql_holder.MysqlHolder(user, password, database, function() {
			_this._pst = new mysql_post.MysqlPost(_this._hdr.inst(), function(err) {
				assert(!err);
				
				done();
			});
		});
	});

	afterEach(function(done) {
		let _this = this;
		
		_this._hdr.destroy(function() {});
		_this._hdr.close(function() {
			done();
		});
	});
	
	it('mysql_post.MysqlPostTest.getCount()', function(done) {
		let _this = this;
		
		this.timeout(0);
		
		// 测试初始post个数为0
		_this._pst.getCount(function(err, res) {
            assert(!err);
			assert.deepEqual(res, {status: 'ok', data: {'count': 0}});
		});
		
		// 添加测试post
		for (let i in testPosts) {
			_this._pst.add(testPosts[i], function() {});
		}
					
		// 测试post个数为添加的post个数
		_this._pst.getCount(function(err, res) {
            assert(!err);
			assert.deepEqual(res, {status: 'ok', data: {'count': testPosts.length}});
			
			done();
		});
	});
	
	it('mysql_post.MysqlPostTest.getAll()', function(done) {
		let _this = this;
		
		_this.timeout(0);
		
		// 测试初始的post为空
		_this._pst.getAll(function(err, res) {
            assert(!err);
			assert.deepEqual(res, {'status': 'ok', 'data': []});
		});
		
		// 添加测试post
		for (let i in testPosts) {
			_this._pst.add(testPosts[i], function() {});
		}
		
		// 测试获取的post时间递减，和添加的post顺序相反、content一致
		_this._pst.getAll(function(err, res) {
            assert(!err);
			assert.equal(res.status, 'ok');
			let posts = res.data;
			assert.equal(posts.length, testPosts.length);
			let savedTimestamp = Number.MAX_SAFE_INTEGER;
			for (let i in posts) {
				let post = posts[i];
				let timestamp = post.timestamp;
				assert(timestamp <= savedTimestamp);
				savedTimestamp = timestamp;
				let content = post.content;
				assert.equal(content, testPosts[testPosts.length - i - 1].content);
			}
			
			done();
		});
	});
	
	it('mysql_post.MysqlPostTest.getById()', function(done) {
		let _this = this;
		
		_this.timeout(0);
		
		// 测试对空的post，通过id获取错误
		_this._pst.getById({id: Number.MAX_SAFE_INTEGER}, function(err, res) {
            assert(!err);
			assert.equal(res.status, 'none_target');
		});
		
		// 添加测试post		
		for (let i in testPosts) {
			_this._pst.add(testPosts[i], function() {});
		}

		// 测试对每一个添加的post，和通过对应的id获取的post一致
		_this._pst.getAll(function(err, res) {
			let posts = res.data;
			for (let i in posts) {
				let post = posts[i];
				let id = post.id;
				_this._pst.getById({id: id}, function(ierr, ires) {
                    assert(!ierr);
					assert.deepEqual(ires, {status: 'ok', data: post});
					
					if (i == posts.length - 1) {
						done();
					}
				});
			}
		});
	});
	
	it('mysql_post.MysqlPostTest.add()', function(done) {
		let _this = this;
		
		_this.timeout(0);
		
		// 添加post
		_this._pst.add({content: 'content'}, function(err, res) {
            assert(!err);
            
			// 测试添加状态ok
			assert.equal(res.status, 'ok');
			
			// 测试添加的id合法
			let post = res.data;
			let id = post.id;
			assert(typeof id == 'number' && id > 0);

			// 测试添加的timestamp合法
			let timestamp = post.timestamp;
			assert(typeof timestamp == 'number' && timestamp > 0);
			
			// 测试添加的content正确
			let content = post.content;
			assert.equal(content, 'content');
			
			done();
		});
	});
	
	it('mysql_post.MysqlPostTest.modify()', function(done) {
		let _this = this;
		
		_this.timeout(0);
		
		// 添加测试post
		_this._pst.add({content:'content'}, function() {});
		
		// 获取post
		_this._pst.getAll(function(err, res) {
			let prePost = res.data[0];
			
			// 修改post
			_this._pst.modify({id:prePost.id, content:'CONTENT'}, function(merr, mres) {
                assert(!merr);
                
				// 测试修改状态ok
				assert.equal(mres.status, 'ok');
				
				// 保存修改后的post
				let modifiedPost = mres.data;
				
				// 再次获取post
				_this._pst.getAll(function(aerr, ares) {
					let nowPost = ares.data[0];
					
					// 测试获取的id和修改后的id一致
					assert.equal(prePost.id, modifiedPost.id);
					
					// 测试获取的timestamp和修改后的timestamp一致
					assert.equal(prePost.timestamp, modifiedPost.timestamp);
					
					// 测试修改后的content正确
					assert.equal(modifiedPost.content, 'CONTENT');
					
					// 测试修改后的post和再次获取的post一致
					assert.deepEqual(modifiedPost, nowPost);
					
					done();
				});
			});
		});
	});
	
	it('mysql_post.MysqlPostTest.remove()', function(done) {
		let _this = this;
		
		_this.timeout(0);

		// 添加post
		_this._pst.add({content: 'content'}, function() {});
		
		// 获取post ID
		_this._pst.getAll(function(err, res) {
			let id = res.data[0].id;
			
			// 删除post
			_this._pst.remove({id: id}, function(rerr, rres) {
				// 测试删除结果
                assert(!rerr);
				assert.deepEqual(rres, {status: 'ok', data: {id: id}});
				
				done();
			});
		});
	});
});
