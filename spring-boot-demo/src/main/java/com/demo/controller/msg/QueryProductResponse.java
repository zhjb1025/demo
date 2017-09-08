package com.demo.controller.msg;

import java.util.List;

import com.demo.mapper.Product;

/**
 * Created by Auser on 2017/7/15.
 */
public class QueryProductResponse extends BaseResponse {
	private List<Product> list;

	public List<Product> getList() {
		return list;
	}

	public void setList(List<Product> list) {
		this.list = list;
	}
    
}
