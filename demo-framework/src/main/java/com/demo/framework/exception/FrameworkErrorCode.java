package com.demo.framework.exception;

public class FrameworkErrorCode {
	/*
     *  成功
     */
	public static ErrorCode SUCCESS =new ErrorCode("0000","处理成功");

    
    /*
     * 系统级别错误码 99 开头
     */
	public static ErrorCode VALIDATE_FAIL=new ErrorCode("9997","数据格式不正确:%s");
	public static ErrorCode SYSTEM_ERROR =new ErrorCode("9998","%s");
	public static ErrorCode SYSTEM_FAIL =new ErrorCode("9999","系统异常 请联系相关人员");
}
