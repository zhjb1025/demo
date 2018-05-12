package com.demo.eoms.mapper;

public interface ConfigApplyMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ConfigApply record);

    int insertSelective(ConfigApply record);

    ConfigApply selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ConfigApply record);

    int updateByPrimaryKey(ConfigApply record);
}