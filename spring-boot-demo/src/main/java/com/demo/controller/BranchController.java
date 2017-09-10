package com.demo.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.common.Constant;
import com.demo.controller.msg.AddBranchRequest;
import com.demo.controller.msg.LoginUserInfo;
import com.demo.controller.msg.QueryAllBranchResponse;
import com.demo.controller.msg.UpdateBranchRequest;
import com.demo.framework.annotation.TradeService;
import com.demo.framework.msg.BaseRequest;
import com.demo.framework.msg.BaseResponse;
import com.demo.framework.util.CommUtil;
import com.demo.framework.util.SpringContextUtil;
import com.demo.mapper.BranchInfo;
import com.demo.service.BranchService;


/**
 * @author Benny
 *
 */
@Service
@TradeService(version="1.0.0")
public class BranchController {
//  private  Logger logger = LoggerFactory.getLogger(this.getClass());
  
  @Autowired
  private BranchService branchService;
    /**
     * 查询所有机构信息
     * @param request
     * @return
     * @throws Exception
     */
  @TradeService(value="query_all_branch",isLog = false)
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
