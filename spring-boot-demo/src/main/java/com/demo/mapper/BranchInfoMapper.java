package com.demo.mapper;

import com.demo.mapper.BranchInfo;

public interface BranchInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(BranchInfo record);

    int insertSelective(BranchInfo record);

    BranchInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(BranchInfo record);

    int updateByPrimaryKey(BranchInfo record);
}