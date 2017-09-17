package com.demo.framework.msg;

import java.io.Serializable;

import com.demo.framework.msg.BaseObject;

public class ApiServiceInfo extends BaseObject  implements Serializable {
	private static final long serialVersionUID = 6708089639643255787L;

	private Integer id;

    private String service;

    private String version;

    private String remark;
    
    private boolean isPublic;
    private boolean isLog;
    private boolean isAuth;
    
    

    public boolean isPublic() {
		return isPublic;
	}

	public void setPublic(boolean isPublic) {
		this.isPublic = isPublic;
	}

	public boolean isLog() {
		return isLog;
	}

	public void setLog(boolean isLog) {
		this.isLog = isLog;
	}

	public boolean isAuth() {
		return isAuth;
	}

	public void setAuth(boolean isAuth) {
		this.isAuth = isAuth;
	}

	public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

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

}