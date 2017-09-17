package com.demo.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.demo.common.Constant;
import com.demo.controller.msg.AddRoleRequest;
import com.demo.controller.msg.RolePageQueryResult;
import com.demo.controller.msg.UpdateRoleRequest;
import com.demo.framework.msg.LoginUserInfo;
import com.demo.framework.session.redis.RedisSessionService;
import com.demo.mapper.MenuInfo;
import com.demo.mapper.RoleInfo;
import com.demo.mapper.RoleInfoMapper;

@Service
public class RoleService {
    @Autowired
    private RoleInfoMapper roleInfoMapper;
    
    @Autowired
    private RedisSessionService redisSessionService;

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
        LoginUserInfo loginUser=(LoginUserInfo)redisSessionService.getSessionAttribute(Constant.LOGIN_USER);
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


    @Transactional(rollbackFor=Exception.class)
    public void updateRole(UpdateRoleRequest request){
        LoginUserInfo loginUser=(LoginUserInfo) redisSessionService.getSessionAttribute(Constant.LOGIN_USER);
        RoleInfo roleInfo= new RoleInfo();
        roleInfo.setId(request.getId());
        roleInfo.setRoleName(request.getRoleName());
        roleInfo.setRemark(request.getRemark());
        roleInfo.setUpdateTime(new Date());
        roleInfo.setUpdateUserId(loginUser.getUserId());
        roleInfoMapper.updateByID(roleInfo);

        roleInfoMapper.deleteRoleAip(request.getId());
        roleInfoMapper.deleteRoleMenu(request.getId());
        for (Integer menuID:request.getMenuIDs()){
            roleInfoMapper.insertRoleMenu(roleInfo.getId(),menuID);
        }
        for (Integer apiID:request.getApiIDs()){
            roleInfoMapper.insertRoleAip(roleInfo.getId(),apiID);
        }
    }

}
