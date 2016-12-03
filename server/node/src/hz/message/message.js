#!/usr/bin/env node

'use strict';

exports.Message = Message;

const checker = require('./checker.js');

/**
 * 消息类
 * <p>
 * 消息类用于处理用户的消息请求，包括添加、查询、修改和删除操作
 *
 * @param {*} hdr holder对象
 * @param {*} pst post对象
 */
function Message(hdr, pst) {
	this._hdr = hdr;
	this._pst = pst;
}

/**
 * 请求处理
 *
 * @param {*} param {'type': '<type值>', ...}
 * @param {function} callback 回调函数 ({Error} err 错误, {*} res {'status': '<状态>', 'message': '<错误描述>', 'data': <结果>})
 */
Message.prototype.request = function(param, callback) {
	// 校验type参数
	let [type, typeErr] = checker.checkParamType(param);
	if (typeErr) {
		callback(undefined, typeErr);
		return;
	}

	// 根据不同type值调用post对象处理
	// 如果type值不支持，返回错误
	if (type == 'get_post_count') {
		this._pst.getCount(callback);
	} else 	if (type == 'get_all_post') {
		this._pst.getAll(callback);
	} else 	if (type == 'get_post_by_id') {
		this._pst.getById(param, callback);
	} else 	if (type == 'add_post') {
		this._pst.add(param, callback);
	} else 	if (type == 'modify_post') {
		this._pst.modify(param, callback);
	} else 	if (type == 'remove_post') {
		this._pst.remove(param, callback);
	} else {
		callback(undefined, {status: 'invalid_parameter', message: '`type` is invalid.'});
	}
}

/**
 * 关闭操作
 *
 * @param {function} callback 回调函数 ({Error} err 错误)
 */
Message.prototype.close = function(callback) {
	this._hdr.close(callback);
}
