package com.shuai.retrofitrx.net.client;

import com.shuai.retrofitrx.net.download.progress.DownloadManagerResponseListener;

import okhttp3.OkHttpClient;

/**
 * @author changshuai
 */
public class DownloadClient {
    private static volatile OkHttpClient okHttpClient = null;//volatile会把变量立即同步到内存中

    private DownloadClient() {
    }

    public static OkHttpClient getInstance(DownloadManagerResponseListener listener) {
        if (okHttpClient == null) {//第一次加锁
            synchronized (DownloadClient.class) {//当两个线程同时到这里时，不做判空还是都会执行的
                if (okHttpClient == null) {
                    okHttpClient = new DownloadOkHttpClientBuilder(listener).build();
                }
            }
        }
        return okHttpClient;
    }
}
