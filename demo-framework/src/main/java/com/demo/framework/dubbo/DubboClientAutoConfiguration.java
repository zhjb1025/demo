package com.demo.framework.dubbo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.demo.zookeeper.ZookeeperClient;

@Configuration
@EnableConfigurationProperties(value = DubboProperties.class)
@ConditionalOnClass(DubboClient.class)
@ConditionalOnProperty(prefix = "dubbo.client", value = "enable", matchIfMissing = false)
public class DubboClientAutoConfiguration {

    @Autowired
    private DubboProperties dubboProperties;
    
    @Autowired
	private ZookeeperClient client;
    
    @Autowired
    private AccessLogService accessLogService;
	

    @Bean
    @ConditionalOnMissingBean(DubboClient.class)
    public DubboClient dubboClient() {
    	DubboClient dubboClient = new DubboClient();
    	dubboClient.setDubboProperties(dubboProperties);
    	dubboClient.setClient(client);
    	dubboClient.setAccessLogService(accessLogService);
        return dubboClient;
    }
}
