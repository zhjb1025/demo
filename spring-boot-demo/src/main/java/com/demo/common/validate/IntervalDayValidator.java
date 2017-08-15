package com.demo.common.validate;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.demo.common.annotation.IntervalDay;
import com.demo.common.util.CommUtil;

public class IntervalDayValidator implements ConstraintValidator<IntervalDay, Object> {
	private String format;
	private String startTimeField;
	private String endTimeField;
	private int interval;
	@Override
	public void initialize(IntervalDay constraintAnnotation) {
		format=constraintAnnotation.format();
		startTimeField=constraintAnnotation.startTimeField();
		endTimeField=constraintAnnotation.endTimeField();
		interval=constraintAnnotation.interval();
		
	}

	@Override
	public boolean isValid(Object value, ConstraintValidatorContext context) {
		if(value==null){
			return true;
		}
		try {
			SimpleDateFormat sdf= new SimpleDateFormat(format);
			Object startTime = CommUtil.getFieldValue(value, startTimeField);
			if(startTime==null){
				return false;
			}
			Object endTime = CommUtil.getFieldValue(value, endTimeField);
			if(endTime==null){
				return false;
			}
			Date startTimeDate = sdf.parse(startTime.toString());
			Date endTimeDate = sdf.parse(endTime.toString());
			
			return ((endTimeDate.getTime()-startTimeDate.getTime())/1000/60/60/24)<=interval;
			
		} catch (Exception e) {
			return false;
		}
	}

}
