package com.demo.mapper;

import com.demo.framework.msg.BaseObject;



public class Config extends BaseObject{
	private long id;
	private String name;  //公司名称
	private String address;//公司地址
	private String postCode;//邮政编码
	private String phone;//联系电话
	private String mail;
	private String fax;//传真
	private String man;//联系人
	private String summary; //简介
	private String content; //内容
	private String factoryAddress;//工厂地址
	private String factoryPhone;//工厂联系电话
	private String factoryFax;//工厂传真
	private String factoryMan;//工厂联系人
	
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getPostCode() {
		return postCode;
	}
	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getMail() {
		return mail;
	}
	public void setMail(String mail) {
		this.mail = mail;
	}
	public String getFax() {
		return fax;
	}
	public void setFax(String fax) {
		this.fax = fax;
	}
	public String getMan() {
		return man;
	}
	public void setMan(String man) {
		this.man = man;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getFactoryAddress() {
		return factoryAddress;
	}
	public void setFactoryAddress(String factoryAddress) {
		this.factoryAddress = factoryAddress;
	}
	public String getFactoryPhone() {
		return factoryPhone;
	}
	public void setFactoryPhone(String factoryPhone) {
		this.factoryPhone = factoryPhone;
	}
	public String getFactoryFax() {
		return factoryFax;
	}
	public void setFactoryFax(String factoryFax) {
		this.factoryFax = factoryFax;
	}
	public String getFactoryMan() {
		return factoryMan;
	}
	public void setFactoryMan(String factoryMan) {
		this.factoryMan = factoryMan;
	}
	
}
