package com.demo.controller.msg;

import org.apache.commons.lang.StringUtils;

public class RoleQueryRequest extends PageQueryRequest {

    private String roleName;

    public String getRoleName() {
        if(StringUtils.isBlank(roleName)){
            return null;
        }
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
