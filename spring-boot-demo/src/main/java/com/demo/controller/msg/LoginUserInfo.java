package com.demo.controller.msg;

import java.util.HashMap;
import java.util.Map;

import com.demo.common.msg.BaseObject;
import com.demo.mapper.ApiServiceInfo;

public class LoginUserInfo extends BaseObject {
    private Integer userId;
    private String token;
    private Map<String ,ApiServiceInfo> apiServiceInfoMap=new HashMap<String ,ApiServiceInfo>();

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Map<String, ApiServiceInfo> getApiServiceInfoMap() {
        return apiServiceInfoMap;
    }

    public void setApiServiceInfoMap(Map<String, ApiServiceInfo> apiServiceInfoMap) {
        this.apiServiceInfoMap = apiServiceInfoMap;
    }
}
