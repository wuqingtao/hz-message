<?php

/**
 * 校验type参数是否合法
 *
 * @param array $param ['type'=>'<type值>', ...]
 * @return array ['<type值>', ['status'=>'<错误状态>', 'message'=>'<错误描述>']]
 */
function checkParamType($param) {
	if (!array_key_exists('type', $param)) {
		return [null, ['status'=>'lost_parameter', 'message'=>'`type` is necessary.']];
	}
	
	$type = $param['type'];
	
	if (gettype($type) != 'string') {
		return [null, ['status'=>'invalid_parameter', 'message'=>'`type` should be string.']];
	}
	
	if (empty($type)) {
		return [null, ['status'=>'invalid_parameter', 'message'=>'`type` should be string and not null.']];
	}
	
	return [$type, null];
}

/**
 * 校验id参数是否合法
 *
 * @param array $param ['id'=><id值>, ...]
 * @return array [<id值>, ['status'=>'<错误状态>', 'message'=>'<错误描述>']]
 */
function checkParamId($param) {
	if (!array_key_exists('id', $param)) {
		return [null, ['status'=>'lost_parameter', 'message'=>'`id` is necessary.']];
	}
	
	$id = $param['id'];

	if (gettype($id) != 'integer') {
		return [null, ['status'=>'invalid_parameter', 'message'=>'`id` should be int.']];
	}
	
	return [$id, null];
}

/**
 * 校验content参数是否合法
 *
 * @param array $param ['content'=>'<content值>', ...]
 * @return array ['<content值>', ['status'=>'<错误状态>', 'message'=>'<错误描述>']]
 */
function checkParamContent($param) {
    if (!array_key_exists('content', $param)) {
        return [null, ['status'=>'lost_parameter', 'message'=>'`content` is necessary.']];
	}
	
    $content = $param['content'];
    
	if (gettype($content) != 'string') {
        return [null, ['status'=>'invalid_parameter', 'message'=>'`content` should be string.']];
	}
    
	if (empty($content)) {
        return [null, ['status'=>'invalid_parameter', 'message'=>'`content` should not be null.']];
	}
    
	return [$content, null];
}

?>
