package com.demo.mapper;

import com.demo.mapper.BranchInfo;

import java.util.List;

public interface BranchInfoMapper {

    List<BranchInfo> getAll();

    int insert(BranchInfo record);

    BranchInfo selectByID(Integer id);

    int updateByID(BranchInfo record);

}