#!/usr/bin/env node

'use strict';

const assert = require('assert');
const message = require('../../../src/hz/message/message.js');

describe('message.Message', function() {
	beforeEach(function() {
        const hdr = new TestHolder();
        this._pst = new TestPost();
		this._msg = new message.Message(hdr, this._pst);
	});

	afterEach(function() {
		this._msg.close(function(err) {
            assert(!err);
        });
	});
  
	it('message.Message.request()', function() {
		this.timeout(0);
		
		const _this = this;

		// 测试{type: 'getpost_count'}
        _this._msg.request({type: 'get_post_count'}, function(err, res) {
            assert(!err);
			_this._pst.getCount(function callback(gerr, gres) {
                assert(!gerr);
				assert.deepEqual(gres, res);
			});
		});

		// 测试{type: 'get_allpost'}
        _this._msg.request({type: 'get_all_post'}, function(err, res) {
            assert(!err);
			_this._pst.getAll(function callback(gerr, gres) {
                assert(!gerr);
				assert.deepEqual(gres, res);
			});
		});

		// 测试{type: 'get_post_by_id'}
        _this._msg.request({type: 'get_post_by_id'}, function(err, res) {
            assert(!err);
			_this._pst.getById({}, function callback(gerr, gres) {
                assert(!gerr);
				assert.deepEqual(gres, res);
			});
		});

		// 测试{type: 'add_post'}
        _this._msg.request({type: 'add_post'}, function(err, res) {
            assert(!err);
			_this._pst.add({}, function callback(aerr, ares) {
                assert(!aerr);
				assert.deepEqual(ares, res);
			});
		});

		// 测试{type: 'modify_post'}
        _this._msg.request({type: 'modify_post'}, function(err, res) {
            assert(!err);
			_this._pst.modify({}, function callback(merr, mres) {
                assert(!merr);
				assert.deepEqual(mres, res);
			});
		});

		// 测试{type: 'remove_post'}
        _this._msg.request({type: 'remove_post'}, function(err, res) {
            assert(!err);
			_this._pst.remove({}, function callback(rerr, rres) {
                assert(!rerr);
				assert.deepEqual(rres, res);
			});
		});

		// 测试{type: 'other'}
        _this._msg.request({type: 'other'}, function(err, res) {
            assert(!err);
			assert.equal(res.status, 'invalid_parameter');
		});
	});
});

/**
 * 模拟holder类
 */
function TestHolder() {
}

TestHolder.prototype.inst = function() {
}

TestHolder.prototype.close = function(callback) {
	callback(undefined);
}

TestHolder.prototype.destroy = function(callback) {
	callback(undefined);
}

/**
 * 模拟post类
 */
function TestPost() {
}

TestPost.prototype.getCount = function(callback) {
	callback(undefined, {status:'ok', data:{'count':1234}});
}

TestPost.prototype.getAll = function(callback) {
	callback(undefined, {status:'ok', data:[{id:1234, timestamp:123456, content:'abcd'}, {id:12345, timestamp:1234567, content:'abcde'}]});
}

TestPost.prototype.getById = function(data, callback) {
	callback(undefined, {status:'ok', data:{id:1234, timestamp:123456, content:'abcd'}});
}

TestPost.prototype.add = function(data, callback) {
	callback(undefined, {status:'ok', data:{id:1234, timestamp:123456, content:'abcd'}});
}

TestPost.prototype.modify = function(data, callback) {
	callback(undefined, {status:'ok', data:{id:1234, timestamp:123456, content:'abcd'}});
}

TestPost.prototype.remove = function(data, callback) {
	callback(undefined, {status:'ok', data:{id:1234}});
}
