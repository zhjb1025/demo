package com.demo.eoms.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.eoms.mapper.MenuInfo;
import com.demo.eoms.mapper.MenuInfoMapper;

@Service
public class MenuInfoService {
    @Autowired
    private MenuInfoMapper menuInfoMapper;

    public List<MenuInfo> queryUserMenuInfo(Integer userID){
    	return menuInfoMapper.queryUserMenuInfo(userID);
    }

}
