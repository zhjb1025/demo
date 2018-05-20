package com.demo.eoms.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.demo.eoms.controller.msg.RoleMenuApiResult;
import com.demo.eoms.controller.msg.RolePageQueryResult;

@Mapper
public interface RoleInfoMapper {

    int insert(RoleInfo record);

    RoleInfo selectByID(Integer id);

    int updateByID(RoleInfo record);

    List<RolePageQueryResult> queryRole(@Param("roleName") String roleName);

    List<MenuInfo>  queryAllMenuApi();

    @Select("select api_service_id as id from role_api_service where role_id=#{roleID,jdbcType=INTEGER}")
    List<Integer> queryRoleAipIDs(@Param("roleID") Integer roleID);

    @Select("select menu_id as menuId,role_id as roleId ,api_service_id as apiId from role_api_service where role_id=#{roleID,jdbcType=INTEGER}")
    List<RoleMenuApiResult> queryRoleMenuIDApiID(@Param("roleID") Integer roleID);

    @Insert("insert into role_api_service(role_id,api_service_id,menu_id) values (#{roleID,jdbcType=INTEGER},#{aipID,jdbcType=INTEGER},#{menuID,jdbcType=INTEGER})")
    int insertRoleAip(@Param("roleID") Integer roleID, @Param("aipID") Integer aipID, @Param("menuID") Integer menuID);

    @Insert("insert into role_menu(role_id,menu_id) values (#{roleID,jdbcType=INTEGER},#{menuID,jdbcType=INTEGER})")
    int insertRoleMenu(@Param("roleID") Integer roleID, @Param("menuID") Integer menuID);

    @Delete("delete from role_api_service where role_id=#{roleID,jdbcType=INTEGER}")
    int deleteRoleAip(@Param("roleID") Integer roleID);

    @Delete("delete from role_menu where role_id=#{roleID,jdbcType=INTEGER}")
    int deleteRoleMenu(@Param("roleID") Integer roleID);

    @Select(" select r.id,r.role_name as roleName  " +
            " from user_role ur left join role_info r on ur.role_id=r.id " +
            " where ur.user_id=#{userID,jdbcType=INTEGER} ")
    List<RoleInfo> selectUserRole(@Param("userID") Integer userID);
    
    
    @Select("select menu_id as menuId,role_id as roleId ,api_service_id as apiId from role_api_service where menu_id=#{menuId,jdbcType=INTEGER}")
    List<RoleMenuApiResult> selectRoleMenuIDApiID(@Param("menuId") Integer menuId);
    
    
    @Delete("delete from role_api_service where menu_id=#{menuId,jdbcType=INTEGER} and api_service_id=#{aipID,jdbcType=INTEGER}")
    int deleteMenuAip(@Param("menuId") Integer menuId, @Param("aipID") Integer aipID);
    
    @Delete("delete from role_api_service where menu_id=#{menuId,jdbcType=INTEGER}")
    int deleteMenuAipByMenuId(@Param("menuId") Integer menuId);
    
    @Delete("delete from role_api_service where api_service_id=#{aipID,jdbcType=INTEGER}")
    int deleteMenuAipByApiId( @Param("aipID") Integer aipID);
    
    @Delete("delete from role_menu where menu_id=#{menuId,jdbcType=INTEGER}")
    int deleteRoleMenuByMenuId(@Param("menuId") Integer menuId);

}