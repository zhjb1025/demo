package com.demo.controller.msg;

import java.util.List;

import com.demo.framework.msg.BaseResponse;
import com.demo.mapper.MenuInfo;

/**
 *
 */
public class QueryUserMenuResponse extends BaseResponse {

    private  List<MenuInfo> menuInfoList;

    public List<MenuInfo> getMenuInfoList() {
        return menuInfoList;
    }

    public void setMenuInfoList(List<MenuInfo> menuInfoList) {
        this.menuInfoList = menuInfoList;
    }
}
