package com.demo.config.service;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException.NodeExistsException;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.demo.config.dao.ConfigCenterDao;
import com.demo.config.dao.ConfigInfo;
import com.demo.framework.util.HttpUtils;
import com.demo.zookeeper.ZookeeperClient;

@Service
public class ConfigCenterService {
	private  Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ZookeeperClient client;
    
    @Autowired
    @Qualifier("FILE")
    private ConfigCenterDao configCenterDao;
    
    private Map<String,List<ConfigInfo>> configGroupMap = new ConcurrentHashMap<String,List<ConfigInfo>>();

    private final String serverUrlPath = "/root/config-center/servers";
    
    private final String groupUrlPath = "/root/config-center/groups/";

    @Value("${server.port}")
    private String port;
    
    @Value("${server.context-path}")
    private String contextPath;
    
    private String url;
    
    @PostConstruct
    public void init() throws Exception {
    	loadConfig();
    	export();
    	
    }
    
    private void loadConfig() throws Exception {
		Map<String,List<ConfigInfo>> groupMap= configCenterDao.loadAllConfig();
		Iterator<String> iter = groupMap.keySet().iterator();
		while(iter.hasNext()) {
			String key=iter.next();
			configGroupMap.put(key, groupMap.get(key));
		}
		
    }
    
    /**
     * 向Zookeeper 发布服务地址
     */
    private void export() {
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
    	url=urlBuilder.toString();
    	while(true) {
    		try {
        		String url_= URLEncoder.encode(url, "UTF-8");
        		logger.info("发布配置中心提供服务URL地址[{}]",url);
    			client.getCuratorFramework().create().withMode(CreateMode.EPHEMERAL)
    			.forPath(serverUrlPath+"/"+url_);
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
	public  String getLocalHostLANAddress() {
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
		String[] group = groups.split(",");
		for(String g:group ) {
			temp.addAll(configGroupMap.get(g));
		}
    	return temp;
    }
    
    public List<String> getALLGroup(){
    	List<String> list= new ArrayList<String>();
    	Iterator<String> iter = configGroupMap.keySet().iterator();
		while(iter.hasNext()) {
			String key=iter.next();
			list.add(key);
		}
		return list;
    }
    
    public String updateConfig(ConfigInfo configInfo) throws Exception{
    	configCenterDao.updateConfig(configInfo);
    	String ret = notifyChange(configInfo.getGroup());
    	
    	//修改zookeeper 时间戳 修改时间戳
    	Stat stat = client.getCuratorFramework().checkExists().forPath(groupUrlPath+configInfo.getGroup());
    	if(stat==null) {
    		client.getCuratorFramework().create().creatingParentContainersIfNeeded().withMode(CreateMode.PERSISTENT).forPath(groupUrlPath+configInfo.getGroup(), (""+System.currentTimeMillis()).getBytes());
    	}else {
    		client.getCuratorFramework().setData().forPath(groupUrlPath+configInfo.getGroup(), (""+System.currentTimeMillis()).getBytes());
    	}
    	return ret;
    }
    
    public void synConfig(String group) throws Exception{
    	List<ConfigInfo> list = configCenterDao.loadGroupConfig(group);
    	configGroupMap.put(group, list);
    }
    
    private String notifyChange(String group) throws Exception {
    	StringBuilder sb= new StringBuilder();
    	List<String> list = client.getCuratorFramework().getChildren().forPath(serverUrlPath);
    	for(String s:list) {
    		String url=URLDecoder.decode(s, "UTF-8");
    		logger.info("通知[{}]同步数据[{}]",url,group);
    		try {
    			HttpUtils.get(url+"/syn/"+group);
    			logger.info("通知[{}]同步数据[{}] 成功",url,group);
    			sb.append(url).append("#").append("成功,");
			} catch (Exception e) {
				logger.info("通知[{}]同步数据[{}] 失败",url,group);
				sb.append(url).append("#").append("失败,");
			}
    	}
    	return sb.toString();
    }
}
