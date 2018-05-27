package com.demo.eoms.controller.msg;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import com.demo.framework.msg.BaseRequest;

public class ConfigAddRequest extends BaseRequest{
    private Integer id;
	
	@NotBlank(message = "业务系统不能为空")
    private String systemCode;

	@NotBlank(message = "配置编码不能为空")
	@Length(min=2,max=64 ,message="配置编码长度不能小于{min} 不能大于{max}")
    private String configCode;
	
	@NotBlank(message = "配置类型不能为空")
	@Length(min=2,max=2 ,message="配置类型长度不能小于{min} 不能大于{max}")
    private String configType;
	
	@NotBlank(message = "配置值不能为空")
	@Length(min=1,max=256 ,message="配置值长度不能小于{min} 不能大于{max}")
    private String configValue;
	
	@NotBlank(message = "描述不能为空")
	@Length(max=256 ,message="描述长度不能大于{max}")
    private String remark;

	public String getSystemCode() {
		return systemCode;
	}

	public void setSystemCode(String systemCode) {
		this.systemCode = systemCode;
	}

	public String getConfigCode() {
		return configCode;
	}

	public void setConfigCode(String configCode) {
		this.configCode = configCode;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getConfigType() {
		return configType;
	}

	public void setConfigType(String configType) {
		this.configType = configType;
	}

	public String getConfigValue() {
		return configValue;
	}

	public void setConfigValue(String configValue) {
		this.configValue = configValue;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
}
