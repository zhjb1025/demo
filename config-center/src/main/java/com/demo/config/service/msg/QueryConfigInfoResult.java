package com.demo.config.service.msg;

import com.demo.config.enums.ConfigTypeEnum;
import com.demo.config.service.mapper.ConfigInfo;

public class QueryConfigInfoResult extends ConfigInfo{
	private String systemName;
	

	public String getSystemName() {
		return systemName;
	}

	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}

	public String getConfigTypeLable() {
		return ConfigTypeEnum.getName(getConfigType());
	}

	
	
	
}
