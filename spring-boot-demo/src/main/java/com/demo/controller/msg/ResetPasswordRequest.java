package com.demo.controller.msg;

import javax.validation.constraints.NotNull;

public class ResetPasswordRequest extends BaseRequest{

    @NotNull(message = "ID不能为空")
    private Integer id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

}
