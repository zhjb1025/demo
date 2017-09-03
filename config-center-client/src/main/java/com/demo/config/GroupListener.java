package com.demo.config;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.demo.config.client.ConfigCenterClient;

public class GroupListener implements TreeCacheListener {
	private  Logger logger = LoggerFactory.getLogger(this.getClass());
	private  String groupUrlPath =null;
	private Map<String,String> groupVersion= new ConcurrentHashMap<String,String>();
	public GroupListener(String groupUrlPath) {
		this.groupUrlPath=groupUrlPath;
		String [] groups = ConfigCenterClient.getGroups().split(",");
		for(String s:groups) {
			groupVersion.put(s, "0");
		}
	}
	
	@Override
	public void childEvent(CuratorFramework client, TreeCacheEvent event) throws Exception {
		
		if(event.getData().getPath().indexOf(groupUrlPath)<0) {
			return;
		}
		logger.info("event:{},path:{}",event.getType().name(),event.getData().getPath());
		if(groupUrlPath.equals(event.getData().getPath())) {
			return;
		}
		if(event.getData().getData()==null) {
			return;
		}
    	switch (event.getType()) {
    	 	case NODE_ADDED:  
    	 		handleAddEvent(event);
                break;  
            case NODE_UPDATED:  
            	handleUpdateEvent(event);
                break;  
            default:  
                break;
        }  
	}
	
	private void handleUpdateEvent(TreeCacheEvent event) {
		String group=getGroup(event.getData().getPath());
		if(groupVersion.get(group)==null) {
			return;
		}
		String version=new String(event.getData().getData());
		if(version.equals(groupVersion.get(group))) {
			logger.info("组[{}]版本[{}] 未发生变化",group,version);
			return;
		}
		ConfigCenterClient.synGroup(group);
		groupVersion.put(group, version);
	}
	private void handleAddEvent(TreeCacheEvent event) {
		String group=getGroup(event.getData().getPath());
		if(groupVersion.get(group)!=null) {
			String version=new String(event.getData().getData());
			logger.info("初始化组[{}]版本信息[{}]",group,version);
			groupVersion.put(group, version);
		}
		
	}
	private String getGroup(String path) {
		return path.substring(groupUrlPath.length()+1,path.length());
	}

}
