package com.demo.test;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.demo.framework.util.CommUtil;

/**
 * 基于JDBC 批量插入
 * @author Administrator
 *
 */
public class SqlBatchServeice {
	private  Logger logger = LoggerFactory.getLogger(this.getClass());
	
	/**
	 * 表名与sql的映射关系
	 */
	private Map<String,String> sqlMap=new HashMap<>();
	
	/**
	 * 字段与索引的映射关系
	 */
	private Map<String,Map<Field,Integer>> columnIndexMap=new HashMap<>();
	
	public void execute(Connection conn,List<Object> list) {
		String tableName=getTableName(list.get(0).getClass());
		execute(conn,tableName,list);
	}
	public void execute(Connection conn,String tableName,List<Object> list) {
		long start=System.currentTimeMillis();
		String sql=sqlMap.get(tableName);
		if(sql==null) {
			createInsertSql(tableName,list.get(0).getClass());
			sql=sqlMap.get(tableName);
		}
		Map<Field, Integer> columnMap = columnIndexMap.get(tableName);
		Field[] fields= new Field[columnMap.keySet().size()];
		columnMap.keySet().toArray(fields);
		PreparedStatement st=null;
		Object result=null;
		int index=0;
		try {
			conn.setAutoCommit(false);
			st= conn.prepareStatement(sql);
			
			for(Object obj:list) {
				for(Field f:fields) {
					result=CommUtil.getFieldValue(obj, f);
					if("java.util.Date".equals(f.getType().getName()) && result!=null) {
						java.sql.Timestamp date=new java.sql.Timestamp(((java.util.Date)result).getTime());
						st.setTimestamp(columnMap.get(f).intValue(), date);
						continue;
					}
					st.setObject(columnMap.get(f).intValue(), result);
				}
				st.addBatch();
				index++;
				if(index%1000==0) {
					st.executeBatch();
					conn.commit();
					logger.info("提交{}条数据",index);
				}
			}
			st.executeBatch();
			conn.commit();
			logger.info("提交{}条数据",index);
			st.close();
		} catch (SQLException e) {
			logger.info("批量插入报错",e);
			
		}
		logger.info("提交{}条数据,耗时:{}",index,System.currentTimeMillis()-start);
		
		
		
	}
	
	private String getTableName(Class<?> clzz) {
		return toUnderline(clzz.getSimpleName());
	}
	
	/**
	 * 驼峰命名法改成下划线
	 * @param name
	 * @return
	 */
	private String toUnderline(String name) {
		StringBuilder sb= new StringBuilder();
		for(int i=0;i<name.length();i++) {
			if(name.charAt(i)>='A' && name.charAt(i)<='Z') {
				if(i!=0) {
					sb.append("_");
				}
				sb.append((char)(name.charAt(i)+32));
			}else {
				sb.append(name.charAt(i));
			}
		}
		
		return sb.toString();
	}
	
	private void createInsertSql(String tableName,Class<?> clzz) {
		Map<Field,Integer> columnMap=new HashMap<>();
		StringBuilder fieldString= new StringBuilder();
		StringBuilder valuesString= new StringBuilder();
		fieldString.append("insert into ").append(tableName).append(" (");
		valuesString.append(" values (");
		Field[] fields = clzz.getDeclaredFields();
		String fieldName;
		for(int i=0;i<fields.length;i++) {
			fieldName=fields[i].getName();
			if("id".equals(fieldName)) {
				continue;
			}
			columnMap.put(fields[i], i);
			
			fieldName=toUnderline(fieldName);
			fieldString.append(fieldName).append(",");
			valuesString.append("?,");
			logger.info("批量插入字段类型{}={}",fields[i].getName(),fields[i].getType().getName());
			
//			System.out.println(fields[i].getName()+"="+fields[i].getType().getName());
		}
		String sql=fieldString.substring(0, fieldString.length()-1)+") ";
		
		sql=sql+valuesString.substring(0, valuesString.length()-1)+") ";
		logger.info("批量插入sql:{}",sql);
		sqlMap.put(tableName,sql);
		columnIndexMap.put(tableName, columnMap);
	}
}
