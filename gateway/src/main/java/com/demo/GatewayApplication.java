package com.demo;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import com.demo.config.client.ConfigCenterClient;
import com.demo.framework.util.SpringContextUtil;


@SpringBootApplication
//@EnableRedisHttpSession(maxInactiveIntervalInSeconds=10,redisFlushMode=RedisFlushMode.IMMEDIATE)
public class GatewayApplication {
	private static Logger logger = LoggerFactory.getLogger(GatewayApplication.class);  
	
	public static void main(String[] args) {
	    try {
	      logger.info("开始启动Gateway...................");
	      
	      ConfigCenterClient.resetUrl(args[0].split(","));
	      ConfigCenterClient.setGroups("logging,tomcat,zookeeper,redis,dubbo,gateway");
	      Properties properties = ConfigCenterClient.loadConfig();
	      SpringApplication springApplication = new SpringApplication(GatewayApplication.class);
	      springApplication.setDefaultProperties(properties);
	      ApplicationContext applicationContext =springApplication.run(args);
	      SpringContextUtil.setApplicationContext(applicationContext);
	      logger.info("启动Gateway成功...................");
        } catch (Throwable e) {
        	logger.error("", e);
        }
		
	}
}
