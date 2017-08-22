package com.demo.framework.validate;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.demo.framework.annotation.DateTime;

public class DateTimeValidator implements ConstraintValidator<DateTime, String> {
	private String format;
	@Override
	public void initialize(DateTime constraintAnnotation) {
		format=constraintAnnotation.format();
		
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if(value==null){
			return true;
		}
		try {
			SimpleDateFormat sdf= new SimpleDateFormat(format);
			Date date = sdf.parse(value);
			String dateStr=sdf.format(date);
			if(dateStr.equals(value)){
				return true;
			}
			
		} catch (Exception e) {
			return false;
		}
		return false;
	}

}
