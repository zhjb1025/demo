package com.demo.framework.exception;


public class FrameworkErrorCode {
	/*
     *  成功
     */
	public static ErrorCode SUCCESS =new ErrorCode("00000000","处理成功");

    
    /*
     * 系统级别错误码 9999 开头
     */
	public static ErrorCode RPC_ERROR=new ErrorCode("99999996","服务[%s]未启动");
	public static ErrorCode VALIDATE_FAIL=new ErrorCode("99999997","数据格式不正确:%s");
	public static ErrorCode SYSTEM_ERROR =new ErrorCode("99999998","%s");
	public static ErrorCode SYSTEM_FAIL =new ErrorCode("99999999","系统异常 请联系相关人员");
}
