//package com.demo.service;
//
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//
//import javax.annotation.PostConstruct;
//
//import org.apache.commons.lang.StringUtils;
//import org.apache.commons.lang.time.DateUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Sort;
//import org.springframework.data.mongodb.core.MongoTemplate;
//import org.springframework.data.mongodb.core.query.Criteria;
//import org.springframework.data.mongodb.core.query.Query;
//import org.springframework.stereotype.Service;
//
//import com.demo.controller.msg.AccessLogPageQueryResult;
//import com.demo.controller.msg.AccessLogQueryRequest;
//import com.demo.controller.msg.PageQueryResponse;
//import com.demo.framework.annotation.TradeService;
//import com.demo.framework.dubbo.AccessLog;
//import com.demo.framework.msg.BaseResponse;
//
//@Service
//@TradeService(version="1.0.0")
//public class QueryAccessLogService extends Thread  {
//	private  Logger logger = LoggerFactory.getLogger(this.getClass());
//    @Autowired
//    private MongoTemplate mongoTemplate;
//    @TradeService(value="page_query_access_log",isLog = false,isAuth=true)
//    public BaseResponse query(AccessLogQueryRequest request) throws Exception {
//        PageQueryResponse<AccessLogPageQueryResult> response= new PageQueryResponse<AccessLogPageQueryResult>();
//        Query query= new Query();
//        Date startTime = DateUtils.parseDate(request.getStartTime(), new String[]{"yyyy-MM-dd HH:mm:ss"});
//        Date endTime = DateUtils.parseDate(request.getEndTime(), new String[]{"yyyy-MM-dd HH:mm:ss"});
//        List<Criteria> criteriaList= new ArrayList<Criteria>();
//        criteriaList.add(Criteria.where("startTimestamp").gte(startTime.getTime()));
//        criteriaList.add(Criteria.where("startTimestamp").lte(endTime.getTime()));
//        if(!StringUtils.isBlank(request.getQueryService())){
//            criteriaList.add(Criteria.where("service").is(request.getQueryService()));
//        }
//        if(!StringUtils.isBlank(request.getQueryVersion())){
//            criteriaList.add(Criteria.where("version").is(request.getQueryVersion()));
//        }
//        if(!StringUtils.isBlank(request.getQueryUserId())){
//            criteriaList.add(Criteria.where("userId").is(request.getQueryUserId()));
//        }
//        if(request.getDealTime()!=null){
//            criteriaList.add(Criteria.where("dealTime").gt(request.getDealTime()));
//        }
//        Criteria criteria= new Criteria();
//        Criteria [] temp=criteriaList.toArray(new Criteria [criteriaList.size()]);
//        criteria.andOperator(temp);
//        logger.info("查询条件：[{}]",criteria.getCriteriaObject().toString());
//        query.addCriteria(criteria);
//        long total = mongoTemplate.count(query, AccessLog.class);
//        response.setTotal(total);
//        query.with(new Sort(Sort.Direction.DESC, "startTimestamp"));
//        int skip=(request.getPageNumber()-1)*request.getPageSize();
//        query.skip(skip).limit(request.getPageSize());
//        List<AccessLogPageQueryResult> list = mongoTemplate.find(query, AccessLogPageQueryResult.class);
//        response.setRows(list);
//
//        return response;
//    }
//    @PostConstruct
//    public void init(){
//        this.start();
//    }
//}
