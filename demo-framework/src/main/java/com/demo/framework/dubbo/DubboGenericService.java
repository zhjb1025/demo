package com.demo.framework.dubbo;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.dubbo.config.ServiceConfig;
import com.alibaba.dubbo.rpc.RpcContext;
import com.alibaba.dubbo.rpc.service.GenericException;
import com.alibaba.dubbo.rpc.service.GenericService;
import com.alibaba.fastjson.JSON;
import com.demo.framework.enums.TradeStatusEnum;
import com.demo.framework.exception.CommException;
import com.demo.framework.exception.FrameworkErrorCode;
import com.demo.framework.msg.BaseResponse;
import com.demo.framework.util.SpringContextUtil;
import com.demo.framework.util.ThreadCacheData;
import com.demo.framework.util.ThreadCacheUtil;
import com.demo.framework.validate.ValidatorService;

public class DubboGenericService implements GenericService {
	private  Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private DubboServiceConfig dubboServiceConfig;
	
	private ValidatorService validatorService;
	
	private DubboProperties dubboProperties;
	
	private ServiceConfig<GenericService> service =null;
	
	
	
	public void setDubboServiceConfig(DubboServiceConfig dubboServiceConfig) {
		this.dubboServiceConfig = dubboServiceConfig;
	}


	public void setValidatorService(ValidatorService validatorService) {
		this.validatorService = validatorService;
	}


	public void setDubboProperties(DubboProperties dubboProperties) {
		this.dubboProperties = dubboProperties;
	}


	@Override
	public Object $invoke(String serviceName, String[] parameterTypes, Object[] args) throws GenericException {
		BaseResponse baseResponse=null;
		long beginTime = System.currentTimeMillis();
        long endTime =beginTime;
        String response=null;
        
		String version=(String)args[0];
		String seqNo=(String)args[1];
		String request=(String)args[2];
		
		ThreadCacheData threadCacheData= new ThreadCacheData();
		threadCacheData.seqNo=seqNo;
		threadCacheData.sessionId= RpcContext.getContext().getAttachment("sessionId");
		ThreadCacheUtil.setThreadLocalData(threadCacheData);
		Thread.currentThread().setName(serviceName+":"+version+":"+seqNo);
		logger.info("1.接收到请求数据[{}]",request);
		try {
	      // 1.获取请求参数
		  baseResponse=handle(request,seqNo,serviceName,version);
		} catch (CommException e) {
			baseResponse= makeErrorResponse(e.getErrCode(),e.getErrMsg());
		}finally{
			baseResponse.setSeqNo(seqNo);
			response=JSON.toJSONString(baseResponse);
			endTime=System.currentTimeMillis();
			logger.info("5.响应数据[{}毫秒][{}]",endTime-beginTime,response);
			ThreadCacheUtil.cleanThreadCacheData();
		}
	    
		return response;
	}
	
	
	private BaseResponse handle(String request,String seqNo,String serviceName,String version) throws CommException {
		try {
			return handle_(request,seqNo,serviceName,version );
		} catch (InvocationTargetException e) {
			if(e.getTargetException() instanceof CommException){
				CommException ex=(CommException)e.getTargetException();
				throw ex;
			}else{
				logger.info("系统异常",e.getTargetException());
				throw new CommException(FrameworkErrorCode.SYSTEM_FAIL);
			}
		}catch(CommException e){
	        throw e;
	    }catch(Throwable e){
			logger.info("系统异常",e);
			throw new CommException(FrameworkErrorCode.SYSTEM_FAIL);
		}
	}
	
	
  private BaseResponse handle_(String parameter,String seqNo,String serviceName,String version) throws CommException, InvocationTargetException ,Exception{
	    logger.info("2.进行服务路由");
		Object serviceBean = dubboServiceConfig.getServiceBean(serviceName, version);
		Method serviceMethod = dubboServiceConfig.getServiceMethod(serviceName, version);
		Class<?> serviceParameter = dubboServiceConfig.getServiceParameter(serviceName, version);
		
		if(serviceBean==null || serviceMethod==null || serviceParameter==null){
			throw new CommException(FrameworkErrorCode.SYSTEM_ERROR,"服务名或者版本号错误");
		}
		logger.info("2.1 服务[{}] 方法 [{}]",serviceBean.getClass().getName(),serviceMethod.getName());
		//validate(service,version,parameter);

		Object arg=null;
		try {
			arg = JSON.parseObject(parameter, serviceParameter);
		} catch (Exception e) {
			logger.info("JSON 转换报错",e);
			throw new CommException(FrameworkErrorCode.SYSTEM_ERROR,"JSON 转换报错");
		}
		logger.info("3.进行参数校验[{}]",serviceParameter.getName());
		validatorService.validate(arg);

		logger.info("4.调用业务方法进行处理",serviceParameter.getName());
        BaseResponse baseResponse = (BaseResponse) serviceMethod.invoke(SpringContextUtil.getBean(serviceBean.getClass()), new Object[]{arg});
		return baseResponse;
	}
	private  BaseResponse  makeErrorResponse(String code,String msg){
		BaseResponse baseResponse= new BaseResponse();
		baseResponse.setRspCode(code);
		baseResponse.setRspMsg(msg);
		baseResponse.setTradeStatus(TradeStatusEnum.FAIL.getTradeStatus());
		return baseResponse;
	}
	
    public void init() throws Exception{
		logger.info("注册Dubbo服务.........");
		service = new ServiceConfig<GenericService>();
		service.setApplication(dubboProperties.getApplication());
		service.setRegistry(dubboProperties.getRegistry()); // 多个注册中心可以用setRegistries()
		service.setProtocol(dubboProperties.getProtocol()); // 多个协议可以用setProtocols()
		// 弱类型接口名 
		service.setInterface("com.demo.DubboService");  
		service.setVersion("1.0.0"); 
		// 指向一个通用服务实现 
		service.setRef(this); 
		// 暴露及注册服务 
		service.export();
		logger.info("注册Dubbo服务成功");
	}

}
