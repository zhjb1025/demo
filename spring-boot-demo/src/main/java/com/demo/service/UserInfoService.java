package com.demo.service;

import java.util.Date;
import java.util.List;

import com.demo.common.Constant;
import com.demo.common.enums.UserInfoStatusEnum;
import com.demo.common.security.DESede;
import com.demo.common.util.SpringContextUtil;
import com.demo.controller.msg.AddUserRequest;
import com.demo.controller.msg.LoginUserInfo;
import com.demo.controller.msg.UpdateUserRequest;
import com.demo.controller.msg.UserPageQueryResult;
import com.demo.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserInfoService {
    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private MetadataService metadataService;
    
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
        LoginUserInfo loginUser=(LoginUserInfo) SpringContextUtil.getThreadLocalData().
                request.getSession().getAttribute(Constant.LOGIN_USER);
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

        LoginUserInfo loginUser=(LoginUserInfo) SpringContextUtil.getThreadLocalData().
                request.getSession().getAttribute(Constant.LOGIN_USER);
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
