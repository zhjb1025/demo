package com.demo.eoms.controller.msg;

import org.apache.commons.lang.StringUtils;


public class ApiQueryRequest extends PageQueryRequest {
	
	private String apiCode;

	private String apiName;

	public String getApiCode() {
		if(StringUtils.isBlank(apiCode)){
            return null;
        }
		return "%"+apiCode+"%";
	}

	public void setApiCode(String apiCode) {
		this.apiCode = apiCode;
	}

	public String getApiName() {
		if(StringUtils.isBlank(apiName)){
            return null;
        }
		return "%"+apiName+"%";
	}

	public void setApiName(String apiName) {
		this.apiName = apiName;
	}
	
	
    
}
