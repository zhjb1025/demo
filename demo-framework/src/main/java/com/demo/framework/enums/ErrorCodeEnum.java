package com.demo.framework.enums;

/**
 * 错误码枚举类
 * 编码规则 前两位为模块编码，后两位为模块内错误信息编码
 * Created by Auser on 2017/7/15.
 */
public enum ErrorCodeEnum {
    /*
        成功
     */
    SUCCESS("0000","处理成功"),

    /*
        框架级别业务错误码 00 开头
     */

    VALIDATE_FAIL("0001","数据格式不正确:%s"),
    SYSTEM_ILLEGAL_ACCESS("0002","非法访问"),
    SYSTEM_NO_ACCESS("0003","无权限访问"),
    SYSTEM_ERROR_SERVICE_VERSION("0004","错误的服务名:[%s]和版本号:[%s]"),

    /*
        用户相关模块  01 开头
     */
    USER_LOGIN_SESSION_TIMEOUT("0100","用户登录会话过期,请重新登录"),
    USER_LOGIN_ERROR("0101","用户名密码错误"),
    USER_STATUS_ERROR("0102","用户状态不正常不允许登录"),
    USER_LOGIN_NAME_EXITS("0103","登录名已经存在"),
    USER_NOT_EXITS("0104","用户不存在"),
    USER_PASSWORD_ERROR("0105","原密码不正确"),
    /*
        系统级别错误码 99 开头
     */
    SYSTEM_ERROR("9998","%s"),
    SYSTEM_FAIL("9999","系统异常 请联系相关人员");

    String code;
    String msg;

    ErrorCodeEnum(String code,String msg){
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
