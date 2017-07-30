package com.demo.service;

import com.demo.controller.msg.RolePageQueryResult;
import com.demo.mapper.BranchInfo;
import com.demo.mapper.BranchInfoMapper;
import com.demo.mapper.RoleInfoMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RoleService {
    @Autowired
    private RoleInfoMapper roleInfoMapper;

    public List<RolePageQueryResult> queryRole( String roleName){
        return  roleInfoMapper.queryRole(roleName);
    }

}
