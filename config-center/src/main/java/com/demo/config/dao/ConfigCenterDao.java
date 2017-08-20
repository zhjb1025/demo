package com.demo.config.dao;

import java.util.List;

public interface ConfigCenterDao {
	/**
	 * 加载所有的配置信息
	 * @return
	 */
	public List<ConfigInfo> loadAllConfig();
	
	/**
	 * 添加一个配置项
	 * @param configInfo
	 */
	public void addConfig(ConfigInfo configInfo);
	
	/**
	 * 更新一个配置项
	 * @param configInfo
	 */
	public void updateConfig(ConfigInfo configInfo);
	
	/**
	 * 删除一个配置项
	 * @param key
	 */
	public void deleteConfig(String key);
}
