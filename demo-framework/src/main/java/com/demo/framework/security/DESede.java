package com.demo.framework.security;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;


/** 对称加密算法
 * 3重des加密
 */
public final class DESede {
	
	
	/** 加密算法 */
	private static final String DESEDE = "DESede";
	
	/** 算法的转换名称 */
	private static final String DES_CBC = "DESede/CBC/PKCS5Padding";
	
	private static final String DEFAULT_KEY = "240262447423713749922240";
	/** 默认向量值 */
	private static String DEFAULT_DESEDE_IV = "2eS_#dw2";
	
	/**
	 * 使用3Des算法进行明文加密
	 * @param key
	 * @param iv
	 * @param value
	 * @return
	 * @throws Exception
	 */
	public static String encrypt(String key, String iv, String value) throws Exception{	
		SecureRandom sr = new SecureRandom();
		SecretKey securekey = new SecretKeySpec(key.getBytes(), DESEDE);
		IvParameterSpec ivParameterSpec = new IvParameterSpec(iv.getBytes());
		Cipher cipher = Cipher.getInstance(DES_CBC);
		cipher.init(Cipher.ENCRYPT_MODE, securekey, ivParameterSpec, sr);
		byte[] temp = cipher.doFinal(value.getBytes());
		return new String(Base64.encodeBase64(temp));  
	}
	/**
	 * 默认加密
	 * @param value
	 * @return
	 * @throws Exception
	 */
	public static String encrypt(String value) throws Exception{	
		return encrypt(DEFAULT_KEY,DEFAULT_DESEDE_IV,value);
	}
	
	/**
	 * 解密
	 * @param key
	 * @param iv
	 * @param value
	 * @return
	 * @throws Exception
	 */
	public static String decrypt(String key, String iv, String value) throws Exception {
		byte[] bytes = Base64.decodeBase64(value);  
		SecureRandom sr = new SecureRandom();
		SecretKey securekey = new SecretKeySpec(key.getBytes(), DESEDE);
		IvParameterSpec ivParameterSpec = new IvParameterSpec(iv.getBytes());
		Cipher cipher = Cipher.getInstance(DES_CBC);
		cipher.init(Cipher.DECRYPT_MODE, securekey, ivParameterSpec, sr);
		byte[] temp = cipher.doFinal(bytes);
		return new String(temp);
	}
	
	/**
	 * 默认解密
	 * @param value
	 * @return
	 * @throws Exception
	 */
	public static String decrypt(String value) throws Exception {
		return decrypt(DEFAULT_KEY,DEFAULT_DESEDE_IV,value);
	}
	
	/**
	 * 加密小工具
	 * @param args
	 */
	public static void main(String[] args) throws Exception{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("###############################################");
		System.out.println("##################  加密小工具           ###############");
		System.out.print  ("### 请输入密码原文：");

		String srcPwd = "";
		while ( true )
		{
			srcPwd = br.readLine();
			if ( srcPwd != null && srcPwd .length() > 0 )
				break;
			else
				System.out.print  ("### 请输入密码原文：");
		}
		
		String pwd =encrypt(srcPwd);
		System.out.println("### 加密后密文：{" + pwd + "}");
		System.out.println("### 解密后明文：{" + decrypt(pwd) + "}");
		System.out.println("###############################################");
		
	}
}
