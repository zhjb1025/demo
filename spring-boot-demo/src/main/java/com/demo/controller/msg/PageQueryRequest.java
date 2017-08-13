package com.demo.controller.msg;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

public class PageQueryRequest extends BaseRequest{

    @NotNull(message = "开始页不能为空")
    private Integer pageNumber;

    @NotNull(message = "页大小不能为空")
    private  Integer pageSize;

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}