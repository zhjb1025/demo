package com.demo.config;

import com.demo.framework.exception.ErrorCode;
import com.demo.framework.exception.FrameworkErrorCode;

/**
 * 配置中心错误码 0003开头
 * @author Auser
 *
 */
public class ConfigErrorCode extends FrameworkErrorCode {
	
	 /*
     * 系统管理模块错误码   000300 开头
     */
	public static ErrorCode SYSTEM_CODE_EXITS=new ErrorCode(" 00030001","系统编码[%s]已经存在");
	public static ErrorCode SYSTEM_CODE_NOT_EXITS=new ErrorCode(" 00030002","系统编码[%s]不存在");
	
    
}
