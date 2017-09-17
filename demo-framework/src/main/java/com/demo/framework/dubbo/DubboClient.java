package com.demo.framework.dubbo;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.rpc.RpcContext;
import com.alibaba.dubbo.rpc.service.GenericService;
import com.alibaba.fastjson.JSON;
import com.demo.framework.enums.TradeStatusEnum;
import com.demo.framework.exception.FrameworkErrorCode;
import com.demo.framework.msg.BaseRequest;
import com.demo.framework.msg.BaseResponse;
import com.demo.framework.util.ThreadCacheUtil;

public class DubboClient {
	private  Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private ReferenceConfig<GenericService> reference;
	
	private DubboProperties dubboProperties;
	
	
	
	public void setDubboProperties(DubboProperties dubboProperties) {
		this.dubboProperties = dubboProperties;
	}

	@PostConstruct
    public void init() throws Exception{
		logger.info("连接后台服务.........");
		reference = new ReferenceConfig<GenericService>(); // 此实例很重，封装了与注册中心的连接以及与提供者的连接，请自行缓存，否则可能造成内存和连接泄漏

		reference.setApplication(dubboProperties.getApplication());
		reference.setRegistry(dubboProperties.getRegistry()); // 多个注册中心可以用setRegistries()
		// 弱类型接口名 
		reference.setInterface("com.demo.DubboService");  
		reference.setVersion("1.0.0"); 
		reference.setGeneric(true);
		reference.setRetries(0);
		reference.setTimeout(30000);
		try {
			reference.get();
		} catch (Exception e) {
			logger.error("",e);
		}
		
		logger.info("连接后台服务成功");
	}
	
	public String send(String request,String seqNo,String serviceName,String version) {
		RpcContext.getContext().setAttachment("sessionId", ThreadCacheUtil.getThreadLocalData().sessionId);
		GenericService dubboService = reference.get();
		String result=null;
		
		try {
			result=(String)dubboService.$invoke(serviceName, 
					 new String[] { "java.lang.String", "java.lang.String","java.lang.String"},
					 new String[] {version,seqNo,request});
		} catch ( Exception e) {
			logger.error("",e);
			BaseResponse rsp = makeErrorResponse(FrameworkErrorCode.SYSTEM_FAIL.getCode(),FrameworkErrorCode.SYSTEM_FAIL.getMsg());
			result=JSON.toJSONString(rsp);
		}
		
		return result;
	}
	
	public <T> T send(BaseRequest request ,Class<T> clazz) {
		String result=send(JSON.toJSONString(request),request.getSeqNo(),request.getService(),request.getVersion());
		return JSON.parseObject(result, clazz);
	}
	
	private  BaseResponse  makeErrorResponse(String code,String msg){
		BaseResponse baseResponse= new BaseResponse();
		baseResponse.setRspCode(code);
		baseResponse.setRspMsg(msg);
		baseResponse.setTradeStatus(TradeStatusEnum.FAIL.getTradeStatus());
		return baseResponse;
	}
}
