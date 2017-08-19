package com.demo.controller.msg;

import java.util.List;

import com.demo.mapper.BranchInfo;

/**
 *
 */
public class QueryAllBranchResponse extends BaseResponse {

    private  List<BranchInfo> branchList;

    public List<BranchInfo> getBranchList() {
        return branchList;
    }

    public void setBranchList(List<BranchInfo> branchList) {
        this.branchList = branchList;
    }
}
