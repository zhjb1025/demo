package com.demo.gateway.controller;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.demo.framework.dubbo.DubboClient;
import com.demo.framework.exception.CommException;
import com.demo.framework.util.CommUtil;
import com.demo.framework.util.ThreadCacheData;
import com.demo.framework.util.ThreadCacheUtil;

/**
 * 网的Controller 处理所有http请求
 * 
 * @author Benny.zhang
 *
 */
@RestController
public class GatewayController {

	private  Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private static final String P3P_HEADER = "CP=\"NOI CURa ADMa DEVa TAIa OUR BUS IND UNI COM NAV INT\"";
	@Autowired
	private DubboClient dubboClient;
	
	@Value("${gateway.session.enable}")
	private boolean sessionEnable;
	
	@RequestMapping(value = "/gateway", method = { RequestMethod.POST,RequestMethod.GET })
	@ResponseBody
	public String gateway(HttpServletRequest request,HttpServletResponse response) throws CommException {
		response.setHeader("P3P",P3P_HEADER );//解决 ifame session 丢失的问题
		long beginTime = System.currentTimeMillis();
        long endTime =beginTime;
        String seqNo=null;
        String service=null;
        String version=null;
        String parameter=null;
        
        // 1.获取请求参数
        parameter=getRequestParameter(request);
        seqNo=CommUtil.getJsonValue(parameter,"seqNo");
        service=CommUtil.getJsonValue(parameter,"service");
        version=CommUtil.getJsonValue(parameter,"version");
        
        ThreadCacheData threadCacheData= new ThreadCacheData();
		threadCacheData.seqNo=seqNo;
		threadCacheData.sessionId= request.getSession().getId();
		
		System.out.println(request.getSession().getId());
		ThreadCacheUtil.setThreadLocalData(threadCacheData);
		
        Thread.currentThread().setName(service+":"+version+":"+seqNo);
        logger.info("1.接收到请求数据[{}]",parameter);
        String result=dubboClient.send(parameter, seqNo, service, version);
        
        ThreadCacheUtil.cleanThreadCacheData();
		endTime = System.currentTimeMillis();
		logger.info("2.响应数据[{}毫秒][{}]",endTime-beginTime,result);
        return result;
	}
	
    /**
     * 获取JSON格式的请求参数
     * @param request
     * @return
     */
	private String getRequestParameter(HttpServletRequest request){
		String parameter="";
		if(request.getMethod().equals(RequestMethod.POST.name())){
			try {
				parameter=getPostData(request);
			} catch (Exception e) {
				logger.info("获取POST数据报错",e);
			}
		}else{
			Map<String, String> map = getParameter(request);
			parameter=JSON.toJSONString(map);
		}
		return parameter;
	}
    /**
     * 获取GET的参数
     * @param request
     * @return
     */
	private  Map<String, String> getParameter(HttpServletRequest request) {
		Map<String, String> parameterMap = new HashMap<String, String>();

		Iterator<String> iterator = request.getParameterMap().keySet().iterator();
		while (iterator.hasNext()) {
			String key = iterator.next().toString();
			String val = request.getParameter(key);
			parameterMap.put(key, val);
		}
		return parameterMap;
	}

    /**
     * 获取POST的请求参数
     * @param request
     * @return
     * @throws Exception
     */
	private  String getPostData(HttpServletRequest request)throws Exception {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		InputStream is = request.getInputStream();
		int read = 1;
		while (read > 0) {
			byte[] bb = new byte[1024];
			read = is.read(bb);
			if (read > 0) {
				outputStream.write(bb, 0, read);
			}
		}
		byte[] bb = outputStream.toByteArray();
		outputStream.close();
		return new String(bb, "UTF-8");
	}
}
