package com.demo.service;

import com.demo.zookeeper.Client;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class LeaderService extends LeaderSelectorListenerAdapter {
    private static Logger logger = LoggerFactory.getLogger(LeaderService.class);
    @Autowired
    private Client client;

    private  LeaderSelector leaderSelector;

    private final String PATH = "/root/leader-selector/config-center";



    @PostConstruct
    public void init(){
        leaderSelector = new LeaderSelector(client.getClient(), PATH, this);
        leaderSelector.autoRequeue();
    }
    @Override
    public void takeLeadership(CuratorFramework curatorFramework) throws Exception {
        logger.info("takeLeadership");
    }
}
