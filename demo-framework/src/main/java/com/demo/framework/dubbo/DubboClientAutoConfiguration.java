package com.demo.framework.dubbo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(value = DubboProperties.class)
@ConditionalOnClass(DubboClient.class)
@ConditionalOnProperty(prefix = "dubbo.client", value = "enable", matchIfMissing = false)
public class DubboClientAutoConfiguration {

    @Autowired
    private DubboProperties dubboProperties;

    @Bean
    @ConditionalOnMissingBean(DubboClient.class)
    public DubboClient dubboClient() {
    	DubboClient client = new DubboClient();
        client.setDubboProperties(dubboProperties);
        return client;
    }
}
