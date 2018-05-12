package com.demo.framework.msg;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class LoginUserInfo extends BaseObject  implements Serializable {
	private static final long serialVersionUID = 1605578504372030891L;
	private Integer userId;
    private String token;
    
    private String loginName;

    private String userName;

    private Integer branchId;
    
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
    
    

    public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

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

	public Map<String, ApiServiceInfo> getApiServiceInfoMap() {
        return apiServiceInfoMap;
    }

    public void setApiServiceInfoMap(Map<String, ApiServiceInfo> apiServiceInfoMap) {
        this.apiServiceInfoMap = apiServiceInfoMap;
    }
}
