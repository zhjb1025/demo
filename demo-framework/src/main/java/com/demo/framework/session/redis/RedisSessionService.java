package com.demo.framework.session.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;

import com.demo.framework.util.ThreadCacheUtil;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class RedisSessionService {
	
	private RedisTemplate<Object,Object> redisTemplate;
	
	@Autowired
	public void setRedisTemplate(RedisTemplate<Object, Object> redisTemplate) {
		RedisSerializer<String> stringSerializer = new StringRedisSerializer();
        // 使用Jackson2JsonRedisSerialize 替换默认序列化
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<Object>(Object.class);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);

        // 设置value的序列化规则和 key的序列化规则
		redisTemplate.setKeySerializer(stringSerializer);
		redisTemplate.setHashKeySerializer(stringSerializer);
		redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
		redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);
		this.redisTemplate = redisTemplate;
	}

	public Object getSessionAttribute(String key) {
		String sessionKey="spring:session:sessions:"+ThreadCacheUtil.getThreadLocalData().sessionId;
		Object value = redisTemplate.opsForHash().get(sessionKey, "sessionAttr:"+key);
		if(value==null) {
			return null;
		}
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
