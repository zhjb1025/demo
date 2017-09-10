package com.demo.framework.exception;

public class CommException extends Exception {
	
	
	private static final long serialVersionUID = 3273373498035203051L;
	
	private String errCode = "";
	private String errMsg = "";
	
	
	public CommException(ErrorCode errorCode,Object... para){
		super(getMsg(errorCode,para));
		this.errCode = errorCode.getCode();
		this.errMsg = this.getMessage();
	}
	private static  String getMsg(ErrorCode errorCode,Object... para){
		String msg=errorCode.getMsg();
		if (para!=null){
			msg=String.format(errorCode.getMsg(),para);
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
