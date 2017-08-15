package com.demo.controller.msg;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

public class UserLoginRequest extends BaseRequest{
	
	@NotBlank(message="登录名不能为空")
	@Length(min=3,max=32 ,message="登录名长度不能小于{min} 不能大于{max}")
	private String loginName;

	@NotBlank(message=" 密码不能为空")
	@Length(min=6,max=512 ,message="密码长度不能小于{min} 不能大于{max}")
    private String pwd;

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
}
