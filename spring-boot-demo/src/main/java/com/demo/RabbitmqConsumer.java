//package com.demo;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.amqp.rabbit.annotation.RabbitHandler;
//import org.springframework.amqp.rabbit.annotation.RabbitListener;
//import org.springframework.stereotype.Component;
//
//@Component
//@RabbitListener(queues = "lender.hello")
//public class RabbitmqConsumer {
//  
//  private static Logger log = LoggerFactory.getLogger(RabbitmqConsumer.class);  
//  @RabbitHandler
//  public void process(String hello){
//    log.info("--------"+hello);
//    try {
//      Thread.sleep(1000*5);
//    } catch (InterruptedException e) {
//      log.error("", e);
//    }
//  }
//}
