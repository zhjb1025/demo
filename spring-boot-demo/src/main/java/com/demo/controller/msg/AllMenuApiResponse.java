package com.demo.controller.msg;

import java.util.List;

import com.demo.framework.msg.BaseResponse;
import com.demo.mapper.MenuInfo;

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
