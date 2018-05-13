package com.demo.eoms.service;

import static com.demo.eoms.common.Constant.API_PREFIX;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.eoms.common.EomsErrorCode;
import com.demo.eoms.controller.msg.PageQueryResponse;
import com.demo.eoms.controller.msg.ConfigInfoQueryRequest;
import com.demo.eoms.controller.msg.ConfigInfoQueryResult;
import com.demo.eoms.controller.msg.QuerySystemInfoRequest;
import com.demo.eoms.controller.msg.SystemInfoRequest;
import com.demo.eoms.mapper.ConfigInfoMapper;
import com.demo.eoms.mapper.SystemInfo;
import com.demo.eoms.mapper.SystemInfoMapper;
import com.demo.framework.annotation.TradeService;
import com.demo.framework.exception.CommException;
import com.demo.framework.msg.BaseResponse;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service
@TradeService(version="1.0.0")
public class ConfigCenterService {
//	private  Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private SystemInfoMapper systemInfoMapper;
	
	@Autowired
	private ConfigInfoMapper configInfoMapper;
	
	@TradeService(value=API_PREFIX+"page_query_system_info",isLog = false)
	public BaseResponse querySystemInfo(QuerySystemInfoRequest request){
		PageQueryResponse<SystemInfo> response= new PageQueryResponse<SystemInfo>();
        PageHelper.startPage(request.getPageNumber(), request.getPageSize());
        SystemInfo record= new SystemInfo();
        record.setSystemCode(request.getSystemCode());
        record.setSystemName(request.getSystemName());
        List<SystemInfo> list = systemInfoMapper.selectByColumn(record);
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
        List<SystemInfo> list = systemInfoMapper.selectByColumn(record);
        if(list!=null && list.size()>0){
        	throw new CommException(EomsErrorCode.SYSTEM_CODE_EXITS,request.getSystemCode());
        }
        record.setSystemName(request.getSystemName());
        systemInfoMapper.insert(record);
        return response;
	}
	
	@TradeService(value=API_PREFIX+"update_system_info",isLog = true)
	public BaseResponse updateSystemInfo(SystemInfoRequest request) throws  Exception{
		BaseResponse response= new BaseResponse();
        SystemInfo record= new SystemInfo();
        record.setSystemCode(request.getSystemCode());
        List<SystemInfo> list = systemInfoMapper.selectByColumn(record);
        if(list==null || list.size()==0){
        	throw new CommException(EomsErrorCode.SYSTEM_CODE_EXITS,request.getSystemCode());
        }
        record.setSystemName(request.getSystemName());
        systemInfoMapper.updateByPrimaryKey(record);
        return response;
	}
	
	
	@TradeService(value=API_PREFIX+"page_query_conifg_info",isLog = false)
	public BaseResponse queryConifgApply(ConfigInfoQueryRequest request){
		PageQueryResponse<ConfigInfoQueryResult> response= new PageQueryResponse<ConfigInfoQueryResult>();
        PageHelper.startPage(request.getPageNumber(), request.getPageSize());
        List<ConfigInfoQueryResult> list = configInfoMapper.selectByColumn(request);
        PageInfo<ConfigInfoQueryResult> page=new PageInfo<ConfigInfoQueryResult>(list);
        response.setTotal(page.getTotal());
        response.setRows(list);
        return response;
	}
   
}
