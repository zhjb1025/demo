package com.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.session.data.redis.RedisFlushMode;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

import com.demo.framework.util.SpringContextUtil;

@SpringBootApplication
@EnableRedisHttpSession(maxInactiveIntervalInSeconds=600,redisFlushMode=RedisFlushMode.IMMEDIATE)
public class GatewayApplication {
	private static Logger logger = LoggerFactory.getLogger(GatewayApplication.class);  
	
	public static void main(String[] args) {
	    try {
	      logger.info("开始启动Gateway...................");
	      SpringApplication springApplication = new SpringApplication(GatewayApplication.class);
	      //springApplication.setDefaultProperties(properties);
	      ApplicationContext applicationContext =springApplication.run(args);
	      SpringContextUtil.setApplicationContext(applicationContext);
	      logger.info("启动Gateway成功...................");
        } catch (Throwable e) {
        	logger.error("", e);
        }
		
	}
}
