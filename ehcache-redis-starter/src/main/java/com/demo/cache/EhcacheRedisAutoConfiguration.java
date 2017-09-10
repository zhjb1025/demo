package com.demo.cache;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.Cache;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;


@Configuration
@ConditionalOnProperty(prefix = "ehcache.redis", value = "enable", matchIfMissing = true)
public class EhcacheRedisAutoConfiguration {
//	@Value("${ehcache.config.file-name}")
    private String ehcacheConfigFileName="ehcache.xml";
	
	@Autowired
	private Environment env;
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
    	   EhcacheRedis ehcacheRedis = new EhcacheRedis();
           ehcacheRedis.setName(name);
           ehcacheRedis.setStringRedisTemplate(stringRedisTemplate);
           ehcacheRedis.setEhcache(bean.getObject().getCache(name));
           caches.add(ehcacheRedis);
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
	
}
