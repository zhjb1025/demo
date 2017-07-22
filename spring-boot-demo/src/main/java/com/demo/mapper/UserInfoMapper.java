package com.demo.mapper;

import java.util.List;

public interface UserInfoMapper {

    int insert(UserInfo record);


    List<UserInfo> select(UserInfo record);

    int updateById(UserInfo record);
}