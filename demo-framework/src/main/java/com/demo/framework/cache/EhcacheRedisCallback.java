package com.demo.framework.cache;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;

public class EhcacheRedisCallback implements RedisCallback<Object> {
	private  Logger logger = LoggerFactory.getLogger(this.getClass());
	private String key;
	 
	private Object value;
	
	@Override
	public Object doInRedis(RedisConnection connection) throws DataAccessException {
		if(value==null){
			return get(connection);
		}else{
			put(connection);
			return null;
		}
	}
	
	
	private void put(RedisConnection connection){
        try {
        	byte[] keyByte = key.getBytes();  
            byte[] valueByte = toByteArray(value); 
        	connection.set(keyByte, valueByte);
		} catch (Exception e) {
			logger.error("",e);
		}
         
	}
	
	private Object get(RedisConnection connection){
		
		try {
			byte[] keyByte = key.toString().getBytes();  
	        byte[] valueByte = connection.get(keyByte);  
	        if (valueByte == null) {  
	            return null;  
	        }  
	        return toObject(valueByte);
		} catch (Exception e) {
			logger.error("",e);
		}
		return null;
	}
	
	/**
	 * 
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	private byte[] toByteArray(Object obj){  
        byte[] bytes = null;  
        try {
        	ByteArrayOutputStream bos = new ByteArrayOutputStream();  
            ObjectOutputStream oos = new ObjectOutputStream(bos);  
            oos.writeObject(obj);  
            oos.flush();  
            bytes = bos.toByteArray();  
            oos.close();
            bos.close();
        } catch (Exception e) {
			logger.error("", e);
		} 
        return bytes;  
    }
	
	
	 /** 
     * 描述 : byte[]转Object
     * @param bytes 
     * @return 
	 * @throws IOException 
     */  
    private Object toObject(byte[] bytes){  
        Object obj = null; 
        try {  
        	ByteArrayInputStream bis = new ByteArrayInputStream(bytes);  
            ObjectInputStream ois = new ObjectInputStream(bis);  
            obj = ois.readObject();
            ois.close();  
            bis.close();  
        } catch (Exception e) {
			logger.error("", e);
		}
        return obj;  
    }

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}
}
