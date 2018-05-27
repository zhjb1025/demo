package com.demo.eoms.controller;

import static com.demo.eoms.common.Constant.API_PREFIX;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.eoms.common.EomsErrorCode;
import com.demo.eoms.controller.msg.ConfigAddRequest;
import com.demo.eoms.controller.msg.ConfigInfoQueryRequest;
import com.demo.eoms.controller.msg.ConfigInfoQueryResult;
import com.demo.eoms.controller.msg.PageQueryResponse;
import com.demo.eoms.controller.msg.SystemInfoQueryRequest;
import com.demo.eoms.controller.msg.SystemInfoRequest;
import com.demo.eoms.mapper.ConfigInfo;
import com.demo.eoms.mapper.SystemInfo;
import com.demo.eoms.service.ConfigCenterService;
import com.demo.framework.annotation.TradeService;
import com.demo.framework.exception.CommException;
import com.demo.framework.msg.BaseResponse;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service
@TradeService(version="1.0.0")
public class ConfigCenterController {
//	private  Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private ConfigCenterService configCenterService;
	
	@TradeService(value=API_PREFIX+"page_query_system_info",isLog = false)
	public BaseResponse querySystemInfo(SystemInfoQueryRequest request){
		PageQueryResponse<SystemInfo> response= new PageQueryResponse<SystemInfo>();
        PageHelper.startPage(request.getPageNumber(), request.getPageSize());
        SystemInfo record= new SystemInfo();
        record.setSystemCode(request.getSystemCode());
        record.setSystemName(request.getSystemName());
        List<SystemInfo> list = configCenterService.querySystemInfo(record);
        PageInfo<SystemInfo> page=new PageInfo<SystemInfo>(list);
        response.setTotal(page.getTotal());
        response.setRows(list);
        return response;
	}
	
	@TradeService(value=API_PREFIX+"add_system_info",isLog = true)
	public BaseResponse addSystemInfo(SystemInfoRequest request) throws  Exception{
		BaseResponse response= new BaseResponse();
        SystemInfo record= new SystemInfo();
        record.setSystemCode(request.getSystemCode());
        List<SystemInfo> list = configCenterService.querySystemInfo(record);
        if(list!=null && list.size()>0){
        	throw new CommException(EomsErrorCode.SYSTEM_CODE_EXITS,request.getSystemCode());
        }
        record.setSystemName(request.getSystemName());
        configCenterService.addSystemInfo(record);
        return response;
	}
	
	@TradeService(value=API_PREFIX+"update_system_info",isLog = true)
	public BaseResponse updateSystemInfo(SystemInfoRequest request) throws  Exception{
		BaseResponse response= new BaseResponse();
        SystemInfo record= new SystemInfo();
        record.setSystemCode(request.getSystemCode());
        List<SystemInfo> list = configCenterService.querySystemInfo(record);
        if(list==null || list.size()==0){
        	throw new CommException(EomsErrorCode.SYSTEM_CODE_EXITS,request.getSystemCode());
        }
        record.setSystemName(request.getSystemName());
        configCenterService.updateSystemInfo(record);
        return response;
	}
	
	
	@TradeService(value=API_PREFIX+"page_query_conifg_info",isLog = false)
	public BaseResponse queryConifgApply(ConfigInfoQueryRequest request){
		PageQueryResponse<ConfigInfoQueryResult> response= new PageQueryResponse<ConfigInfoQueryResult>();
        PageHelper.startPage(request.getPageNumber(), request.getPageSize());
        List<ConfigInfoQueryResult> list = configCenterService.queryConifg(request);
        PageInfo<ConfigInfoQueryResult> page=new PageInfo<ConfigInfoQueryResult>(list);
        response.setTotal(page.getTotal());
        response.setRows(list);
        return response;
	}
	
	@TradeService(value=API_PREFIX+"add_config_info",isLog = true)
	public BaseResponse addConfigInfo(ConfigAddRequest request) throws  Exception{
		if(request.getConfigCode().indexOf("#")>=0) {
			throw new CommException(EomsErrorCode.VALIDATE_FAIL,"编码中不能包含#号");
		}
		request.setId(null);
		BaseResponse response= new BaseResponse();
		ConfigInfoQueryRequest query= new ConfigInfoQueryRequest();
		query.setConfigCode(request.getConfigCode());
		query.setSystemCode(request.getSystemCode());
		List<ConfigInfoQueryResult> list= configCenterService.queryConifg(query);
        if(list!=null && list.size()>0){
        	throw new CommException(EomsErrorCode.CONFIG_CODE_EXITS,request.getConfigCode());
        }
        
        ConfigInfo record= new ConfigInfo();
        BeanUtils.copyProperties(request, record);
        configCenterService.addConfigInfo(record);
        return response;
	}
	
	@TradeService(value=API_PREFIX+"update_config_info",isLog = true)
	public BaseResponse updateConfigInfo(ConfigAddRequest request) throws  Exception{
		if(request.getId()==null) {
			throw new CommException(EomsErrorCode.VALIDATE_FAIL,"ID不能为空");
		}
		if(request.getConfigCode().indexOf("#")>=0) {
			throw new CommException(EomsErrorCode.VALIDATE_FAIL,"编码中不能包含#号");
		}
		BaseResponse response= new BaseResponse();
		ConfigInfoQueryRequest query= new ConfigInfoQueryRequest();
		query.setId(request.getId());
		//query.setConfigCode(request.getConfigCode());
		//query.setSystemCode(request.getSystemCode());
		List<ConfigInfoQueryResult> list= configCenterService.queryConifg(query);
        if(list==null || list.size()==0){
        	throw new CommException(EomsErrorCode.CONFIG_ID_ERROR);
        }
        ConfigInfo record= list.get(0);
        String oldCode=record.getConfigCode();
        
        query= new ConfigInfoQueryRequest();
		query.setId(null);
		query.setConfigCode(request.getConfigCode());
		query.setSystemCode(request.getSystemCode());
		list= configCenterService.queryConifg(query);
		
		if(list!=null && list.size()>0 && list.get(0).getId().intValue()!=record.getId().intValue()){
        	throw new CommException(EomsErrorCode.CONFIG_CODE_NOT_EXITS,request.getConfigCode());
        }
        BeanUtils.copyProperties(request, record);
        configCenterService.updateConfigInfo(record,oldCode);
        return response;
	}
   
}
