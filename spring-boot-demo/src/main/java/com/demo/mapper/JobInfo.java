package com.demo.mapper;

import com.demo.framework.msg.BaseObject;

public class JobInfo  extends BaseObject{
	private long id;
	
	private String title;
	
	/**
	 * 有效期
	 */
	private String endDate;
	
	/**
	 * 是否发布
	 */
	private Integer isIssue;
	
	/**
	 * 岗位
	 */
	private String post;
	
	/**
	 * 工作年限
	 */
	private String workYear;
	
	/**
	 * 学历要求
	 */
	private String education;
	/**
	 * 招聘人数
	 */
	private String number;
	/**
	 * 职位要求
	 */
	private String jobRequirements;
	
	/**
	 * 工作职责
	 */
	private String jobResponsibilities;

	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public Integer getIsIssue() {
		return isIssue;
	}

	public void setIsIssue(Integer isIssue) {
		this.isIssue = isIssue;
	}

	public String getPost() {
		return post;
	}

	public void setPost(String post) {
		this.post = post;
	}

	public String getWorkYear() {
		return workYear;
	}

	public void setWorkYear(String workYear) {
		this.workYear = workYear;
	}

	public String getEducation() {
		return education;
	}

	public void setEducation(String education) {
		this.education = education;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getJobRequirements() {
		return jobRequirements;
	}

	public void setJobRequirements(String jobRequirements) {
		this.jobRequirements = jobRequirements;
	}

	public String getJobResponsibilities() {
		return jobResponsibilities;
	}

	public void setJobResponsibilities(String jobResponsibilities) {
		this.jobResponsibilities = jobResponsibilities;
	}
}
