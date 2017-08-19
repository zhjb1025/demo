package com.demo.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

public class Client {
    private static Logger logger = LoggerFactory.getLogger(Client.class);
    private CuratorFramework client;
    private ZookeeperClientProperteis properteis;

    @PostConstruct
    public void init(){
        client= CuratorFrameworkFactory.builder().connectString(properteis.getConnectString())
                .sessionTimeoutMs(properteis.getSessionTimeout())
                .connectionTimeoutMs(properteis.getConnectionTimeout())
                .retryPolicy(new ExponentialBackoffRetry(1000, Integer.MAX_VALUE))
                .defaultData(null)
                .build();
        logger.info("开始连接zookeeper[{}]..........",properteis.getConnectString());
        client.start();
        try {
            client.blockUntilConnected();
        } catch (InterruptedException e) {
            client=null;
            logger.info("",e);
        }
        logger.info("连接zookeeper[{}]成功",properteis.getConnectString());
    }

    public CuratorFramework getClient() {
        return client;
    }

    @PreDestroy
    public void finish(){
        try{
            logger.info("开始关闭zookeeper[{}] Client ..........",properteis.getConnectString());
            client.close();
            logger.info("关闭zookeeper[{}] Client 成功",properteis.getConnectString());
        }catch (Exception e){
            logger.error("",e);
        }
    }

    public void setProperteis(ZookeeperClientProperteis properteis) {
        this.properteis = properteis;
    }
}
