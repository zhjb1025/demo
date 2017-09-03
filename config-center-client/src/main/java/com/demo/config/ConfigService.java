package com.demo.config;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.zookeeper.ZookeeperClient;

@Service
public class ConfigService {
	
	private  Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
    private ZookeeperClient client;
	
	private final String groupUrlPath = "/root/config-center/groups";
	
	private TreeCache treeCache;
	
	@PostConstruct
    public void init() throws Exception{
		//设置节点的cache  
        treeCache = new TreeCache(client.getCuratorFramework(), groupUrlPath);  
        //设置监听器和处理过程  
        treeCache.getListenable().addListener(new TreeCacheListener() {  
            @Override  
            public void childEvent(CuratorFramework client, TreeCacheEvent event) throws Exception {
            	logger.info("event:{},path:{}",event.getType().name(),event.getData().getPath());
            	switch (event.getType()) {  
	                case NODE_ADDED:  
	                    System.out.println("NODE_ADDED : "+ event.getData().getPath() +"  数据:"+ new String(event.getData().getData()));  
	                    break;  
	                case NODE_REMOVED:  
	                    System.out.println("NODE_REMOVED : "+ event.getData().getPath() +"  数据:"+ new String(event.getData().getData()));  
	                    break;  
	                case NODE_UPDATED:  
	                    System.out.println("NODE_UPDATED : "+ event.getData().getPath() +"  数据:"+ new String(event.getData().getData()));  
	                    break;  
	                default:  
	                    break;
                }  
            }  
        });  
        //开始监听  
        treeCache.start();  
    }
	
	@PreDestroy
    public void finish(){
        try{
        	if(treeCache!=null) {
        		treeCache.close();
        	}
        }catch (Exception e){
            logger.error("",e);
        }
    }
}
