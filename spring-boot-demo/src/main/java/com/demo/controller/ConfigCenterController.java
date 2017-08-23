package com.demo.controller;

import org.springframework.stereotype.Service;

import com.demo.config.client.ConfigCenterClient;
import com.demo.controller.msg.BaseRequest;
import com.demo.controller.msg.BaseResponse;
import com.demo.controller.msg.QueryAllGroupResponse;
import com.demo.controller.msg.QueryGroupConfigRequest;
import com.demo.controller.msg.QueryGroupConfigResponse;
import com.demo.framework.annotation.TradeService;


/**
 * @author Benny
 *
 */
@Service
@TradeService(version="1.0.0")
public class ConfigCenterController {
	
  @TradeService(value="query_all_group",isLog = false)
  public BaseResponse queryAllGroup(BaseRequest request) throws Exception {
	  QueryAllGroupResponse response = new QueryAllGroupResponse();
	  response.setGroupList(ConfigCenterClient.queryAllGroup());
      return response;
  }
  
  @TradeService(value="query_group_config",isLog = false)
  public BaseResponse queryGroupConfig(QueryGroupConfigRequest request) throws Exception {
	  QueryGroupConfigResponse response = new QueryGroupConfigResponse();
	  response.setConfigList(ConfigCenterClient.queryByGroup(request.getGroup()));
      return response;
  }
}
