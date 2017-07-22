package com.demo.test;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.demo.common.util.HttpUtils;
import com.demo.controller.msg.UserLoginRequest;

public class TestUser {

	public static void main(String[] args) {
		try {
//			HttpUtils.get("http://127.0.0.1:8081/demo/query/1");
			UserLoginRequest request= new UserLoginRequest();
			request.setSeqNo("test_"+System.currentTimeMillis());
			request.setService("user_login");
			request.setVersion("1.0.0");
			request.setLoginName("admin");
//			request.setPwd("111111");
			Map<String,String> header =new HashMap<String, String>();
			header.put("contentType", "application/json");
//			rqUserInfo.setName("问问");
			System.out.println(HttpUtils.post("http://127.0.0.1:8081/demo/gateway/trade",JSON.toJSONString(request) ,header));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
