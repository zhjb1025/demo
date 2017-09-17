package com.demo.mapper;

import java.util.List;

import com.demo.framework.msg.ApiServiceInfo;
import com.demo.framework.msg.BaseObject;

public class MenuInfo extends BaseObject{
    private Integer id;

    private String menuName;

    private String menuCode;

    private Integer parentId;

    private String url;

    private List<ApiServiceInfo> apiServiceInfos;

    public List<ApiServiceInfo> getApiServiceInfos() {
        return apiServiceInfos;
    }

    public void setApiServiceInfos(List<ApiServiceInfo> apiServiceInfos) {
        this.apiServiceInfos = apiServiceInfos;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName == null ? null : menuName.trim();
    }

    public String getMenuCode() {
        return menuCode;
    }

    public void setMenuCode(String menuCode) {
        this.menuCode = menuCode == null ? null : menuCode.trim();
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url == null ? null : url.trim();
    }
}