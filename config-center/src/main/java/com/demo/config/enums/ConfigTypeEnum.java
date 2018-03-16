package com.demo.config.enums;

public enum ConfigTypeEnum {
	SYSTEM("00","系统参数"),
	BUSINESS("01","业务参数");
	
	private String type;
	private String name;
	ConfigTypeEnum (String type,String name){
		this.type=type;
		this.name=name;
	}
//	public String getType() {
//		return type;
//	}
//	public void setType(String type) {
//		this.type = type;
//	}
//	public String getName() {
//		return name;
//	}
//	public void setName(String name) {
//		this.name = name;
//	}
	
	public static String getName(String type) {
		if(ConfigTypeEnum.SYSTEM.type.equals(type)){
			return ConfigTypeEnum.SYSTEM.name;
		}else if(ConfigTypeEnum.BUSINESS.type.equals(type)){
			return ConfigTypeEnum.BUSINESS.name;
		}else{
			return null;
		}
	}
}
