package com.demo.controller.msg;

public class QueryProductRequest extends BaseRequest{

    private String type;
    
    private int size;
    
    
	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
    
   
}
