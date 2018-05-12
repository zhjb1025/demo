package com.demo.framework.exception;

public class ErrorCode {
	private String code = "";
	private String msg = "";
	
	public ErrorCode(String code,String msg) {
		this.code=code;
		this.msg=msg;
	}

	public String getCode() {
		return code;
	}

	public String getMsg() {
		return msg;
	}
	
}
