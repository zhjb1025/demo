package com.demo.eoms.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ApiServiceInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ApiServiceInfo record);

    int insertSelective(ApiServiceInfo record);

    ApiServiceInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ApiServiceInfo record);

    int updateByPrimaryKey(ApiServiceInfo record);
    
    List<ApiServiceInfo> selectByColumn(ApiServiceInfo record);
    
    @Select("select id, service, remark, version from api_service_info " +
            "where service=#{service,jdbcType=VARCHAR}")
    ApiServiceInfo queryApiServiceInfo(@Param("service") String  service);
    
    
}