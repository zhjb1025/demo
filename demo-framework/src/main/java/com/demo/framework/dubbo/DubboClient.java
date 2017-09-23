package com.demo.framework.dubbo;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.rpc.RpcContext;
import com.alibaba.dubbo.rpc.service.GenericService;
import com.alibaba.fastjson.JSON;
import com.demo.framework.exception.CommException;
import com.demo.framework.exception.FrameworkErrorCode;
import com.demo.framework.msg.ApiServiceInfo;
import com.demo.framework.msg.BaseRequest;
import com.demo.framework.util.ThreadCacheUtil;
import com.demo.zookeeper.ZookeeperClient;

public class DubboClient {
	private  Logger logger = LoggerFactory.getLogger(this.getClass());
	private DubboProperties dubboProperties;
	
	private ZookeeperClient client;
	
	/**
	 * key=Interface
	 */
	private Map<String,ReferenceConfig<GenericService>> referenceMap = new ConcurrentHashMap<String,ReferenceConfig<GenericService>>();
	
	/**
	 * key=serviceName
	 */
	private Map<String,ReferenceConfig<GenericService>> serviceMap = new ConcurrentHashMap<String,ReferenceConfig<GenericService>>();
	
	/**
	 * key=serviceName
	 */
	private Map<String,ApiServiceInfo> apiMap = new ConcurrentHashMap<String,ApiServiceInfo>();
	
	
	public void setClient(ZookeeperClient client) {
		this.client = client;
	}

	public void setDubboProperties(DubboProperties dubboProperties) {
		this.dubboProperties = dubboProperties;
	}

	@PostConstruct
    public void init() throws Exception{
		logger.info("连接后台Dubbo服务.........");
		String [] interfaces=dubboProperties.getRefInterfaces().split(",");
		for(String Interface:interfaces) {
			if("*".equals(Interface)) {
				logger.info("网关服务,从zookeeper 获取接口列表");
				continue;
			}
			if(referenceMap.get(Interface)!=null) {
				continue;
			}
			ReferenceConfig<GenericService> reference = new ReferenceConfig<GenericService>(); // 此实例很重，封装了与注册中心的连接以及与提供者的连接，请自行缓存，否则可能造成内存和连接泄漏
			reference.setApplication(dubboProperties.getApplication());
			reference.setRegistry(dubboProperties.getRegistry());
			reference.setConsumer(dubboProperties.getConsumer());
			// 弱类型接口名 
			reference.setInterface(Interface);
			reference.setGeneric(true);
			try {
				logger.info("开始连接[{}] 服务........",Interface);
				reference.get();
				referenceMap.put(Interface, reference);
				getAipInfo(Interface,reference);
				logger.info("连接[{}] 服务成功",Interface);
			} catch (Exception e) {
				logger.info("连接[{}] 服务失败",Interface);
				logger.error("",e);
			}
			
		}
		logger.info("连接后台Dubbo服务成功");
	}
	private void getAipInfo(String Interface,ReferenceConfig<GenericService> reference) throws Exception {
		String path="/root/api/"+Interface;
		byte[] data = client.getCuratorFramework().getData().forPath(path);
		String api= new String(data,"UTF-8");
		logger.info("获取接口[{}]API 信息:{}",Interface,api);
		List<ApiServiceInfo> apiList = JSON.parseArray(api, ApiServiceInfo.class);
		
		for(ApiServiceInfo apiServiceInfo:apiList) {
			serviceMap.put(apiServiceInfo.getService(), reference);
			apiMap.put(apiServiceInfo.getService(), apiServiceInfo);
		}
	}
	
	/**
	 * 调用远程服务
	 * @param request
	 * @param seqNo
	 * @param serviceName
	 * @param version
	 * @return
	 * @throws CommException
	 * @throws Exception
	 */
	public String send(String request,String seqNo,String serviceName,String version) throws CommException ,Exception{
		
		if(ThreadCacheUtil.getThreadLocalData()!=null && ThreadCacheUtil.getThreadLocalData().sessionId!=null) {
			RpcContext.getContext().setAttachment("sessionId", ThreadCacheUtil.getThreadLocalData().sessionId);
		}
		ReferenceConfig<GenericService> reference = serviceMap.get(serviceName);
		if(reference==null) {
			throw new CommException(FrameworkErrorCode.SYSTEM_ERROR,"服务名错误");
		}
		GenericService dubboService = reference.get();
		String result =(String)dubboService.$invoke(serviceName, 
				 new String[] { "java.lang.String", "java.lang.String","java.lang.String"},
				 new String[] {version,seqNo,request});
		
		return result;
	}
	
	/**
	 * 调用远程服务
	 * @param request
	 * @param clazz
	 * @return
	 * @throws Exception 
	 * @throws CommException 
	 */
	public <T> T send(BaseRequest request ,Class<T> clazz) throws CommException, Exception {
		String result=send(JSON.toJSONString(request),request.getSeqNo(),request.getService(),request.getVersion());
		return JSON.parseObject(result, clazz);
	}	
	public ApiServiceInfo getApiServiceInfo(String serviceName) {
		return apiMap.get(serviceName);
	}
	
}
