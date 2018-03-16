package com.demo.config.service.msg;

import org.apache.commons.lang.StringUtils;


public class QueryConfigInfoRequest extends PageQueryRequest {
	
	private String systemCode;
	
	private String configType;
	
	private String configCode;

	public String getSystemCode() {
		if(StringUtils.isBlank(systemCode)){
            return null;
        }
        return systemCode;
	}
	public void setSystemCode(String systemCode) {
		this.systemCode = systemCode;
	}
	public String getConfigType() {
		if(StringUtils.isBlank(configType)){
            return null;
        }
		return configType;
	}
	public void setConfigType(String configType) {
		this.configType = configType;
	}
	public String getConfigCode() {
		if(StringUtils.isBlank(configCode)){
            return null;
        }
		return configCode;
	}
	public void setConfigCode(String configCode) {
		this.configCode = configCode;
	}
	
	
    
}
