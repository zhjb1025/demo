package com.demo;

import java.util.Properties;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import com.demo.config.client.ConfigCenterClient;
import com.demo.framework.util.SpringContextUtil;


@SpringBootApplication
@ServletComponentScan
@EnableAspectJAutoProxy(proxyTargetClass = true)
@MapperScan("com.demo.mapper")
@EnableCaching
public class DemoApplication {
    private static Logger log = LoggerFactory.getLogger(DemoApplication.class);  
	public static void main(String[] args) {
	    try {
	      log.info("开始启动服务...................");
	      
//	      ConfigCenterClient.resetUrl(new String[] {"http://127.0.0.1:8080/config-center"});
	      ConfigCenterClient.resetUrl(args[0].split(","));
	      ConfigCenterClient.setGroups("logging,tomcat,datasource,mybatis,mongodb,actuator,zookeeper,redis,demo");
	      Properties properties = ConfigCenterClient.loadConfig();
	      SpringApplication springApplication = new SpringApplication(DemoApplication.class);
	      springApplication.setDefaultProperties(properties);
	      ApplicationContext applicationContext =springApplication.run(args);
	      SpringContextUtil.setApplicationContext(applicationContext);
	      log.info("成功启动服务...................");
        } catch (Throwable e) {
          log.error("", e);
        }
		
	}
}
