package com.demo.controller.msg;

import org.apache.commons.lang.StringUtils;
import org.hibernate.validator.constraints.NotBlank;

public class AccessLogQueryRequest extends PageQueryRequest {
    @NotBlank(message="开始时间不能为空")
    private String startTime;
    @NotBlank(message="结束时间不能为空")
    private String endTime;
    private String queryService;
    private String queryVersion;
    private String queryUserId;

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
}
