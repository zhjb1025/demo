package com.demo.framework.validate;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.demo.framework.annotation.StartEndTime;
import com.demo.framework.util.CommUtil;

public class StartEndTimeValidator implements ConstraintValidator<StartEndTime, Object> {
	private String format;
	private String startTimeField;
	private String endTimeField;
	@Override
	public void initialize(StartEndTime constraintAnnotation) {
		format=constraintAnnotation.format();
		startTimeField=constraintAnnotation.startTimeField();
		endTimeField=constraintAnnotation.endTimeField();
		
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
			return endTimeDate.getTime()>=startTimeDate.getTime();
			
		} catch (Exception e) {
			return false;
		}
	}

}
