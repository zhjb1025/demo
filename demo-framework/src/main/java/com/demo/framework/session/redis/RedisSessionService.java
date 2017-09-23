package com.demo.framework.session.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;

import com.demo.framework.util.ThreadCacheUtil;

@Service
public class RedisSessionService {
	
	private RedisTemplate<Object,Object> redisTemplate;
	
	@Autowired
	public void setRedisTemplate(RedisTemplate<Object, Object> redisTemplate) {
		RedisSerializer<String> stringSerializer = new StringRedisSerializer();
		redisTemplate.setHashKeySerializer(stringSerializer);
		redisTemplate.setKeySerializer(stringSerializer);
		this.redisTemplate = redisTemplate;
	}

	public Object getSessionAttribute(String key) {
		String sessionKey="spring:session:sessions:"+ThreadCacheUtil.getThreadLocalData().sessionId;
		Object value = redisTemplate.opsForHash().get(sessionKey, "sessionAttr:"+key);
		if(value==null) {
			return null;
		}
//		redisTemplate.expire(sessionKey, 60*2, TimeUnit.SECONDS);
		return  value;
	}
	
	public void setSessionAttribute(String key,Object value) {
		String sessionKey="spring:session:sessions:"+ThreadCacheUtil.getThreadLocalData().sessionId;
		redisTemplate.opsForHash().put(sessionKey, "sessionAttr:"+key,value);
		//redisTemplate.opsForValue().set(sessionKey, value);
//		redisTemplate.expire(sessionKey, 60*2, TimeUnit.SECONDS);
	}
	public void removeSessionAttribute(String key) {
		String sessionKey="spring:session:sessions:"+ThreadCacheUtil.getThreadLocalData().sessionId;
//		redisTemplate.delete(sessionKey);
		redisTemplate.opsForHash().delete(sessionKey, "sessionAttr:"+key);
	}
}
