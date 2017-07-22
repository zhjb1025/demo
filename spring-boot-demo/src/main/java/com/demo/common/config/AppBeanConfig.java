//package com.demo.common.config;
//
//import org.mybatis.spring.mapper.MapperScannerConfigurer;
//import org.springframework.amqp.core.Queue;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//
//@Configuration
//public class AppBeanConfig {
//  @Bean
//  Queue queue() {
//    return new Queue("lender.hello");
//  }
//
////  @Bean
////  public DataSource dataSource() {
////    DruidDataSource dataSource = new DruidDataSource();
////    // 配置获取连接等待超时的时间
////    dataSource.setDriverClassName("org.postgresql.Driver");
////    dataSource.setUrl("jdbc:postgresql://127.0.0.1:5432/umpay");
////    dataSource.setUsername("test");// 用户名
////    dataSource.setPassword("111111");// 密码
////    dataSource.setInitialSize(2);
////    dataSource.setMaxActive(20);
////    dataSource.setMinIdle(2);
////    // 配置获取连接等待超时的时间
////    dataSource.setMaxWait(60000);
////    // 配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒
////    dataSource.setTimeBetweenEvictionRunsMillis(60000);
////    // 空闲测试SQL
////    dataSource.setValidationQuery("select 1");
////
////    // 配置一个连接在池中最小生存的时间，单位是毫秒
////    dataSource.setMinEvictableIdleTimeMillis(300000);
////    dataSource.setTestOnBorrow(false);
////    dataSource.setTestWhileIdle(true);
////    dataSource.setTestOnReturn(false);
////    dataSource.setPoolPreparedStatements(false);
////    dataSource.setMaxPoolPreparedStatementPerConnectionSize(300);
////    try {
////      dataSource.setConnectionProperties("druid.stat.mergeSql=true;druid.stat.slowSqlMillis=5000");
////      dataSource.setFilters("stat,wall,log4j");
////    } catch (SQLException e) {
////      log.error("", e);
////    }
////    return dataSource;
////  }
//
//  @Bean
//  public MapperScannerConfigurer mapperScannerConfigurer() {
//    MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
//    mapperScannerConfigurer.setBasePackage("com.demo");
//    return mapperScannerConfigurer;
//  }
//
//  // @Bean
//  // Binding binding(Queue queue, DirectExchange exchange) {
//  // return BindingBuilder.bind(queue).to(exchange).with("lender.hello");
//  // }
//}
