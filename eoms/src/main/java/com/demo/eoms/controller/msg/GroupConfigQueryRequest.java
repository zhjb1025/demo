package com.demo.eoms.controller.msg;

import org.apache.commons.lang.StringUtils;
import org.hibernate.validator.constraints.NotBlank;

import com.demo.framework.msg.BaseRequest;

public class GroupConfigQueryRequest extends BaseRequest {
	
	@NotBlank(message="group:不能为空")
	private String group;

	public String getGroup() {
		if(StringUtils.isBlank(group)){
            return null;
        }
        return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}
	
    
}
