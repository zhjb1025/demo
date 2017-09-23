package com.demo.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.demo.common.enums.UserInfoStatusEnum;
import com.demo.controller.msg.AddUserRequest;
import com.demo.controller.msg.UpdateUserRequest;
import com.demo.controller.msg.UserPageQueryResult;
import com.demo.framework.Constant;
import com.demo.framework.msg.ApiServiceInfo;
import com.demo.framework.msg.LoginUserInfo;
import com.demo.framework.security.DESede;
import com.demo.framework.session.redis.RedisSessionService;
import com.demo.mapper.Metadata;
import com.demo.mapper.UserInfo;
import com.demo.mapper.UserInfoMapper;

@Service
public class UserInfoService {
    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private MetadataService metadataService;
    
    @Autowired
    private RedisSessionService redisSessionService;
    
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
    public void updateUser(UpdateUserRequest request){
        LoginUserInfo loginUser=(LoginUserInfo)redisSessionService.getSessionAttribute(Constant.LOGIN_USER);
        UserInfo record= new UserInfo();
        record.setId(request.getId());
        record.setUserName(request.getUserName());
        record.setBranchId(request.getBranchId());
        record.setUpdateUserId(loginUser.getUserId());
        record.setUpdateTime(new Date());
        userInfoMapper.updateById(record);

        userInfoMapper.deleteUserRole(request.getId());

        for (Integer roleID:request.getRoleList()){
            userInfoMapper.insertUserRole(request.getId(),roleID);
        }
    }

    @Transactional(rollbackFor=Exception.class)
    public void addUser(AddUserRequest request) throws Exception {

        LoginUserInfo loginUser=(LoginUserInfo) redisSessionService.getSessionAttribute(Constant.LOGIN_USER);
        UserInfo record= new UserInfo();
        record.setLoginName(request.getLoginName());
        record.setUserName(request.getUserName());
        record.setBranchId(request.getBranchId());
        record.setCreateTime(new Date());
        record.setCreateUserId(loginUser.getUserId());
        record.setStatus(UserInfoStatusEnum.NORMAL.getTradeStatus());
        Metadata defaultPassword = metadataService.queryMetaData("default_password");
        record.setPassword(DESede.encrypt(defaultPassword.getMetaCode()));
        record.setUpdateUserId(loginUser.getUserId());
        record.setUpdateTime(new Date());
        userInfoMapper.insert(record);

        for (Integer roleID:request.getRoleList()){
            userInfoMapper.insertUserRole(record.getId(),roleID);
        }
    }

    public List<ApiServiceInfo> queryUserApiService(Integer userID){
        return userInfoMapper.queryUserApiService(userID);
    }

    public List<UserPageQueryResult> pageQueryUserInfo(String userName, String loginName){
        return userInfoMapper.pageQueryUserInfo(userName,loginName);
    }
}
