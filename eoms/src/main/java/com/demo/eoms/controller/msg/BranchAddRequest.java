package com.demo.eoms.controller.msg;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import com.demo.framework.msg.BaseRequest;

public class BranchAddRequest extends BaseRequest{

    @NotNull(message = "父ID不能为空")
    private Integer parentId;

	@NotBlank(message="机构名称不能为空")
	@Length(min=2,max=32 ,message="机构名称长度不能小于{min} 不能大于{max}")
	private String branchName;


	@Length(max=64 ,message="备注长度不能大于{max}")
    private String remark;

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
