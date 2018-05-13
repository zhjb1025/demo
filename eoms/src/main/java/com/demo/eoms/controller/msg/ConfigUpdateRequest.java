package com.demo.eoms.controller.msg;

import org.hibernate.validator.constraints.NotBlank;

import com.demo.framework.msg.BaseRequest;

public class ConfigUpdateRequest extends BaseRequest{

	@NotBlank(message = "分组不能为空")
    private String group;

	@NotBlank(message = "描述不能为空")
    private String remark;
	
	@NotBlank(message = "KEY不能为空")
    private String key;

	@NotBlank(message = "值不能为空")
    private String value;

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
    
}
