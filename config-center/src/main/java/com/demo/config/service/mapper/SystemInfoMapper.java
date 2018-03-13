package com.demo.config.service.mapper;

import com.demo.config.service.mapper.SystemInfo;

public interface SystemInfoMapper {
    int deleteByPrimaryKey(String systemCode);

    int insert(SystemInfo record);

    int insertSelective(SystemInfo record);

    SystemInfo selectByPrimaryKey(String systemCode);

    int updateByPrimaryKeySelective(SystemInfo record);

    int updateByPrimaryKey(SystemInfo record);
}