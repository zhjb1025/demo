package com.demo.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserInfoMapper {

    int insert(UserInfo record);


    List<UserInfo> select(UserInfo record);

    int updateById(UserInfo record);

    @Select(" SELECT DISTINCT a.id,a.service,a.is_log as isLog ,a.is_auth as isAuth ,a.version " +
            " from user_role r " +
            " LEFT JOIN role_api_service  ra on ra.role_id=r.role_id " +
            " LEFT JOIN api_service_info a on a.id=ra.api_service_id " +
            " where r.user_id=#{userID,jdbcType=INTEGER}")
    List<ApiServiceInfo> queryUserApiService(@Param("userID") Integer userID);
}