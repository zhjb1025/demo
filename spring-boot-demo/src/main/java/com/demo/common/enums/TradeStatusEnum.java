package com.demo.common.enums;

public enum TradeStatusEnum {
	SUCCESS(1),  //成功
	PROCESSING(2), // 处理中
	FAIL(3);    //失败
	Integer tradeStatus;
	
	TradeStatusEnum(Integer tradeStatus){
		this.tradeStatus=tradeStatus;
	}

    public static String getLabel(Integer tradeStatus){
	    if (SUCCESS.tradeStatus.equals(tradeStatus)){
	        return "成功";
        }else if(PROCESSING.tradeStatus.equals(tradeStatus)){
            return "处理中";
        }else if(FAIL.tradeStatus.equals(tradeStatus)){
            return "失败";
        }else{
            return null;
        }
    }

	public Integer getTradeStatus() {
		return tradeStatus;
	}
}
