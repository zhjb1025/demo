package com.demo.framework.dubbo;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.demo.framework.annotation.TradeService;
import com.demo.framework.exception.CommException;
import com.demo.framework.exception.FrameworkErrorCode;
import com.demo.framework.msg.ApiServiceInfo;
import com.demo.framework.msg.BaseRequest;
import com.demo.framework.msg.BaseResponse;
import com.demo.zookeeper.ZookeeperClient;
@Service
public class DubboServiceConfig implements ApplicationListener<ContextRefreshedEvent> {
	private  Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private ZookeeperClient client;
	
	//@Autowired
    private DubboProperties dubboProperties;
	
	/**
	 * key=serviceNane_version
	 */
	private Map<String, Object> serviceBean=  new ConcurrentHashMap<String, Object>();
	
	/**
	 * key=serviceNane_version
	 */
	private Map<String, Method> serviceMethod=  new ConcurrentHashMap<String, Method>();


    /**
     * key=serviceNane_version
     */
    private Map<String, TradeService> tradeServiceMap=  new ConcurrentHashMap<String, TradeService>();
	
	
	/**
	 * key=serviceNane_version
	 */
	private Map<String, Class<?>> serviceParameter=  new ConcurrentHashMap<String, Class<?>>();
	
	private List<ApiServiceInfo> apiInfoList= new ArrayList<ApiServiceInfo>();
	
	
	public List<ApiServiceInfo> getApiInfoList() {
		return apiInfoList;
	}

	private void addServiceBean(String service,String version,Object bean){
		String key=service+":"+version;
		if(serviceBean.get(key)==null){
			serviceBean.put(key, bean);
		}
	}
	
	private void addServiceMethod(String service,String version,Method method){
		serviceMethod.put(service+":"+version, method);
	}
	
	private void addServiceParameter(String service,String version,Class<?> clazz){
		serviceParameter.put(service+":"+version, clazz);
	}
    private void addTradeService(String service,String version,TradeService tradeService){
        tradeServiceMap.put(service+":"+version, tradeService);
    }
	
	public Class<?> getServiceParameter(String service,String version){
		return serviceParameter.get(service+":"+version);
	}
    public TradeService getTradeService(String service,String version){
        return tradeServiceMap.get(service+":"+version);
    }
	
	public Object getServiceBean(String service,String version){
		return serviceBean.get(service+":"+version); 
	}
	
	public Method getServiceMethod(String service,String version){
		return serviceMethod.get(service+":"+version); 
	}
	
	public void postProcessBeanFactory(ConfigurableListableBeanFactory factory)
			throws BeansException {
	}
	
	
	public boolean isParent(Class<?> parent, Class<?> children){
		boolean result=false;
		while(true){
			if(children==null){
				break;
			}
			if(children.equals(parent)){
				result=true;
				break;
			}
			children=children.getSuperclass();
		}
		return result;
	}


	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		ApplicationContext context = event.getApplicationContext();
		if(context.getBeanNamesForType(DubboServiceAutoConfiguration.class).length==0) {
			return;
		}
		dubboProperties=context.getBean(DubboProperties.class);
		String[] names=context.getBeanNamesForAnnotation(TradeService.class);
		for(String name:names){
			Object bean = context.getBean(name);
			String beanVersion=bean.getClass().getAnnotation(TradeService.class).version();
			Method[] methods = bean.getClass().getDeclaredMethods();
			for(Method m: methods){
				
				TradeService tradeService=m.getAnnotation(TradeService.class);
				if(tradeService!=null){
					
					Class<?>[] clazzParameter = m.getParameterTypes();
					Class<?> returnType = m.getReturnType();
					String errorMsg=null;
					
					String version=tradeService.version();
					
					
					try {
						if(!isParent(BaseResponse.class,returnType)){
							errorMsg=String.format("服务:%s:方法%s　返回结果不合规", tradeService.value(),bean.getClass().getName()+"."+m.getName()+"()");
							throw new CommException(FrameworkErrorCode.SYSTEM_ERROR,errorMsg);
						}
						
					} catch (Exception e) {
						logger.error(bean.getClass().getName()+"配置不正确", e);
					}
					
					try {
						
						if(StringUtils.isBlank(version) && StringUtils.isBlank(beanVersion) ){
							throw new CommException(FrameworkErrorCode.SYSTEM_ERROR,"版本号不能为空");
						}
						if(StringUtils.isBlank(version)){
							version=beanVersion;
						}
						
						if(clazzParameter !=null && clazzParameter.length==1 && isParent(BaseRequest.class,clazzParameter[0])){
							logger.info("服务映射关系:{}:{}={}",tradeService.value(),version,bean.getClass().getName()+"."+m.getName()+"("+clazzParameter[0].getName()+")");
							ApiServiceInfo api= new ApiServiceInfo();
							api.setService(tradeService.value());
							api.setAuth(tradeService.isAuth());
							api.setLog(tradeService.isLog());
							api.setPublic(tradeService.isPublic());
							api.setVersion(version);
							apiInfoList.add(api);
							
							addServiceBean(tradeService.value(),version,bean);
							addServiceMethod(tradeService.value(),version,m);
							addServiceParameter(tradeService.value(),version,clazzParameter[0]);
                            addTradeService(tradeService.value(),version,tradeService);

						}else{
							errorMsg=String.format("服务方法%s　参数不合规",bean.getClass().getName()+"."+m.getName()+"()");
							throw new CommException(FrameworkErrorCode.SYSTEM_ERROR,errorMsg);
						}
					} catch (Exception e) {
						logger.error(bean.getClass().getName()+"配置不正确", e);
					}
				}
			}
		}
		
		//把服务配置信息发布到zookeeper
		try {
			
			client.lock(dubboProperties.getServiceInterface(), 10, TimeUnit.SECONDS);
			String path="/root/api/"+dubboProperties.getServiceInterface();
			Stat stat = client.getCuratorFramework().checkExists().forPath(path);
			String aip=JSON.toJSONString(apiInfoList);
			
	    	if(stat==null) {
	    		logger.info("API服务配置信息发布到zookeeper:{}",aip);
	    		client.getCuratorFramework().create().creatingParentContainersIfNeeded().withMode(CreateMode.PERSISTENT).forPath(path,aip.getBytes());
	    	}else if(!compare(path)) {
	    		logger.info("API服务配置信息发布到zookeeper:{}",aip);
	    		client.getCuratorFramework().setData().forPath(path, aip.getBytes());
	    	}
		} catch (Exception e) {
			logger.error("", e);
		}
		client.unlock(dubboProperties.getServiceInterface());
	}
	
	/**
	 * 比较本地与zookeeper api信息
	 * @param path
	 * @return true  表示信息一致
	 * @throws Exception
	 */
	private boolean  compare(String path) throws Exception {
		byte[] data = client.getCuratorFramework().getData().forPath(path);
		String api= new String(data,"UTF-8");
		List<ApiServiceInfo> apiList = JSON.parseArray(api, ApiServiceInfo.class);
		if(apiInfoList.size()!=apiList.size()) {
			return false;
		}
		Map<String,ApiServiceInfo> map= new HashMap<String,ApiServiceInfo>();
		for(ApiServiceInfo a:apiInfoList) {
			map.put(a.getService()+":"+a.getVersion(), a);
		}
		for(ApiServiceInfo a:apiList) {
			if(!a.equals(map.get(a.getService()+":"+a.getVersion()))){
				return false;
			}
		}
		logger.info("本地API配置信息与zookeeper服务上的信息一致");
		return true;
	}
	
}
