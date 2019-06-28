package com.demo.test;

import java.math.BigDecimal;
import java.util.Date;

public class TestBatch {
	private Integer id;
	private String name;
	private Date createTime;
	private Integer avgAge;
	private String remarkTestCon;
	
	private BigDecimal price;
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Integer getAvgAge() {
		return avgAge;
	}
	public void setAvgAge(Integer avgAge) {
		this.avgAge = avgAge;
	}
	public String getRemarkTestCon() {
		return remarkTestCon;
	}
	public void setRemarkTestCon(String remarkTestCon) {
		this.remarkTestCon = remarkTestCon;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	
	
}
