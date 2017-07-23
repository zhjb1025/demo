package com.demo.service;

import com.demo.mapper.BranchInfo;
import com.demo.mapper.BranchInfoMapper;
import com.demo.mapper.MenuInfo;
import com.demo.mapper.MenuInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BranchService {
    @Autowired
    private BranchInfoMapper branchInfoMapper;

    public List<BranchInfo> getAll(){
    	return branchInfoMapper.getAll();
    }
}
