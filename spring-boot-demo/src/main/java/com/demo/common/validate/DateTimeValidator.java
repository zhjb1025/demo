package com.demo.common.validate;

import com.demo.common.annotation.DateTime;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

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
