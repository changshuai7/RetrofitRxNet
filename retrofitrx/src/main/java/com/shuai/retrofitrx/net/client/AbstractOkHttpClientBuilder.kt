package com.shuai.retrofitrx.net.client;

import com.shuai.retrofitrx.config.NetConfig;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

public abstract class AbstractOkHttpClientBuilder implements IClientBuilder<OkHttpClient> {
    public static long TIMEOUT_DEBUG = 20;
    public static long TIMEOUT_REAL = 15;

    @Override
    public OkHttpClient build() {
        OkHttpClient.Builder builder = getOkHttpClientBuilder();
        setRequestInterceptor(builder);
        setCache(builder);
        setTimeOut(builder);
        return builder.build();
    }

    public OkHttpClient.Builder getOkHttpClientBuilder() {
        return new OkHttpClient().newBuilder();
    }

    public abstract void setRequestInterceptor(OkHttpClient.Builder builder);

    public abstract void setCache(OkHttpClient.Builder builder);

    public void setTimeOut(OkHttpClient.Builder builder) {
        if (NetConfig.getConfig().getBaseConfig().isDebug()) {
            builder.connectTimeout(TIMEOUT_DEBUG, TimeUnit.SECONDS)
                    .readTimeout(TIMEOUT_DEBUG, TimeUnit.SECONDS)
                    .writeTimeout(TIMEOUT_DEBUG, TimeUnit.SECONDS);
        } else {
            builder.connectTimeout(TIMEOUT_REAL, TimeUnit.SECONDS)
                    .readTimeout(TIMEOUT_REAL, TimeUnit.SECONDS)
                    .writeTimeout(TIMEOUT_REAL, TimeUnit.SECONDS);
        }
    }
}
