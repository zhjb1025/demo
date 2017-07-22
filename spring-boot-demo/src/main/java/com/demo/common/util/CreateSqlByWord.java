package com.demo.common.util;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

public class CreateSqlByWord{
	public static void main(String[] args)  {
//		Scanner  read= new Scanner(System.in);
//		if(args==null ||args.length<2){
//			System.out.println("参数1：createTableSql | createQuerySql | createReportSql ");
//			System.out.println("参数2：文件路径");
//			System.out.println("参数3：[表名|查询ID|报表ID] --可空");
//			System.out.println("请输入参数不对");
//			read.nextLine();
//			return ;
//		}
		CreateSqlByWord temp= new CreateSqlByWord();
//		String path=args[1];
//		String table_name=null;
//		if(args.length==3){
//			table_name=args[2];
//		}
//		if(args[0].equals("createTableSql")){
//			temp.createTableSql(path, table_name);
//		}
		temp.createTableSql("E:/work/数据库设计.docx");
		
	}
	
	
	public  void createTableSql(String path) {
		createTableSql(path,null);
	}
	
	public  void createTableSql(String path,String table_name){
		try{
		     FileInputStream in = new FileInputStream(path);//载入文档
		     XWPFDocument hwpf = new XWPFDocument(in);  
		     Iterator<XWPFTable> it = hwpf.getTables().iterator();
		     //迭代文档中的表格
		     while (it.hasNext()) {  
		    	 XWPFTable tb = (XWPFTable) it.next();  
		         
		         if(tb.getRow(0).getCell(0).getText().indexOf("中文表名")<0){
		        	 continue;
		         }
		         //每个数据库表
		         //中文名字
		         String tableName=tb.getRow(0).getCell(1).getText();
		         //英文名字
		         String tableNameEn=tb.getRow(1).getCell(1).getText().trim();
		         if(table_name!=null && !tableNameEn.equals(table_name)){
		        	 continue;
		         }
		         //主键
//		         String pk=tb.getRow(2).getCell(1).getText();
		         
		         //索引
		         String [] index=tb.getRow(2).getCell(1).getText().trim().toLowerCase().split(",");
		         
		         //表空间
//		         String [] ts=tb.getRow(5).getCell(1).getText().trim().split("/");
		         
		         //外键
//		         String [] fk=tb.getRow(6).getCell(1).getText().split("[\\)]");
		         
		         StringBuilder sb= new StringBuilder();
	            
	             sb.append("drop table if exists ").append(tableNameEn).append(" ;\n");
	             sb.append("create table ").append(tableNameEn).append("\n");
	             sb.append("(\n");
		         
	             List<Field> list=new ArrayList<Field>();
	             int rowNum=tb.getRows().size();
	             for (int i = 4; i < rowNum; i++) { 
		             XWPFTableRow tr = tb.getRow(i);  
		             
		             String cellName = tr.getCell(1).getText();// 中文名称
		             if(cellName.length()==0){
		            	 break;
		             }
		             Field field = new Field();
		             field.name=cellName;
		             
		             field.nameEn= tr.getCell(2).getText().toLowerCase();// 英文名称
		             field.nameEn=CommUtil.fill(field.nameEn, ' ', 32, 'R');
		             
		             field.type= tr.getCell(3).getText();// 类型
		             
		             field.length= tr.getCell(4).getText();// 长度
		             
		             field.precision= tr.getCell(5).getText();// 精度
		             
		             field.notNull= tr.getCell(6).getText();// 非空
		             field.isPk = tr.getCell(7).getText();//  是否主键
		             String remake = tr.getCell(8).getText();// 说明
		             if(remake.length()>0 && remake.indexOf("HYPERLINK")>0){
		            	 remake="";
		             }
		             field.remake=remake.replaceAll("[\\t\\n\\r]", "#");
		             list.add(field);
		             
		         }   //end for
	             
	             
	             for(int k=0;k< list.size();k++){
	            	 sb.append("\t").append(list.get(k).nameEn);
		             if("DATETIME".equals(list.get(k).type.toUpperCase())){
		            	 sb.append(CommUtil.fill(list.get(k).type, ' ', 20, 'R'));
		             }else{
		            	 String type_=list.get(k).type+"("+list.get(k).length;
		            	 if(list.get(k).precision.trim().length()>0){
		            		 type_=type_+","+list.get(k).precision;
			             }
		            	 sb.append(CommUtil.fill(type_+")", ' ', 20, 'R'));
		             }
		             if("是".equals(list.get(k).isPk)){
		            	 sb.append(" primary key   auto_increment  ");
		             }
		             if("是".equals(list.get(k).notNull)){
		            	 sb.append("not null ");
		             }else{
		            	 sb.append("         ");
		             }
		             sb.append(" comment '").append(list.get(k).name).append(" ").append(list.get(k).remake).append("' ");
		             if(k!=list.size()-1){
		            	 sb.append(" ,");
		             }else{
		            	 sb.append("  ");
		             }
		                      
		             sb.append("\n");
	             }
	             sb.append(") ENGINE=InnoDB DEFAULT CHARSET=utf8").append(";\n");
	             
	             //ALTER TABLE test_data COMMENT='存放测试用例相关数据';
	             sb.append("alter table ").append(tableNameEn).append("  COMMENT='").append(tableName).append("' ;\n");
	            
	             
	             //创建索引
	             for(int k=0;k< index.length;k++){
	            	 if(index[k].trim().length()==0){
	            		 continue;
	            	 }
	            	 //create index I_OPERATELOGINFO_0 on OPERATE_LOG_INFO(EXCH_DATE)  tablespace TS_GESS_IDX_01 ;
	            	 sb.append("alter table ").append(tableNameEn).append(" add index ").append(tableNameEn+"_"+index[k].trim()).append(" (").append(index[k].trim()).append(");\n");
	             }
	             
	             System.out.println(sb.toString());
	             
		     } //end while
		  }catch(Exception e){
		   e.printStackTrace();
		  }
		
	}
	
}
	class Field {
		String name;
		String nameEn;
		String type;
		String length;
		String precision;
		String notNull;
		String isPk;
		String remake;
		String viewOrder;
		String defValue;
		String paras;
		String srcURL;
	}


