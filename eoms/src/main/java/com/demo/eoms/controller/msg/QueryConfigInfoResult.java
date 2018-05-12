package com.demo.eoms.controller.msg;

import com.demo.eoms.common.enums.ConfigTypeEnum;
import com.demo.eoms.mapper.ConfigInfo;

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
