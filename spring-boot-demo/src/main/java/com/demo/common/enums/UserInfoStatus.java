package com.demo.common.enums;

public enum UserInfoStatus {
	NORMAL("00"),    //正常
	FROZEN("01"),      //冻结
	CANCEL("02"); //注销;
	String status;
	
	UserInfoStatus(String status){
		this.status=status;
	}

	public String getTradeStatus() {
		return status;
	}
}
