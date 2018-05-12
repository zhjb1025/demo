package com.demo.eoms.controller.msg;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import com.demo.framework.msg.BaseRequest;

public class AddRoleRequest extends BaseRequest{


	@NotBlank(message="角色名称不能为空")
	@Length(min=2,max=32 ,message="角色名称长度不能小于{min} 不能大于{max}")
	private String roleName;


	@Length(max=64 ,message="备注长度不能大于{max}")
    private String remark;

	@NotNull(message = "接口ID集合不能为空")
    private List<Integer>  apiIDs;

    @NotNull(message = "菜单ID集合不能为空")
    private List<Integer>  menuIDs;

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public List<Integer> getApiIDs() {
        return apiIDs;
    }

    public void setApiIDs(List<Integer> apiIDs) {
        this.apiIDs = apiIDs;
    }

    public List<Integer> getMenuIDs() {
        return menuIDs;
    }

    public void setMenuIDs(List<Integer> menuIDs) {
        this.menuIDs = menuIDs;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
