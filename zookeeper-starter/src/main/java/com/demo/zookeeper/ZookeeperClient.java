package com.demo.zookeeper;

import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;

public class ZookeeperClient {
	private  Logger logger = LoggerFactory.getLogger(this.getClass());
    private CuratorFramework curatorFramework;
    private ZookeeperClientProperteis properteis;
    private String rootLockPath="/root/lock/";
    private final ConcurrentMap<Thread, InterProcessMutex> threadData = Maps.newConcurrentMap();
    
    
    public String getRootLockPath() {
		return rootLockPath;
	}

	public void setRootLockPath(String rootLockPath) {
		this.rootLockPath = rootLockPath;
	}

	@PostConstruct
    public void init(){
    	curatorFramework= CuratorFrameworkFactory.builder().connectString(properteis.getConnectString())
                .sessionTimeoutMs(properteis.getSessionTimeout())
                .connectionTimeoutMs(properteis.getConnectionTimeout())
                .retryPolicy(new ExponentialBackoffRetry(1000, Integer.MAX_VALUE))
                .defaultData(null)
                .build();
        logger.info("开始连接zookeeper[{}]..........",properteis.getConnectString());
        curatorFramework.start();
        try {
        	curatorFramework.blockUntilConnected();
        } catch (InterruptedException e) {
        	curatorFramework=null;
            logger.info("",e);
        }
        logger.info("连接zookeeper[{}]成功",properteis.getConnectString());
    }

    public CuratorFramework getCuratorFramework() {
        return curatorFramework;
    }
    
    /**
     * 获取锁，不支持同一个线程内 锁两个不同的key
     * @param key
     * @param time
     * @param unit
     * @return
     * @throws Exception
     */
    public boolean lock(String key,long time, TimeUnit unit) throws Exception{
    	Thread currentThread = Thread.currentThread();
    	InterProcessMutex lock=threadData.get(currentThread);
    	if(lock!=null){
    		try {
    			boolean ret = lock.acquire(time, unit);
    			if(!ret){
    				threadData.remove(currentThread);
    			}
    			return ret;
			} catch (Exception e) {
				threadData.remove(currentThread);
				logger.error("获取锁[{}]超时",key);
				return false;
			}
    	}
    	
    	lock = new InterProcessMutex(curatorFramework, rootLockPath+key);
    	try {
			boolean ret = lock.acquire(time, unit);
			if(ret){
				threadData.put(currentThread, lock);
			}
			return ret;
		} catch (Exception e) {
			logger.error("获取锁[{}]超时",key);
			return false;
		}
    }
    
    /**
     * 释放锁
     * @param key
     */
    public void unlock(String key){
    	Thread currentThread = Thread.currentThread();
    	InterProcessMutex lock=threadData.get(currentThread);
    	if(lock==null){
    		return;
    	}
    	threadData.remove(currentThread);
    	try {
    		lock.release();
		} catch (Exception e) {
			logger.error("释放锁[{}]失败",key);
			logger.error("",e);
		}
    	
    }
    @PreDestroy
    public void finish(){
        try{
            logger.info("开始关闭zookeeper[{}] Client ..........",properteis.getConnectString());
            if(curatorFramework!=null)curatorFramework.close();
            logger.info("关闭zookeeper[{}] Client 成功",properteis.getConnectString());
        }catch (Exception e){
            logger.error("",e);
        }
    }

    public void setProperteis(ZookeeperClientProperteis properteis) {
        this.properteis = properteis;
    }
}
