package com.demo.eoms.controller;

import static com.demo.eoms.common.Constant.API_PREFIX;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.eoms.controller.msg.QueryUserMenuResponse;
import com.demo.eoms.mapper.MenuInfo;
import com.demo.eoms.service.MenuInfoService;
import com.demo.framework.annotation.TradeService;
import com.demo.framework.msg.BaseRequest;
import com.demo.framework.msg.BaseResponse;


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
  @TradeService(value=API_PREFIX+"query_user_menu",isLog = false,isAuth = false)
  public BaseResponse queryUserMenu(BaseRequest request) throws Exception {
      QueryUserMenuResponse  response = new QueryUserMenuResponse();
      List<MenuInfo> list = menuInfoService.queryUserMenuInfo(request.getUserId());
      response.setMenuInfoList(list);
      return response;
  }
}
