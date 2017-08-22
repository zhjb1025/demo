package com.demo.controller.msg;

import org.hibernate.validator.constraints.NotBlank;

import com.demo.framework.annotation.DateTime;
import com.demo.framework.annotation.IntervalDay;
import com.demo.framework.annotation.SameYear;
import com.demo.framework.annotation.StartEndTime;

@StartEndTime(endTimeField="endTime",startTimeField="startTime")
@IntervalDay(endTimeField="endTime",startTimeField="startTime",interval=10)
@SameYear(endTimeField="endTime",startTimeField="startTime")
public class AccessLogQueryRequest extends PageQueryRequest {
    @NotBlank(message="开始时间不能为空")
    @DateTime
    private String startTime;
    @NotBlank(message="结束时间不能为空")
    @DateTime
    private String endTime;
    private String queryService;
    private String queryVersion;
    private String queryUserId;
    private Integer dealTime;

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getQueryService() {
        return queryService;
    }

    public void setQueryService(String queryService) {
        this.queryService = queryService;
    }

    public String getQueryVersion() {
        return queryVersion;
    }

    public void setQueryVersion(String queryVersion) {
        this.queryVersion = queryVersion;
    }

    public String getQueryUserId() {
        return queryUserId;
    }

    public void setQueryUserId(String queryUserId) {
        this.queryUserId = queryUserId;
    }

    public Integer getDealTime() {
        return dealTime;
    }

    public void setDealTime(Integer dealTime) {
        this.dealTime = dealTime;
    }
}
