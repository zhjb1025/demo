package com.demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.demo.config.client.ConfigCenterClient;
import com.demo.controller.msg.BaseRequest;
import com.demo.controller.msg.BaseResponse;
import com.demo.controller.msg.QueryAllGroupResponse;
import com.demo.controller.msg.QueryGroupConfigRequest;
import com.demo.controller.msg.QueryGroupConfigResponse;
import com.demo.controller.msg.UpdateConfigRequest;
import com.demo.framework.annotation.TradeService;
import com.demo.framework.enums.ErrorCodeEnum;
import com.demo.framework.exception.CommException;


/**
 * @author Benny
 *
 */
@Service
@TradeService(version="1.0.0")
public class ConfigCenterController {
  private  Logger logger = LoggerFactory.getLogger(this.getClass());
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
  
  @TradeService(value="update_config")
  public BaseResponse updateConfig(UpdateConfigRequest request) throws Exception {
	  BaseResponse response= new BaseResponse();
	  try {
		  String ret = ConfigCenterClient.updateConfig(request.getGroup(), request.getKey(), request.getValue(), request.getRemark());
		  response.setRspMsg(ret);
	  } catch (Exception e) {
		  logger.error("",e);
		  throw new CommException(ErrorCodeEnum.CONFIG_ACCESS_ERROR);
	  }
      return response;
  }
}
