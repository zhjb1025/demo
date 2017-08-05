package com.demo.controller;

import com.alibaba.fastjson.JSON;
import com.demo.common.annotation.TradeService;
import com.demo.common.util.CommUtil;
import com.demo.common.util.SpringContextUtil;
import com.demo.controller.msg.*;
import com.demo.mapper.BranchInfo;
import com.demo.mapper.MenuInfo;
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
    public BaseResponse pageQueryRole(RoleQueryRequest request) throws Exception {
        PageQueryResponse<RolePageQueryResult> response= new PageQueryResponse<RolePageQueryResult>();
        PageHelper.startPage(request.getPageNumber(), request.getPageSize());
        List<RolePageQueryResult> list = roleService.queryRole(request.getRoleName());
        PageInfo<RolePageQueryResult> page=new PageInfo<RolePageQueryResult>(list);
        response.setRows(list);
        response.setTotal(page.getTotal());
        return response;
    }

    @TradeService(value="query_all_menu_api")
    public BaseResponse queryAllMenuApi(BaseRequest request) throws Exception {
        AllMenuApiResponse response= new AllMenuApiResponse();
        List<MenuInfo> list = roleService.queryAllMenuApi();
        response.setMenuInfoList(list);
        return response;
    }

    @TradeService(value="query_role_menu_api")
    public BaseResponse queryRoleMenuApi(QueryRoleMenuApiRequest request) throws Exception {
        QueryRoleMenuApiResponse response= new QueryRoleMenuApiResponse();
        response.setApiIDs(roleService.queryRoleAipIDs(request.getRoleID()));
        response.setMenuIDs(roleService.queryRoleMenuIDs(request.getRoleID()));
        return response;
    }

    @TradeService(value="add_role")
    public BaseResponse queryRoleMenuApi(AddRoleRequest request) throws Exception {
        BaseResponse response= new BaseResponse();
        roleService.addRole(request);
        return response;
    }

    @TradeService(value="update_role")
    public BaseResponse queryRoleMenuApi(UpdateRoleRequest request) throws Exception {
        BaseResponse response= new BaseResponse();
        roleService.updateRole(request);
        return response;
    }


    @TradeService(value="query_all_role")
    public BaseResponse queryAllRole(BaseRequest request) throws Exception {
        PageQueryResponse<RolePageQueryResult> response= new PageQueryResponse<RolePageQueryResult>();
        List<RolePageQueryResult> list = roleService.queryRole(null);
        response.setRows(list);
        response.setTotal(Long.valueOf(list.size()));
        return response;
    }
}
