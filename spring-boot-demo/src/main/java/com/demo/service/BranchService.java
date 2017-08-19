package com.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.demo.mapper.BranchInfo;
import com.demo.mapper.BranchInfoMapper;

@Service
public class BranchService {
    @Autowired
    private BranchInfoMapper branchInfoMapper;

    public List<BranchInfo> getAll(){
    	return branchInfoMapper.getAll();
    }

    @Transactional(rollbackFor=Exception.class)
    public void updateBranchInfo(BranchInfo record){
        branchInfoMapper.updateByID(record);
    }

    @Transactional(rollbackFor=Exception.class)
    public void addBranchInfo(BranchInfo record){
        branchInfoMapper.insert(record);
    }

    public String getMaxBranchCode(Integer parentId){
        return branchInfoMapper.getMaxBranchCode(parentId);
    }

    public BranchInfo getBranchInfo(Integer id){
        return branchInfoMapper.selectByID(id);
    }


}
