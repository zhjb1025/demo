package com.demo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface IndexMapper {
	
	
	@Select("SELECT c.id,c.address,c.content,c.factory_address as factoryAddress ,c.factory_fax as factoryFax, " + 
	 		"c.factory_man as factoryMan ,c.factory_phone as factoryPhone,c.fax,c.mail,c.man,c.name,c.phone,c.post_code as postCode, " + 
	 		"c.summary " + 
	 		"from config c where id=1")
	public Config getConfig();
	
	@Select("select p.id,p.title,pf.name,t.name as typeName,t.info from product p " + 
			"LEFT JOIN product_file pf on p.id=pf.product "+ 
			"LEFT JOIN type_ t on t.id=p.produt_type " + 
			"where p.type=#{type,jdbcType=VARCHAR} " + 
			"order by p.id desc")
	public List<Product> getProduct(@Param("type") String type);
	
	
	@Select("select j.id,j.education,j.end_date as endDate,j.job_requirements as jobRequirements,j.job_responsibilities as jobResponsibilities, " + 
			"j.number,j.post,j.title,j.work_year as workYear " + 
			"from  job_info j where is_issue=1")
	public List<JobInfo> getJob();
	
	@Select("select j.id,j.education,j.end_date as endDate,j.job_requirements as jobRequirements,j.job_responsibilities as jobResponsibilities, " + 
			"j.number,j.post,j.title,j.work_year as workYear " + 
			"from  job_info j where is_issue=1 and id=#{id,jdbcType=INTEGER}")
	public List<JobInfo> getJobByID(@Param("id")  long id);
	
}
