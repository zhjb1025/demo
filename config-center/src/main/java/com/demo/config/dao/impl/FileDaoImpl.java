package com.demo.config.dao.impl;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.demo.config.dao.ConfigCenterDao;
import com.demo.config.dao.ConfigInfo;

@Service("FILE")
public class FileDaoImpl implements ConfigCenterDao {
	private static Logger logger = LoggerFactory.getLogger(FileDaoImpl.class);

	@Autowired
    private Environment env;
	
	@Override
	public List<ConfigInfo> loadAllConfig() {
		String path=env.getProperty("demo.config.path");
		StringBuilder sb= new StringBuilder();
		 try {
			InputStreamReader read = new InputStreamReader(
           new FileInputStream(path),"UTF-8");//考虑到编码格式
           BufferedReader bufferedReader = new BufferedReader(read);
           String lineTxt = null;
           while((lineTxt = bufferedReader.readLine()) != null){
           	sb.append(lineTxt);
           }
           read.close();
			
		} catch (Exception e) {
			logger.error("", e);
		}
		return JSON.parseArray(sb.toString(), ConfigInfo.class);
	}

	@Override
	public void addConfig(ConfigInfo configInfo) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateConfig(ConfigInfo configInfo) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteConfig(String key) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<ConfigInfo> loadGroupConfig(String group) {
		// TODO Auto-generated method stub
		return null;
	}

}
