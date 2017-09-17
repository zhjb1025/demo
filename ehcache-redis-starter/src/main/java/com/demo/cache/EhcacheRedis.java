package com.demo.cache;

import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.support.SimpleValueWrapper;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;

import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;


/**
 * 两级缓存，一级:ehcache,二级为redis
 * @author 
 *
 */
public class EhcacheRedis implements Cache,MessageListener  {
	
	private  Logger logger = LoggerFactory.getLogger(this.getClass());

    private String name;

    private Ehcache ehcache;

    private RedisTemplate<Object,Object> redisTemplate;
    
    private RedisExceptionHandle redisExceptionHandle;
    
	@Override
	public String getName() {
		logger.info("RedisEhcache.getName={}",name);
		return this.name;
	}

	@Override
	public Object getNativeCache() {
		logger.info("RedisEhcache.getNativeCache");
		return this;
	}

	@Override
	public ValueWrapper get(Object key) {
		logger.info("RedisEhcache.get,key={}",key);
		Object result=null;
		try {
			result=get_(key);
		} catch (Exception e) {
			logger.error("",e);
		}
		return result==null? null:new SimpleValueWrapper(result);
	}
	
	public Object get_(Object key){
		
		Element value = ehcache.get(key);
		Object result =null;
		if(value!=null){
			result=value.getObjectValue();
		}else{
			logger.info("Get value from Redis");
			result=redisTemplate.opsForValue().get(key);
			if(result!=null){
				logger.info("修复本地缺失的数据key={},Value={}",key,result);
				Element element= new Element(key, result);
				ehcache.put(element);
			}
		}
		logger.info("RedisEhcache.get,key={},Value={}",key,result);
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T get(Object key, Class<T> type) {
		logger.info("RedisEhcache.get,key={},type={}",key,type.getName());
		Object result=null;
		try {
			result=get_(key);
		} catch (Exception e) {
			logger.error("",e);
		}
		
		return (T)result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T get(Object key, Callable<T> valueLoader) {
		logger.info("RedisEhcache.get,key={},valueLoader={}",key,valueLoader.getClass().getName());
		Object result=null;
		try {
			result=get_(key);
			if(result==null){
				Object val= valueLoader.call();
				if(val!=null){
					result=val;
					logger.info("修复本地以及Redis缺失的数据 key={},Value={}",key,result);
					put(key,val);
				}
			}
		} catch (Exception e) {
			logger.error("",e);
		}
		
		logger.info("RedisEhcache.get,key={},valueLoader={},Value={}",key,valueLoader.getClass().getName(),result);
		return (T)result;
		
	}

	@Override
	public void put(Object key, Object value) {
		logger.info("RedisEhcache.put,key={},value={}",key,value);
		try {
			if(value==null) {
				return;
			}
			put_(key,value);
		} catch (Exception e) {
			redisExceptionHandle.add(key, value,"put");
			logger.error("put redis 出错",e);
		}
	}
	
	public void put_(Object key, Object value){
		redisTemplate.opsForValue().set(key,value);
		redisTemplate.convertAndSend(getChannel(), key+":put");
		Element element = new Element(key, value);
		ehcache.put(element);
	}
	@Override
	public ValueWrapper putIfAbsent(Object key, Object value) {
		logger.info("RedisEhcache.putIfAbsent,key={},value={}",key,value);
		try {
			if(value==null) {
				return new SimpleValueWrapper(value); 
			}
			put_(key,value);
		} catch (Exception e) {
			redisExceptionHandle.add(key, value,"put");
			logger.error("put redis 出错",e);
		}
		return new SimpleValueWrapper(value); 
	}

	@Override
	public void evict(Object key) {
		logger.info("RedisEhcache.evict,key={}",key);
		try {
			ehcache.remove(key);
			redisTemplate.delete(key);
			redisTemplate.convertAndSend(getChannel(), key+":remove");
		} catch (Exception e) {
			redisExceptionHandle.add(key, null,"remove");
			logger.error("删除 key 出错",e);
		}
		

	}

	@Override
	public void clear() {
		logger.info("RedisEhcache.clear");
		try {
			ehcache.removeAll();
		} catch (Exception e) {
			logger.error("",e);
		}
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public void setEhcache(Ehcache ehcache) {
		this.ehcache = ehcache;
	}
	public String getChannel(){
		return "REDIS_EHCACHE:"+this.name;
	}
	
	@Override
	public void onMessage(Message message, byte[] pattern) {
		try {
			String key= new String(message.getBody());
			logger.info("RedisEhcache.receiveMessage,key={}",key);
			String [] keys=key.split(":");
			if(keys[1].equals("remove")){
				logger.info("删除,key={}",keys[0]);
				ehcache.remove(keys[0]);
				
			}else if(keys[1].equals("put")){
				if(ehcache.get(keys[0])==null) {
					return;
				}
				Object result = redisTemplate.opsForValue().get(keys[0]);
				logger.info("更新数据,key={},value={}",keys[0],result);
				Element element = new Element(keys[0], result);
				ehcache.put(element);
			}
		} catch (Exception e) {
			logger.error("",e);
		}
		
	}

	public void setRedisExceptionHandle(RedisExceptionHandle redisExceptionHandle) {
		this.redisExceptionHandle = redisExceptionHandle;
	}

	public void setRedisTemplate(RedisTemplate<Object, Object> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}
	
}
