package com.demo.cache;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.Cache;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;



@Configuration
@ConditionalOnProperty(prefix = "ehcache.redis", value = "enable", matchIfMissing = true)
public class EhcacheRedisAutoConfiguration {
//	@Value("${ehcache.config.file-name}")
    private String ehcacheConfigFileName="ehcache.xml";
	
	@Autowired
	private Environment env;
	
	@Autowired  
    private ApplicationContext applicationContext;
	
	@Bean
    RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        return container;
    }

	/** 
     *  ehcache 主要的管理器 
     * @param bean 
     * @return 
     */   
    @Bean  
    public SimpleCacheManager simpleCacheManager(
    		EhCacheManagerFactoryBean bean,
    		RedisConnectionFactory connectionFactory,
    		RedisTemplate<Object,Object> redisTemplate,
    		RedisMessageListenerContainer container){
    	
    	RedisSerializer<String> stringSerializer = new StringRedisSerializer();
    	 // 使用Jackson2JsonRedisSerialize 替换默认序列化
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);

        // 设置value的序列化规则和 key的序列化规则
       
		redisTemplate.setKeySerializer(stringSerializer);
		redisTemplate.setHashKeySerializer(stringSerializer);
		redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
		redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);
		
       String[] cacheNames = bean.getObject().getCacheNames();
       SimpleCacheManager simpleCacheManager=new SimpleCacheManager();
       List<Cache> caches =new  ArrayList<Cache>();
       for(String name:cacheNames){
    	   registerBean("ehcacheRedis_"+name,EhcacheRedis.class);
    	   registerBean("redisExceptionHandle_"+name,RedisExceptionHandle.class);
    	   EhcacheRedis ehcacheRedis=(EhcacheRedis)applicationContext.getBean("ehcacheRedis_"+name);
           ehcacheRedis.setName(name);
           ehcacheRedis.setRedisTemplate(redisTemplate);
           ehcacheRedis.setEhcache(bean.getObject().getCache(name));
           caches.add(ehcacheRedis);
           
           
           RedisExceptionHandle redisExceptionHandle=(RedisExceptionHandle)applicationContext.getBean("redisExceptionHandle_"+name);
           
           ehcacheRedis.setRedisExceptionHandle(redisExceptionHandle);
           redisExceptionHandle.setRedisEhcache(ehcacheRedis);
           redisExceptionHandle.setRedisTemplate(redisTemplate);
           
           container.addMessageListener(ehcacheRedis, new ChannelTopic(ehcacheRedis.getChannel()));
       }
       simpleCacheManager.setCaches(caches);
       return simpleCacheManager;  
    }  
     
    /* 
     * 据shared与否的设置, 
     * Spring分别通过CacheManager.create() 
     * 或new CacheManager()方式来创建一个ehcache基地. 
     * 也说是说通过这个来设置cache的基地是这里的Spring独用,还是跟别的(如hibernate的Ehcache共享) 
     */
     @Bean  
     public EhCacheManagerFactoryBean ehCacheManagerFactoryBean(){
       EhCacheManagerFactoryBean cacheManagerFactoryBean = new EhCacheManagerFactoryBean (); 
       String ehcacheConfigFile = env.getProperty("ehcache.config.file-name");
       if(ehcacheConfigFile==null) {
    	   ehcacheConfigFile=ehcacheConfigFileName;
       }
       cacheManagerFactoryBean.setConfigLocation (new ClassPathResource(ehcacheConfigFile));  
       cacheManagerFactoryBean.setShared(true);
       return cacheManagerFactoryBean;  
     }
     
     private void registerBean(String name,Class<?> clazz) {
    	 ConfigurableApplicationContext context = (ConfigurableApplicationContext)applicationContext;  
         DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory)context.getBeanFactory();  
    	 BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.rootBeanDefinition(clazz);
          /** 
           * 注册到spring容器中 
           */  
         beanFactory.registerBeanDefinition(name, beanDefinitionBuilder.getBeanDefinition());  
     }
	
}
