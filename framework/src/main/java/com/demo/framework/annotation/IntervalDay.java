package com.demo.framework.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import com.demo.framework.validate.IntervalDayValidator;

@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = { IntervalDayValidator.class })
public @interface IntervalDay {
	String format() default "yyyy-MM-dd HH:mm:ss";
	
	String message() default "查询间隔时间不能超过 [{interval}]天";
	String startTimeField();
	int interval();
	String endTimeField();
	Class<?>[] groups() default { };
	 
	Class<? extends Payload>[] payload() default { };
	
}
