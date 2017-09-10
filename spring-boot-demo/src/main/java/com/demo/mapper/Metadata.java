package com.demo.mapper;

import java.io.Serializable;

import com.demo.framework.msg.BaseObject;

public class Metadata extends BaseObject implements Serializable {
	private static final long serialVersionUID = 2060653314226258519L;

	private Integer id;

    private String metaGroup;

    private String metaCode;

    private String remark;

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

    public String getMetaGroup() {
        return metaGroup;
    }

    public void setMetaGroup(String metaGroup) {
        this.metaGroup = metaGroup;
    }

    public String getMetaCode() {
        return metaCode;
    }

    public void setMetaCode(String metaCode) {
        this.metaCode = metaCode;
    }
}