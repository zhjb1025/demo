package com.demo.controller;

import com.alibaba.fastjson.JSON;
import com.demo.common.annotation.TradeService;
import com.demo.common.util.CommUtil;
import com.demo.common.util.SpringContextUtil;
import com.demo.controller.msg.*;
import com.demo.mapper.BranchInfo;
import com.demo.service.BranchService;
import com.demo.service.RoleService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;


/**
 * @author Benny
 *
 */
@Service
@TradeService(version="1.0.0")
public class RoleController {
  private static Logger logger = LoggerFactory.getLogger(RoleController.class);
  
  @Autowired
  private RoleService roleService;

    @TradeService(value="page_query_role")
    public BaseResponse queryAllBranch(RoleQueryRequest request) throws Exception {
        PageQueryResponse<RolePageQueryResult> response= new PageQueryResponse<RolePageQueryResult>();
        PageHelper.startPage(request.getPageNumber(), request.getPageSize());
        List<RolePageQueryResult> list = roleService.queryRole(request.getRoleName());
        PageInfo<RolePageQueryResult> page=new PageInfo<RolePageQueryResult>(list);
        response.setRows(list);
        logger.info(JSON.toJSONString(list));
        response.setTotal(page.getTotal());
        logger.info(JSON.toJSONString(response));
        return response;
    }

}
