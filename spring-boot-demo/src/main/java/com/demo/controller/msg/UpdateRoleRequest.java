package com.demo.controller.msg;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.util.List;

public class UpdateRoleRequest extends BaseRequest{

    @NotNull(message = "ID不能为空")
    private Integer id;

	@NotBlank(message="角色名称不能为空")
	@Length(min=2,max=32 ,message="角色名称长度不能小于{min} 不能大于{max}")
	private String roleName;


	@Length(max=64 ,message="备注长度不能大于{max}")
    private String remark;

	@NotNull(message = "接口ID集合不能为空")
    private List<Integer>  apiIDs;

    @NotNull(message = "菜单ID集合不能为空")
    private List<Integer>  menuIDs;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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
