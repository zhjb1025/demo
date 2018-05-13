package com.demo.eoms.controller.msg;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import com.demo.framework.msg.BaseRequest;


public class ApiUpdateRequest extends BaseRequest {
	
	@NotNull(message = "ID不能为空")
    private Integer id;
	
	@NotBlank(message="接口编码不能为空")
	@Length(min=2,max=32 ,message="接口编码长度不能小于{min} 不能大于{max}")
	private String apiCode;

	@NotBlank(message="接口名称不能为空")
	@Length(min=2,max=64 ,message="接口编码长度不能小于{min} 不能大于{max}")
	private String apiName;
	
	@NotBlank(message="接口版本号不能为空")
	@Length(min=5,max=16 ,message="接口版本号长度不能小于{min} 不能大于{max}")
	private String apiVersion;
	
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getApiCode() {
		return apiCode;
	}

	public void setApiCode(String apiCode) {
		this.apiCode = apiCode;
	}

	public String getApiName() {
		return apiName;
	}

	public void setApiName(String apiName) {
		this.apiName = apiName;
	}

	public String getApiVersion() {
		return apiVersion;
	}

	public void setApiVersion(String apiVersion) {
		this.apiVersion = apiVersion;
	}
	
}
