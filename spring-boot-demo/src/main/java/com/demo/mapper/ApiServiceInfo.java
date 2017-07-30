package com.demo.mapper;

import com.demo.common.msg.BaseObject;

import java.util.Date;

public class ApiServiceInfo extends BaseObject {
	
    private Integer id;

    private String service;

    private String version;

    private Integer isLog;

    private Integer isAuth;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Integer getIsLog() {
        return isLog;
    }

    public void setIsLog(Integer isLog) {
        this.isLog = isLog;
    }

    public Integer getIsAuth() {
        return isAuth;
    }

    public void setIsAuth(Integer isAuth) {
        this.isAuth = isAuth;
    }
}