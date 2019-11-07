package com.shuai.retrofitrx.net.retrofit;

import com.google.gson.Gson;
import com.shuai.retrofitrx.config.NetConfig;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author changshuai
 */
public abstract class AbstractRetrofitFactory implements IRetrofitCreator {

    private Gson gson;

    public AbstractRetrofitFactory(Gson gson) {
        this.gson = gson;
    }

    public AbstractRetrofitFactory() {
    }


    public abstract OkHttpClient getClient(String baseUrl);


    @Override
    public Retrofit create(){
        return create(NetConfig.getConfig().getRequestConfigProvider().getBaseUrl());
    }

    @Override
    public Retrofit create(String baseUrl) {
        Retrofit retrofit;

        //  优先选择外部传入的gson工厂，如果没有，选择初始化传入的，如果依然没有，那么选择默认的。
        Gson gsonIns = this.gson != null ? this.gson : NetConfig.getConfig().getRequestConfigProvider().getGsonInstance();

        retrofit = new Retrofit.Builder()
                .client(getClient(baseUrl))
                .baseUrl(baseUrl)
                .addConverterFactory(gsonIns == null ? GsonConverterFactory.create() : GsonConverterFactory.create(gsonIns))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        return retrofit;
    }

}
