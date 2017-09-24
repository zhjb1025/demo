package com.demo.framework.dubbo;

import java.util.concurrent.LinkedBlockingQueue;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import com.demo.framework.util.CommUtil;

@Service
public class AccessLogService extends Thread  {
	private  Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private MongoTemplate mongoTemplate;

    private LinkedBlockingQueue<AccessLog> logQueue= new LinkedBlockingQueue<AccessLog>(1024*100);

    public void addLog(AccessLog accessLog){
        try {
            logQueue.put(accessLog);
        } catch (InterruptedException e) {
        	logger.error("",e);
        }
    }
    
    @PostConstruct
    public void init(){
        this.start();
    }
    @PreDestroy
    public void finish(){
        try{
            this.interrupt();
        }catch (Exception e){
            logger.error("",e);
        }
    }
    @Override
    public void run() {
        logger.info("启动日志处理线程");
        AccessLog accessLog=null;
        while (true){
            try {
                accessLog=logQueue.take();
                handle(accessLog);
            }catch (InterruptedException e){
                break;
            }catch (Exception e){
                logger.error("写日mongo志异常[{}]",accessLog);
                logger.error("",e);
            }
        }
        //程序退出时，把队列里面剩余的日志处理完
        while(logQueue.size()>0){
            accessLog=logQueue.poll();
            handle(accessLog);
        }
        logger.info("停止日志处理线程");
    }

    private void handle(AccessLog accessLog){
        if (accessLog==null){
            return;
        }
        Object userID=CommUtil.getJsonValue(accessLog.getRequest(),"userId");
        if(userID!=null){
            accessLog.setUserId(userID.toString());
        }
        Object rspCode=CommUtil.getJsonValue(accessLog.getResponse(),"rspCode");
        if(rspCode!=null){
            accessLog.setRspCode(rspCode.toString());
        }
        Object rspMsg=CommUtil.getJsonValue(accessLog.getResponse(),"rspMsg");
        if(rspMsg!=null){
            accessLog.setRspMsg(rspMsg.toString());
        }
        Object tradeStatus=CommUtil.getJsonValue(accessLog.getResponse(),"tradeStatus");
        if(tradeStatus!=null){
            accessLog.setTradeStatus(tradeStatus.toString());
        }
        mongoTemplate.save(accessLog);
    }
}
