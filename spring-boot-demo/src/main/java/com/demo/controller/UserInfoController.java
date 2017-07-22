package com.demo.controller;

import com.demo.common.annotation.TradeService;
import com.demo.common.enums.ErrorCodeEnum;
import com.demo.common.enums.UserInfoStatus;
import com.demo.common.exception.CommException;
import com.demo.common.security.DESede;
import com.demo.common.util.SpringContextUtil;
import com.demo.controller.msg.BaseResponse;
import com.demo.controller.msg.UserLoginRequest;
import com.demo.controller.msg.UserLoginResponse;
import com.demo.mapper.UserInfo;
import com.demo.service.UserInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;


/**
 * @author Benny
 *
 */
@Service
@TradeService(version="1.0.0")
public class UserInfoController {
  private static Logger logger = LoggerFactory.getLogger(UserInfoController.class);  
  
  @Autowired
  private UserInfoService userInfoService;
  
  
  @TradeService(value="user_login")
  public BaseResponse login(UserLoginRequest request) throws Exception {
	  logger .info("用户[{}]登录",request.getLoginName());
	  UserInfo query= new UserInfo();
	  query.setLoginName(request.getLoginName());
	  List<UserInfo> list = userInfoService.getUserInfoList(query);
	  UserLoginResponse response=null;
	  if(list.size()>0){
		  UserInfo u=list.get(0);
		  // 检查密码
		  if(!u.getPassword().equals(DESede.encrypt(request.getPwd()))){
			  throw new CommException(ErrorCodeEnum.USER_LOGIN_ERROR);
		  }
		  //检查状态
		  if(!UserInfoStatus.NORMAL.getTradeStatus().equals(u.getStatus())){
			  throw new CommException(ErrorCodeEnum.USER_STATUS_ERROR);
		  }

		  response= new UserLoginResponse();
		  response.setSeqNo(request.getSeqNo());
		  response.setUserId(u.getId());
		  response.setToken(SpringContextUtil.getThreadLocalData().request.getSession().getId());
		  response.setBranchId(u.getBranchId());
		  response.setUserName(u.getUserName());

		  //更新登录时间
		  UserInfo update= new UserInfo();
		  update.setLoginTime(new Date());
		  update.setId(u.getId());
		  userInfoService.updateUserInfo(update);
	  }else{
		  throw new CommException(ErrorCodeEnum.USER_LOGIN_ERROR);
	  }
      return response;
  }
}
