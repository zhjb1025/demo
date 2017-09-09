package com.demo.controller.msg;

import java.util.List;

import com.demo.mapper.JobInfo;

/**
 * Created by Auser on 2017/7/15.
 */
public class QueryJobResponse extends BaseResponse {
	private List<JobInfo> list;

	public List<JobInfo> getList() {
		return list;
	}

	public void setList(List<JobInfo> list) {
		this.list = list;
	}
    
}
