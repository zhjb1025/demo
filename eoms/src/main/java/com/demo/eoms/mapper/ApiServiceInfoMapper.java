package com.demo.eoms.mapper;

import com.demo.eoms.mapper.ApiServiceInfo;

public interface ApiServiceInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ApiServiceInfo record);

    int insertSelective(ApiServiceInfo record);

    ApiServiceInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ApiServiceInfo record);

    int updateByPrimaryKey(ApiServiceInfo record);
}