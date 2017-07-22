package com.demo.service;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.demo.common.enums.ErrorCodeEnum;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;

import com.demo.common.annotation.TradeService;
import com.demo.common.exception.CommException;
import com.demo.controller.msg.BaseRequest;
import com.demo.controller.msg.BaseResponse;


@Service
public class RouteService implements ApplicationListener<ContextRefreshedEvent> {
	private static Logger logger = LoggerFactory.getLogger(RouteService.class);  
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
	private Map<String, Class<?>> serviceParameter=  new ConcurrentHashMap<String, Class<?>>();
	
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
	
	public Class<?> getServiceParameter(String service,String version){
		return serviceParameter.get(service+":"+version);
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
							throw new CommException(ErrorCodeEnum.SYSTEM_ERROR,errorMsg);
						}
						
					} catch (Exception e) {
						logger.error(bean.getClass().getName()+"配置不正确", e);
					}
					
					try {
						
						if(StringUtils.isBlank(version) && StringUtils.isBlank(beanVersion) ){
							throw new CommException(ErrorCodeEnum.SYSTEM_ERROR,"版本号不能为空");
						}
						if(StringUtils.isBlank(version)){
							version=beanVersion;
						}
						
						if(clazzParameter !=null && clazzParameter.length==1 && isParent(BaseRequest.class,clazzParameter[0])){
							logger.info("服务映射关系:{}:{}={}",tradeService.value(),version,bean.getClass().getName()+"."+m.getName()+"("+clazzParameter[0].getName()+")");
							addServiceBean(tradeService.value(),version,bean);
							addServiceMethod(tradeService.value(),version,m);
							addServiceParameter(tradeService.value(),version,clazzParameter[0]);
						}else{
							errorMsg=String.format("服务方法%s　参数不合规",bean.getClass().getName()+"."+m.getName()+"()");
							throw new CommException(ErrorCodeEnum.SYSTEM_ERROR,errorMsg);
						}
					} catch (Exception e) {
						logger.error(bean.getClass().getName()+"配置不正确", e);
					}
					
					
				}
			}
		}
		
	}
	
	
	
}
