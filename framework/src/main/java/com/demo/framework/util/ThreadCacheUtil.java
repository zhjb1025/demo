package com.demo.framework.util;

public class ThreadCacheUtil {
	private static ThreadLocal<ThreadCacheData> threadLocalData = new ThreadLocal<ThreadCacheData>();
	
	public static void setThreadLocalData(ThreadCacheData threadCacheData) {
		threadLocalData.set(threadCacheData);
	}

	public static ThreadCacheData getThreadLocalData() {
		return threadLocalData.get();
	}

	public static void cleanThreadCacheData() {
		threadLocalData.remove();
	}
}
