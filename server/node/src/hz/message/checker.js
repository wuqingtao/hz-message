#!/usr/bin/env node

'use strict';

exports.checkParamType = checkParamType;
exports.checkParamId = checkParamId;
exports.checkParamContent = checkParamContent;

/**
 * 校验type参数是否合法
 *
 * @param {*} param {'type': '<type值>', ...}
 * @return {*} ['<type值>', {'status': '<错误状态>', 'message': '<错误描述>'}]
 */
function checkParamType(param) {
	if (!('type' in param)) {
		return [undefined, {status: 'lost_parameter', message: '`type` is necessary.'}];
	}

	let type = param.type;
	
	if (typeof type != 'string') {
		return [undefined, {status: 'invalid_parameter', message: '`type` should be string.'}];
	}
	
	if (!type) {
		return [undefined, {status: 'invalid_parameter', message: '`type` should not be empty.'}];
	}

	return [type, undefined];
}

/**
 * 校验id参数是否合法
 *
 * @param {*} param {'id': <id值>, ...}
 * @return {*} [<id值>, {'status': '<错误状态>', 'message': '<错误描述>'}]
 */
function checkParamId(param) {
	if (!('id' in param)) {
		return [undefined, {status: 'lost_parameter', message: '`id` is necessary.'}];
	}

	let id = param.id;
	
	if (typeof id != 'number') {
		return [undefined, {status: 'invalid_parameter', message: '`id` should be int.'}];
	}

	// 返回`id`值
	return [id, undefined];
}

/**
 * 校验content参数是否合法
 *
 * @param {*} param {'content': '<content值>', ...}
 * @return {*} ['<content值>', {'status': '<错误状态>', 'message': '<错误描述>'}]
 */
function checkParamContent(param) {
	if (!('content' in param)) {
        return [undefined, {status: 'lost_parameter', message: '`content` is necessary.'}];
	}

	let content = param.content;
	
	if (typeof content != 'string') {
		return [undefined, {status: 'invalid_parameter', message: '`content` should be string.'}];
	}
	
	if (!content) {
		return [undefined, {status: 'invalid_parameter', message: '`content` should not be empty.'}];
	}

	return [content, undefined];
}
