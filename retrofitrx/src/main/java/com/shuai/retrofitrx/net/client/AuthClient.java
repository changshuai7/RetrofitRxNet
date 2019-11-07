package com.shuai.retrofitrx.net.client;

import android.content.Context;

import okhttp3.OkHttpClient;

/**
 * @author changshuai
 *
 * // TODO 思考OkHttpClient是否有必要作为一个单例。单例模式的缺点便是有可能导致数据出错，且不够灵活，传参不便。优点便是节省内存。
 *
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
