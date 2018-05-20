package com.demo.eoms.controller.msg;

import org.apache.commons.lang.StringUtils;


public class SystemInfoQueryRequest extends PageQueryRequest {
	
	private String systemCode;
	
	private String systemName;

	public String getSystemCode() {
		if(StringUtils.isBlank(systemCode)){
            return null;
        }
        return systemCode;
	}

	public String getSystemName() {
		if(StringUtils.isBlank(systemName)){
            return null;
        }
		return "%"+systemName+"%";
	}

	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}

	public void setSystemCode(String systemCode) {
		this.systemCode = systemCode;
	}
	
	
    
}
