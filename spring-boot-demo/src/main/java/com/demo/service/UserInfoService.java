package com.demo.service;

import java.util.List;

import com.demo.mapper.ApiServiceInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.demo.mapper.UserInfo;
import com.demo.mapper.UserInfoMapper;

@Service
public class UserInfoService {
    @Autowired
    private UserInfoMapper userInfoMapper;
    
    public UserInfo getUserInfoById(Integer id){
    	UserInfo record= new UserInfo();
    	record.setId(id);
    	List<UserInfo> list = userInfoMapper.select(record);
    	if(list!=null){
    		return list.get(0);
    	}
     	return null;
    }
    
    @Transactional(rollbackFor=Exception.class)
    public int updateUserInfo(UserInfo userInfo) throws Exception{
      return userInfoMapper.updateById(userInfo);
    }
    
    public List<UserInfo> getUserInfoList(UserInfo record){
      return userInfoMapper.select(record);
    }
    
   
    
    @Transactional(rollbackFor=Exception.class)
    public int addUser(UserInfo userInfo){
      return userInfoMapper.insert(userInfo);
    }

    public List<ApiServiceInfo> queryUserApiService(Integer userID){
        return userInfoMapper.queryUserApiService(userID);
    }
}
