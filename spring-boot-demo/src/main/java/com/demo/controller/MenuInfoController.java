package com.demo.controller;

import com.demo.common.annotation.TradeService;
import com.demo.controller.msg.*;
import com.demo.mapper.MenuInfo;
import com.demo.service.MenuInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * @author Benny
 *
 */
@Service
@TradeService(version="1.0.0")
public class MenuInfoController {
  private static Logger logger = LoggerFactory.getLogger(MenuInfoController.class);
  
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
