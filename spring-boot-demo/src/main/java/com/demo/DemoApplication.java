package com.demo;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.context.WebApplicationContext;

import com.demo.framework.util.SpringContextUtil;


@SpringBootApplication
@ServletComponentScan
@EnableAspectJAutoProxy(proxyTargetClass = true)
@MapperScan("com.demo.mapper")
public class DemoApplication  extends SpringBootServletInitializer {
    private static Logger log = LoggerFactory.getLogger(DemoApplication.class);  
	public static void main(String[] args) {
	    try {
	      log.info("开始启动服务...................");
	      ApplicationContext applicationContext = SpringApplication.run(DemoApplication.class, args);
	      SpringContextUtil.setApplicationContext(applicationContext);
	      log.info("成功启动服务...................");
        } catch (Throwable e) {
          log.error("", e);
        }
		
	}
	
	@Override 
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(DemoApplication.class); 
	}
	
	protected WebApplicationContext run(SpringApplication application) {
		WebApplicationContext applicationContext= (WebApplicationContext) application.run();
		SpringContextUtil.setApplicationContext(applicationContext);
		return applicationContext;
	}

}
