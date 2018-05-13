package com.demo.eoms.controller.msg;

import org.hibernate.validator.constraints.NotBlank;

import com.demo.framework.msg.BaseRequest;

public class ConfigDeleteRequest extends BaseRequest{

	@NotBlank(message = "分组不能为空")
    private String group;

	@NotBlank(message = "KEY不能为空")
    private String key;
	
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

}
