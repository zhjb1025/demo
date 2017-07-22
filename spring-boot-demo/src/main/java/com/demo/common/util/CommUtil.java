package com.demo.common.util;

import org.apache.log4j.Logger;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommUtil {
	
	
	private static final Logger logger = Logger.getLogger(CommUtil.class);
	
	
	
	private static String DATE_FORMAT_YYYYMMDD = "yyyyMMdd";
	
	private static String DATE_FORMAT_YYYY_MM_DD ="yyyy-MM-dd";
	
	private static String DATE_FORMAT_YYYY_MM_DD_HMS ="yyyy-MM-dd HH:mm:ss";

	private static String DATE_FORMAT_YYYYMMDDHMS ="yyyyMMddHHmmss";
	
	
	 
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
		return new SimpleDateFormat(DATE_FORMAT_YYYYMMDD).format(new Date());
	}
	
	/**
	 * 获取当前时间
	 * @return
	 */
	public static String getDateYYYY_MM_DD(){
		return new SimpleDateFormat(DATE_FORMAT_YYYY_MM_DD).format(new Date());
	}
	
	/**
	 * 获取当前时间
	 * @return
	 */
	public static String getDateYYYY_MM_DD_HMS(){
		return new SimpleDateFormat(DATE_FORMAT_YYYY_MM_DD_HMS).format(new Date());
	}

	/**
	 * 获取当前时间
	 * @return
	 */
	public static String getDateFormatYYYYMMDDHMS(){
		return new SimpleDateFormat(DATE_FORMAT_YYYYMMDDHMS).format(new Date());
	}

	/**
	 * 格式化时间
	 * @param date
	 * @param format
	 * @return
	 */
	public static String dateFormat(Date date,String format){
		return new SimpleDateFormat(format).format(date);
	}

    /**
     * json格式数据脱敏
     * @param src
     * @param fieldList
     * @return
     */
    public static String desensitizationForJson(String src,List<String> fieldList){
        if(fieldList!=null&&fieldList.size()>0){
            try {
                for (String sensitiveLabel : fieldList) {
                    Pattern jsonp = Pattern.compile("(\"" + sensitiveLabel
                            + "\")([^,}]*)(,|})",Pattern.CASE_INSENSITIVE);
                    Matcher jsonm = jsonp.matcher(src);
                    if (jsonm.find()) {
                        int start = jsonm.start();
                        int end = jsonm.end() - 1;
                        src = src.replace(src.substring(start, end), "\""
                                + sensitiveLabel + "\"" + ":\"******\"");
                    }
                    String regXML = "(\\<"+sensitiveLabel+"\\>)([^\\<]*)(\\<\\/"+sensitiveLabel+"\\>)";
                    src = src.replaceAll(regXML, "$1******$3");
                }
            } catch (Exception e) {
                logger.error("对请求，响应消息体进行脱敏错误",e);
            }
        }else{
            return src;
        }
        return src;
    }


    /**
     * 从JSON获取变量值，如果有多个key 只获取第一个
     * @param json
     * @param key
     * @return
     */
    public static String getJsonValue(String json,String key) {
        char separatorValue=':';
        if ( json == null || json.length() <= 0 )
            return null;
        if ( key == null || key.length() <= 0 )
            return null;
        key="\""+key+"\"";
        int jsonLength = json.length();
        int keyLen = key.length();
        if ( jsonLength > 0 && keyLen > 0 ) {
            int startIndex = 0;
            while ( true ){
                startIndex = json.indexOf(key,startIndex);
                if ( startIndex <= -1 )
                    break;
                int endIndex=json.indexOf(",",startIndex);
                if(endIndex<0){
                    endIndex=json.indexOf("}",startIndex);
                }
                if(endIndex<0){
                    break;
                }
                return json.substring(startIndex+keyLen+1,endIndex).replaceAll("\"","").trim();
            }

        }
        return null;
    }
}
