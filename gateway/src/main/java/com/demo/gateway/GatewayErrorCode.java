package com.demo.gateway;

import com.demo.framework.exception.ErrorCode;
import com.demo.framework.exception.FrameworkErrorCode;

public class GatewayErrorCode extends FrameworkErrorCode {
	/*
     * 网关错误码 0001 开头
     */
	public static ErrorCode GATEWAY_ILLEGAL_ACCESS=new ErrorCode("00010001","非法访问");
	public static ErrorCode GATEWAY_NO_ACCESS=new ErrorCode("00010002","无权限访问");
	public static ErrorCode GATEWAY_ERROR_SERVICE=new ErrorCode("00010003","错误的服务名:[%s]");
	public static ErrorCode GATEWAY_SESSION_TIMEOUT=new ErrorCode("00010004","用户登录会话过期,请重新登录");

    
    
}
