package com.demo.config.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.config.ConfigErrorCode;
import com.demo.config.service.mapper.SystemInfo;
import com.demo.config.service.mapper.SystemInfoMapper;
import com.demo.config.service.msg.SystemInfoRequest;
import com.demo.config.service.msg.PageQueryResponse;
import com.demo.config.service.msg.QuerySystemInfoRequest;
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
	
	@TradeService(value="page_query_system_info",isLog = false)
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
	
	@TradeService(value="add_system_info",isLog = true)
	public BaseResponse addSystemInfo(SystemInfoRequest request) throws  Exception{
		BaseResponse response= new BaseResponse();
        SystemInfo record= new SystemInfo();
        record.setSystemCode(request.getSystemCode());
        List<SystemInfo> list = systemInfoMapper.selectByColumn(record);
        if(list!=null && list.size()>0){
        	throw new CommException(ConfigErrorCode.SYSTEM_CODE_EXITS,request.getSystemCode());
        }
        record.setSystemName(request.getSystemName());
        systemInfoMapper.insert(record);
        return response;
	}
	
	@TradeService(value="update_system_info",isLog = true)
	public BaseResponse updateSystemInfo(SystemInfoRequest request) throws  Exception{
		BaseResponse response= new BaseResponse();
        SystemInfo record= new SystemInfo();
        record.setSystemCode(request.getSystemCode());
        List<SystemInfo> list = systemInfoMapper.selectByColumn(record);
        if(list==null || list.size()==0){
        	throw new CommException(ConfigErrorCode.SYSTEM_CODE_EXITS,request.getSystemCode());
        }
        record.setSystemName(request.getSystemName());
        systemInfoMapper.updateByPrimaryKey(record);
        return response;
	}
	
	
	@TradeService(value="page_query_conifg_apply",isLog = false)
	public BaseResponse queryConifgApply(QuerySystemInfoRequest request){
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
   
}
