package com.demo.eoms.controller.msg;

import org.apache.commons.lang.StringUtils;

public class UserQueryRequest extends PageQueryRequest {

    private String userName;

    private String loginName;

    public String getLoginName() {
        if(StringUtils.isBlank(loginName)){
            return null;
        }
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getUserName() {
        if(StringUtils.isBlank(userName)){
            return null;
        }
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
