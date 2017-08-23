package com.demo.controller.msg;

import org.apache.commons.lang.StringUtils;
import org.hibernate.validator.constraints.NotBlank;

public class QueryGroupConfigRequest extends BaseRequest {
	
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
