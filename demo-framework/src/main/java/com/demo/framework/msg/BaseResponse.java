package com.demo.framework.msg;

import com.demo.framework.enums.TradeStatusEnum;
import com.demo.framework.exception.FrameworkErrorCode;
import com.demo.framework.msg.BaseObject;

public class BaseResponse extends BaseObject {
	
	/**
	 * 流水号同请求
	 */
	private String seqNo;
	
	/**
	 *  响应码
	 */
	private String rspCode= FrameworkErrorCode.SUCCESS.getCode();
	
	/**
	 * 响应信息
	 */
	private String rspMsg=FrameworkErrorCode.SUCCESS.getMsg();
	
	/**
	 * 处理状态
	 */
	private Integer tradeStatus=TradeStatusEnum.SUCCESS.getTradeStatus();
	
//	/**
//	 * 签名方式
//	 */
//	private String sign;
//	
//	/**
//	 * 签名方式
//	 */
//	private String signValue;

	public String getSeqNo() {
		return seqNo;
	}

	public void setSeqNo(String seqNo) {
		this.seqNo = seqNo;
	}

	public String getRspCode() {
		return rspCode;
	}

	public void setRspCode(String rspCode) {
		this.rspCode = rspCode;
	}

	public String getRspMsg() {
		return rspMsg;
	}

	public void setRspMsg(String rspMsg) {
		this.rspMsg = rspMsg;
	}

	public Integer getTradeStatus() {
		return tradeStatus;
	}

	public void setTradeStatus(Integer tradeStatus) {
		this.tradeStatus = tradeStatus;
	}

//	public String getSign() {
//		return sign;
//	}
//
//	public void setSign(String sign) {
//		this.sign = sign;
//	}
//
//	public String getSignValue() {
//		return signValue;
//	}
//
//	public void setSignValue(String signValue) {
//		this.signValue = signValue;
//	}
}
