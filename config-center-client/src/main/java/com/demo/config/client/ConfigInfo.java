package com.demo.config.client;

public class ConfigInfo {
	private String group;
	private String key;
	private String value;
	private String remark;
	public String getGroup() {
		return group;
	}
	public void setGroup(String group) {
		this.group = group;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	@Override
	public String toString() {
		return "ConfigInfo [group=" + group + ", key=" + key + ", value=" + value + ", remark=" + remark + "]";
	}
	
	
}
