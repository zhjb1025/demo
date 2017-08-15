package com.demo.common.annotation;

import com.demo.common.validate.SameYearValidator;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = { SameYearValidator.class })
public @interface SameYear {
	String format() default "yyyy-MM-dd HH:mm:ss";
	
	String message() default "查时间段不能跨年";
	String startTimeField();
	String endTimeField();
	Class<?>[] groups() default { };
	 
	Class<? extends Payload>[] payload() default { };
	
}
