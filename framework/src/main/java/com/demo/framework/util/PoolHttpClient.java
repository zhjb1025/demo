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
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PoolHttpClient {
	// 日志
	private static final Logger logger = LoggerFactory.getLogger(PoolHttpClient.class);
	/**
	 * 最大连接数400
	 */
	private static int DEFAULT_MAX_CONNECTION_NUM = 400;

	/**
	 * 单路由最大连接数80
	 */
	private static int DEFAULT_MAX_PER_ROUTE = 80;

	// 默认连接超时时间
	private static int connectTimeout = 1000 * 10;

	// 默认读数据超时时间
	private static int readTimeout = 1000 * 30;

	private static String[] supportProtocols = { "TLSv1" };

	private static String CHARSET = "UTF-8";

	/**
	 * 构造函数
	 */
	private PoolHttpClient() {
	}

	public static void setMaxTotal(int max) {
		DEFAULT_MAX_CONNECTION_NUM = max;
	}

	public static void setDefaultMaxPerRoute(int maxPerRoute) {
		DEFAULT_MAX_PER_ROUTE = maxPerRoute;
	}

	public static void setConnectTimeout(int connectTimeout) {
		PoolHttpClient.connectTimeout = connectTimeout;
	}

	public static void setReadTimeout(int readTimeout) {
		PoolHttpClient.readTimeout = readTimeout;
	}

	public static void setSupportProtocols(String[] supportProtocols) {
		PoolHttpClient.supportProtocols = supportProtocols;
	}

	public static void setCharset(String charset) {
		PoolHttpClient.CHARSET = charset;
	}

	private static Object LOCAL_LOCK = new Object();

	/**
	 * 连接池管理对象
	 */
	private static PoolingHttpClientConnectionManager cm = null;

	/**
	 * 
	 * 功能描述: <br>
	 * 初始化连接池管理对象
	 * 
	 * @throws @throws
	 *             Exception
	 */
	private static PoolingHttpClientConnectionManager getPoolManager() throws Exception {

		if (null == cm) {
			synchronized (LOCAL_LOCK) {
				if (null == cm) {
					logger.info("开始创建http连接池");
					SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
						@Override
						public boolean isTrusted(java.security.cert.X509Certificate[] chain, String authType)
								throws java.security.cert.CertificateException {
							return true;
						}
					}).build();

					SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(sslContext,
							supportProtocols, null, new HostnameVerifier() {
								public boolean verify(String hostname, SSLSession session) {
									return true;
								}
							});

					Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder
							.<ConnectionSocketFactory>create().register("https", socketFactory)
							.register("http", new PlainConnectionSocketFactory()).build();
					cm = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
					cm.setMaxTotal(DEFAULT_MAX_CONNECTION_NUM);
					cm.setDefaultMaxPerRoute(DEFAULT_MAX_PER_ROUTE);
					logger.info("创建http连接池结束");
				}
			}
		}
		return cm;
	}

	/**
	 * 创建线程安全的HttpClient
	 * 
	 * @param config
	 *            客户端超时设置
	 * 
	 * @return
	 * @throws Exception
	 */
	private static CloseableHttpClient getHttpClient(RequestConfig config) throws Exception {
		CloseableHttpClient httpClient = HttpClients.custom().setDefaultRequestConfig(config)
				.setConnectionManager(getPoolManager()).disableAutomaticRetries().build();
	
		return httpClient;
	}

	/**
	 * GET 调用
	 * @param url
	 * @return
	 * @throws SocketTimeoutException
	 * @throws ConnectTimeoutException
	 * @throws Exception
	 */
	public static String get(String url) throws SocketTimeoutException, ConnectTimeoutException,Exception {
		return get(url, new HashMap<String, String>(), readTimeout, null);
	}

	/**
	 * GET 调用
	 * @param url
	 * @param header
	 * @return
	 * @throws SocketTimeoutException
	 * @throws ConnectTimeoutException
	 * @throws Exception
	 */
	public static String get(String url, Map<String, String> header) throws SocketTimeoutException, ConnectTimeoutException,Exception {
		return get(url, header, readTimeout, CHARSET);
	}

	/**
	 * GET 调用
	 * @param url
	 * @param timeout
	 * @return
	 * @throws SocketTimeoutException
	 * @throws ConnectTimeoutException
	 * @throws Exception
	 */
	public static String get(String url, int timeout) throws SocketTimeoutException, ConnectTimeoutException,Exception {
		return get(url, new HashMap<String, String>(), timeout, CHARSET);
	}
	/**
	 * GET 调用
	 * @param url
	 * @param header
	 * @param timeout
	 * @return
	 * @throws SocketTimeoutException
	 * @throws ConnectTimeoutException
	 * @throws Exception
	 */
	public static String get(String url, Map<String, String> header,int timeout) throws SocketTimeoutException, ConnectTimeoutException,Exception {
		return get(url, header, timeout, CHARSET);
	}
	/**
	 * GET 调用
	 * @param url
	 * @param header
	 * @param timeout
	 * @param charset
	 * @return
	 * @throws SocketTimeoutException
	 * @throws ConnectTimeoutException
	 * @throws Exception
	 */
	public static String get(String url, Map<String, String> header, int timeout, String charset)
			throws SocketTimeoutException, ConnectTimeoutException, Exception {
		logger.info("请求url=" + url);
		String rspMsg = null;
		RequestConfig config = RequestConfig.custom().setConnectTimeout(connectTimeout).setSocketTimeout(timeout)
				.build();
		HttpGet httpGet = new HttpGet(url);
		CloseableHttpResponse response = null;
		try {
			if (header != null && header.size() > 0) {
				Iterator<Entry<String, String>> iter = header.entrySet().iterator();
				while (iter.hasNext()) {
					Entry<String, String> h = iter.next();
					httpGet.addHeader(h.getKey(), h.getValue());
				}
			}

			response = getHttpClient(config).execute(httpGet);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != 200) {
				httpGet.abort();
				throw new RuntimeException("HttpClient,error status code :" + statusCode);
			}

			HttpEntity entity = response.getEntity();
			if (entity != null) {
				rspMsg = EntityUtils.toString(entity, charset);
				logger.info("响应数据：" + rspMsg);
			}
			EntityUtils.consume(entity);

		} finally {
			if (httpGet != null) {
				httpGet.releaseConnection();
			}
			if (response != null) {
				try {
					EntityUtils.consume(response.getEntity());
					response.close();
				} catch (Exception e) {
					logger.info("", e);
				}
			}

		}
		return rspMsg;
	}

	/**
	 * Post 调用
	 * @param url
	 * @param data
	 * @return
	 * @throws SocketTimeoutException
	 * @throws ConnectTimeoutException
	 * @throws Exception
	 */
	public static String post(String url, String data) throws SocketTimeoutException,ConnectTimeoutException, Exception {
		return post(url, data, null, readTimeout, CHARSET);
	}

	/**
	 * Post 调用
	 * @param url
	 * @param data
	 * @param header
	 * @return
	 * @throws SocketTimeoutException
	 * @throws ConnectTimeoutException
	 * @throws Exception
	 */
	public static String post(String url, String data, Map<String, String> header)
			throws SocketTimeoutException, ConnectTimeoutException,Exception {
		return post(url, data, header, readTimeout, CHARSET);
	}

	/**
	 * Post 调用
	 * @param url
	 * @param data
	 * @param timeout
	 * @return
	 * @throws SocketTimeoutException
	 * @throws ConnectTimeoutException
	 * @throws Exception
	 */
	public static String get(String url, String data, int timeout) throws SocketTimeoutException,ConnectTimeoutException, Exception {
		return post(url, data, null, timeout, CHARSET);
	}
	
	/**
	 * Post 调用
	 * @param url
	 * @param data
	 * @param header
	 * @param timeout
	 * @return
	 * @throws SocketTimeoutException
	 * @throws ConnectTimeoutException
	 * @throws Exception
	 */
	public static String post(String url, String data, Map<String, String> header,int timeout)
			throws SocketTimeoutException, ConnectTimeoutException,Exception {
		return post(url, data, header, timeout, CHARSET);
	}

	/**
	 * Post 调用
	 * @param url
	 * @param data
	 * @param header
	 * @param timeout
	 * @param charset
	 * @return
	 * @throws SocketTimeoutException
	 * @throws ConnectTimeoutException
	 * @throws Exception
	 */
	public static String post(String url, String data, Map<String, String> header, int timeout, String charset)
			throws SocketTimeoutException,ConnectTimeoutException, Exception {
		logger.info("请求url=" + url);
		logger.info("请求数据=" + data);
		String rspMsg = null;
		RequestConfig config = RequestConfig.custom().setConnectTimeout(connectTimeout).setSocketTimeout(timeout)
				.build();
		HttpPost httpPost = new HttpPost(url);
		CloseableHttpResponse response = null;
		try {
			if (header != null) {
				Iterator<Entry<String, String>> iter = header.entrySet().iterator();
				while (iter.hasNext()) {
					Entry<String, String> h = iter.next();
					httpPost.addHeader(h.getKey(), h.getValue());
				}
			}
			httpPost.setEntity(new StringEntity(data, charset));
			response = getHttpClient(config).execute(httpPost);
			int statusCode = response.getStatusLine().getStatusCode();
			HttpEntity entity = response.getEntity();

			if (statusCode != 200) {
				if (entity != null) {
					rspMsg = EntityUtils.toString(entity, charset);
					logger.info("响应数据：" + rspMsg);
				}
				httpPost.abort();
				throw new RuntimeException("HttpClient,error status code :" + statusCode + ",rspMsg:");
			}

			if (entity != null) {
				rspMsg = EntityUtils.toString(entity, charset);
				logger.info("响应数据：" + rspMsg);
			}
		} finally {
			if (httpPost != null) {
				httpPost.releaseConnection();
			}
			if (response != null) {
				try {
					EntityUtils.consume(response.getEntity());
					response.close();
				} catch (Exception e) {
					logger.info("", e);
				}
			}

		}
		return rspMsg;
	}
	/**
	 * 关闭HTTTP连接池
	 */
	public static void close() {
		if (null == cm) {
			synchronized (LOCAL_LOCK) {
				if (null == cm) {
					try {
						cm.close();
					} catch (Exception e) {
						logger.info("", e);
					}
					cm=null;
				}
			}
		}
	}
	
}
