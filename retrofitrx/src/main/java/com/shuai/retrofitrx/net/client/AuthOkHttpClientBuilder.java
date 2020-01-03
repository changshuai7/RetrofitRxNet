package com.shuai.retrofitrx.net.client;

import android.content.Context;

import com.shuai.retrofitrx.config.NetConfig;
import com.shuai.retrofitrx.constants.NetConstants;
import com.shuai.retrofitrx.net.Interceptor.AuthParamsInterceptor;
import com.shuai.retrofitrx.net.Interceptor.HttpLoggingInterceptor;
import com.shuai.retrofitrx.net.Interceptor.MoreBaseUrlInterceptor;

import java.io.File;
import java.util.logging.Level;

import okhttp3.Cache;
import okhttp3.OkHttpClient;


public class AuthOkHttpClientBuilder extends AbstractOkHttpClientBuilder {
    public static final int OKHTTP_CACHE_SIZE = 10 * 1024 * 1024;
    public static final String OKHTTP_CACHE_FILE_NAME = "OKHttpCache";

    private Context context;
    private String baseUrl;

    public AuthOkHttpClientBuilder(Context context) {
        this.context = context;
    }

    public AuthOkHttpClientBuilder(Context context, String baseUrl) {
        this.context = context;
        this.baseUrl = baseUrl;
    }

    @Override
    public OkHttpClient.Builder getOkHttpClientBuilder() {
        return super.getOkHttpClientBuilder();
    }

    @Override
    public void setRequestInterceptor(OkHttpClient.Builder builder) {

        /**
         * URL拦截解决多BaseURL的问题。
         */
        builder.addInterceptor(new MoreBaseUrlInterceptor(baseUrl));

        /**
         * 增加默认的全局配置参数
         */
        builder.addInterceptor(new AuthParamsInterceptor());

        /**
         * 日志拦截器
         */
        HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor(NetConstants.TAG);
        //log打印级别，决定了log显示的详细程度
        logInterceptor.setPrintLevel(NetConfig.getConfig().getBaseConfig().isLogDebug() ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);
        //log颜色级别，决定了log在控制台显示的颜色
        logInterceptor.setColorLevel(Level.INFO);
        builder.addInterceptor(logInterceptor);


    }

    @Override
    public void setCache(OkHttpClient.Builder builder) {
        File cacheDirectory = new File(context.getExternalCacheDir(), OKHTTP_CACHE_FILE_NAME);
        builder.cache(new Cache(cacheDirectory, OKHTTP_CACHE_SIZE));
    }
}
