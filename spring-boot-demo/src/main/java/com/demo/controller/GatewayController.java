package com.demo.controller;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.demo.common.Constant;
import com.demo.controller.msg.BaseResponse;
import com.demo.controller.msg.LoginUserInfo;
import com.demo.framework.annotation.TradeService;
import com.demo.framework.enums.ErrorCodeEnum;
import com.demo.framework.enums.TradeStatusEnum;
import com.demo.framework.exception.CommException;
import com.demo.framework.util.CommUtil;
import com.demo.framework.util.SpringContextUtil;
import com.demo.framework.util.ThreadCacheData;
import com.demo.framework.validate.ValidatorService;
import com.demo.service.RouteService;

/**
 * 网的Controller 处理所有http请求
 * 
 * @author Benny.zhang
 *
 */
@RestController
public class GatewayController {

	private  Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private RouteService routeService;
	
	@Autowired
	private ValidatorService validatorService;

	@RequestMapping(value = "/gateway", method = { RequestMethod.POST,RequestMethod.GET })
	@ResponseBody
	public BaseResponse gateway(HttpServletRequest request,HttpServletResponse response) throws CommException {
		long beginTime = System.currentTimeMillis();
        long endTime =beginTime;
		BaseResponse baseResponse=null;
		ThreadCacheData data= new ThreadCacheData();
		data.request=request;
		data.response=response;
		SpringContextUtil.setThreadLocalData(data);
        String seqNo=null;

        String service=null;
        String version=null;
        String parameter=null;
		try {
            // 1.获取请求参数
            parameter=getRequestParameter(request);
            seqNo=CommUtil.getJsonValue(parameter,"seqNo");
            service=CommUtil.getJsonValue(parameter,"service");
            version=CommUtil.getJsonValue(parameter,"version");
            Thread.currentThread().setName(service+":"+version+":"+seqNo);
            logger.info("1.接收到请求数据[{}]",parameter);
			baseResponse=handle(parameter,seqNo,service,version);
		} catch (CommException e) {
			baseResponse= makeErrorResponse(e.getErrCode(),e.getErrMsg());
		}finally{
			endTime = System.currentTimeMillis();
			logger.info("5.响应数据[{}毫秒][{}]",endTime-beginTime,JSON.toJSONString(baseResponse));
			SpringContextUtil.cleanThreadCacheData();
		}
        baseResponse.setSeqNo(seqNo);
       
        return baseResponse;
	}
//
    /**
     * 获取JSON格式的请求参数
     * @param request
     * @return
     */
	private String getRequestParameter(HttpServletRequest request){
		String parameter="";
		if(request.getMethod().equals(RequestMethod.POST.name())){
			try {
				parameter=getPostData(request);
			} catch (Exception e) {
				logger.info("获取POST数据报错",e);
			}
		}else{
			Map<String, String> map = getParameter(request);
			parameter=JSON.toJSONString(map);
		}
		return parameter;
	}
	private BaseResponse handle(String parameter,String seqNo,String service,String version) throws CommException {
		try {
			return handle_(parameter,seqNo,service,version );
		} catch (InvocationTargetException e) {
			if(e.getTargetException() instanceof CommException){
				CommException ex=(CommException)e.getTargetException();
				throw ex;
			}else{
				logger.info("系统异常",e.getTargetException());
				throw new CommException(ErrorCodeEnum.SYSTEM_FAIL);
			}
		}catch(CommException e){
            throw e;
        }catch(Throwable e){
			logger.info("系统异常",e);
			throw new CommException(ErrorCodeEnum.SYSTEM_FAIL);
		}

	}



    private BaseResponse handle_(String parameter,String seqNo,String service,String version) throws CommException, InvocationTargetException ,Exception{
		// 2.服务路由
		Object serviceBean = routeService.getServiceBean(service, version);
		Method serviceMethod = routeService.getServiceMethod(service, version);
		Class<?> serviceParameter = routeService.getServiceParameter(service, version);
		
		if(serviceBean==null || serviceMethod==null || serviceParameter==null){
			throw new CommException(ErrorCodeEnum.SYSTEM_ERROR,"服务名或者版本号错误");
		}
		// 进行访问权限控制
		logger.info("2.进行访问权限控制");
		validate(service,version,parameter);

		Object arg=null;
		try {
			arg = JSON.parseObject(parameter, serviceParameter);
		} catch (Exception e) {
			logger.info("JSON 转换报错",e);
			throw new CommException(ErrorCodeEnum.SYSTEM_ERROR,"JSON 转换报错");
		}
		logger.info("3.进行参数校验[{}]",serviceParameter.getName());
		validatorService.validate(arg);

		logger.info("4.调用业务方法进行处理",serviceParameter.getName());
        BaseResponse baseResponse = (BaseResponse) serviceMethod.invoke(SpringContextUtil.getBean(serviceBean.getClass()), new Object[]{arg});
		return baseResponse;
	}

    /**
     * 获取GET的参数
     * @param request
     * @return
     */
	private  Map<String, String> getParameter(HttpServletRequest request) {
		Map<String, String> parameterMap = new HashMap<String, String>();

		Iterator<String> iterator = request.getParameterMap().keySet().iterator();
		while (iterator.hasNext()) {
			String key = iterator.next().toString();
			String val = request.getParameter(key);
			parameterMap.put(key, val);
		}
		return parameterMap;
	}
	private  BaseResponse  makeErrorResponse(String code,String msg){
		BaseResponse baseResponse= new BaseResponse();
		baseResponse.setRspCode(code);
		baseResponse.setRspMsg(msg);
		baseResponse.setTradeStatus(TradeStatusEnum.FAIL.getTradeStatus());
		return baseResponse;
	}

    /**
     * 获取POST的请求参数
     * @param request
     * @return
     * @throws Exception
     */
	private  String getPostData(HttpServletRequest request)throws Exception {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		InputStream is = request.getInputStream();
		int read = 1;
		while (read > 0) {
			byte[] bb = new byte[1024];
			read = is.read(bb);
			if (read > 0) {
				outputStream.write(bb, 0, read);
			}
		}
		byte[] bb = outputStream.toByteArray();
		outputStream.close();
		return new String(bb, "UTF-8");
	}

    /**
     * 访问控制
     * @param serviceName
     * @param version
     * @param parameter
     * @throws CommException
     */
	private void validate(String serviceName,String version,String parameter) throws CommException {
        TradeService tradeService = routeService.getTradeService(serviceName, version);
        if(tradeService==null){
            throw  new CommException(ErrorCodeEnum.SYSTEM_ERROR_SERVICE_VERSION,serviceName,version);
        }
        if (tradeService.isPublic()){  //公共开放接口 不进行访问控制
            return;
        }
        //私有接口需要登录才能访问
        String token=CommUtil.getJsonValue(parameter,"token");
        String userId=CommUtil.getJsonValue(parameter,"userId");
        LoginUserInfo loginUser=(LoginUserInfo)SpringContextUtil.getThreadLocalData().
                request.getSession().getAttribute(Constant.LOGIN_USER);
        if (loginUser==null){
            throw  new CommException(ErrorCodeEnum.USER_LOGIN_SESSION_TIMEOUT);
        }
		if (!loginUser.getToken().equals(token)|| !loginUser.getUserId().toString().equals(userId)){
            throw  new CommException(ErrorCodeEnum.SYSTEM_ILLEGAL_ACCESS);
        }
        if (!tradeService.isAuth()){  //不进行基于角色的权限访问控制(RBAC)的访问控制
            return;
        }
        if (loginUser.getApiServiceInfoMap().get(serviceName+":"+version)==null){
            throw  new CommException(ErrorCodeEnum.SYSTEM_NO_ACCESS);
        }
	}
}
