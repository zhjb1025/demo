package com.demo.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.demo.common.annotation.ThreadManager;
import com.demo.common.util.CommUtil;
import com.demo.controller.msg.AccessLog;
import com.demo.controller.msg.BaseResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.LinkedBlockingQueue;

@Service
@ThreadManager(name = "AccessLogService")
public class AccessLogService extends Thread  {
    private static Logger logger = LoggerFactory.getLogger(AccessLogService.class);
    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private RouteService routeService;

    private LinkedBlockingQueue<AccessLog> logQueue= new LinkedBlockingQueue<AccessLog>(1024*100);

    public void addLog(AccessLog accessLog){
        try {
            logQueue.put(accessLog);
        } catch (InterruptedException e) {

        }
    }
    @Override
    public void run() {
        logger.info("启动日志处理线程");
        while (true){
            AccessLog accessLog=null;
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
        logger.info("停止日志处理线程");
    }

    private void handle(AccessLog accessLog){
        if (accessLog==null){
            return;
        }
        accessLog.setTradeDate(CommUtil.getDateYYYYMMDD());
        BaseResponse response = accessLog.getResponse();
        accessLog.setRspMsg(response.getRspMsg());
        accessLog.setRspCode(response.getRspCode());
        JSONObject req = JSON.parseObject(accessLog.getRequest().toString());
        accessLog.setUserId(req.getString("userId"));
        accessLog.setRequest(req);
        if(accessLog.getUserId()==null){
            Object userID=CommUtil.getFieldValue(accessLog.getResponse(),"userId");
             if(userID!=null){
                 accessLog.setUserId(userID.toString());
             }
        }
        mongoTemplate.save(accessLog);
        ;
    }
}
