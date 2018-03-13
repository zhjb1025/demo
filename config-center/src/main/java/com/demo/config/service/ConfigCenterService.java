package com.demo.config.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.demo.framework.annotation.TradeService;

@Service
@TradeService(version="1.0.0")
public class ConfigCenterService {
	private  Logger logger = LoggerFactory.getLogger(this.getClass());
   
}
