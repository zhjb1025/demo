package com.demo.config.service;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException.NodeExistsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.demo.config.dao.ConfigCenterDao;
import com.demo.config.dao.ConfigInfo;
import com.demo.zookeeper.ZookeeperClient;

@Service
public class ConfigCenterService {
    private static Logger logger = LoggerFactory.getLogger(ConfigCenterService.class);

    @Autowired
    private ZookeeperClient client;
    
    @Autowired
    @Qualifier("${demo.config.persistence.type}")
    private ConfigCenterDao configCenterDao;
    
    @Autowired
    private Environment env;
    
    private Map<String,ConfigInfo> configKeyMap = new ConcurrentHashMap<String,ConfigInfo>();
    private Map<String,List<ConfigInfo>> configGroupMap = new ConcurrentHashMap<String,List<ConfigInfo>>();

//    private final String lockPath = "/root/config-center/lock";
    private final String urlPath = "/root/config-center/servers";


    @PostConstruct
    public void init() {
    	loadConfig();
    	export();
    	
    }
    
    private void loadConfig() {
		List<ConfigInfo> list = configCenterDao.loadAllConfig();
		Map<String,List<ConfigInfo>> groupMap= new HashMap<String,List<ConfigInfo>>();
		Map<String,ConfigInfo> keyMap = new HashMap<String,ConfigInfo>();
		for(ConfigInfo configInfo:list) {
			keyMap.put(configInfo.getKey(), configInfo);
			if(groupMap.get(configInfo.getGroup())==null) {
				List<ConfigInfo> temp=new ArrayList<ConfigInfo>();
				groupMap.put(configInfo.getGroup(), temp);
			}
			groupMap.get(configInfo.getGroup()).add(configInfo);
		}
		
		Iterator<String> iter = configKeyMap.keySet().iterator();
		while(iter.hasNext()) {
			String key=iter.next();
			if(keyMap.get(key)==null) {
				logger.info("删除key:"+key);
				iter.remove();
			}
		}
		
		iter = configGroupMap.keySet().iterator();
		while(iter.hasNext()) {
			String key=iter.next();
			logger.info("删除group:"+key);
			if(groupMap.get(key)==null) {
				iter.remove();
			}
		}
		
		iter=keyMap.keySet().iterator();
		while(iter.hasNext()) {
			String key=iter.next();
			logger.info("加载配置:"+keyMap.get(key).toString());
			configKeyMap.put(key, keyMap.get(key));
		}
		
		iter=groupMap.keySet().iterator();
		while(iter.hasNext()) {
			String key=iter.next();
			configGroupMap.put(key, groupMap.get(key));
		}
		
    }
    
    /**
     * 向Zookeeper 发布服务地址
     */
    private void export() {
    	String port=env.getProperty("server.port");
    	
    	String contextPath=env.getProperty("server.context-path");
    	String ip=getLocalHostLANAddress();
    	if(ip==null) {
    		ip="127.0.0.1";
    	}
    	
    	StringBuilder urlBuilder=new StringBuilder("http://");
    	urlBuilder.append(ip)
    	   .append(":")
    	   .append(port);
    	
    	if(contextPath.charAt(0)!='/') {
    		urlBuilder.append("/");
    	}
    	if(contextPath.charAt(contextPath.length()-1)=='/') {
    		contextPath=contextPath.substring(0, contextPath.length()-1);
    	}
    	urlBuilder.append(contextPath);
    	
    	while(true) {
    		try {
        		String url= URLEncoder.encode(urlBuilder.toString(), "UTF-8");
        		logger.info("发布服务URL访问地址[{}]",urlBuilder.toString());
    			client.getCuratorFramework().create().withMode(CreateMode.EPHEMERAL)
    			.forPath(urlPath+"/"+url);
    			break;
    		} catch (NodeExistsException e) {
    			try {
					Thread.sleep(1000*2);
				} catch (InterruptedException e1) {
					logger.error("", e1);
				}
    		}catch (Exception e) {
    			logger.error("", e);
    		}
    	}
    	
    }

    @SuppressWarnings("rawtypes")
	public static String getLocalHostLANAddress() {
	    try {
	        InetAddress candidateAddress = null;
	        // 遍历所有的网络接口
	        for (Enumeration ifaces = NetworkInterface.getNetworkInterfaces(); ifaces.hasMoreElements(); ) {
	            NetworkInterface iface = (NetworkInterface) ifaces.nextElement();
	            // 在所有的接口下再遍历IP
	            for (Enumeration inetAddrs = iface.getInetAddresses(); inetAddrs.hasMoreElements(); ) {
	                InetAddress inetAddr = (InetAddress) inetAddrs.nextElement();
	                if (!inetAddr.isLoopbackAddress()) {// 排除loopback类型地址
	                    if (inetAddr.isSiteLocalAddress()) {
	                        // 如果是site-local地址，就是它了
	                        return inetAddr.getHostAddress();
	                    } else if (candidateAddress == null) {
	                        // site-local类型的地址未被发现，先记录候选地址
	                        candidateAddress = inetAddr;
	                    }
	                }
	            }
	        }
	        if (candidateAddress != null) {
	            return candidateAddress.getHostAddress();
	        }
	        // 如果没有发现 non-loopback地址.只能用最次选的方案
	        InetAddress jdkSuppliedAddress = InetAddress.getLocalHost();
	        return jdkSuppliedAddress.getHostAddress();
	    } catch (Exception e) {
	    	logger.error("",e);
	    }
	    return null;
	}
    
    
    public List<ConfigInfo> queryConfigInfo(String groups){
    	List<ConfigInfo> temp=new ArrayList<ConfigInfo>();
    	if("ALL".equals(groups.toUpperCase())) {
			Iterator<String> iter = configGroupMap.keySet().iterator();
    		while(iter.hasNext()) {
    			String key=iter.next();
    			temp.addAll(configGroupMap.get(key));
    		}
    	}else {
    		String[] group = groups.split(",");
    		for(String g:group ) {
    			temp.addAll(configGroupMap.get(g));
    		}
    		
    	}
    	return temp;
    }
    
    public ConfigInfo getConfigInfo(String key){
    	return configKeyMap.get(key);
    }
    
}
