package com.demo;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationContext;

import com.demo.framework.util.SpringContextUtil;


@SpringBootApplication
@MapperScan("com.demo.mapper")
@EnableCaching
public class DemoApplication {
    private static Logger log = LoggerFactory.getLogger(DemoApplication.class);  
	public static void main(String[] args) {
	    try {
	      log.info("开始启动服务...................");
	      SpringApplication springApplication = new SpringApplication(DemoApplication.class);
	      ApplicationContext applicationContext =springApplication.run(args);
	      SpringContextUtil.setApplicationContext(applicationContext);
	      log.info("成功启动服务...................");
        } catch (Throwable e) {
          log.error("", e);
        }
		
	}
}
