package com.demo.common;

import com.demo.framework.exception.ErrorCode;
import com.demo.framework.exception.FrameworkErrorCode;

public class DemoErrorCode extends FrameworkErrorCode {
	/*
     * 框架级别业务错误码 00 开头
     */
	public static ErrorCode SYSTEM_ILLEGAL_ACCESS=new ErrorCode("0001","非法访问");
	public static ErrorCode SYSTEM_NO_ACCESS=new ErrorCode("0002","无权限访问");
	public static ErrorCode SYSTEM_ERROR_SERVICE_VERSION=new ErrorCode("0003","错误的服务名:[%s]和版本号:[%s]");

    /*
     * 用户相关模块  01 开头
     */
	public static ErrorCode USER_LOGIN_SESSION_TIMEOUT=new ErrorCode("0100","用户登录会话过期,请重新登录");
	public static ErrorCode USER_LOGIN_ERROR=new ErrorCode("0101","用户名密码错误");
	public static ErrorCode USER_STATUS_ERROR=new ErrorCode("0102","用户状态不正常不允许登录");
	public static ErrorCode USER_LOGIN_NAME_EXITS=new ErrorCode("0103","登录名已经存在");
	public static ErrorCode USER_NOT_EXITS=new ErrorCode("0104","用户不存在");
	public static ErrorCode USER_PASSWORD_ERROR=new ErrorCode("0105","原密码不正确");
    
    /*
     * 配置中心相关模块  02 开头
     */
	public static ErrorCode CONFIG_ACCESS_ERROR=new ErrorCode("0200","访问配置中心失败");
    
}
