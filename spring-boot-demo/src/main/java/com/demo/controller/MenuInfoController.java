package com.demo.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.controller.msg.BaseRequest;
import com.demo.controller.msg.BaseResponse;
import com.demo.controller.msg.QueryUserMenuResponse;
import com.demo.framework.annotation.TradeService;
import com.demo.mapper.MenuInfo;
import com.demo.service.MenuInfoService;


/**
 * @author Benny
 *
 */
@Service
@TradeService(version="1.0.0")
public class MenuInfoController {
//  private  Logger logger = LoggerFactory.getLogger(this.getClass());
  
  @Autowired
  private MenuInfoService menuInfoService;
    /**
     * c查询用户菜单信息
     * @param request
     * @return
     * @throws Exception
     */
  @TradeService(value="query_user_menu",isLog = false,isAuth = false)
  public BaseResponse queryUserMenu(BaseRequest request) throws Exception {
      QueryUserMenuResponse  response = new QueryUserMenuResponse();
      List<MenuInfo> list = menuInfoService.queryUserMenuInfo(request.getUserId());
      response.setMenuInfoList(list);
      return response;
  }
}
