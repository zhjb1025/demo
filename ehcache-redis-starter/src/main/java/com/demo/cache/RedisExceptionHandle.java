package com.demo.cache;

import java.util.concurrent.LinkedBlockingQueue;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * 
 * @author Administrator
 *
 */
public class RedisExceptionHandle extends Thread {
	private  Logger logger = LoggerFactory.getLogger(this.getClass());
	private LinkedBlockingQueue<Object[]> queue= new LinkedBlockingQueue<Object[]>();
	private StringRedisTemplate stringRedisTemplate;
	private EhcacheRedis ehcacheRedis;
	
	public RedisExceptionHandle(EhcacheRedis ehcacheRedis){
		this.ehcacheRedis=ehcacheRedis;
	}
	public void setRedisEhcache(EhcacheRedis ehcacheRedis) {
		this.ehcacheRedis = ehcacheRedis;
	}
	
	public void setStringRedisTemplate(StringRedisTemplate stringRedisTemplate) {
		this.stringRedisTemplate = stringRedisTemplate;
	}

	public void add(Object key,Object value,String action){
		Object[] obj=new Object[]{key,value,action};
		queue.add(obj);
	}
	
	@Override
	public void run() {
		Object[] obj=null;
		Object key=null;
		Object value=null;
		Object action=null;
		try {
			while(true){
				obj=queue.take();
				testRedis();
				logger.error("redis成功,开始处理异常数据");
				do{
					key=obj[0];
					value=obj[1];
					action=obj[2];
					try {
						logger.info("处理异常缓存数据,key={},value={},action={}",key,value,action);
						if("put".equals(action)){
							ehcacheRedis.put_(key, value);
						}else{
							ehcacheRedis.evict(key);
						}
						
					} catch (Exception e) {
						testRedis();
						logger.error("处理异常缓存数据出错",e);
					}
					if(!queue.isEmpty()){
						obj=queue.take();
					}else{
						obj=null;
					}
				}while(obj!=null);
				
				
			}
		} catch (Exception e) {
			logger.error("退出缓存异常处理线程");
		}
		
		
	}
	/**
	 * 测试redis是否正常
	 */
	private void testRedis(){
		logger.error("开始测试redis服务器是否可用.......");
		while(true){
			try {
				stringRedisTemplate.opsForValue().get("test");
				break;
			} catch (Exception e) {
				logger.error("连接redis失败 5000毫秒后重试.......");
				try {
					sleep(5000);
				} catch (InterruptedException e1) {
					logger.error("", e1);
				}
			}
		}
	 }
	
	@PostConstruct
	public void init() {
		logger.info("启动缓存异常处理线程");
		this.start();
	}
	@PreDestroy
    public void finish(){
		logger.info("关闭缓存异常处理线程");
		this.interrupt();
	}
	
}
