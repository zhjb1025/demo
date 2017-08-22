package com.demo.controller.msg;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import com.demo.framework.msg.BaseObject;

public class BaseRequest extends BaseObject {
	
	@NotBlank(message="流水号不能空")
	@Length(min=14,max=32 ,message="流水号长度不能小于{min} 不能大于{max}")
	private String seqNo;
	
	
	@NotBlank(message="服务名不能空")
	@Length(min=3,max=32 ,message="服务名长度不能小于{min} 不能大于{max}")
	private String service;
	
	
	@NotBlank(message="版本号不能空")
	@Length(min=5,max=10 ,message="版本号长度不能小于{min} 不能大于{max}")
	private String version;


	private Integer userId;


	private String token;


//	@NotNull(message="签名方式不能空")
//	@Length(min=3,max=10 ,message="签名方式长度不能小于{min} 不能大于{max}")
//	private String sign;
//	
//	@NotNull(message="签名值不能空")
//	@Length(min=16,max=64 ,message="签名值长度不能小于{min} 不能大于{max}")
//	private String signValue;

	public String getSeqNo() {
		return seqNo;
	}

	public void setSeqNo(String seqNo) {
		this.seqNo = seqNo;
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

//	public String getSign() {
//		return sign;
//	}
//
//	public void setSign(String sign) {
//		this.sign = sign;
//	}
//
//	public String getSignValue() {
//		return signValue;
//	}
//
//	public void setSignValue(String signValue) {
//		this.signValue = signValue;
//	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
}
