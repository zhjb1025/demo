package com.demo.controller.msg;

import org.springframework.data.mongodb.core.mapping.Document;

import com.demo.common.msg.BaseObject;

@Document(collection="access_log")
public class AccessLog extends BaseObject {
    private String seqNo;
    private String service;
    private String version;
    private String userId;
    private String rspCode;
    private String rspMsg;
    private Integer tradeStatus;
    private long startTimestamp;
    private long dealTime;
    private String tradeDate;
    private Object request;
    private BaseResponse  response;

    public String getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(String seqNo) {
        this.seqNo = seqNo;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public long getStartTimestamp() {
        return startTimestamp;
    }

    public void setStartTimestamp(long startTimestamp) {
        this.startTimestamp = startTimestamp;
    }

    public long getDealTime() {
        return dealTime;
    }

    public void setDealTime(long dealTime) {
        this.dealTime = dealTime;
    }

    public String getTradeDate() {
        return tradeDate;
    }

    public void setTradeDate(String tradeDate) {
        this.tradeDate = tradeDate;
    }

    public Object getRequest() {
        return request;
    }

    public void setRequest(Object request) {
        this.request = request;
    }

    public BaseResponse getResponse() {
        return response;
    }

    public void setResponse(BaseResponse response) {
        this.response = response;
    }
}
