package com.demo.eoms.mapper;

import java.util.List;

import com.demo.eoms.mapper.MenuInfo;

public interface MenuInfoMapper {

    List<MenuInfo> queryUserMenuInfo(Integer userID);

}