package com.demo.config;

import java.net.URLDecoder;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.demo.config.client.ConfigCenterClient;

public class ServerListener implements TreeCacheListener {
	private  Logger logger = LoggerFactory.getLogger(this.getClass());
	private  String serverUrlPath =null;
	public ServerListener(String serverUrlPath) {
		this.serverUrlPath=serverUrlPath;
		ConfigCenterClient.resetUrl(new String[] {});
	}
	
	@Override
	public void childEvent(CuratorFramework client, TreeCacheEvent event) throws Exception {
		
		if(event.getData().getPath().indexOf(serverUrlPath)<0) {
			return;
		}
		logger.info("event:{},path:{}",event.getType().name(),event.getData().getPath());
    	
    	if(serverUrlPath.equals(event.getData().getPath())) {
			return;
		}
    	String url=URLDecoder.decode(event.getData().getPath(), "UTF-8");
    	url=url.substring(serverUrlPath.length()+1,url.length());
    	switch (event.getType()) {
    	 	case NODE_ADDED:  
    	 		ConfigCenterClient.addUrl(url);
                break;  
            case NODE_REMOVED:  
            	ConfigCenterClient.deleteUrl(url);
                break;  
            default:  
                break;
        }   
	}
}
