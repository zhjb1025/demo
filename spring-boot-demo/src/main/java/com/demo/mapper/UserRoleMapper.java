package com.demo.mapper;

import com.demo.mapper.UserRole;

public interface UserRoleMapper {
    int insert(UserRole record);

    int insertSelective(UserRole record);
}