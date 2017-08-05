package com.demo.controller;

import com.alibaba.fastjson.JSON;
import com.demo.common.Constant;
import com.demo.common.annotation.TradeService;
import com.demo.common.enums.ErrorCodeEnum;
import com.demo.common.enums.UserInfoStatusEnum;
import com.demo.common.exception.CommException;
import com.demo.common.security.DESede;
import com.demo.common.util.SpringContextUtil;
import com.demo.controller.msg.*;
import com.demo.mapper.ApiServiceInfo;
import com.demo.mapper.Metadata;
import com.demo.mapper.UserInfo;
import com.demo.service.MetadataService;
import com.demo.service.UserInfoService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
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

  @Autowired
  private MetadataService metadataService;
  
  
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
		  if(!UserInfoStatusEnum.NORMAL.getTradeStatus().equals(u.getStatus())){
			  throw new CommException(ErrorCodeEnum.USER_STATUS_ERROR);
		  }

		  response= new UserLoginResponse();
		  response.setSeqNo(request.getSeqNo());
		  response.setUserId(u.getId());
		  response.setToken(SpringContextUtil.getThreadLocalData().request.getSession().getId());
		  response.setBranchId(u.getBranchId());
		  response.setUserName(u.getUserName());

          LoginUserInfo loginUserInfo= new LoginUserInfo();
          loginUserInfo.setUserId(response.getUserId());
          loginUserInfo.setToken(response.getToken());
          List<ApiServiceInfo> apiList = userInfoService.queryUserApiService(response.getUserId());
          for(ApiServiceInfo api:apiList){
              loginUserInfo.getApiServiceInfoMap().put(api.getService()+":"+api.getVersion(),api);
          }
		  SpringContextUtil.getThreadLocalData().request.getSession().setAttribute(Constant.LOGIN_USER,loginUserInfo);

          logger .info("用户[{}]登录信息[{}]",request.getLoginName(), JSON.toJSONString(loginUserInfo));
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


    @TradeService(value="page_query_user")
    public BaseResponse pageQueryUserInfo(UserQueryRequest request) throws Exception {
        PageQueryResponse<UserPageQueryResult> response= new PageQueryResponse<UserPageQueryResult>();
        PageHelper.startPage(request.getPageNumber(), request.getPageSize());
        List<UserPageQueryResult> list = userInfoService.pageQueryUserInfo(request.getUserName(), request.getLoginName());
        PageInfo<UserPageQueryResult> page=new PageInfo<UserPageQueryResult>(list);
        response.setRows(list);
        response.setTotal(page.getTotal());
        return response;
    }

    @TradeService(value="update_user_status")
    public BaseResponse updateUserStatus(UpdateUserStatusRequest request) throws Exception {
        BaseResponse response= new BaseResponse();
        UserInfo userInfo= new UserInfo();
        userInfo.setId(request.getId());
        userInfo.setStatus(request.getStatus());
        userInfo.setUpdateTime(new Date());
        LoginUserInfo loginUser=(LoginUserInfo) SpringContextUtil.getThreadLocalData().
                request.getSession().getAttribute(Constant.LOGIN_USER);
        userInfo.setUpdateUserId(loginUser.getUserId());
        userInfoService.updateUserInfo(userInfo);
        return response;
    }

    @TradeService(value="reset_password")
    public BaseResponse resetPassword(ResetPasswordRequest request) throws Exception {
        BaseResponse response= new BaseResponse();
        UserInfo userInfo= new UserInfo();
        userInfo.setId(request.getId());
        Metadata defaultPassword = metadataService.queryMetaData("default_password");
        userInfo.setPassword(DESede.encrypt(defaultPassword.getMetaCode()));
        userInfo.setUpdateTime(new Date());
        LoginUserInfo loginUser=(LoginUserInfo) SpringContextUtil.getThreadLocalData().
                request.getSession().getAttribute(Constant.LOGIN_USER);
        userInfo.setUpdateUserId(loginUser.getUserId());
        userInfoService.updateUserInfo(userInfo);
        return response;
    }

    @TradeService(value="update_user")
    public BaseResponse updateUser(UpdateUserRequest request) throws Exception {
        BaseResponse response= new BaseResponse();
        userInfoService.updateUser(request);
        return response;
    }

    @TradeService(value="add_user")
    public BaseResponse addUser(AddUserRequest request) throws Exception {
        BaseResponse response= new BaseResponse();

        userInfoService.addUser(request);
        return response;
    }
}
