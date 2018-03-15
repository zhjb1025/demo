/**
 * 
 */
package com.demo.gateway;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.session.data.redis.RedisOperationsSessionRepository;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Administrator
 *
 */
@Component
public class RedisTemplateInit {
	private  Logger logger = LoggerFactory.getLogger(this.getClass());  
	@Autowired
	@Qualifier("sessionRedisTemplate")
	private  RedisTemplate<Object, Object> sessionRedisTemplate;
	
	@Autowired
	private RedisOperationsSessionRepository redisOperationsSessionRepository;
	
	@PostConstruct
	public void init(){
		logger.info("初始化sessionRedisTemplate的序列化类" );
		 // 使用Jackson2JsonRedisSerialize 替换默认序列化
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<Object>(Object.class);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);
        RedisSerializer<String> stringSerializer = new StringRedisSerializer();
        // 设置value的序列化规则和 key的序列化规则
        sessionRedisTemplate.setKeySerializer(stringSerializer);
        sessionRedisTemplate.setDefaultSerializer(jackson2JsonRedisSerializer);
        sessionRedisTemplate.setHashKeySerializer(stringSerializer);
        sessionRedisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        sessionRedisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);
        
        redisOperationsSessionRepository.setDefaultSerializer(jackson2JsonRedisSerializer);
	}
}
