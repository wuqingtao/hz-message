#!/usr/bin/env node

'use strict';

// 引入模块
const assert = require('assert');
const message_creator = require('../../../src/hz/message/message_creator.js');

/** mysql用户名 */
const user = 'root';
/** mysql用户密码 */
const password = 'Ningning~1';
/** mysql数据库名 */
const database = 'test_message';

describe('message.Message', function() {
	it('message.Message.request()', function(done) {
		this.timeout(0);
		
		// 执行createMessageByMysql
		message_creator.createMessageByMysql(user, password, database, function(err, msg) {
			// 测试创建对象为message
            assert(!err);
			assert('request' in msg);
            
            done();
		});
	});
});
