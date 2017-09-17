package com.demo.config.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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
	private static Map<String,List<ConfigInfo>> configGroupMap = new HashMap<String,List<ConfigInfo>>();
	
	public static void resetUrl(String[] urls) {
		urlList.clear();
		for(String url:urls) {
			urlList.add(url);
		}
		logger.info("重置配置中心url[{}]",urlList);
	}
	public static void addUrl(String url) {
		logger.info("增加配置中心url[{}]",url);
		urlList.add(url);
	}
	public static void deleteUrl(String url) {
		Iterator<String> iter = urlList.iterator();
		while(iter.hasNext()) {
			if(url.equals(iter.next())) {
				logger.info("删除配置中心url[{}]",url);
				iter.remove();
				return;
			}
		}
	}
	public static Properties loadConfig() throws Exception {
		List<ConfigInfo> list = queryByGroup(groups);
		for(int i=0;i<list.size();i++) {
			properties.put(list.get(i).getKey(), list.get(i).getValue());
			List<ConfigInfo> groupList=configGroupMap.get(list.get(i).getGroup());
			if(groupList==null) {
				groupList= new ArrayList<ConfigInfo>();
				configGroupMap.put(list.get(i).getGroup(), groupList);
			}
			groupList.add(list.get(i));
		}
		return properties;
	}

	public static void setGroups(String groups) {
		ConfigCenterClient.groups = groups;
	}
	
	public static String getGroups() {
		return groups;
	}

	public static String get(String key) {
		Object value = properties.get(key);
		if(value==null) {
			return null;
		}
		return value.toString();
	}
	public static String get(String key,String defaultValue) {
		Object value = properties.get(key);
		if(value==null) {
			return defaultValue;
		}
		return value.toString();
	}
	public static Integer getInt(String key,int defaultValue) {
		Object value = properties.get(key);
		if(value==null) {
			return defaultValue;
		}
		return Integer.parseInt(value.toString());
	}
	public static Integer getInt(String key) {
		Object value = properties.get(key);
		if(value==null) {
			return null;
		}
		return Integer.parseInt(value.toString());
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
	
	public static void synGroup(String group) {
		try {
			List<ConfigInfo> list = queryByGroup(group);
			Map<String,String> map=new HashMap<String,String>();
			for(ConfigInfo c:list) {
				map.put(c.getKey(), c.getValue());
				String value = properties.getProperty(c.getKey());
				if(value==null) {
					logger.info("新增配置项[{}={}]",c.getKey(), c.getValue());
					properties.put(c.getKey(), c.getValue());
				}else if(!value.equals(c.getValue())) {
					logger.info("修改配置项{}:[新值:{},旧值:{}]",c.getKey(), c.getValue(),value);
					properties.put(c.getKey(), c.getValue());
				}
			}
			List<ConfigInfo> old=configGroupMap.get(group);
			for(ConfigInfo c:old) {
				if(map.get(c.getKey())==null) {
					logger.info("删除配置项[{}={}]",c.getKey(), c.getValue());
					properties.remove(c.getKey());
				}
			}
			configGroupMap.put(group, list);
			
		} catch (Exception e) {
			logger.error("同步配置中心数据报错",e);
		}
	}
	
	
	public static String addConfig(String group,String key,String value,String remark) throws Exception {
		ConfigInfo configInfo = new ConfigInfo();
		configInfo.setGroup(group);
		configInfo.setKey(key);
		configInfo.setRemark(remark);
		configInfo.setValue(value);
		boolean success=false;
		String ret="";
		for(String url:urlList) {
			try {
				ret = HttpUtils.post(url+"/add", JSON.toJSONString(configInfo));
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
	
	
	public static String deleteConfig(String group,String key) throws Exception {
		ConfigInfo configInfo = new ConfigInfo();
		configInfo.setGroup(group);
		configInfo.setKey(key);
		boolean success=false;
		String ret="";
		for(String url:urlList) {
			try {
				ret = HttpUtils.post(url+"/delete", JSON.toJSONString(configInfo));
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
