package com.demo.eoms.service;

import static com.demo.eoms.common.Constant.API_PREFIX;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.eoms.common.EomsErrorCode;
import com.demo.eoms.controller.msg.MenuApiQueryRequest;
import com.demo.eoms.controller.msg.MenuDeleteRequest;
import com.demo.eoms.controller.msg.MenuRequest;
import com.demo.eoms.controller.msg.PageQueryResponse;
import com.demo.eoms.controller.msg.UserMenuQueryResponse;
import com.demo.eoms.controller.msg.RoleMenuApiResult;
import com.demo.eoms.mapper.ApiServiceInfo;
import com.demo.eoms.mapper.MenuInfo;
import com.demo.eoms.mapper.MenuInfoMapper;
import com.demo.eoms.mapper.RoleInfoMapper;
import com.demo.framework.annotation.TradeService;
import com.demo.framework.exception.CommException;
import com.demo.framework.msg.BaseRequest;
import com.demo.framework.msg.BaseResponse;

@Service
@TradeService(version="1.0.0")
public class MenuInfoService {
    @Autowired
    private MenuInfoMapper menuInfoMapper;
    
    @Autowired
    private RoleInfoMapper  roleInfoMapper;

    @TradeService(value=API_PREFIX+"query_user_menu",isLog = false,isAuth=false)
    public BaseResponse queryUserMenu(BaseRequest request) throws Exception {
        UserMenuQueryResponse  response = new UserMenuQueryResponse();
        List<MenuInfo> list = menuInfoMapper.queryUserMenuInfo(request.getUserId());
        response.setMenuInfoList(list);
        return response;
    }
    
    @TradeService(value=API_PREFIX+"query_all_menu",isLog = false)
    public BaseResponse queryAllMenu(BaseRequest request) throws Exception {
        UserMenuQueryResponse  response = new UserMenuQueryResponse();
        List<MenuInfo> list = menuInfoMapper.queryAllMenuInfo();
        response.setMenuInfoList(list);
        return response;
    }
    
    @TradeService(value=API_PREFIX+"query_menu_api",isLog = false)
    public BaseResponse queryMenuApi(MenuApiQueryRequest request) throws Exception {
    	PageQueryResponse<ApiServiceInfo> response= new PageQueryResponse<ApiServiceInfo>();
        List<ApiServiceInfo> list = menuInfoMapper.queryMenuApi(request.getId());
        response.setRows(list);
        return response;
    }
    
    @TradeService(value=API_PREFIX+"add_menu")
    public BaseResponse addMenu(MenuRequest request) throws Exception {
    	BaseResponse response= new BaseResponse();
    	MenuInfo record= new MenuInfo();
    	BeanUtils.copyProperties(request, record);
    	menuInfoMapper.insert(record);
    	for(Integer apiId:request.getApi()) {
    		menuInfoMapper.insertMenuApi(record.getId(), apiId);
    	}
        return response;
    }
    
    @TradeService(value=API_PREFIX+"update_menu")
    public BaseResponse updateMenu(MenuRequest request) throws Exception {
    	BaseResponse response= new BaseResponse();
    	if(request.getId()==null) {
   		 	throw new CommException(EomsErrorCode.VALIDATE_FAIL,"菜单ID不能为空");
    	}
    	MenuInfo oldRecord = menuInfoMapper.selectByPrimaryKey(request.getId());
    	MenuInfo record= new MenuInfo();
    	BeanUtils.copyProperties(request, record);
    	record.setParentId(oldRecord.getParentId());
    	menuInfoMapper.updateByPrimaryKey(record);
    	
    	/**
    	 * 如果有删除的接口，同步删除，角色与接口的关系
    	 */
    	List<RoleMenuApiResult> list = roleInfoMapper.selectRoleMenuIDApiID(record.getId());
    	for(RoleMenuApiResult vo:list) {
    		boolean find=false;
    		for(Integer apiId:request.getApi()) {
        		if(vo.getApiId().intValue()==apiId.intValue()) {
        			find=true;
        			break;
        		}
        	}
    		if(!find) {
    			roleInfoMapper.deleteMenuAip(record.getId(), vo.getApiId());
    		}
    	}
    	
    	/**
    	 * 重新初始化菜单与接口的关系表
    	 */
    	menuInfoMapper.deleteMenuApi(record.getId());
    	for(Integer apiId:request.getApi()) {
    		menuInfoMapper.insertMenuApi(record.getId(), apiId);
    	}
        return response;
    }
    
    @TradeService(value=API_PREFIX+"delete_menu")
    public BaseResponse deleteMenu(MenuDeleteRequest request) throws Exception {
    	BaseResponse response= new BaseResponse();
    	if(request.getId()==null) {
   		 	throw new CommException(EomsErrorCode.VALIDATE_FAIL,"菜单ID不能为空");
    	}
    	MenuInfo record = menuInfoMapper.selectByPrimaryKey(request.getId());
    	
    	List<MenuInfo> list = menuInfoMapper.queryMenuInfo(record.getMenuCode()+"%");
    	for(MenuInfo m:list) {
    		/**
        	 * 如果有删除的接口，同步删除，角色与接口的关系
        	 */
        	roleInfoMapper.deleteMenuAipByMenuId(m.getId());
        	roleInfoMapper.deleteRoleMenuByMenuId(m.getId());
        	/**
        	 * 删除菜单与接口的关系表
        	 */
        	menuInfoMapper.deleteMenuApi(m.getId());
        	menuInfoMapper.delete(m.getId());
    	}
        return response;
    }

}
