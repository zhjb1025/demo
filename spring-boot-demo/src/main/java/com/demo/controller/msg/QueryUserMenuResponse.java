package com.demo.controller.msg;

import com.demo.mapper.MenuInfo;

import java.util.List;

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
