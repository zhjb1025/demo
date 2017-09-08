package com.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.controller.msg.BaseRequest;
import com.demo.controller.msg.BaseResponse;
import com.demo.controller.msg.QueryConfigResponse;
import com.demo.controller.msg.QueryProductRequest;
import com.demo.controller.msg.QueryProductResponse;
import com.demo.framework.annotation.TradeService;
import com.demo.mapper.Config;
import com.demo.mapper.IndexMapper;
import com.demo.mapper.Product;
import com.github.pagehelper.PageHelper;

@Service
@TradeService(version="1.0.0")
public class IndexController {
	 @Autowired
	 private IndexMapper indexMapper;
	 
	 @TradeService(value="query_config",isLog=false,isPublic=true)
	 public BaseResponse queryConfig(BaseRequest request) throws Exception {
		  QueryConfigResponse response = new QueryConfigResponse();
		  Config config = indexMapper.getConfig();
		  response.setConfig(config);
	      return response;
	 } 
	 
	 @TradeService(value="query_product",isLog=false,isPublic=true)
	 public BaseResponse queryProduct(QueryProductRequest request) throws Exception {
		  QueryProductResponse response = new QueryProductResponse();
		  PageHelper.startPage(1, request.getSize());
		  List<Product> list = indexMapper.getProduct(request.getType());
		  response.setList(list);
	      return response;
	 } 
	 
}
