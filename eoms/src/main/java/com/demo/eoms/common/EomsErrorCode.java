package com.demo.eoms.common;

import com.demo.framework.exception.ErrorCode;
import com.demo.framework.exception.FrameworkErrorCode;

/**
 * 管理平台错误码 0002开头
 * @author Auser
 *
 */
public class EomsErrorCode extends FrameworkErrorCode {
	
    /*
     * 用户相关模块  000201 开头
     */
	public static ErrorCode USER_LOGIN_SESSION_TIMEOUT=new ErrorCode(" 00020100","用户登录会话过期,请重新登录");
	public static ErrorCode USER_LOGIN_ERROR=new ErrorCode(" 00020101","用户名密码错误");
	public static ErrorCode USER_STATUS_ERROR=new ErrorCode(" 00020102","用户状态不正常不允许登录");
	public static ErrorCode USER_LOGIN_NAME_EXITS=new ErrorCode(" 00020103","登录名已经存在");
	public static ErrorCode USER_NOT_EXITS=new ErrorCode(" 00020104","用户不存在");
	public static ErrorCode USER_PASSWORD_ERROR=new ErrorCode(" 00020105","原密码不正确");
    
    /*
     * 配置中心相关模块   000202 开头
     */
	public static ErrorCode CONFIG_ACCESS_ERROR=new ErrorCode(" 00020200","访问配置中心失败");
	
	/*
     * 系统管理模块错误码   000300 开头
     */
	public static ErrorCode SYSTEM_CODE_EXITS=new ErrorCode(" 00030001","系统编码[%s]已经存在");
	public static ErrorCode SYSTEM_CODE_NOT_EXITS=new ErrorCode(" 00030002","系统编码[%s]不存在");
    
}
