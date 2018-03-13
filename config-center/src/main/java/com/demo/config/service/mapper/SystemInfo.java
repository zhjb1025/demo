package com.demo.config.service.mapper;

import com.demo.framework.msg.BaseObject;

public class SystemInfo extends BaseObject{
    private String systemCode;

    private String systemName;

    public String getSystemCode() {
        return systemCode;
    }

    public void setSystemCode(String systemCode) {
        this.systemCode = systemCode == null ? null : systemCode.trim();
    }

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName == null ? null : systemName.trim();
    }
}