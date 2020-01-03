package com.shuai.retrofitrx.net.retrofit;

import android.content.Context;

import com.google.gson.Gson;
import com.shuai.retrofitrx.net.client.AuthClient;
import com.shuai.retrofitrx.net.client.AuthOkHttpClientBuilder;

import okhttp3.OkHttpClient;

/**
 * 需要使用默认添加公共参数以及token的网络请求 使用AuthRetrofitFactory构建Api
 */
public class AuthRetrofitFactory extends AbstractRetrofitFactory {
    private Context ctx;

    public AuthRetrofitFactory(Gson gson, Context ctx) {
        super(gson);
        this.ctx = ctx.getApplicationContext();
    }

    public AuthRetrofitFactory(Context ctx) {
        this.ctx = ctx.getApplicationContext();
    }


    @Override
    public OkHttpClient getClient(String baseUrl) {

        // 这里其实最好是采用一个单例：return AuthClient.newInstance(ctx);
        // 但是如果采用单例，虽然节省内存，但是会导致baseUrl无法传入，MoreBaseUrlInterceptor无法获取到baseUrl来执行拦截
        // 事实上一个Retrofit对象对应一个OkHttpClient是可以接受的。不会造成多大的内存浪费。所以这里直接new 一个OkHttpClient

        return new AuthOkHttpClientBuilder(ctx,baseUrl).build();
    }

}
