package com.support.mvc.exception;

/**
 * 自定义异常: token 不存在
 *
 * @author 谢长春 2016-11-23
 */
public class TokenNotExistException extends RuntimeException {
	public TokenNotExistException(String msg) {
		super(msg);
	}
}
