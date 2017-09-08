package com.demo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface IndexMapper {
	
	
	@Select("SELECT c.id,c.address,c.content,c.factory_address as factoryAddress ,c.factory_fax as factoryFax, " + 
	 		"c.factory_man as factoryMan ,c.factory_phone as factoryPhone,c.fax,c.mail,c.man,c.name,c.phone,c.post_code, " + 
	 		"c.summary " + 
	 		"from config c where id=1")
	public Config getConfig();
	
	@Select("select p.id,p.title,pf.name from product p " + 
			"LEFT JOIN product_file pf on p.id=pf.product " + 
			"where p.type=#{type,jdbcType=VARCHAR} " + 
			"order by p.id desc")
	public List<Product> getProduct(String type);
	
}
