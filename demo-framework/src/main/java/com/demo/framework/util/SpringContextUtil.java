package com.demo.framework.util;

import org.springframework.context.ApplicationContext;

/**
 * Spring上下文获取工具类
 * 
 * @author benny
 *
 */
public class SpringContextUtil {
	private static ApplicationContext applicationContext;


	public static void setApplicationContext(ApplicationContext context) {
		applicationContext = context;
	}

	public static Object getBean(String beanId) {
		return applicationContext.getBean(beanId);
	}

	public static <T> T getBean(Class<T> clazz) {
		return applicationContext.getBean(clazz);
	}

}
