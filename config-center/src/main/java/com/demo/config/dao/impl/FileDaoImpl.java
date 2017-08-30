package com.demo.config.dao.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.demo.config.dao.ConfigCenterDao;
import com.demo.config.dao.ConfigInfo;

@Service("FILE")
public class FileDaoImpl implements ConfigCenterDao {
	private  Logger logger = LoggerFactory.getLogger(this.getClass());

	
	@Value("${demo.config.path}")
	private String path;
	
	@Override
	public Map<String,List<ConfigInfo>> loadAllConfig() {
		Map<String,List<ConfigInfo>> ret=new HashMap<String,List<ConfigInfo>>();
		File dir= new File(path);
		if(!dir.exists()) {
			logger.error("目录不存在[{}]",path);
			return ret;
		}
		File[] fileList = dir.listFiles();
		for(File f:fileList) {
			try {
				ret.put(f.getName(), loadFile(f));   
			} catch (Exception e) {
				logger.error("", e);
			}
		}
		return ret;
	}

	@Override
	public void addConfig(ConfigInfo configInfo) throws Exception {
		File file= new File(path+"/"+configInfo.getGroup());
		List<ConfigInfo> list=null;
		if(file.exists()) {
			list=loadFile(file);
		}else {
			
			list=new ArrayList<ConfigInfo>();
		}
		list.add(configInfo);
		FileUtils.write(file, JSON.toJSONString(list), "UTF-8");

	}

	@Override
	public void updateConfig(ConfigInfo configInfo) throws Exception {
		File file= new File(path+"/"+configInfo.getGroup());
		List<ConfigInfo> list=loadFile(file);
		Iterator<ConfigInfo> iter = list.iterator();
		while(iter.hasNext()) {
			ConfigInfo c=iter.next();
			if(c.getKey().equals(configInfo.getKey())) {
				c.setRemark(configInfo.getRemark());
				c.setValue(configInfo.getValue());
				break;
			}
		}
		FileUtils.write(file, JSON.toJSONString(list), "UTF-8");
	}

	@Override
	public void deleteConfig(String group,String key) throws Exception {
		File file= new File(path+"/"+group);
		List<ConfigInfo> list=loadFile(file);
		Iterator<ConfigInfo> iter = list.iterator();
		while(iter.hasNext()) {
			ConfigInfo c=iter.next();
			if(c.getKey().equals(key)) {
				iter.remove();
				break;
			}
		}
		if(list.size()==0) {
			FileUtils.deleteQuietly(file);
		}else {
			FileUtils.write(file, JSON.toJSONString(list), "UTF-8");
		}
		

	}

	@Override
	public List<ConfigInfo> loadGroupConfig(String group) throws Exception {
		File file= new File(path+"/"+group);
		List<ConfigInfo> list=null;
		if(!file.exists()) {
			logger.error("目录不存在[{}]",path);
			return list;
		}
		
		return loadFile(file);
	}
	
	private List<ConfigInfo> loadFile(File file) throws Exception{
		String content=FileUtils.readFileToString(file, "UTF-8");
		logger.error("加载[{}]:{}",file.getName(),content);
		return JSON.parseArray(content, ConfigInfo.class);
	}

}
