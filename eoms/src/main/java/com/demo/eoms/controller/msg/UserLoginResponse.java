package com.demo.eoms.controller.msg;

import com.demo.framework.msg.BaseResponse;

/**
 * Created by Auser on 2017/7/15.
 */
public class UserLoginResponse extends BaseResponse {

    private Integer userId;

    private String token;

    private String userName;

    private Integer branchId;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getBranchId() {
        return branchId;
    }

    public void setBranchId(Integer branchId) {
        this.branchId = branchId;
    }

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
}
