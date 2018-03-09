package com.demo.controller;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.demo.common.DemoErrorCode;
import com.demo.common.enums.UserInfoStatusEnum;
import com.demo.controller.msg.AddUserRequest;
import com.demo.controller.msg.ModifyPasswordRequest;
import com.demo.controller.msg.PageQueryResponse;
import com.demo.controller.msg.ResetPasswordRequest;
import com.demo.controller.msg.UpdateUserRequest;
import com.demo.controller.msg.UpdateUserStatusRequest;
import com.demo.controller.msg.UserLoginRequest;
import com.demo.controller.msg.UserLoginResponse;
import com.demo.controller.msg.UserPageQueryResult;
import com.demo.controller.msg.UserQueryRequest;
import com.demo.framework.Constant;
import com.demo.framework.annotation.TradeService;
import com.demo.framework.exception.CommException;
import com.demo.framework.msg.ApiServiceInfo;
import com.demo.framework.msg.BaseRequest;
import com.demo.framework.msg.BaseResponse;
import com.demo.framework.msg.LoginUserInfo;
import com.demo.framework.security.DESede;
import com.demo.framework.security.RSAUtil;
import com.demo.framework.session.redis.RedisSessionService;
import com.demo.mapper.Metadata;
import com.demo.mapper.UserInfo;
import com.demo.service.MetadataService;
import com.demo.service.UserInfoService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;


/**
 * @author Benny
 *
 */
@Service
@TradeService(version="1.0.0")
public class UserInfoController {
  private  Logger logger = LoggerFactory.getLogger(this.getClass());
  
  @Autowired
  private UserInfoService userInfoService;

  @Autowired
  private MetadataService metadataService;
  
  @Autowired
  private RedisSessionService redisSessionService;
  
  @Value("${rsa.key.path}")
  private String rsaKeyPath;
  
  @TradeService(value="user_login",isPublic = true)
  public BaseResponse login(UserLoginRequest request) throws Exception {
	  logger .info("用户[{}]登录",request.getLoginName());
	  UserInfo query= new UserInfo();
	  query.setLoginName(request.getLoginName());
	  List<UserInfo> list = userInfoService.getUserInfoList(query);
	  UserLoginResponse response=null;
	  if(list.size()>0){
		  UserInfo u=list.get(0);
		  // 检查密码
          String password=RSAUtil.decryptJSRsa(request.getPwd(),rsaKeyPath);
		  if(!u.getPassword().equals(DESede.encrypt(password))){
			  throw new CommException(DemoErrorCode.USER_LOGIN_ERROR);
		  }
		  //检查状态
		  if(!UserInfoStatusEnum.NORMAL.getTradeStatus().equals(u.getStatus())){
			  throw new CommException(DemoErrorCode.USER_STATUS_ERROR);
		  }

		  response= new UserLoginResponse();
		  response.setSeqNo(request.getSeqNo());
		  response.setUserId(u.getId());
		  response.setToken(UUID.randomUUID().toString());
		  response.setBranchId(u.getBranchId());
		  response.setUserName(u.getUserName());

          LoginUserInfo loginUserInfo= new LoginUserInfo();
          loginUserInfo.setUserId(response.getUserId());
          loginUserInfo.setToken(response.getToken());
          loginUserInfo.setBranchId(u.getBranchId());
          loginUserInfo.setLoginName(u.getLoginName());
          loginUserInfo.setUserName(u.getUserName());
          List<ApiServiceInfo> apiList = userInfoService.queryUserApiService(response.getUserId());
          for(ApiServiceInfo api:apiList){
              loginUserInfo.getApiServiceInfoMap().put(api.getService()+":"+api.getVersion(),api);
          }
          redisSessionService.setSessionAttribute(Constant.LOGIN_USER,loginUserInfo);

          logger .info("用户[{}]登录信息[{}]",request.getLoginName(), JSON.toJSONString(loginUserInfo));
		  //更新登录时间
		  UserInfo update= new UserInfo();
		  update.setLoginTime(new Date());
		  update.setId(u.getId());
		  userInfoService.updateUserInfo(update);
	  }else{
		  throw new CommException(DemoErrorCode.USER_LOGIN_ERROR);
	  }
      return response;
  }


    @TradeService(value="page_query_user",isLog = false)
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
        LoginUserInfo loginUser=(LoginUserInfo) redisSessionService.getSessionAttribute(Constant.LOGIN_USER);
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
        LoginUserInfo loginUser=(LoginUserInfo) redisSessionService.getSessionAttribute(Constant.LOGIN_USER);
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
        UserInfo query= new UserInfo();
        query.setLoginName(request.getLoginName());
        List<UserInfo> list = userInfoService.getUserInfoList(query);
        if(list.size()>0){
            throw new CommException(DemoErrorCode.USER_LOGIN_NAME_EXITS);
        }
        userInfoService.addUser(request);
        return response;
    }

    @TradeService(value="modify_password",isAuth = false)
    public BaseResponse modifyPassword(ModifyPasswordRequest request) throws Exception {
        BaseResponse response= new BaseResponse();
        UserInfo user = userInfoService.getUserInfoById(request.getUserId());
        if(user==null){
            throw new CommException(DemoErrorCode.USER_NOT_EXITS);
        }
        // 检查原密码
        String password=RSAUtil.decryptJSRsa(request.getPassword(),rsaKeyPath);
        String newPassword=RSAUtil.decryptJSRsa(request.getNewPassword(),rsaKeyPath);
        if(!user.getPassword().equals(DESede.encrypt(password))){
            throw new CommException(DemoErrorCode.USER_PASSWORD_ERROR);
        }

        UserInfo userInfo= new UserInfo();
        userInfo.setId(request.getUserId());
        userInfo.setPassword(DESede.encrypt(newPassword));
        userInfoService.updateUserInfo(userInfo);
        return response;
    }


    @TradeService(value="user_logout",isPublic = true)
    public BaseResponse userLogout(BaseRequest request) throws Exception {
        BaseResponse response= new BaseResponse();
        redisSessionService.removeSessionAttribute(Constant.LOGIN_USER);
        return response;
    }
}
