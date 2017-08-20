package com.demo;

import java.util.Properties;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import com.demo.common.util.SpringContextUtil;
import com.demo.config.client.ConfigCenterClient;


@SpringBootApplication
@ServletComponentScan
@EnableAspectJAutoProxy(proxyTargetClass = true)
@MapperScan("com.demo.mapper")
public class DemoApplication {
    private static Logger log = LoggerFactory.getLogger(DemoApplication.class);  
	public static void main(String[] args) {
	    try {
	      log.info("开始启动服务...................");
	      ConfigCenterClient.resetUrl(new String[] {"http://127.0.0.1:8080/config-center"});
	      ConfigCenterClient.setGroups("all");
	      Properties properties = ConfigCenterClient.loadConfig();
	      SpringApplication springApplication = new SpringApplication(DemoApplication.class);
	      springApplication.setDefaultProperties(properties);
	      //ApplicationContext applicationContext = SpringApplication.run(DemoApplication.class, args);
	      ApplicationContext applicationContext =springApplication.run(args);
	      SpringContextUtil.setApplicationContext(applicationContext);
	      log.info("成功启动服务...................");
        } catch (Throwable e) {
          log.error("", e);
        }
		
	}
}
