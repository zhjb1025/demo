package com.demo.framework.util;

import java.net.SocketTimeoutException;

public class PoolHttpClientTest {

	public static void main(String[] args) throws SocketTimeoutException, Exception {
		String res=PoolHttpClient.get("https://blog.csdn.net/zmx729618/article/details/78328628");
		System.out.println(res);
		PoolHttpClient.close();
	}

}
