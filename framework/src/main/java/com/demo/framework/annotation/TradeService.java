package com.demo.framework.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TradeService {
	String value() default "";
	boolean isPublic() default false; //是否公有开放性接口
	boolean isLog() default true;  //是否记录日志
    boolean isAuth() default true; //是否进行权限控制
	String version() default ""; //
}
