package com.demo.gateway.controller;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.demo.framework.Constant;
import com.demo.framework.dubbo.DubboClient;
import com.demo.framework.enums.TradeStatusEnum;
import com.demo.framework.exception.CommException;
import com.demo.framework.exception.FrameworkErrorCode;
import com.demo.framework.msg.ApiServiceInfo;
import com.demo.framework.msg.BaseResponse;
import com.demo.framework.msg.LoginUserInfo;
import com.demo.framework.util.CommUtil;
import com.demo.framework.util.ThreadCacheData;
import com.demo.framework.util.ThreadCacheUtil;
import com.demo.gateway.GatewayErrorCode;

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
	private DubboClient dubboClient;
	
	@Value("${gateway.session.enable}")
	private boolean sessionEnable;
	
	@RequestMapping(value = "/trade", method = { RequestMethod.POST,RequestMethod.GET })
	@ResponseBody
	public String gateway(HttpServletRequest request,HttpServletResponse response) throws CommException {
		long beginTime = System.currentTimeMillis();
        long endTime =beginTime;
        String seqNo=null;
        String service=null;
        String version=null;
        String parameter=null;
        String result=null;
        
        // 1.获取请求参数
        parameter=getRequestParameter(request);
        seqNo=CommUtil.getJsonValue(parameter,"seqNo");
        service=CommUtil.getJsonValue(parameter,"service");
        version=CommUtil.getJsonValue(parameter,"version");
        
        ThreadCacheData threadCacheData= new ThreadCacheData();
		threadCacheData.traceId=seqNo;
		if(sessionEnable) {
			threadCacheData.sessionId=request.getSession().getId();
		}
		
		ThreadCacheUtil.setThreadLocalData(threadCacheData);
		
		try {
			//Thread.currentThread().setName(service+":"+version+":"+seqNo);
	        logger.info("1.接收到请求数据[{}]",parameter);
	        accessControl(service,version,parameter,request);
	        result=dubboClient.send(parameter, seqNo, service, version);
		} catch ( CommException e) {
			BaseResponse rsp = makeErrorResponse(e.getErrCode(),e.getErrMsg());
			result=JSON.toJSONString(rsp);
		}catch ( Throwable e) {
			logger.error("",e);
			BaseResponse rsp = makeErrorResponse(FrameworkErrorCode.SYSTEM_FAIL.getCode(),FrameworkErrorCode.SYSTEM_FAIL.getMsg());
			result=JSON.toJSONString(rsp);
		}finally {
			endTime = System.currentTimeMillis();
			logger.info("3.响应数据[{}毫秒][{}]",endTime-beginTime,result);
			ThreadCacheUtil.cleanThreadCacheData();
		}
        
        
       
        return result;
	}
	
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
	private void accessControl(String serviceName, String version, String parameter,HttpServletRequest request) throws CommException {
		logger.info("2.进行API访问控制");
		if (serviceName == null) {
			throw new CommException(GatewayErrorCode.GATEWAY_ILLEGAL_ACCESS);
		}
		ApiServiceInfo apiServiceInfo = dubboClient.getApiServiceInfo(serviceName);
		if (apiServiceInfo == null) {
			throw new CommException(GatewayErrorCode.GATEWAY_ERROR_SERVICE, serviceName);
		}
		if (apiServiceInfo.isPublic()) { // 公共开放接口 不进行访问控制
			return;
		}
		
		if (sessionEnable) {
			// 私有接口需要登录才能访问
			String userId = CommUtil.getJsonValue(parameter, "userId");
			String token = CommUtil.getJsonValue(parameter, "token");
			LoginUserInfo loginUser = (LoginUserInfo) request.getSession().getAttribute(Constant.LOGIN_USER);
			if (loginUser == null) {
				throw new CommException(GatewayErrorCode.GATEWAY_SESSION_TIMEOUT);
			}
			if (!loginUser.getToken().equals(token) || !loginUser.getUserId().toString().equals(userId)) {
				throw new CommException(GatewayErrorCode.GATEWAY_ILLEGAL_ACCESS);
			}
			if (!apiServiceInfo.isAuth()) { // 不进行基于角色的权限访问控制(RBAC)的访问控制
				return;
			}
			if (loginUser.getApiServiceInfoMap().get(serviceName + ":" + version) == null) {
				throw new CommException(GatewayErrorCode.GATEWAY_NO_ACCESS);
			}
		}else {
			//TODO :待实现
		}
      
	}
	
	private  BaseResponse  makeErrorResponse(String code,String msg){
		BaseResponse baseResponse= new BaseResponse();
		baseResponse.setRspCode(code);
		baseResponse.setRspMsg(msg);
		baseResponse.setTradeStatus(TradeStatusEnum.FAIL.getTradeStatus());
		return baseResponse;
	}
}
