package com.demo.gateway.controller;

import java.io.Serializable;

public class Test implements Serializable {
	private static final long serialVersionUID = 984426544423879634L;
	private int id=1;
	private String name="张三";
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Override
	public String toString() {
		return "Test [id=" + id + ", name=" + name + "]";
	}
	
	
}
