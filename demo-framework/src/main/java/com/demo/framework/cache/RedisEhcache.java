package com.demo.framework.cache;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.support.SimpleValueWrapper;

import net.sf.ehcache.Element;


/**
 * 两级缓存，一级:ehcache,二级为redis
 * @author 
 *
 */
public class RedisEhcache implements Cache {
	
    private static final Logger logger = LoggerFactory.getLogger(RedisEhcache.class); 

    private String name;

    private net.sf.ehcache.Ehcache ehcache;

//    private RedisTemplate<String, Object> redisTemplate;

	@Override
	public String getName() {
		logger.info("RedisEhcache.getName={}",name);
		return this.name;
	}

	@Override
	public Object getNativeCache() {
		return this;
	}

	@Override
	public ValueWrapper get(Object key) {
		logger.info("RedisEhcache.get,key={}",key);
		Element value = ehcache.get(key);
		return value != null ? new SimpleValueWrapper(value.getObjectValue()) : null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T get(Object key, Class<T> type) {
		logger.info("RedisEhcache.get,key={},type={}",key,type.getName());
		Element value = ehcache.get(key);
		if(value==null){
			return null;
		}
		
		return (T)value.getObjectValue();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T get(Object key, Callable<T> valueLoader) {
		logger.info("RedisEhcache.get,key={},valueLoader={}",key,valueLoader.getClass().getName());
		Element value = ehcache.get(key);
		if(value==null){
			return null;
		}
		return (T)value.getObjectValue();
	}

	@Override
	public void put(Object key, Object value) {
		logger.info("RedisEhcache.put,key={},value={}",key,value);
		Element element = new Element(key, value);
		ehcache.putIfAbsent(element);

	}

	@Override
	public ValueWrapper putIfAbsent(Object key, Object value) {
		logger.info("RedisEhcache.putIfAbsent,key={},value={}",key,value);
		Element element = new Element(key, value);
		ehcache.putIfAbsent(element);
		return new SimpleValueWrapper(value); 
	}

	@Override
	public void evict(Object key) {
		logger.info("RedisEhcache.evict,key={}",key);
		ehcache.remove(key);

	}

	@Override
	public void clear() {
		logger.info("RedisEhcache.clear");
		ehcache.removeAll();

	}
	
	/**
	 * 
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	private byte[] toByteArray(Object obj) throws Exception {  
        byte[] bytes = null;  
        ByteArrayOutputStream bos = new ByteArrayOutputStream();  
        ObjectOutputStream oos = new ObjectOutputStream(bos);  
        try {  
            oos.writeObject(obj);  
            oos.flush();  
            bytes = bos.toByteArray();  
            
        } finally {
        	if(oos !=null){
        		oos.close();  
        	}
        	if(oos !=null){
        		bos.close();  
        	}
		}  
        return bytes;  
    }
	
	
	 /** 
     * 描述 : byte[]转Object
     * @param bytes 
     * @return 
	 * @throws IOException 
     */  
    private Object toObject(byte[] bytes) throws Exception {  
        Object obj = null; 
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);  
        ObjectInputStream ois = new ObjectInputStream(bis);  
        try {  
            obj = ois.readObject();  
        } finally {
        	if(ois !=null){
        		ois.close();  
        	}
        	if(bis !=null){
        		bis.close();  
        	}
		} 
        return obj;  
    }

	public void setName(String name) {
		this.name = name;
	}

	public void setEhcache(net.sf.ehcache.Ehcache ehcache) {
		this.ehcache = ehcache;
	} 
    
    

}
