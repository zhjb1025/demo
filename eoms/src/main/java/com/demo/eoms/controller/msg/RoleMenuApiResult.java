package com.demo.eoms.controller.msg;

import com.demo.framework.msg.BaseObject;

public class RoleMenuApiResult extends BaseObject {
    private Integer roleId;
    private Integer menuId;
    private Integer apiId;
	public Integer getRoleId() {
		return roleId;
	}
	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}
	public Integer getMenuId() {
		return menuId;
	}
	public void setMenuId(Integer menuId) {
		this.menuId = menuId;
	}
	public Integer getApiId() {
		return apiId;
	}
	public void setApiId(Integer apiId) {
		this.apiId = apiId;
	}
    
    
}