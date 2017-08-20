package com.demo.zookeeper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(value = ZookeeperClientProperteis.class)
@ConditionalOnClass(ZookeeperClient.class)
@ConditionalOnProperty(prefix = "spring.zookeeper", value = "enable", matchIfMissing = false)
public class ZookeeperClientAutoConfiguration {

    @Autowired
    private ZookeeperClientProperteis zookeeperClientProperteis;

    @Bean
    @ConditionalOnMissingBean(ZookeeperClient.class)
    public ZookeeperClient zookeeperClient() {
    	ZookeeperClient client = new ZookeeperClient();
        client.setProperteis(zookeeperClientProperteis);
        return client;
    }
}
