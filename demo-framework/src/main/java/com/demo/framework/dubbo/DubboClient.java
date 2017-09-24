package com.demo.framework.dubbo;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.rpc.RpcContext;
import com.alibaba.dubbo.rpc.service.GenericService;
import com.alibaba.fastjson.JSON;
import com.demo.framework.enums.TradeStatusEnum;
import com.demo.framework.exception.CommException;
import com.demo.framework.exception.FrameworkErrorCode;
import com.demo.framework.msg.ApiServiceInfo;
import com.demo.framework.msg.BaseRequest;
import com.demo.framework.msg.BaseResponse;
import com.demo.framework.util.CommUtil;
import com.demo.framework.util.ThreadCacheUtil;
import com.demo.zookeeper.ZookeeperClient;

public class DubboClient implements TreeCacheListener{
	private  Logger logger = LoggerFactory.getLogger(this.getClass());
	private DubboProperties dubboProperties;
	
	private ZookeeperClient client;
	
	private AccessLogService accessLogService;
	
	private  TreeCache treeCache;
	
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
	
	public void setAccessLogService(AccessLogService accessLogService) {
		this.accessLogService = accessLogService;
	}

	public void setDubboProperties(DubboProperties dubboProperties) {
		this.dubboProperties = dubboProperties;
	}

	@PostConstruct
    public void init() throws Exception{
		logger.info("连接后台Dubbo服务.........");
		String [] interfaces=new String [] {}  ;
		if("*".equals(dubboProperties.getRefInterfaces())) {
			logger.info("网关服务,从zookeeper 获取接口列表");
			treeCache = new TreeCache(client.getCuratorFramework(), "/root/api");
			treeCache.getListenable().addListener(this);
			treeCache.start();
			return;
		}else {
			interfaces=dubboProperties.getRefInterfaces().split(",");
		}
		
		for(String Interface:interfaces) {
			if(referenceMap.get(Interface)!=null) {
				continue;
			}
			getAipInfo(Interface);
			
		}
		if("*".equals(dubboProperties.getRefInterfaces())) {
			
		}
		
		logger.info("连接后台Dubbo服务成功");
	}
	private void getAipInfo(String Interface) throws Exception {
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
			
			String path="/root/api/"+Interface;
			byte[] data = client.getCuratorFramework().getData().forPath(path);
			String api= new String(data,"UTF-8");
			logger.info("获取接口[{}]API 信息:{}",Interface,api);
			List<ApiServiceInfo> apiList = JSON.parseArray(api, ApiServiceInfo.class);
			for(ApiServiceInfo apiServiceInfo:apiList) {
				serviceMap.put(apiServiceInfo.getService(), reference);
				apiMap.put(apiServiceInfo.getService(), apiServiceInfo);
			}
			
			logger.info("连接[{}] 服务成功",Interface);
		} catch (Exception e) {
			logger.info("连接[{}] 服务失败",Interface);
			logger.error("",e);
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
		String result=null;
		long beginTime = System.currentTimeMillis();
        long endTime =beginTime;
        logger.info("发送数据,请求参数 seqNo=[{}],serviceName=[{}],version=[{}],request=[{}]",seqNo,serviceName,version,request);
		try {
			if(ThreadCacheUtil.getThreadLocalData()!=null && ThreadCacheUtil.getThreadLocalData().sessionId!=null) {
				RpcContext.getContext().setAttachment("sessionId", ThreadCacheUtil.getThreadLocalData().sessionId);
			}
			ReferenceConfig<GenericService> reference = serviceMap.get(serviceName);
			if(reference==null) {
				throw new CommException(FrameworkErrorCode.SYSTEM_ERROR,"服务名错误");
			}
			GenericService dubboService = reference.get();
			result =(String)dubboService.$invoke(serviceName, 
					 new String[] { "java.lang.String", "java.lang.String","java.lang.String"},
					 new String[] {version,seqNo,request});
		} catch (Exception e) {
			BaseResponse baseResponse= new BaseResponse();
			baseResponse.setSeqNo(seqNo);
			baseResponse.setRspCode(FrameworkErrorCode.SYSTEM_FAIL.getCode());
			baseResponse.setRspMsg(FrameworkErrorCode.SYSTEM_FAIL.getMsg());
			baseResponse.setTradeStatus(TradeStatusEnum.FAIL.getTradeStatus());
			result=JSON.toJSONString(baseResponse);
			throw e;
		}finally {
			endTime = System.currentTimeMillis();
			logger.info("结果耗时[{}]毫秒 result=[{}]",endTime-beginTime,result);
			if(apiMap.get(serviceName)!=null && apiMap.get(serviceName).isLog()) {
				AccessLog accessLog= new AccessLog();
				accessLog.setDealTime(endTime-beginTime);
				accessLog.setRequest(request);
				accessLog.setResponse(result);
				accessLog.setSeqNo(seqNo);
				accessLog.setService(serviceName);
				accessLog.setStartTimestamp(System.currentTimeMillis());
				accessLog.setTradeDate(CommUtil.getDateYYYYMMDD());
				accessLog.setVersion(version);
				accessLogService.addLog(accessLog);
			}
		
			
		}
		
		
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

	@Override
	public void childEvent(CuratorFramework client, TreeCacheEvent event) throws Exception {
		String path=event.getData().getPath();
		logger.info("event:{},path:{}",event.getType().name(),path);
    	switch (event.getType()) {
    	 	case NODE_ADDED:  
    	 		handleAddEvent(event);
                break;  
            case NODE_UPDATED:  
            	handleUpdateEvent(event);
                break;  
            default:  
                break;
        }  
		
	}
	
	
	private void handleUpdateEvent(TreeCacheEvent event) {
		String path=event.getData().getPath();
		String Interface = path.substring("/root/api/".length());
		ReferenceConfig<GenericService> reference = referenceMap.get(Interface);
		try {
			if(reference==null) {
				getAipInfo(Interface);
			}else {
				byte[] data = client.getCuratorFramework().getData().forPath(path);
				String api= new String(data,"UTF-8");
				logger.info("获取接口[{}]API 信息:{}",Interface,api);
				List<ApiServiceInfo> apiList = JSON.parseArray(api, ApiServiceInfo.class);
				//TODO 暂时不处理删除的服务
				for(ApiServiceInfo apiServiceInfo:apiList) {
					if(serviceMap.get(apiServiceInfo.getService())==null) {
						serviceMap.put(apiServiceInfo.getService(), reference);
						logger.info("新增接口服务[{}]",apiServiceInfo);
					}
					if(!apiServiceInfo.equals(apiMap.get(apiServiceInfo.getService()))) {
						logger.info("修改API服务配置新接口:[{}] ,旧接口:[{}]",apiServiceInfo,apiMap.get(apiServiceInfo.getService()));
						apiMap.put(apiServiceInfo.getService(), apiServiceInfo);
					}
					
				}
			}
		} catch (Exception e) {
			logger.error("",e);
		}
	}
	private void handleAddEvent(TreeCacheEvent event) {
		String path=event.getData().getPath();
		String Interface = path.substring("/root/api/".length());
		try {
			getAipInfo(Interface);
		} catch (Exception e) {
			logger.error("",e);
		}
	}
	
}
