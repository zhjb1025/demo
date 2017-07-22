package com.demo.common.enums;

public enum TradeStatusEnum {
	SUCCESS(1),
	PROCESSING(2),
	FAIL(3);
	Integer tradeStatus;
	
	TradeStatusEnum(Integer tradeStatus){
		this.tradeStatus=tradeStatus;
	}

	public Integer getTradeStatus() {
		return tradeStatus;
	}
}
