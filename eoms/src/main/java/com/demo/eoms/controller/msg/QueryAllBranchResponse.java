package com.demo.eoms.controller.msg;

import java.util.List;

import com.demo.eoms.mapper.BranchInfo;
import com.demo.framework.msg.BaseResponse;

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
