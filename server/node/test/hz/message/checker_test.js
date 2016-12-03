#!/usr/bin/env node

'use strict';

const assert = require('assert');
const checker = require('../../../src/hz/message/checker.js');

describe('checker.checkParamType()', function() {
	it('测试空参数', function() {
		let [val, err] = checker.checkParamType({});
		assert(val == undefined && err.status == 'lost_parameter');
	});
	
	it('测试非法key参数', function() {
		let [val, err] = checker.checkParamType({abcd:'1234'});
        assert(val == undefined && err.status == 'lost_parameter');
		[val, err] = checker.checkParamType({Type:'1234'});
        assert(val == undefined && err.status == 'lost_parameter');
	});
	
	it('测试非法value参数', function() {
		let [val, err] = checker.checkParamType({type:1234});
        assert(val == undefined && err.status == 'invalid_parameter');
		[val, err] = checker.checkParamType({type:''});
        assert(val == undefined && err.status == 'invalid_parameter');
	});
	
	it('测试合法参数', function() {
		let [val, err] = checker.checkParamType({type:'1234'});
        assert(val == '1234' && err == undefined);
	});
});

describe('checker.checkParamId()', function() {
	it('测试空参数', function() {
		let [val, err] = checker.checkParamId({});
		assert(val == undefined && err.status == 'lost_parameter');
	});
	
	it('测试非法key参数', function() {
		let [val, err] = checker.checkParamId({abcd:1234});
		assert(val == undefined && err.status == 'lost_parameter');
		[val, err] = checker.checkParamId({Id:1234});
		assert(val == undefined && err.status == 'lost_parameter');
	});
	
	it('测试非法value参数', function() {
		let [val, err] = checker.checkParamId({id:'1234'});
		assert(val == undefined && err.status == 'invalid_parameter');
	});
	
	it('测试合法参数', function() {
		let [val, err] = checker.checkParamId({id:1234});
		assert(val == 1234 && err == undefined);
	});
});

describe('checker.checkParamContent()', function() {
	it('测试空参数', function() {
		let [val, err] = checker.checkParamContent({});
		assert(val == undefined && err.status == 'lost_parameter');
	});
	
	it('测试非法key参数', function() {
		let [val, err] = checker.checkParamContent({abcd:'1234'});
		assert(val == undefined && err.status == 'lost_parameter');
		[val, err] = checker.checkParamContent({Content:'1234'});
		assert(val == undefined && err.status == 'lost_parameter');
	});
	
	it('测试非法value参数', function() {
		let [val, err] = checker.checkParamContent({content:1234});
		assert(val == undefined && err.status == 'invalid_parameter');
		[val, err] = checker.checkParamContent({content:''});
		assert(val == undefined && err.status == 'invalid_parameter');
	});
	
	it('测试合法参数', function() {
		let [val, err] = checker.checkParamContent({content:'1234'});
		assert(val == '1234' && err == undefined);
	});
});
