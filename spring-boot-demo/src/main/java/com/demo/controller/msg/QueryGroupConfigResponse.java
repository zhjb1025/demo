package com.demo.controller.msg;

import java.util.List;

import com.demo.config.client.ConfigInfo;

public class QueryGroupConfigResponse extends BaseResponse {
	
    private List<ConfigInfo> configList;

	public List<ConfigInfo> getConfigList() {
		return configList;
	}

	public void setConfigList(List<ConfigInfo> configList) {
		this.configList = configList;
	}

	
    
}
