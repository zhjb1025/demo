package com.demo.framework.util;


import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;

import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Http客户端工具类
 * @author benny
 *
 */
public class HttpUtils {
    private static Logger log = LoggerFactory.getLogger(HttpUtils.class); 
	//默认编码
	private static final String CHARSET = "UTF-8";
	
	//默认连接超时时间
	private static final int connectTimeout =1000*10 ;
	
	
	//默认读数据超时时间
	private static final int readTimeout =1000*30 ;
	
	private static final String[] SUPPORT_PROTOCOLS = {"TLSv1"};
	/**
	 * GET 调用
	 * @param url
	 * @return
	 * @throws SocketTimeoutException
	 * @throws Exception
	 */
	public static String get(String url) throws SocketTimeoutException, Exception{
		return get(url,new HashMap<String, String>(),readTimeout);
	}
	
	/**
	 * GET 调用
	 * @param url
	 * @param header
	 * @return
	 * @throws SocketTimeoutException
	 * @throws Exception
	 */
	public static String get(String url,Map<String,String> header) throws SocketTimeoutException, Exception{
		return get(url,header,readTimeout);
	}
	/**
	 * GET 调用
	 * @param url
	 * @param timeout
	 * @return
	 * @throws SocketTimeoutException
	 * @throws Exception
	 */
	public static String get(String url,int timeout) throws SocketTimeoutException, Exception{
		return get(url, new HashMap<String, String>(), timeout);
	}
	
	/**
	 * GET 调用
	 * @param url
	 * @param header
	 * @param timeout
	 * @return
	 * @throws SocketTimeoutException
	 * @throws Exception
	 */
	public static String get(String url,Map<String,String> header,int timeout) throws SocketTimeoutException,Exception{
	    log.info("请求url=" + url);
		String rspMsg=null;
		RequestConfig config = RequestConfig.custom().setConnectTimeout(connectTimeout).setSocketTimeout(timeout).build();
		HttpGet httpGet = new HttpGet(url);
		CloseableHttpClient httpClient=null;
		try {
			if(url.toLowerCase().startsWith("https")){
				httpClient= createSSLClientDefault();
			}else{
				httpClient=HttpClientBuilder.create().setDefaultRequestConfig(config).build();
			}
			
			if(header!=null&&header.size()>0){
				Iterator<Entry<String, String>> iter = header.entrySet().iterator();
				while(iter.hasNext()){
					Entry<String, String> h = iter.next();
					httpGet.addHeader(h.getKey(), h.getValue());
				}
			}
			
			CloseableHttpResponse response = httpClient.execute(httpGet);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != 200) {
				httpGet.abort();
				throw new RuntimeException("HttpClient,error status code :" + statusCode);
			}
			
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				rspMsg = EntityUtils.toString(entity, CHARSET);
				log.info("响应数据：" + rspMsg);
			}
			EntityUtils.consume(entity);
			response.close();
		} finally{
			httpClient.close();
		}
		return rspMsg;
	}
	
	/**
	 * Post 调用
	 * @param url
	 * @return
	 * @throws SocketTimeoutException
	 * @throws Exception
	 */
	public static String post(String url,String data) throws SocketTimeoutException, Exception{
		return post(url,data,null,readTimeout);
	}
	
	/**
	 * Post 调用
	 * @param url
	 * @param header
	 * @return
	 * @throws SocketTimeoutException
	 * @throws Exception
	 */
	public static String post(String url,String data,Map<String,String> header) throws SocketTimeoutException, Exception{
		return post(url,data,header,readTimeout);
	}
	/**
	 * Post 调用
	 * @param url
	 * @param timeout
	 * @return
	 * @throws SocketTimeoutException
	 * @throws Exception
	 */
	public static String get(String url,String data,int timeout) throws SocketTimeoutException, Exception{
		return post(url,data,null,timeout);
	}
	/**
	 * Post 调用
	 * @param url
	 * @param data
	 * @param header
	 * @param timeout
	 * @return
	 * @throws SocketTimeoutException
	 * @throws Exception
	 */
	public static String post(String url,String data,Map<String,String> header,int timeout) throws SocketTimeoutException,Exception{
	    log.info("请求url=" + url);
	    log.info("请求数据=" + data);
		String rspMsg=null;
		RequestConfig config = RequestConfig.custom().setConnectTimeout(connectTimeout).setSocketTimeout(timeout).build();
		HttpPost httpPost = new HttpPost(url);
		CloseableHttpClient httpClient=null;
		try {
			if(url.toLowerCase().startsWith("https")){
				httpClient= createSSLClientDefault();
			}else{
				httpClient=HttpClientBuilder.create().setDefaultRequestConfig(config).build();
			}
			
			if(header!=null){
				Iterator<Entry<String, String>> iter = header.entrySet().iterator();
				while(iter.hasNext()){
					Entry<String, String> h = iter.next();
					httpPost.addHeader(h.getKey(), h.getValue());
				}
			}
			httpPost.setEntity(new StringEntity(data, CHARSET));  
			CloseableHttpResponse response = httpClient.execute(httpPost);
			int statusCode = response.getStatusLine().getStatusCode();
			HttpEntity entity = response.getEntity();
			
			if (statusCode != 200) {
				if(entity!=null){
					rspMsg = EntityUtils.toString(entity, CHARSET);
					log.info("响应数据：" + rspMsg);
				}
				httpPost.abort();
				throw new RuntimeException("HttpClient,error status code :" + statusCode+",rspMsg:");
			}
			
			if (entity != null) {
				rspMsg = EntityUtils.toString(entity, CHARSET);
				log.info("响应数据：" + rspMsg);
			}
			EntityUtils.consume(entity);
			response.close();
		} finally{
			httpClient.close();
		}
		return rspMsg;
	}
	
	
	public static CloseableHttpClient createSSLClientDefault() throws Exception {
		SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(
				null, new TrustStrategy() {
					@Override
					public boolean isTrusted(
							java.security.cert.X509Certificate[] chain,
							String authType)
							throws java.security.cert.CertificateException {
						return true;
					}
				}).build();
		
		SSLConnectionSocketFactory factory = new SSLConnectionSocketFactory(sslContext, SUPPORT_PROTOCOLS, null, new HostnameVerifier() {
			public boolean verify(String hostname, SSLSession session) {
				return true;
			}
		});
		return HttpClients.custom().setSSLSocketFactory(factory).build();

	}
}