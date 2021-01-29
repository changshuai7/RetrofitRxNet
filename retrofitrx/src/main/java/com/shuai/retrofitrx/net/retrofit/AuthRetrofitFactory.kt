package com.shuai.retrofitrx.net.retrofit;

import android.content.Context;

import com.shuai.retrofitrx.config.provider.NetRequestConfigProvider;
import com.shuai.retrofitrx.net.client.AuthOkHttpClientBuilder;

import okhttp3.OkHttpClient;

/**
 * 需要使用默认添加公共参数以及token的网络请求 使用AuthRetrofitFactory构建Api
 */
public class AuthRetrofitFactory extends AbstractRetrofitFactory {


    public AuthRetrofitFactory(Context ctx, NetRequestConfigProvider netRequestConfigProvider) {
        super(ctx, netRequestConfigProvider);
    }

    public AuthRetrofitFactory(Context ctx) {
        super(ctx);
    }

    @Override
    public OkHttpClient getClient() {

        return new AuthOkHttpClientBuilder(getCtx(),getNetRequestConfigProvider()).build();
    }

}
