package com.demo.eoms.controller.msg;

import java.util.List;

import com.demo.framework.msg.BaseResponse;

public class GroupQueryAllResponse extends BaseResponse {
	
    private List<String> groupList;

	public List<String> getGroupList() {
		return groupList;
	}

	public void setGroupList(List<String> groupList) {
		this.groupList = groupList;
	}
    
}
