package com.demo.mapper;

import com.demo.controller.msg.RolePageQueryResult;
import com.demo.mapper.RoleInfo;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface RoleInfoMapper {

    int insert(RoleInfo record);

    RoleInfo selectByID(Integer id);

    int updateByID(RoleInfo record);

    List<RolePageQueryResult> queryRole(@Param("roleName") String roleName);

    List<MenuInfo>  queryAllMenuApi();

    @Select("select api_service_id as id from role_api_service where role_id=#{roleID,jdbcType=INTEGER}")
    List<Integer> queryRoleAipIDs(@Param("roleID") Integer roleID);

    @Select("select menu_id as id from role_menu where role_id=#{roleID,jdbcType=INTEGER}")
    List<Integer> queryRoleMenuIDs(@Param("roleID") Integer roleID);

    @Insert("insert into role_api_service(role_id,api_service_id) values (#{roleID,jdbcType=INTEGER},#{aipID,jdbcType=INTEGER})")
    int insertRoleAip(@Param("roleID") Integer roleID, @Param("aipID") Integer aipID);

    @Insert("insert into role_menu(role_id,menu_id) values (#{roleID,jdbcType=INTEGER},#{menuID,jdbcType=INTEGER})")
    int insertRoleMenu(@Param("roleID") Integer roleID, @Param("menuID") Integer menuID);

    @Delete("delete from role_api_service where role_id=#{roleID,jdbcType=INTEGER}")
    int deleteRoleAip(@Param("roleID") Integer roleID);

    @Delete("delete from role_menu where role_id=#{roleID,jdbcType=INTEGER}")
    int deleteRoleMenu(@Param("roleID") Integer roleID);
}