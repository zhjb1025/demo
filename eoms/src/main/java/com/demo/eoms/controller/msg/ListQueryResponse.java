package com.demo.eoms.controller.msg;

import java.util.List;

import com.demo.framework.msg.BaseObject;
import com.demo.framework.msg.BaseResponse;

public class ListQueryResponse<T extends BaseObject> extends BaseResponse {
    private List<T> rows;

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }
}
