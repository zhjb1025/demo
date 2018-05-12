package com.demo.framework.validate;

import java.util.Iterator;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.demo.framework.exception.CommException;
import com.demo.framework.exception.FrameworkErrorCode;

@Component
public class ValidatorService {
	@Autowired
	private Validator validator;
	
	public <T> void validate(T object, Class<?>... groups) throws CommException{
		 Set<ConstraintViolation<T>> validate = validator.validate(object);
	     Iterator<ConstraintViolation<T>> iter = validate.iterator();
	     StringBuilder sb= new StringBuilder();
	     while(iter.hasNext()){
	    	 sb.append(iter.next().getMessage()).append(",");
	     }
	     if(sb.length()>0){ 
	    	 throw new CommException(FrameworkErrorCode.VALIDATE_FAIL,sb.substring(0, sb.length()-1));
	     }
	}

	public interface AuthGroup{}

	public interface NotAuthGroup{}
}
