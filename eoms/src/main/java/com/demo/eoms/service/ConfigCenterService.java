package com.demo.eoms.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.demo.eoms.controller.msg.ConfigInfoQueryRequest;
import com.demo.eoms.controller.msg.ConfigInfoQueryResult;
import com.demo.eoms.mapper.ConfigInfo;
import com.demo.eoms.mapper.ConfigInfoMapper;
import com.demo.eoms.mapper.SystemInfo;
import com.demo.eoms.mapper.SystemInfoMapper;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service

public class ConfigCenterService {
	private  Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private SystemInfoMapper systemInfoMapper;
	
	@Autowired
	private ConfigInfoMapper configInfoMapper;
	
	private RedisTemplate<Object,Object> redisTemplate;;
	
	public List<SystemInfo> querySystemInfo(SystemInfo record){
        return systemInfoMapper.selectByColumn(record);
	}
	
	@Transactional(rollbackFor=Exception.class)
	public void addSystemInfo(SystemInfo record) throws  Exception{
        systemInfoMapper.insert(record);
	}
	
	@Transactional(rollbackFor=Exception.class)
	public void updateSystemInfo(SystemInfo record) throws  Exception{
        systemInfoMapper.updateByPrimaryKey(record);
	}
	
	
	public List<ConfigInfoQueryResult> queryConifg(ConfigInfoQueryRequest request){
        return configInfoMapper.selectByColumn(request);
	}
	
	@Transactional(rollbackFor=Exception.class)
	public void addConfigInfo(ConfigInfo record) throws  Exception{
		configInfoMapper.insert(record);
		redisTemplate.opsForValue().set(record.getSystemCode()+":"+record.getConfigCode(),record.getConfigValue());
	}
	@Transactional(rollbackFor=Exception.class)
	public void updateConfigInfo(ConfigInfo record,String oldCode) throws  Exception{
		configInfoMapper.updateByPrimaryKeySelective(record);
		if(!record.getConfigCode().equals(oldCode)){
			logger.info("编码发生变化旧编码为[{}],新编码为[{}]",oldCode,record.getConfigCode());
			//如果编码变化，删除原来的值
			redisTemplate.delete(record.getSystemCode()+":"+oldCode);
			logger.info("删除redis上旧KEY[{}]",record.getSystemCode()+":"+oldCode);
			redisTemplate.convertAndSend(record.getSystemCode(), "delete#"+oldCode);
			logger.info("发出通知[channel={},message={}]",record.getSystemCode(),"delete#"+oldCode);
		}
		
		redisTemplate.opsForValue().set(record.getSystemCode()+":"+record.getConfigCode(),record.getConfigValue());
		logger.info("更新redis[key={},value={}]",record.getSystemCode()+":"+record.getConfigCode(),record.getConfigValue());
		//发送消息通知
		redisTemplate.convertAndSend(record.getSystemCode(), "put#"+record.getConfigCode());
		logger.info("发出通知[channel={},message={}]",record.getSystemCode(),"put#"+record.getConfigCode());
		
	}
	
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
   
}
