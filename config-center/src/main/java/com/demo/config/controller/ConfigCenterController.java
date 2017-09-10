package com.demo.config.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.demo.config.dao.ConfigInfo;
import com.demo.config.service.ConfigCenterService;



@RestController
public class ConfigCenterController {
	private  Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private ConfigCenterService configCenterService;
	
	@RequestMapping(value = "/query/{groups}", method = RequestMethod.GET)
	@ResponseBody
	public List<ConfigInfo> queryConfigInfo(@PathVariable("groups") String groups)  {
		
		return configCenterService.queryConfigInfo(groups);
	}
	
	@RequestMapping(value = "/get/group", method = RequestMethod.GET)
	@ResponseBody
	public List<String> getALLGroup()  {
		return configCenterService.getALLGroup();
	}
	
	
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	@ResponseBody
	public String updateConfigInfo(@RequestBody String data)  {
		logger.info("更新配置数据[{}]",data);
		String ret="";
		try {
			ConfigInfo configInfo=JSON.parseObject(data, ConfigInfo.class);
			ret=configCenterService.updateConfig(configInfo);
		} catch (Exception e) {
			logger.error("更新配置数据失败",e);
			return "更新配置失败";
		}
		return ret;
	}
	
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ResponseBody
	public String addConfigInfo(@RequestBody String data)  {
		logger.info("新增配置数据[{}]",data);
		String ret="";
		try {
			ConfigInfo configInfo=JSON.parseObject(data, ConfigInfo.class);
			ret=configCenterService.addConfig(configInfo);
		} catch (Exception e) {
			logger.error("新增配置数据失败",e);
			return "新增配置失败";
		}
		return ret;
	}
	
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	@ResponseBody
	public String deleteConfigInfo(@RequestBody String data)  {
		logger.info("删除配置数据[{}]",data);
		String ret="";
		try {
			ConfigInfo configInfo=JSON.parseObject(data, ConfigInfo.class);
			ret=configCenterService.deleteConfig(configInfo);
		} catch (Exception e) {
			logger.error("删除配置数据失败",e);
			return "删除配置失败";
		}
		return ret;
	}
	
	@RequestMapping(value = "/syn/{group}", method = RequestMethod.GET)
	@ResponseBody
	public String synConfigInfo(@PathVariable("group") String group) throws Exception  {
		logger.info("同步数据[{}]",group);
		configCenterService.synConfig(group);
		return "OK";
	}
}
