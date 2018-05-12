package com.demo.eoms.mapper;

import java.util.List;

public interface SystemInfoMapper {
    int deleteByPrimaryKey(String systemCode);

    int insert(SystemInfo record);

    int insertSelective(SystemInfo record);

    List<SystemInfo> selectByColumn(SystemInfo record);

    int updateByPrimaryKeySelective(SystemInfo record);

    int updateByPrimaryKey(SystemInfo record);
}