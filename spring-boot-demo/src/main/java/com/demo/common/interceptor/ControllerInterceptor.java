package com.demo.common.interceptor;

import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.demo.controller.msg.BaseRequest;
import com.demo.controller.msg.BaseResponse;
import com.demo.framework.enums.ErrorCodeEnum;
import com.demo.framework.enums.TradeStatusEnum;
import com.demo.framework.exception.CommException;


//@Aspect  
//@Component 
public class ControllerInterceptor {
	
	 private static Logger log = LoggerFactory.getLogger(ControllerInterceptor.class);  

	/** 
     * 定义拦截规则：拦截com.demo.controller包下面的所有类中所有的方法。 
     */  
	@Pointcut("execution(* com.demo.controller.*.*(..))")
    public void controllerMethodPointcut(){
    	log.info("controllerMethodPointcut");
    }
    
    /** 
     * 拦截器具体实现 
     * @param pjp 
     * @return JsonResult（被拦截方法的执行结果，或需要登录的错误提示。） 
     * @throws Throwable 
     */  
    @Around("controllerMethodPointcut()") //指定拦截器规则；也可以直接把“execution(* com.xjj.........)”写进这里  
    public Object Interceptor(ProceedingJoinPoint pjp) throws Throwable{
    	
    	long beginTime = System.currentTimeMillis();  
        MethodSignature signature = (MethodSignature) pjp.getSignature();  
        Method method = signature.getMethod(); //获取被拦截的方法  
         
        String methodName = method.getName(); //获取被拦截的方法名  
         
        Object[] args = pjp.getArgs();  
        BaseRequest baseRequest=null;
        log.info("开始调用{}.{}",pjp.getTarget().getClass().getName(),methodName);
        if(args!=null && args.length>0){
        	for(Object arg: args){
        	 if(arg instanceof BaseRequest){
        		 baseRequest=(BaseRequest)arg;
        	 }
        	 log.info("请求参数[{}]",arg);
        	}
        }
        Object rsp=null;
        long endTime=beginTime;
        String seqNo=null;
        if(baseRequest!=null && baseRequest.getSeqNo()!=null){
        	seqNo=baseRequest.getSeqNo();
        }
        try {
        	 rsp = pjp.proceed();
        	 
		}catch (CommException e) {
			log.info("业务处理异常",e);
			BaseResponse baseResponse= new BaseResponse();
			baseResponse.setRspCode(e.getErrCode());
			baseResponse.setRspMsg(e.getErrMsg());
			baseResponse.setSeqNo(seqNo);
			baseResponse.setTradeStatus(TradeStatusEnum.FAIL.getTradeStatus());
			rsp=baseResponse;
		}catch (Throwable e) {
			log.info("系统异常",e);
			BaseResponse baseResponse= new BaseResponse();
			baseResponse.setRspCode(ErrorCodeEnum.SYSTEM_FAIL.getCode());
			baseResponse.setRspMsg(ErrorCodeEnum.SYSTEM_FAIL.getMsg());
			baseResponse.setSeqNo(seqNo);
			baseResponse.setTradeStatus(TradeStatusEnum.FAIL.getTradeStatus());
			rsp=baseResponse;
		}finally{
			endTime = System.currentTimeMillis();  
			log.info("结束调用{}.{} [{}]",pjp.getTarget().getClass().getName(),methodName,endTime-beginTime);
			log.info("响应数据[{}]",rsp);
		}
         
		return  rsp;
    	
    }
    
    
    
}
