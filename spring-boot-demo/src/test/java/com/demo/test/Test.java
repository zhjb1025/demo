package com.demo.test;

import java.net.SocketTimeoutException;

import com.demo.framework.util.HttpUtils;

public class Test {

  public static void main(String[] args) throws SocketTimeoutException, Exception {
    
    for(int i=0;i<20;i++){
      new Work().start();
    }
  }

}
class Work extends Thread{

  @Override
  public void run() {
    try {
      System.out.println(HttpUtils.get("http://127.0.0.1:8081/demo/home"));
    } catch (Exception e) {
      e.printStackTrace();
    }
    
  }
  
}
