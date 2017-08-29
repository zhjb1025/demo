package com.demo.framework.cache;

import java.util.ArrayList;
import java.util.List;

import org.springframework.cache.Cache;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;


@Configuration
public class RedisEhcacheConfig {
	
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
    		StringRedisTemplate stringRedisTemplate,
    		RedisMessageListenerContainer container){
    	
       String[] cacheNames = bean.getObject().getCacheNames();
       SimpleCacheManager simpleCacheManager=new SimpleCacheManager();
       List<Cache> caches =new  ArrayList<Cache>();
       for(String name:cacheNames){
    	   RedisEhcache redisEhcache = new RedisEhcache();
           redisEhcache.setName(name);
           redisEhcache.setStringRedisTemplate(stringRedisTemplate);
           redisEhcache.setEhcache(bean.getObject().getCache(name));
           caches.add(redisEhcache);
           container.addMessageListener(redisEhcache, new ChannelTopic(redisEhcache.getChannel()));
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
       cacheManagerFactoryBean.setConfigLocation (new ClassPathResource("ehcache.xml"));  
       cacheManagerFactoryBean.setShared(true);
       return cacheManagerFactoryBean;  
     }
	
}
