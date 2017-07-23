package com.demo.controller.msg;

import com.demo.mapper.BranchInfo;
import com.demo.mapper.MenuInfo;

import java.util.List;

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
