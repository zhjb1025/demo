package com.demo.config.dao.impl;

import java.util.List;
import java.util.Map;

import com.demo.config.dao.ConfigCenterDao;
import com.demo.config.dao.ConfigInfo;

public class RedisDaoImpl implements ConfigCenterDao {

	@Override
	public Map<String, List<ConfigInfo>> loadAllConfig() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ConfigInfo> loadGroupConfig(String group) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addConfig(ConfigInfo configInfo) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateConfig(ConfigInfo configInfo) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteConfig(String group, String key) throws Exception {
		// TODO Auto-generated method stub
		
	}

	

}
