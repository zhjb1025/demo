package com.demo.config.dao;

import java.util.List;
import java.util.Map;

public interface ConfigCenterDao {
	/**
	 * 加载所有的配置信息
	 * @return
	 */
	public Map<String,List<ConfigInfo>> loadAllConfig() throws Exception;
	
	/**
	 * 加载组的配置信息
	 * @return
	 */
	public List<ConfigInfo> loadGroupConfig(String group) throws Exception;
	
	/**
	 * 添加一个配置项
	 * @param configInfo
	 */
	public void addConfig(ConfigInfo configInfo) throws Exception;
	
	/**
	 * 更新一个配置项
	 * @param configInfo
	 */
	public void updateConfig(ConfigInfo configInfo) throws Exception;
	
	/**
	 * 删除一个配置项
	 * @param group
	 * @param key
	 * @throws Exception
	 */
	public void deleteConfig(String group,String key) throws Exception;
}
