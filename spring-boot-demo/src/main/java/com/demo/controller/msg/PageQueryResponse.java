package com.demo.controller.msg;

import com.demo.common.enums.ErrorCodeEnum;
import com.demo.common.enums.TradeStatusEnum;
import com.demo.common.msg.BaseObject;

import java.util.List;

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
