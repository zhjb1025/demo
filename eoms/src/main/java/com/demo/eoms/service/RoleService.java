package com.demo.eoms.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.demo.eoms.controller.msg.RoleAddRequest;
import com.demo.eoms.controller.msg.RoleMenuApiResult;
import com.demo.eoms.controller.msg.RolePageQueryResult;
import com.demo.eoms.controller.msg.RoleUpdateRequest;
import com.demo.eoms.mapper.MenuInfo;
import com.demo.eoms.mapper.RoleInfo;
import com.demo.eoms.mapper.RoleInfoMapper;
import com.demo.framework.Constant;
import com.demo.framework.msg.LoginUserInfo;
import com.demo.framework.session.redis.RedisSessionService;

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
//    public  List<Integer> queryRoleAipIDs(Integer roleID){
//        return roleInfoMapper.queryRoleAipIDs(roleID);
//    }


    public List<RoleMenuApiResult> queryRoleMenuIDApiID( Integer roleID){
        return roleInfoMapper.queryRoleMenuIDApiID(roleID);
    }


    @Transactional(rollbackFor=Exception.class)
    public void addRole(RoleAddRequest request){
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
        //apiId#menuId
        for (String apiID:request.getApiIDs()){
        	String [] temp=apiID.split("#");
            roleInfoMapper.insertRoleAip(roleInfo.getId(),Integer.parseInt(temp[0]),Integer.parseInt(temp[1]));
        }
    }


    @Transactional(rollbackFor=Exception.class)
    public void updateRole(RoleUpdateRequest request){
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
        for (String apiID:request.getApiIDs()){
        	String [] temp=apiID.split("#");
            roleInfoMapper.insertRoleAip(roleInfo.getId(),Integer.parseInt(temp[0]),Integer.parseInt(temp[1]));
        }
    }

}
