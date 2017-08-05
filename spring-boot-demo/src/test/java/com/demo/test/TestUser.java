package com.demo.test;

import com.demo.common.enums.UserInfoStatusEnum;

public class TestUser {

	public static void main(String[] args) {
		try {
//			HttpUtils.get("http://127.0.0.1:8081/demo/query/1");
//			UserLoginRequest request= new UserLoginRequest();
//			request.setSeqNo("test_"+System.currentTimeMillis());
//			request.setService("user_login");
//			request.setVersion("1.0.0");
//			request.setLoginName("admin");
//			request.setPwd("111111");
//			Map<String,String> header =new HashMap<String, String>();
//			header.put("contentType", "application/json");
////			rqUserInfo.setName("问问");
//			String json = HttpUtils.post("http://127.0.0.1:8081/demo/gateway/trade", JSON.toJSONString(request), header);
//			System.out.println("["+ json+"]");
//			System.out.println("["+ CommUtil.getJsonValue(json,"userName")+"]");
            System.out.println( UserInfoStatusEnum.valueOf("01"));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
