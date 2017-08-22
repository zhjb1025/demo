package com.demo.framework.exception;

import com.demo.framework.enums.ErrorCodeEnum;

public class CommException extends Exception {
	
	
	private static final long serialVersionUID = 3273373498035203051L;
	
	private String errCode = "";
	private String errMsg = "";
	
	
	public CommException(ErrorCodeEnum errorCodeEnum,Object... para){
		super(getMsg(errorCodeEnum,para));
		this.errCode = errorCodeEnum.getCode();
		this.errMsg = this.getMessage();
	}
	private static  String getMsg(ErrorCodeEnum errorCodeEnum,Object... para){
		String msg=errorCodeEnum.getMsg();
		if (para!=null){
			msg=String.format(errorCodeEnum.getMsg(),para);
		}
		return msg;
	}



	public String getErrCode() {
		return errCode;
	}


	public void setErrCode(String errCode) {
		this.errCode = errCode;
	}


	public String getErrMsg() {
		return errMsg;
	}


	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}
}
