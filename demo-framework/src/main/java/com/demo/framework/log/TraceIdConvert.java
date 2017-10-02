package com.demo.framework.log;

import com.demo.framework.util.ThreadCacheUtil;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;

public class TraceIdConvert extends ClassicConverter {

	@Override
	public String convert(ILoggingEvent event) {
		if(ThreadCacheUtil.getThreadLocalData()==null) {
			return "";
		}
		String traceId = ThreadCacheUtil.getThreadLocalData().traceId;
		if(traceId==null){
			return  "";
		}
		return traceId;
	}

}
