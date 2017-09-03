package com.demo.config.client;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.demo.framework.util.HttpUtils;

public class ConfigCenterClient {
	
	private static Logger logger = LoggerFactory.getLogger(ConfigCenterClient.class); 
	private static List<String> urlList= new ArrayList<String>(); 
	private static Properties properties= new Properties();
	private static String groups;
	
	public static void resetUrl(String[] urls) {
		urlList.clear();
		for(String url:urls) {
			urlList.add(url);
		}
	}
	
	public static Properties loadConfig() throws Exception {
		List<ConfigInfo> list = queryByGroup(groups);
		for(int i=0;i<list.size();i++) {
			properties.put(list.get(i).getKey(), list.get(i).getValue());
		}
		return properties;
	}

	public static void setGroups(String groups) {
		ConfigCenterClient.groups = groups;
	}
	
	public static String get(String key) {
		Object value = properties.get(key);
		if(value==null) {
			return null;
		}
		return value.toString();
	}
	
	public static List<String> queryAllGroup() throws Exception {
		List<String> list= new ArrayList<String>();
		for(String url:urlList) {
			try {
				String ret = HttpUtils.get(url+"/get/group");
				list = JSON.parseArray(ret, String.class);
				break;
			} catch (Exception e) {
				logger.error("无效的配置中心地址[{}]",url);
				logger.error("",e);
			}
		}
		return list;
	}
	
	public static List<ConfigInfo> queryByGroup(String group) throws Exception {
		List<ConfigInfo> list=null;
		
		Random random = new Random(System.currentTimeMillis());
		int index=Math.abs(random.nextInt())%urlList.size();
		list=queryByGroup_(urlList.get(index),group);
		if(list==null) {
			for(int i=0;i<urlList.size();i++) {
				if(i==index) {
					continue;
				}
				list=queryByGroup_(urlList.get(i),group);
				if(list!=null) {
					break;
				}
			}
		}
		if(list==null) {
			throw new Exception("无法访问中心地址:"+urlList.toString());
		}
		return list;
	}
	
	private static List<ConfigInfo> queryByGroup_(String url,String group) throws Exception {
		List<ConfigInfo> list=null;
		try {
			String ret = HttpUtils.get(url+"/query/"+group);
			list = JSON.parseArray(ret, ConfigInfo.class);
		} catch (Exception e) {
			logger.error("无效的配置中心地址[{}]",url);
			logger.error("",e);
		}
		return list;
	}
	
	public static String updateConfig(String group,String key,String value,String remark) throws Exception {
		ConfigInfo configInfo = new ConfigInfo();
		configInfo.setGroup(group);
		configInfo.setKey(key);
		configInfo.setRemark(remark);
		configInfo.setValue(value);
		boolean success=false;
		String ret="";
		for(String url:urlList) {
			try {
				ret = HttpUtils.post(url+"/update", JSON.toJSONString(configInfo));
				success=true;
				break;
			} catch (Exception e) {
				logger.error("无效的配置中心地址[{}]",url);
				logger.error("",e);
			}
		}
		if(!success) {
			throw new Exception("无法访问中心地址:"+urlList.toString());
		}
		return ret;
	}
	
}
