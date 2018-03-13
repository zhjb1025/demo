package com.demo.config.service.mapper;

import com.demo.config.service.mapper.ConfigApply;

public interface ConfigApplyMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ConfigApply record);

    int insertSelective(ConfigApply record);

    ConfigApply selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ConfigApply record);

    int updateByPrimaryKey(ConfigApply record);
}