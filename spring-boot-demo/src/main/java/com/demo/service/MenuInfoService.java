package com.demo.service;

import com.demo.mapper.MenuInfo;
import com.demo.mapper.MenuInfoMapper;
import com.demo.mapper.UserInfo;
import com.demo.mapper.UserInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MenuInfoService {
    @Autowired
    private MenuInfoMapper menuInfoMapper;

    public List<MenuInfo> queryUserMenuInfo(Integer userID){
    	return menuInfoMapper.queryUserMenuInfo(userID);
    }

}
