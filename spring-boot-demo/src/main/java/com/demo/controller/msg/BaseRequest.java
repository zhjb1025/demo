package com.demo.controller.msg;

import javax.validation.constraints.NotNull;

import com.demo.common.validate.ValidatorService;
import org.hibernate.validator.constraints.Length;

import com.demo.common.msg.BaseObject;

public class BaseRequest extends BaseObject {
	
	@NotNull(message="流水号不能空",groups = {ValidatorService.AuthGroup.class,ValidatorService.NotAuthGroup.class})
	@Length(min=14,max=32 ,message="流水号长度不能小于{min} 不能大于{max}"
			,groups = {ValidatorService.AuthGroup.class,ValidatorService.NotAuthGroup.class})
	private String seqNo;
	
	
	@NotNull(message="服务名不能空",groups = {ValidatorService.AuthGroup.class,ValidatorService.NotAuthGroup.class})
	@Length(min=3,max=32 ,message="服务名长度不能小于{min} 不能大于{max}",groups = {ValidatorService.AuthGroup.class,ValidatorService.NotAuthGroup.class})
	private String service;
	
	
	@NotNull(message="版本号不能空",groups = {ValidatorService.AuthGroup.class,ValidatorService.NotAuthGroup.class})
	@Length(min=5,max=10 ,message="版本号长度不能小于{min} 不能大于{max}",groups = {ValidatorService.AuthGroup.class,ValidatorService.NotAuthGroup.class})
	private String version;

	@NotNull(message="用户ID不能空",groups = {ValidatorService.AuthGroup.class})
	private Integer userId;

	@NotNull(message="令牌不能空",groups = {ValidatorService.AuthGroup.class})
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
