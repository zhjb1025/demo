package com.demo.controller.msg;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;

public class ModifyPasswordRequest extends BaseRequest{

	@NotBlank(message="原密码不能为空")
	private String password;

    @NotBlank(message="新密码不能为空")
    @Pattern(regexp ="^(?![a-zA-z]+$)(?!\\d+$)(?![!@#$%^&*_]+$)[a-zA-Z\\d!@#$%^&*_]+$",message = "密码不合规，必须是字母+数字，字母+特殊字符，数字+特殊字符")
    private String newPassword;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
