package com.demo.eoms.common.enums;

public enum UserInfoStatusEnum {
	NORMAL("00"),    //正常
	FROZEN("01"),      //冻结
	CANCEL("02"); //注销;
	String status;
	
	UserInfoStatusEnum(String status){
		this.status=status;
	}

	public String getTradeStatus() {
		return status;
	}
}
