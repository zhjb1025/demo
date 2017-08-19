package com.demo.controller.msg;

import javax.validation.constraints.NotNull;

public class QueryRoleMenuApiRequest extends BaseRequest{

    @NotNull(message = "角色ID不能为空")
    private Integer roleID;

    public Integer getRoleID() {
        return roleID;
    }

    public void setRoleID(Integer roleID) {
        this.roleID = roleID;
    }
}
