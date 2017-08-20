package com.demo.config.client;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.demo.config.util.HttpUtils;

public class ConfigCenterClient {
	
	private static Logger logger = LoggerFactory.getLogger(HttpUtils.class); 
	private static List<String> urlList= new ArrayList<String>(); 
	private static Properties properties= new Properties();
	private static String groups;
	
	public static void resetUrl(String[] urls) {
		urlList.clear();
		for(String url:urls) {
			urlList.add(url);
		}
	}
	
	public static Properties loadConfig() {
		for(String url:urlList) {
			try {
				String ret = HttpUtils.get(url+"/query/"+groups);
				JSONArray list = JSON.parseArray(ret);
				for(int i=0;i<list.size();i++) {
					properties.put(list.getJSONObject(i).getString("key"), list.getJSONObject(i).getString("value"));
				}
				break;
			} catch (Exception e) {
				logger.error("",e);
			}
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
	
	
}
