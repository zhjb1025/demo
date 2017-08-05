package com.demo.controller.msg;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.util.List;

public class UpdateUserRequest extends BaseRequest{
    @NotNull(message="用户ID不能为空")
    private Integer id;
	@NotBlank(message="用户名不能为空")
	@Length(min=3,max=32 ,message="用户名长度不能小于{min} 不能大于{max}")
	private String userName;

    @NotNull(message="机构ID不能为空")
    private Integer branchId;

    @NotNull(message="角色列表不能为空")
    private List<Integer> roleList;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
