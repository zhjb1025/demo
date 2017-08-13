package com.demo.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.demo.common.annotation.ThreadManager;
import com.demo.common.annotation.TradeService;
import com.demo.common.util.CommUtil;
import com.demo.controller.msg.*;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

@Service
@ThreadManager(name = "AccessLogService")
@TradeService(version="1.0.0")
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
    @TradeService(value="page_query_access_log",isLog = false)
    public BaseResponse query(AccessLogQueryRequest request) throws Exception {
        PageQueryResponse<AccessLogPageQueryResult> response= new PageQueryResponse<AccessLogPageQueryResult>();
        Query query= new Query();
        Date startTime = DateUtils.parseDate(request.getStartTime(), new String[]{"yyyy-MM-dd HH:mm:ss"});
        Date endTime = DateUtils.parseDate(request.getEndTime(), new String[]{"yyyy-MM-dd HH:mm:ss"});
        List<Criteria> criteriaList= new ArrayList<Criteria>();
        criteriaList.add(Criteria.where("startTimestamp").gte(startTime.getTime()));
        criteriaList.add(Criteria.where("startTimestamp").lte(endTime.getTime()));
        if(!StringUtils.isBlank(request.getQueryService())){
            criteriaList.add(Criteria.where("service").is(request.getQueryService()));
        }
        if(!StringUtils.isBlank(request.getQueryVersion())){
            criteriaList.add(Criteria.where("version").is(request.getQueryVersion()));
        }
        if(!StringUtils.isBlank(request.getQueryUserId())){
            criteriaList.add(Criteria.where("userId").is(request.getQueryUserId()));
        }
        Criteria criteria= new Criteria();
        Criteria [] temp=criteriaList.toArray(new Criteria [criteriaList.size()]);
        criteria.andOperator(temp);
        logger.info("查询条件：[{}]",criteria.getCriteriaObject().toString());
        query.addCriteria(criteria);
        long total = mongoTemplate.count(query, AccessLog.class);
        response.setTotal(total);
        query.with(new Sort(Sort.Direction.DESC, "startTimestamp"));
        int skip=(request.getPageNumber()-1)*request.getPageSize();
        query.skip(skip).limit(request.getPageSize());
        List<AccessLogPageQueryResult> list = mongoTemplate.find(query, AccessLogPageQueryResult.class);
        response.setRows(list);

        return response;
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
        accessLog.setTradeStatus(response.getTradeStatus());
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
