package com.demo.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationContext;

import com.demo.framework.util.SpringContextUtil;

@SpringBootApplication
@EnableCaching
public class ConfigCenterApplication {
	private static Logger log = LoggerFactory.getLogger(ConfigCenterApplication.class);  
    public static void main(String[] args) {
    	try {
  	      log.info("开始启动配置中心服务...................");
  	      SpringApplication springApplication = new SpringApplication(ConfigCenterApplication.class);
  	      ApplicationContext applicationContext =springApplication.run(args);
  	      SpringContextUtil.setApplicationContext(applicationContext);
  	      log.info("成功启动配置中心服务...................");
          } catch (Throwable e) {
            log.error("", e);
          }

    }

}
