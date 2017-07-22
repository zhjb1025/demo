package com.demo.common.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.sql.Clob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

public class CommUtil {
	
	
	private static final Logger logger = Logger.getLogger(CommUtil.class);
	
	
	
	private static SimpleDateFormat sdfYYYYMMDD = new SimpleDateFormat("yyyyMMdd");
	
	private static SimpleDateFormat sdfYYYY_MM_DD = new SimpleDateFormat("yyyy-MM-dd");
	
	private static SimpleDateFormat sdfYYYY_MM_DD_HMS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	
	 
	/**
	 * 获取异常堆栈信息
	 * @param e
	 * @return
	 */
	public static String getExpStack(Exception e) {
		ByteArrayOutputStream bo = new ByteArrayOutputStream();
		PrintWriter pw = new PrintWriter(bo);
		e.printStackTrace(pw);
		pw.flush();
		pw.close();
		return bo.toString();
	}
	/**
	 * 循环向上转型, 获取对象的DeclaredField.
	 * 
	 * 如向上转型到Object仍无法找到, 返回null.
	 */
	public static Field getDeclaredField(final Object object, final String fieldName) {
		for (Class<?> superClass = object.getClass(); superClass != Object.class; superClass = superClass
				.getSuperclass()) {
			try {
				return superClass.getDeclaredField(fieldName);
			} catch (NoSuchFieldException e) {//NOSONAR
				// Field不在当前类定义,继续向上转型
			}
		}
		return null;
	}
	/**
	 * 直接设置对象属性值, 无视private/protected修饰符, 不经过setter函数.
	 */
	public static void setFieldValue(final Object object, final String fieldName, final Object value) {
		Field field = getDeclaredField(object, fieldName);

		if (field == null) {
			throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + object + "]");
		}
		setFieldValue(object,field,value);
	}
	
	/**
	 * 直接设置对象属性值, 无视private/protected修饰符, 不经过setter函数.
	 */
	public static void setFieldValue(final Object object, final Field field, final Object value) {
		makeAccessible(field);
		try {
			field.set(object, value);
		} catch (IllegalAccessException e) {
			logger.error("", e);
		}
	}
	/**
	 * 直接读取对象属性值, 无视private/protected修饰符, 不经过getter函数.
	 * @param object
	 * @param field
	 * @param fieldName
	 * @return
	 */
	public static Object getFieldValue(final Object object,Field field) {
		makeAccessible(field);

		Object result = null;
		try {
			result = field.get(object);
		} catch (IllegalAccessException e) {
			logger.error(getExpStack(e));
		}
		return result;
	}
	
	/**
	 * 直接读取对象属性值, 无视private/protected修饰符, 不经过getter函数.
	 * @param object
	 * @param fieldName
	 * @return
	 */
	public static Object getFieldValue(final Object object,String fieldName) {
		Field field = getDeclaredField(object, fieldName);
		if(field==null){
			return null;
		}
		return getFieldValue(object,field);
	}
	/**
	 * 强行设置Field可访问.
	 */
	public static void makeAccessible(final Field field) {
		if (!Modifier.isPublic(field.getModifiers()) || !Modifier.isPublic(field.getDeclaringClass().getModifiers())) {
			field.setAccessible(true);
		}
	}
	
	/**
	 * <p>方法名称: </p>
	 * <p>功能描述:字符串填充 </p>
	 * <p>参数介绍:需要填充的字符串;填充的字符;填充长度;左右标志 </p>
	 */
	public static String fill(String s, char c, int n, char f){
		int iByteLen = s.getBytes().length;
		if ( iByteLen  >= n) {
			return s;
		} else {			
			byte[] fillChars = new byte[n-iByteLen];
			for ( int i = 0 ; i < fillChars.length ; i++ )
				fillChars[i] = (byte)c;
			
			if ( f == 'L' ) {	//左补			
				return new String(fillChars) + s;
			}else {//右补
				return s + new String(fillChars) ;
			}
			
		}
	}
	
	
    @SuppressWarnings("unchecked")
	public static   <T> T getClassInstance(String clazz) throws Exception {
		return (T) Class.forName(clazz).newInstance();
	}
    
    
    public static boolean isEmpty(String value){
    	if(value==null||value.trim().length()==0){
    		return true;
    	}else{
    		return false;
    	}
    }
    
	
	/**
	 * 获取当前时间
	 * @return
	 */
	public static String getDateYYYYMMDD(){
		return sdfYYYYMMDD.format(new Date());
	}
	
	/**
	 * 获取当前时间
	 * @return
	 */
	public static String getDateYYYY_MM_DD(){
		return sdfYYYY_MM_DD.format(new Date());
	}
	
	/**
	 * 获取当前时间
	 * @return
	 */
	public static String getDateYYYY_MM_DD_HMS(){
		return sdfYYYY_MM_DD_HMS.format(new Date());
	}
	

	public static int getNvlInt(ResultSet rs, int columnIndex)
			throws SQLException {
		int tmpInt = rs.getInt(columnIndex);
		return tmpInt;
	}

	public static int getNvlInt(ResultSet rs, String columnName)
			throws SQLException {
		int tmpInt = rs.getInt(columnName);
		return tmpInt;
	}

	public static long getNvlLong(ResultSet rs, int columnIndex)
			throws SQLException {
		long tmpLong = rs.getLong(columnIndex);
		return tmpLong;
	}

	public static long getNvlLong(ResultSet rs, String columnName)
			throws SQLException {
		long tmpLong = rs.getLong(columnName);
		return tmpLong;
	}

	public static double getNvlDouble(ResultSet rs, int columnIndex)
			throws SQLException {
		return rs.getDouble(columnIndex);
	}

	public static double getNvlDouble(ResultSet rs, String columnName)
			throws SQLException {
		return rs.getDouble(columnName);
	}
	
	public static String getNvlClob(ResultSet rs, String columnName)throws SQLException,IOException {
		StringBuffer sbResult = new StringBuffer();
		Clob clob = rs.getClob(columnName);
		BufferedReader brClob = null;
		try
		{
			brClob = new BufferedReader(clob.getCharacterStream());
			
			for(;;)
			{
				String line = brClob.readLine();
				if ( line == null )
					break;
				sbResult.append(line);
			}
		}finally{
			if ( brClob != null )
				brClob.close();
		}
		return sbResult.toString();
	}
	
	public static String getNvlClob(ResultSet rs, int columnIndex) throws SQLException,IOException{
		StringBuffer sbResult = new StringBuffer();
		Clob clob = rs.getClob(columnIndex);
		BufferedReader brClob = null;
		try
		{
			brClob = new BufferedReader(clob.getCharacterStream());
			
			for(;;)
			{
				String line = brClob.readLine();
				if ( line == null )
					break;
				sbResult.append(line);
			}
		}finally{
			if ( brClob != null )
				brClob.close();
		}
		return sbResult.toString();
	}

	public static BigDecimal getNvlMoney(ResultSet rs,int columnIndex )			throws SQLException 
	{
		BigDecimal bd_money = rs.getBigDecimal(columnIndex);
		if ( bd_money == null )
			return BigDecimal.ZERO;
		else
			return bd_money.setScale(2,BigDecimal.ROUND_HALF_UP);
	}
	
	public static BigDecimal getNvlMoney(ResultSet rs,String columnName )			throws SQLException 
	{
		BigDecimal bd_money = rs.getBigDecimal(columnName);
		if ( bd_money == null )
			return BigDecimal.ZERO;
		else
			return bd_money.setScale(2,BigDecimal.ROUND_HALF_UP);
	}
	
	public static String getNvlString(ResultSet rs, int columnIndex) 			throws SQLException {
		String tmpStr = rs.getString(columnIndex);
		if (tmpStr == null) {
			tmpStr = "";
		}
		return tmpStr.trim();
	}

	public static String getNvlString(ResultSet rs, String columnName) 			throws SQLException {
		String tmpStr = rs.getString(columnName);
		if (tmpStr == null) {
			tmpStr = "";
		}
		return tmpStr.trim();
	}
	
	public static boolean getNvlBoolean(ResultSet rs, int columnIndex) 			throws SQLException {
		String tmpStr = rs.getString(columnIndex);
		if ("1".equals(tmpStr)) {
			return true;
		}
		return false;
	}

	public static boolean getNvlBoolean(ResultSet rs, String columnName) 			throws SQLException {
		String tmpStr = rs.getString(columnName);
		if ("1".equals(tmpStr)) {
			return true;
		}
		return false;
	}
	public static byte[] getNvlBytes(ResultSet rs, int columnIndex)			throws SQLException {
		String str = rs.getString(columnIndex);
		if (str == null) {
			return "".getBytes();
		} else
			return str.getBytes();
	}

	public static byte[] getNvlBytes(ResultSet rs, String columnName)			throws SQLException {
		String str = rs.getString(columnName);
		if (str == null) {
			return "".getBytes();
		} else
			return str.getBytes();
	}

	public static char getNvlChar(ResultSet rs, int columnIndex)			throws SQLException {
		byte[] bytes = rs.getBytes(columnIndex);
		if (bytes == null || bytes.length <= 0) {
			return '\0';
		}
		return (char) bytes[0];
	}

	public static char getNvlChar(ResultSet rs, String columnName) 			throws SQLException {
		byte[] bytes = rs.getBytes(columnName);
		if (bytes == null || bytes.length <= 0 ) {
			return '\0';
		}
		return (char) bytes[0];
	}

	public static BigDecimal getNvlBigDecimalByPrecision(ResultSet rs,
			int columnIndex, int pre) throws SQLException {
		return getNvlBigDecimal(rs, columnIndex).setScale(pre,BigDecimal.ROUND_HALF_UP);
	}

	public static BigDecimal getNvlBigDecimalByPrecision(ResultSet rs,
			String columnName, int pre) throws SQLException {
		return getNvlBigDecimal(rs, columnName).setScale(pre,BigDecimal.ROUND_HALF_UP);
	}

	public static BigDecimal getNvlBigDecimal(ResultSet rs, int columnIndex) 			throws SQLException {
		BigDecimal tmpBigDecimal = rs.getBigDecimal(columnIndex);
		if (tmpBigDecimal == null) {
			tmpBigDecimal = new BigDecimal(0.00);
		}
		return tmpBigDecimal;
	}

	public static BigDecimal getNvlBigDecimal(ResultSet rs, String columnName) 			throws SQLException {
		BigDecimal tmpBigDecimal = rs.getBigDecimal(columnName);
		if (tmpBigDecimal == null) {
			tmpBigDecimal = new BigDecimal(0.00);
		}
		return tmpBigDecimal;
	}
	

	public static String getNvlDateString(ResultSet rs, int columnIndex) 			throws SQLException {
		java.sql.Timestamp date = rs.getTimestamp(columnIndex);
		if ( date != null )
		{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			return sdf.format(date);
		}else
		{
			return "";
		}
	}

	
	public static String getNvlDateString(ResultSet rs, String columnName) 			throws SQLException {
		java.sql.Timestamp date = rs.getTimestamp(columnName);
		if ( date != null )
		{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			return sdf.format(date);
		}else
		{
			return "";
		}
	}
}
