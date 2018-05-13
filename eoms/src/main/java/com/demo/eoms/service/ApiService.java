package com.demo.eoms.service;

import static com.demo.eoms.common.Constant.API_PREFIX;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.eoms.common.EomsErrorCode;
import com.demo.eoms.controller.msg.ApiAddRequest;
import com.demo.eoms.controller.msg.ApiQueryRequest;
import com.demo.eoms.controller.msg.ApiUpdateRequest;
import com.demo.eoms.controller.msg.PageQueryResponse;
import com.demo.eoms.mapper.ApiServiceInfo;
import com.demo.eoms.mapper.ApiServiceInfoMapper;
import com.demo.framework.annotation.TradeService;
import com.demo.framework.exception.CommException;
import com.demo.framework.msg.BaseResponse;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service
@TradeService(version="1.0.0")
public class ApiService {
//	private  Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private ApiServiceInfoMapper apiServiceInfoMapper;
	
	
	@TradeService(value=API_PREFIX+"page_query_api_info",isLog = false)
	public BaseResponse query(ApiQueryRequest request){
		PageQueryResponse<ApiServiceInfo> response= new PageQueryResponse<ApiServiceInfo>();
        
        ApiServiceInfo record= new ApiServiceInfo();
        record.setService(request.getApiCode());
        record.setRemark(request.getApiName());
        PageHelper.startPage(request.getPageNumber(), request.getPageSize());
        List<ApiServiceInfo> list = apiServiceInfoMapper.selectByColumn(record);
        PageInfo<ApiServiceInfo> page=new PageInfo<ApiServiceInfo>(list);
        response.setTotal(page.getTotal());
        response.setRows(list);
        return response;
	}
	
	@TradeService(value=API_PREFIX+"add_api_info",isLog = true)
	public BaseResponse add(ApiAddRequest request) throws  Exception{
		BaseResponse response= new BaseResponse();
      
		ApiServiceInfo apiServiceInfo = apiServiceInfoMapper.queryApiServiceInfo(request.getApiCode());
        if(apiServiceInfo!=null){
        	throw new CommException(EomsErrorCode.API_CODE_EXITS,request.getApiCode());
        }
        apiServiceInfo= new ApiServiceInfo();
        apiServiceInfo.setService(request.getApiCode());
        apiServiceInfo.setRemark(request.getApiName());
        apiServiceInfo.setVersion(request.getApiVersion());
        apiServiceInfoMapper.insert(apiServiceInfo);
        return response;
	}
	
	@TradeService(value=API_PREFIX+"update_api_info",isLog = true)
	public BaseResponse update(ApiUpdateRequest request) throws  Exception{
		BaseResponse response= new BaseResponse();
		ApiServiceInfo apiServiceInfo = apiServiceInfoMapper.selectByPrimaryKey(request.getId());
		if(apiServiceInfo==null) {
			throw new CommException(EomsErrorCode.API_ID_ERROR);
		}
		//如果接口编码不一致，检查接口编码是否重复
		if(!apiServiceInfo.getService().equals(request.getApiCode())) {
			ApiServiceInfo temp = apiServiceInfoMapper.queryApiServiceInfo(request.getApiCode());
			if(temp!=null) {
				throw new CommException(EomsErrorCode.API_CODE_EXITS,request.getApiCode());
			}
		}
		apiServiceInfo.setService(request.getApiCode());
        apiServiceInfo.setRemark(request.getApiName());
        apiServiceInfo.setVersion(request.getApiVersion());
		apiServiceInfoMapper.updateByPrimaryKey(apiServiceInfo);
        return response;
	}
   
}
