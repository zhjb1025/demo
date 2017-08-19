package com.demo.common.util;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.ApplicationContext;

/**
 * Spring上下文获取工具类
 * @author benny
 *
 */
public class SpringContextUtil {
      private static ApplicationContext applicationContext;
      
      private static ThreadLocal<ThreadCacheData> threadLocalData= new ThreadLocal<ThreadCacheData>();
      
      public static void setApplicationContext(ApplicationContext context) {
        applicationContext = context;
      }
      
      public static Object getBean(String beanId) {
        return applicationContext.getBean(beanId);
      }
      
      public static <T> T getBean(Class<T> clazz) {
        return applicationContext.getBean(clazz);
      }

      public static void setThreadLocalData(ThreadCacheData threadCacheData){
          threadLocalData.set(threadCacheData);
      }

    public static ThreadCacheData getThreadLocalData() {
        return threadLocalData.get();
    }

    public static void cleanThreadCacheData(){
          threadLocalData.remove();
      }
}
