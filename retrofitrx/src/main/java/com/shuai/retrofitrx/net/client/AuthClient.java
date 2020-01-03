package com.shuai.retrofitrx.net.client;

import android.content.Context;

import okhttp3.OkHttpClient;

/**
 * @author changshuai
 **
 */
public class AuthClient {
    private static volatile OkHttpClient okHttpClient = null;//volatile会把变量立即同步到内存中

    private AuthClient() {
    }

    public static OkHttpClient newInstance(Context ctx) {
        if (okHttpClient == null) {//第一次加锁
            synchronized (AuthClient.class) {//当两个线程同时到这里时，不做判空还是都会执行的
                if (okHttpClient == null) {
                    //http://blog.csdn.net/yanzi1225627/article/details/24937439
                    okHttpClient = new AuthOkHttpClientBuilder(ctx).build();
                }
            }
        }
        return okHttpClient;
    }
}
