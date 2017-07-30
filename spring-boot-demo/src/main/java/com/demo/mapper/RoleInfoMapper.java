package com.demo.mapper;

import com.demo.controller.msg.RolePageQueryResult;
import com.demo.mapper.RoleInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface RoleInfoMapper {

    int insert(RoleInfo record);

    RoleInfo selectByID(Integer id);

    int updateByID(RoleInfo record);

    List<RolePageQueryResult> queryRole(@Param("roleName") String roleName);
}