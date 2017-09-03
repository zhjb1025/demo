package com.demo.config;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.curator.framework.recipes.cache.TreeCache;
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
	
	private final String configCenterPath = "/root/config-center";
	
	private final String groupUrlPath = "/root/config-center/groups";
	
	private final String serverUrlPath = "/root/config-center/servers";
	
	private TreeCache treeCache;
	
	@PostConstruct
    public void init() throws Exception{
		//设置节点的cache
		treeCache = new TreeCache(client.getCuratorFramework(), configCenterPath);
		treeCache.getListenable().addListener(new GroupListener(groupUrlPath));
		treeCache.getListenable().addListener(new ServerListener(serverUrlPath));
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
