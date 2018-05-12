package com.demo.eoms.controller.msg;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import com.demo.framework.msg.BaseRequest;

public class AddUserRequest extends BaseRequest{

    @NotBlank(message="登录名不能为空")
    @Length(min=3,max=32 ,message="登录名长度不能小于{min} 不能大于{max}")
    private String loginName;

	@NotBlank(message="用户名不能为空")
	@Length(min=2,max=32 ,message="用户名长度不能小于{min} 不能大于{max}")
	private String userName;

    @NotNull(message="机构ID不能为空")
    private Integer branchId;

    @NotNull(message="角色列表不能为空")
    private List<Integer> roleList;


    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getBranchId() {
        return branchId;
    }

    public void setBranchId(Integer branchId) {
        this.branchId = branchId;
    }

    public List<Integer> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<Integer> roleList) {
        this.roleList = roleList;
    }
}
