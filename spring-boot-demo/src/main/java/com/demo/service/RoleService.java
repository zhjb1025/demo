package com.demo.service;

import com.demo.common.Constant;
import com.demo.common.util.SpringContextUtil;
import com.demo.controller.msg.AddRoleRequest;
import com.demo.controller.msg.LoginUserInfo;
import com.demo.controller.msg.RolePageQueryResult;
import com.demo.mapper.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class RoleService {
    @Autowired
    private RoleInfoMapper roleInfoMapper;

    public List<RolePageQueryResult> queryRole( String roleName){
        return  roleInfoMapper.queryRole(roleName);
    }

    public List<MenuInfo>  queryAllMenuApi(){
        return  roleInfoMapper.queryAllMenuApi();
    }
    public  List<Integer> queryRoleAipIDs(Integer roleID){
        return roleInfoMapper.queryRoleAipIDs(roleID);
    }


    public List<Integer> queryRoleMenuIDs( Integer roleID){
        return roleInfoMapper.queryRoleMenuIDs(roleID);
    }


    @Transactional(rollbackFor=Exception.class)
    public void addRole(AddRoleRequest request){
        LoginUserInfo loginUser=(LoginUserInfo) SpringContextUtil.getThreadLocalData().
                request.getSession().getAttribute(Constant.LOGIN_USER);
        RoleInfo roleInfo= new RoleInfo();
        roleInfo.setRoleName(request.getRoleName());
        roleInfo.setRemark(request.getRemark());
        roleInfo.setCreateTime(new Date());
        roleInfo.setCreateUserId(loginUser.getUserId());
        roleInfo.setUpdateTime(new Date());
        roleInfo.setUpdateUserId(loginUser.getUserId());
        roleInfoMapper.insert(roleInfo);

        for (Integer menuID:request.getMenuIDs()){
            roleInfoMapper.insertRoleMenu(roleInfo.getId(),menuID);
        }
        for (Integer apiID:request.getApiIDs()){
            roleInfoMapper.insertRoleAip(roleInfo.getId(),apiID);
        }
    }

}
