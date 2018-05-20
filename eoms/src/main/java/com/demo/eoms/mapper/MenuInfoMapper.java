package com.demo.eoms.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.demo.eoms.mapper.MenuInfo;

@Mapper
public interface MenuInfoMapper {

    List<MenuInfo> queryUserMenuInfo(Integer userID);
    
    
    @Select("SELECT m.id ,m.menu_name as menuName,m.parent_id as parentId ,m.url,m.menu_code as menuCode FROM menu_info m  ORDER BY m.menu_code ")
    List<MenuInfo> queryAllMenuInfo();
    
    
    @Select("select a.id, a.service, a.remark, a.version from menu_api_service ma LEFT JOIN api_service_info a ON ma.api_service_id=a.id "
    		+ "where ma.menu_id =#{id,jdbcType=INTEGER}")
    List<ApiServiceInfo> queryMenuApi(@Param("id") Integer id);
    
    int insert(MenuInfo record);
    
    int updateByPrimaryKey(MenuInfo record);
    
    MenuInfo selectByPrimaryKey(Integer id);
    
    @Insert("insert into menu_api_service(menu_id,api_service_id) values (#{menuId,jdbcType=INTEGER},#{apiId,jdbcType=INTEGER})")
    int insertMenuApi(@Param("menuId")  Integer menuId,@Param("apiId")  Integer apiId);
    
    @Delete("delete from menu_api_service where menu_id=#{menuId,jdbcType=INTEGER}")
    int deleteMenuApi(@Param("menuId") Integer menuId);
    
    @Select("SELECT m.id ,m.menu_name as menuName,m.parent_id as parentId ,m.url  FROM menu_info m  where menu_code like #{menuCode}  ORDER BY m.menu_code desc")
    List<MenuInfo> queryMenuInfo(@Param("menuCode") String menuCode);
    
    @Delete("delete from menu_info where id=#{id,jdbcType=INTEGER}")
    int delete(@Param("id") Integer id);
    
    @Delete("delete from menu_api_service where api_service_id=#{apiId,jdbcType=INTEGER}")
    int deleteMenuApiByApiId(@Param("apiId") Integer apiId);
    
}