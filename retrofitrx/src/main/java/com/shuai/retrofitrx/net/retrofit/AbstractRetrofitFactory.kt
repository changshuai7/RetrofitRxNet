package com.shuai.retrofitrx.net.retrofit;

import android.content.Context;

import com.google.gson.Gson;
import com.shuai.retrofitrx.config.NetConfig;
import com.shuai.retrofitrx.config.provider.NetRequestConfigProvider;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author changshuai
 */
public abstract class AbstractRetrofitFactory implements IRetrofitCreator {

    private Context ctx;
    private NetRequestConfigProvider netRequestConfigProvider;

    public Context getCtx() {
        return ctx;
    }

    public NetRequestConfigProvider getNetRequestConfigProvider() {
        return netRequestConfigProvider;
    }

    public AbstractRetrofitFactory(Context ctx) {
        this.ctx = ctx.getApplicationContext();
        this.netRequestConfigProvider = NetConfig.getConfig().getRequestConfigProvider();
    }

    public AbstractRetrofitFactory(Context ctx, NetRequestConfigProvider netRequestConfigProvider) {
        this.ctx = ctx.getApplicationContext();
        this.netRequestConfigProvider = netRequestConfigProvider;
    }

    public abstract OkHttpClient getClient();


    @Override
    public Retrofit create() {

        if (netRequestConfigProvider != null) {
            Retrofit retrofit;

            retrofit = new Retrofit.Builder()
                    .client(getClient())
                    .baseUrl(netRequestConfigProvider.getBaseUrl())
                    .addConverterFactory(netRequestConfigProvider.getGsonInstance() == null ? GsonConverterFactory.create() : GsonConverterFactory.create(netRequestConfigProvider.getGsonInstance()))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();

            return retrofit;
        }
        return null;

    }

}
