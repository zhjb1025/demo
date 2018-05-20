package com.demo.eoms.controller;

import static com.demo.eoms.common.Constant.API_PREFIX;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.eoms.controller.msg.ListQueryResponse;
import com.demo.eoms.controller.msg.MenuApiAllResponse;
import com.demo.eoms.controller.msg.PageQueryResponse;
import com.demo.eoms.controller.msg.RoleAddRequest;
import com.demo.eoms.controller.msg.RoleMenuApiQueryRequest;
import com.demo.eoms.controller.msg.RoleMenuApiResult;
import com.demo.eoms.controller.msg.RolePageQueryResult;
import com.demo.eoms.controller.msg.RoleQueryRequest;
import com.demo.eoms.controller.msg.RoleUpdateRequest;
import com.demo.eoms.mapper.MenuInfo;
import com.demo.eoms.service.RoleService;
import com.demo.framework.annotation.TradeService;
import com.demo.framework.msg.BaseRequest;
import com.demo.framework.msg.BaseResponse;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;


/**
 * @author Benny
 *
 */
@Service
@TradeService(version="1.0.0")
public class RoleController {
//  private  Logger logger = LoggerFactory.getLogger(this.getClass());
  
  @Autowired
  private RoleService roleService;

    @TradeService(value=API_PREFIX+"page_query_role",isLog = false)
    public BaseResponse pageQueryRole(RoleQueryRequest request) throws Exception {
        PageQueryResponse<RolePageQueryResult> response= new PageQueryResponse<RolePageQueryResult>();
        PageHelper.startPage(request.getPageNumber(), request.getPageSize());
        List<RolePageQueryResult> list = roleService.queryRole(request.getRoleName());
        PageInfo<RolePageQueryResult> page=new PageInfo<RolePageQueryResult>(list);
        response.setRows(list);
        response.setTotal(page.getTotal());
        return response;
    }

    @TradeService(value=API_PREFIX+"query_all_menu_api",isLog = false)
    public BaseResponse queryAllMenuApi(BaseRequest request) throws Exception {
        MenuApiAllResponse response= new MenuApiAllResponse();
        List<MenuInfo> list = roleService.queryAllMenuApi();
        response.setMenuInfoList(list);
        return response;
    }

    @TradeService(value=API_PREFIX+"query_role_menu_api",isLog = false)
    public BaseResponse queryRoleMenuApi(RoleMenuApiQueryRequest request) throws Exception {
    	ListQueryResponse<RoleMenuApiResult> response= new ListQueryResponse<RoleMenuApiResult>(); 
    	response.setRows(roleService.queryRoleMenuIDApiID(request.getRoleID()));
        return response;
    }

    @TradeService(value=API_PREFIX+"add_role")
    public BaseResponse addRole(RoleAddRequest request) throws Exception {
        BaseResponse response= new BaseResponse();
        roleService.addRole(request);
        return response;
    }

    @TradeService(value=API_PREFIX+"update_role")
    public BaseResponse queryRoleMenuApi(RoleUpdateRequest request) throws Exception {
        BaseResponse response= new BaseResponse();
        roleService.updateRole(request);
        return response;
    }


    @TradeService(value=API_PREFIX+"query_all_role",isLog = false)
    public BaseResponse queryAllRole(BaseRequest request) throws Exception {
        PageQueryResponse<RolePageQueryResult> response= new PageQueryResponse<RolePageQueryResult>();
        List<RolePageQueryResult> list = roleService.queryRole(null);
        response.setRows(list);
        response.setTotal(Long.valueOf(list.size()));
        return response;
    }
}
