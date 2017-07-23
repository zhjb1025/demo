package com.demo.controller;

import com.demo.common.annotation.TradeService;
import com.demo.controller.msg.BaseRequest;
import com.demo.controller.msg.BaseResponse;
import com.demo.controller.msg.QueryAllBranchResponse;
import com.demo.controller.msg.QueryUserMenuResponse;
import com.demo.mapper.BranchInfo;
import com.demo.mapper.MenuInfo;
import com.demo.service.BranchService;
import com.demo.service.MenuInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * @author Benny
 *
 */
@Service
@TradeService(version="1.0.0")
public class BranchController {
  private static Logger logger = LoggerFactory.getLogger(BranchController.class);
  
  @Autowired
  private BranchService branchService;
    /**
     * 查询所有机构信息
     * @param request
     * @return
     * @throws Exception
     */
  @TradeService(value="query_all_branch")
  public BaseResponse queryAllBranch(BaseRequest request) throws Exception {
      QueryAllBranchResponse response = new QueryAllBranchResponse();
      List<BranchInfo> list = branchService.getAll();
      response.setBranchList(list);
      return response;
  }
}
