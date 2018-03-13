package com.demo.config.service.mapper;

import com.demo.config.service.mapper.ConfigInfo;

public interface ConfigInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ConfigInfo record);

    int insertSelective(ConfigInfo record);

    ConfigInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ConfigInfo record);

    int updateByPrimaryKey(ConfigInfo record);
}