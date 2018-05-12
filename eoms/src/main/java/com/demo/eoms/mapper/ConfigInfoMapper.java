package com.demo.eoms.mapper;

import java.util.List;

import com.demo.eoms.controller.msg.QueryConfigInfoRequest;
import com.demo.eoms.controller.msg.QueryConfigInfoResult;

public interface ConfigInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ConfigInfo record);

    int insertSelective(ConfigInfo record);

    List<QueryConfigInfoResult> selectByColumn(QueryConfigInfoRequest record);

    int updateByPrimaryKeySelective(ConfigInfo record);

    int updateByPrimaryKey(ConfigInfo record);
}