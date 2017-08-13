package com.demo.controller.msg;

import com.demo.mapper.MenuInfo;

import java.util.List;

/**
 * Created by Auser on 2017/7/15.
 */
public class QueryRoleMenuApiResponse extends BaseResponse {
    private List<Integer>  apiIDs;

    private List<Integer>  menuIDs;

    public List<Integer> getApiIDs() {
        return apiIDs;
    }

    public void setApiIDs(List<Integer> apiIDs) {
        this.apiIDs = apiIDs;
    }

    public List<Integer> getMenuIDs() {
        return menuIDs;
    }

    public void setMenuIDs(List<Integer> menuIDs) {
        this.menuIDs = menuIDs;
    }
}