package com.demo.eoms.controller.msg;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import com.demo.framework.msg.BaseRequest;


public class SystemInfoRequest extends  BaseRequest {
	@NotBlank(message="系统编码不能为空")
	@Length(min=2,max=64 ,message="系统编码长度不能小于{min} 不能大于{max}")
	private String systemCode;
	
	@NotBlank(message="系统名称不能为空")
	@Length(min=2,max=128 ,message="系统名称长度不能小于{min} 不能大于{max}")
	private String systemName;

	public String getSystemCode() {
        return systemCode;
	}

	public String getSystemName() {
		return systemName;
	}

	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}

	public void setSystemCode(String systemCode) {
		this.systemCode = systemCode;
	}
	
	
    
}
