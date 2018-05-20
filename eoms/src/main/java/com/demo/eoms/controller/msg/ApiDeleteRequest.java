package com.demo.eoms.controller.msg;

import javax.validation.constraints.NotNull;

import com.demo.framework.msg.BaseRequest;


public class ApiDeleteRequest extends BaseRequest {
	
	@NotNull(message = "ID不能为空")
    private Integer id;
	
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
}
