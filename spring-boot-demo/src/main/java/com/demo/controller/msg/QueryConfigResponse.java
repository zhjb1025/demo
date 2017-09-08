package com.demo.controller.msg;

import com.demo.mapper.Config;

/**
 * Created by Auser on 2017/7/15.
 */
public class QueryConfigResponse extends BaseResponse {
	private Config config;

	public Config getConfig() {
		return config;
	}

	public void setConfig(Config config) {
		this.config = config;
	}
	
	
    
}
