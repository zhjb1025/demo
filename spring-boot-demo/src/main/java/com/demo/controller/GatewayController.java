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

import com.demo.common.enums.ErrorCodeEnum;
import com.demo.common.util.ThreadCacheData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.demo.common.enums.TradeStatusEnum;
import com.demo.common.exception.CommException;
import com.demo.common.util.SpringContextUtil;
import com.demo.common.validate.ValidatorService;
import com.demo.controller.msg.BaseRequest;
import com.demo.controller.msg.BaseResponse;
import com.demo.service.RouteService;

/**
 * 网的Controller 处理所有http请求
 * 
 * @author Benny.zhang
 *
 */
@RestController
public class GatewayController {

	private static Logger logger = LoggerFactory.getLogger(GatewayController.class);
	
	@Autowired
	private RouteService routeService;
	
	@Autowired
	private ValidatorService validatorService;

	@RequestMapping(value = "/gateway/trade", method = { RequestMethod.POST,RequestMethod.GET })
	@ResponseBody
	public BaseResponse gateway(HttpServletRequest request,
			HttpServletResponse response) throws CommException {


		long beginTime = System.currentTimeMillis();
		BaseResponse baseResponse=null;
		ThreadCacheData data= new ThreadCacheData();
		data.request=request;
		data.response=response;
		SpringContextUtil.setThreadLocalData(data);

		try {
			baseResponse=handle(request);
		} catch (CommException e) {
			baseResponse= makeErrorResponse(e.getErrCode(),e.getErrMsg());
		}finally{
			long endTime = System.currentTimeMillis(); 
			logger.info("5.响应数据[{}毫秒][{}]",endTime-beginTime,baseResponse);
			SpringContextUtil.cleanThreadCacheData();
		}
		
		return baseResponse;
	}
	private String getRequestPrameter(HttpServletRequest request){
		String prameter="";
		logger.info(request.getContentType());
		if(request.getMethod().equals(RequestMethod.POST.name())){
			try {
				prameter=getPostData(request);
			} catch (Exception e) {
				logger.info("获取POST数据报错",e);
			}
		}else{
			Map<String, String> map = getPrameter(request);
			prameter=JSON.toJSONString(map);
		}
		return prameter;
	}
	private BaseResponse handle(HttpServletRequest request) throws CommException {
		try {
			return handle_(request);
		} catch (InvocationTargetException e) {
			if(e.getTargetException() instanceof CommException){
				CommException ex=(CommException)e.getTargetException();
				throw ex;
			}else{
				logger.info("系统异常",e.getTargetException());
				throw new CommException(ErrorCodeEnum.SYSTEM_FAIL);
			}
		}catch(Throwable e){
			logger.info("系统异常",e);
			throw new CommException(ErrorCodeEnum.SYSTEM_FAIL);
		}

	}
	private BaseResponse handle_(HttpServletRequest request) throws CommException, InvocationTargetException ,Exception{
		String threadName=request.getRemoteAddr()+":"+request.getRemotePort();
		Thread.currentThread().setName(threadName);
		logger.info("0.接收到请求 method={}, ip={},port={}",request.getMethod(), request.getRemoteAddr(),request.getRemotePort());
		// 1.获取请求参数
		String prameter=getRequestPrameter(request);
		logger.info("1.接收到请求数据[{}]",prameter);
		// 2.服务路由
		BaseRequest head=null;
		BaseResponse baseResponse=null;
		try {
			head=JSON.parseObject(prameter,BaseRequest.class);
		} catch (Exception e) {
			logger.info("JSON 转换报错",e);
			throw new CommException(ErrorCodeEnum.SYSTEM_ERROR,"JSON 转换报错");
		}
		SpringContextUtil.getThreadLocalData().seqNo=head.getSeqNo();
		Thread.currentThread().setName(threadName+":"+head.getService()+":"+head.getSeqNo());
		
		Object serviceBean = routeService.getServiceBean(head.getService(), head.getVersion());
		Method serviceMethod = routeService.getServiceMethod(head.getService(), head.getVersion());
		Class<?> serviceParameter = routeService.getServiceParameter(head.getService(), head.getVersion());
		
		if(serviceBean==null || serviceMethod==null || serviceParameter==null){
			throw new CommException(ErrorCodeEnum.SYSTEM_ERROR,"服务名或者版本号错误");
		}
		// 进行访问权限控制
		logger.info("2.进行访问权限控制");
		validate(head.getService(),head);

		Object arg=null;
		try {
			arg = JSON.parseObject(prameter, serviceParameter);
		} catch (Exception e) {
			logger.info("JSON 转换报错",e);
			throw new CommException(ErrorCodeEnum.SYSTEM_ERROR,"JSON 转换报错");
		}
		logger.info("3.进行参数校验[{}]",serviceParameter.getName());
		validatorService.validate(arg,getValidatorGroup(head.getService()));

		logger.info("4.调用业务方法进行处理",serviceParameter.getName());
		baseResponse=(BaseResponse)serviceMethod.invoke(SpringContextUtil.getBean(serviceBean.getClass()), new Object []{arg});
		return baseResponse;
	}
	
	private  Map<String, String> getPrameter(HttpServletRequest request) {
		Map<String, String> parameterMap = new HashMap<String, String>();
		@SuppressWarnings("rawtypes")
		Iterator iter = request.getParameterMap().keySet().iterator();
		while (iter.hasNext()) {
			String key = iter.next().toString();
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
	private  String getPostData(HttpServletRequest request)throws Exception {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		InputStream is = request.getInputStream();

		int read = 1;
		while (read > 0) {
			byte[] bb = new byte[1024];
			read = is.read(bb);
			if (read > 0) {
				baos.write(bb, 0, read);
			}
		}
		byte[] bb = baos.toByteArray();
		baos.close();
		return new String(bb, "UTF-8");
	}


	private void validate(String serviceName,BaseRequest head) throws CommException {
		if ("user_login".equals(serviceName)){
			return;
		}

		String token=SpringContextUtil.getThreadLocalData().request.getSession().getId();
		if(!token.equals(head.getToken())){
			throw new CommException(ErrorCodeEnum.SYSTEM_ILLEGAL_ACCESS);
		}
	}

	private Class<?> getValidatorGroup(String serviceName){
		if ("user_login".equals(serviceName)){
			return  ValidatorService.NotAuthGroup.class;
		}else {
			return ValidatorService.AuthGroup.class;
		}
	}

}
