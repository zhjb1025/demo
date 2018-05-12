package com.demo.eoms.controller.msg;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import com.demo.framework.msg.BaseRequest;

public class UpdateUserStatusRequest extends BaseRequest{

    @NotNull(message = "ID不能为空")
    private Integer id;

	@NotBlank(message="状态不能为空")
	private String status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
