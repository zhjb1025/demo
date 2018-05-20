package com.demo.eoms.controller.msg;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import com.demo.framework.msg.BaseRequest;


public class MenuRequest extends BaseRequest {
	 private Integer id;
	 private Integer parentId;
	 
	 @NotBlank(message="菜单名称不能为空")
	 @Length(min=2,max=32 ,message="菜单名称长度不能小于{min} 不能大于{max}")
	 private String menuName;
	 
	 @NotNull(message="菜单编码不能为空")
	 private String menuCode;
	 
	 private String url;
	 
	 private List<Integer> api;
	 
	 
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getParentId() {
		return parentId;
	}
	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}
	public String getMenuName() {
		return menuName;
	}
	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}
	public String getMenuCode() {
		return menuCode;
	}
	public void setMenuCode(String menuCode) {
		this.menuCode = menuCode;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public List<Integer> getApi() {
		return api;
	}
	public void setApi(List<Integer> api) {
		this.api = api;
	}
	 
	 
}
