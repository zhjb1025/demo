package com.demo.mapper;

import com.demo.controller.msg.UserPageQueryResult;
import org.apache.ibatis.annotations.*;

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


    List<UserPageQueryResult> pageQueryUserInfo(@Param("userName") String userName, @Param("loginName") String loginName);

    @Delete("delete from user_role where user_id=#{userID,jdbcType=INTEGER}")
    int deleteUserRole(@Param("userID") Integer userID);

    @Insert("insert into user_role(user_id,role_id) values (#{userID,jdbcType=INTEGER},#{roleID,jdbcType=INTEGER})")
    int insertUserRole(@Param("userID") Integer userID,@Param("roleID") Integer roleID);
}