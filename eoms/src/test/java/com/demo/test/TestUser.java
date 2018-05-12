package com.demo.test;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import com.alibaba.fastjson.JSON;

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
//            System.out.println( UserInfoStatusEnum.valueOf("01"));
			
			 FileInputStream fis = null;
			 fis = new FileInputStream("E:/work/demo/spring-boot-demo/src/main/resources/application-dev.properties");
		     Properties props = new Properties();
		     props.load(fis);
		     List<ConfigInfo> list= new ArrayList<ConfigInfo>();
		     Iterator<Object> iter = props.keySet().iterator();
		     while(iter.hasNext()) {
		    	 String key=iter.next().toString();
		    	 ConfigInfo configInfo= new ConfigInfo();
		    	 configInfo.setKey(key);
		    	 configInfo.setGroup(key.split("\\.")[1]);
		    	 configInfo.setValue(props.getProperty(key));
		    	 list.add(configInfo);
		     }
		     fis.close();
		     System.out.println(JSON.toJSONString(list));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
}
class ConfigInfo{
	private String group="test";
	private String key;
	private String value;
	private String remark="待补充";
	public String getGroup() {
		return group;
	}
	public void setGroup(String group) {
		this.group = group;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
}
