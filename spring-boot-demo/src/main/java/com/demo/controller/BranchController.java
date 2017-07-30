package com.demo.controller;

import com.demo.common.Constant;
import com.demo.common.annotation.TradeService;
import com.demo.common.util.CommUtil;
import com.demo.common.util.SpringContextUtil;
import com.demo.controller.msg.*;
import com.demo.mapper.BranchInfo;
import com.demo.service.BranchService;
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
    @TradeService(value="update_branch")
    public BaseResponse updateBranch(UpdateBranchRequest request) throws Exception {
        BaseResponse response=new BaseResponse();
        BranchInfo branchInfo = new BranchInfo();
        branchInfo.setId(request.getId());
        branchInfo.setBranchName(request.getBranchName());
        branchInfo.setRemark(request.getRemark());
        branchInfo.setUpdateTime(new Date());
        LoginUserInfo loginUser=(LoginUserInfo) SpringContextUtil.getThreadLocalData().
                request.getSession().getAttribute(Constant.LOGIN_USER);
        branchInfo.setUpdateUserId(loginUser.getUserId());
        branchService.updateBranchInfo(branchInfo);
        return response;
    }

    @TradeService(value="add_branch")
    public BaseResponse addBranch(AddBranchRequest request) throws Exception {
        BaseResponse response=new BaseResponse();
        BranchInfo branchInfo = new BranchInfo();
        branchInfo.setParentId(request.getParentId());
        branchInfo.setBranchName(request.getBranchName());
        branchInfo.setRemark(request.getRemark());
        branchInfo.setUpdateTime(new Date());
        branchInfo.setCreateTime(new Date());
        LoginUserInfo loginUser=(LoginUserInfo) SpringContextUtil.getThreadLocalData().
                request.getSession().getAttribute(Constant.LOGIN_USER);
        branchInfo.setUpdateUserId(loginUser.getUserId());
        branchInfo.setCreateUserId(loginUser.getUserId());
        BranchInfo parentBranch = branchService.getBranchInfo(request.getParentId());
        String maxBranchCode=branchService.getMaxBranchCode(request.getParentId());
        if(maxBranchCode==null){
            branchInfo.setBranchCode(parentBranch.getBranchCode()+"00");
        }else{
            long code=Long.parseLong(maxBranchCode);
            code++;
            String branchCode=CommUtil.fill(code+"",'0',maxBranchCode.length(),'L');
            branchInfo.setBranchCode(branchCode);
        }

        branchService.addBranchInfo(branchInfo);
        return response;
    }
}
