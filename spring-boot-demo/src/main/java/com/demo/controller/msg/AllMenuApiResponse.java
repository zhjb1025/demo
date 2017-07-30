package com.demo.controller.msg;

import com.demo.mapper.MenuInfo;

import java.util.List;

/**
 * Created by Auser on 2017/7/15.
 */
public class AllMenuApiResponse extends BaseResponse {
    private List<MenuInfo> menuInfoList;

    public List<MenuInfo> getMenuInfoList() {
        return menuInfoList;
    }

    public void setMenuInfoList(List<MenuInfo> menuInfoList) {
        this.menuInfoList = menuInfoList;
    }
}
