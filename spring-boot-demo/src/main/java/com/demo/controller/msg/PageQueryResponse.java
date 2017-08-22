package com.demo.controller.msg;

import java.util.List;

import com.demo.framework.msg.BaseObject;

public class PageQueryResponse<T extends BaseObject> extends BaseResponse {
	
	private Long total;

    private List<T> rows;

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }
}
