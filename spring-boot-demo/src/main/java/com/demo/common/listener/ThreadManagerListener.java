//package com.demo.common.listener;
//
//import com.demo.common.annotation.ThreadManager;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
//import org.springframework.boot.context.event.ApplicationPreparedEvent;
//import org.springframework.boot.context.event.ApplicationReadyEvent;
//import org.springframework.context.ApplicationContext;
//import org.springframework.context.ApplicationEvent;
//import org.springframework.context.ApplicationListener;
//import org.springframework.context.event.ContextClosedEvent;
//import org.springframework.context.event.ContextRefreshedEvent;
//import org.springframework.context.event.ContextStartedEvent;
//import org.springframework.context.event.ContextStoppedEvent;
//
//public class ThreadManagerListener implements ApplicationListener {
//    private  Logger logger = LoggerFactory.getLogger(this.getClass());
//    @Override
//    public void onApplicationEvent(ApplicationEvent event) {
//        // 在这里可以监听到Spring Boot的生命周期
//        if (event instanceof ApplicationEnvironmentPreparedEvent) {
//            // 初始化环境变量//
//            logger.info("ApplicationEnvironmentPreparedEvent");
//        }
//        else if (event instanceof ApplicationPreparedEvent) {
//            // 初始化完成
//            logger.info("ApplicationPreparedEvent");
//        }
//        else if (event instanceof ContextRefreshedEvent) {
//            // 应用刷新
//            logger.info("ContextRefreshedEvent");
//        }
//        else if (event instanceof ApplicationReadyEvent) {
//            // 应用已启动完成
//            ApplicationContext context = ((ApplicationReadyEvent)event).getApplicationContext();
//            String[] names=context.getBeanNamesForAnnotation(ThreadManager.class);
//            for(String name:names){
//                try {
//                    Thread bean =(Thread) context.getBean(name);
//                    ThreadManager threadManager=bean.getClass().getAnnotation(ThreadManager.class);
//                    if (threadManager.isUsed()){
//                        if (threadManager.name()!=null){
//                            bean.setName(threadManager.name());
//                        }
//                        logger.info("启动线程:[{}]",bean.getClass().getName());
//                        bean.start();
//                    }
//                }catch (Exception e){
//                    logger.error("",e);
//                }
//            }
//        }
//        else if (event instanceof ContextStartedEvent) {
//            // 应用启动，需要在代码动态添加监听器才可捕获
//            logger.info("ContextStartedEvent");
//        }
//        else if (event instanceof ContextStoppedEvent) {
//            // 应用停止
//            logger.info("ContextStoppedEvent");
//        }
//        else if (event instanceof ContextClosedEvent) {
//            // 应用关闭
//            logger.info("ContextClosedEvent");
//            ApplicationContext context = ((ContextClosedEvent)event).getApplicationContext();
//
//            String[] names=context.getBeanNamesForAnnotation(ThreadManager.class);
//            logger.info("线程类数量："+names.length);
//            for(String name:names){
//                try {
//                    Thread bean =(Thread) context.getBean(name);
//                    if (!bean.isInterrupted()){
//                        logger.info("停止线程:[{}]",bean.getClass().getName());
//                        bean.interrupt();
//                    }
//                }catch (Exception e){
//                    logger.error("",e);
//                }
//            }
//        }
//    }
//}
