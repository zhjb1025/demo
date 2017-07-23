package com.demo.mapper;

import com.demo.mapper.MenuInfo;

import java.util.List;

public interface MenuInfoMapper {

    List<MenuInfo> queryUserMenuInfo(Integer userID);

}