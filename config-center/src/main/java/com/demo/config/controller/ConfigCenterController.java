package com.demo.config.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.demo.config.dao.ConfigInfo;
import com.demo.config.service.ConfigCenterService;



@RestController
public class ConfigCenterController {
	
	@Autowired
	private ConfigCenterService configCenterService;
	
	@RequestMapping(value = "/query/{groups}", method = RequestMethod.GET)
	@ResponseBody
	public List<ConfigInfo> queryConfigInfo(@PathVariable("groups") String groups)  {
		
		return configCenterService.queryConfigInfo(groups);
	}
	
//	@RequestMapping(value = "/get/{key}", method = RequestMethod.GET)
//	@ResponseBody
//	public ConfigInfo getConfigInfo(@PathVariable("key") String key)  {
//		System.out.println(key);
//		return configCenterService.getConfigInfo(key);
//	}
}
