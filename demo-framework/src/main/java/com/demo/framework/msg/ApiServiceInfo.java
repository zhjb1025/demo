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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (isAuth ? 1231 : 1237);
		result = prime * result + (isLog ? 1231 : 1237);
		result = prime * result + (isPublic ? 1231 : 1237);
		result = prime * result + ((service == null) ? 0 : service.hashCode());
		result = prime * result + ((version == null) ? 0 : version.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ApiServiceInfo other = (ApiServiceInfo) obj;
		if (isAuth != other.isAuth)
			return false;
		if (isLog != other.isLog)
			return false;
		if (isPublic != other.isPublic)
			return false;
		if (service == null) {
			if (other.service != null)
				return false;
		} else if (!service.equals(other.service))
			return false;
		if (version == null) {
			if (other.version != null)
				return false;
		} else if (!version.equals(other.version))
			return false;
		return true;
	}

}