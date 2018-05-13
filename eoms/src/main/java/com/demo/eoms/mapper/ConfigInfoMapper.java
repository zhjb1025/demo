package com.demo.eoms.mapper;

import java.util.List;

import com.demo.eoms.controller.msg.ConfigInfoQueryRequest;
import com.demo.eoms.controller.msg.ConfigInfoQueryResult;

public interface ConfigInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ConfigInfo record);

    int insertSelective(ConfigInfo record);

    List<ConfigInfoQueryResult> selectByColumn(ConfigInfoQueryRequest record);

    int updateByPrimaryKeySelective(ConfigInfo record);

    int updateByPrimaryKey(ConfigInfo record);
}