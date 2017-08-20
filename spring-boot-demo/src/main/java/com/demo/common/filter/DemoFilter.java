//package com.demo.common.filter;
//
//import java.io.IOException;
//
//import javax.servlet.Filter;
//import javax.servlet.FilterChain;
//import javax.servlet.FilterConfig;
//import javax.servlet.ServletException;
//import javax.servlet.ServletRequest;
//import javax.servlet.ServletResponse;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.springframework.boot.context.embedded.FilterRegistrationBean;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.stereotype.Component;
//
//
//@Component
//@Configuration  
//public class DemoFilter implements Filter{
//
//  @Override
//  public void destroy() {
//
//  }
//
//  @Override
//  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
//      throws IOException, ServletException {
////      HttpServletResponse rsp = (HttpServletResponse) response;
////      HttpServletRequest req = (HttpServletRequest) request;
//      System.out.println(((HttpServletRequest) request).getRequestURI());
////      rsp.setHeader("Content-Type", "application/json; charset=UTF-8");
////      rsp.addHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
////      rsp.addHeader("Access-Control-Allow-Headers", "Origin, No-Cache, X-Requested-With, If-Modified-Since, Pragma, Last-Modified, Cache-Control, Expires, Content-Type, X-E4M-With");
//      chain.doFilter(request, response);
//    
//  }
//
//  @Override
//  public void init(FilterConfig arg0) throws ServletException {
//    System.out.println("DemoFilter");        
//  }
//  @Bean  
//  public FilterRegistrationBean filterRegistrationBean(DemoFilter filter){  
//      FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();  
//      filterRegistrationBean.setFilter(filter);  
//      filterRegistrationBean.setEnabled(true);  
//      filterRegistrationBean.addUrlPatterns("/*");  
//      filterRegistrationBean.setOrder(1);
//      return filterRegistrationBean;  
//  }    
//
//}
