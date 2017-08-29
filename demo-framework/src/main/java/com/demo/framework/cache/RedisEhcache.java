package com.demo.framework.cache;

import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.support.SimpleValueWrapper;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.StringRedisTemplate;

import net.sf.ehcache.Element;


/**
 * 两级缓存，一级:ehcache,二级为redis
 * @author 
 *
 */
public class RedisEhcache implements Cache,MessageListener  {
	
    private static final Logger logger = LoggerFactory.getLogger(RedisEhcache.class); 

    private String name;

    private net.sf.ehcache.Ehcache ehcache;

    private StringRedisTemplate stringRedisTemplate;
    
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
		Element value = ehcache.get(key);
		Object result =null;
		if(value!=null){
			result=value.getObjectValue();
		}else{
			logger.info("Get value from Redis");
			EhcacheRedisCallback callback= new EhcacheRedisCallback();
			callback.setKey(key.toString());
			result=stringRedisTemplate.execute(callback);
			if(result!=null){
				logger.info("修复本地缺失的数据key={},Value={}",key,result);
				Element element= new Element(key, result);
				ehcache.put(element);
			}
		}
		logger.info("RedisEhcache.get,key={},Value={}",key,result);
		return result != null ? new SimpleValueWrapper(result) : null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T get(Object key, Class<T> type) {
		logger.info("RedisEhcache.get,key={},type={}",key,type.getName());
		ValueWrapper result = get(key);
		if(result==null){
			return null;
		}
		
		return (T)result.get();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T get(Object key, Callable<T> valueLoader) {
		logger.info("RedisEhcache.get,key={},valueLoader={}",key,valueLoader.getClass().getName());
		ValueWrapper result = get(key);
		Object val=null;
		if(result==null){
			try {
				val= valueLoader.call();
			} catch (Exception e) {
				logger.error("",e);
			}
			if(val!=null){
				logger.info("修复本地以及Redis缺失的数据 key={},Value={}",key,result);
				put(key,val);
			}
		}else{
			val=result.get();
		}
		logger.info("RedisEhcache.get,key={},valueLoader={},Value={}",key,valueLoader.getClass().getName(),val);
		return (T)val;
		
	}

	@Override
	public void put(Object key, Object value) {
		logger.info("RedisEhcache.put,key={},value={}",key,value);
		EhcacheRedisCallback callback= new EhcacheRedisCallback();
		callback.setKey(key.toString());
		callback.setValue(value);
		stringRedisTemplate.execute(callback);
		stringRedisTemplate.convertAndSend(getChannel(), key);
		Element element = new Element(key, value);
		ehcache.put(element);
	}

	@Override
	public ValueWrapper putIfAbsent(Object key, Object value) {
		logger.info("RedisEhcache.putIfAbsent,key={},value={}",key,value);
		EhcacheRedisCallback callback= new EhcacheRedisCallback();
		callback.setKey(key.toString());
		callback.setValue(value);
		stringRedisTemplate.execute(callback);
		stringRedisTemplate.convertAndSend(getChannel(), key);
		Element element = new Element(key, value);
		ehcache.putIfAbsent(element);
		return new SimpleValueWrapper(value); 
	}

	@Override
	public void evict(Object key) {
		logger.info("RedisEhcache.evict,key={}",key);
		ehcache.remove(key);
		stringRedisTemplate.delete(key.toString());

	}

	@Override
	public void clear() {
		logger.info("RedisEhcache.clear");
		ehcache.removeAll();

	}
	
	public void setName(String name) {
		this.name = name;
	}

	public void setEhcache(net.sf.ehcache.Ehcache ehcache) {
		this.ehcache = ehcache;
	}
	

	public void setStringRedisTemplate(StringRedisTemplate stringRedisTemplate) {
		this.stringRedisTemplate = stringRedisTemplate;
	}
	
	public String getChannel(){
		return "REDIS_EHCACHE:"+this.name;
	}
	
	@Override
	public void onMessage(Message message, byte[] pattern) {
		String key= new String(message.getBody());
		logger.info("RedisEhcache.receiveMessage,key={}",key);
		EhcacheRedisCallback callback= new EhcacheRedisCallback();
		callback.setKey(key.toString());
		Object result = stringRedisTemplate.execute(callback);
		if(result==null){
			logger.info("删除,key={}",key);
			ehcache.remove(key);
		}else{
			logger.info("更新数据,key={},value={}",key,result);
			Element element = new Element(key, result);
			ehcache.put(element);
		}
		
	}
}
