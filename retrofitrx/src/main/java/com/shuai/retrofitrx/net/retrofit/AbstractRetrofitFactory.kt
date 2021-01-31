package com.shuai.retrofitrx.net.retrofit

import android.content.Context
import com.shuai.retrofitrx.config.NetConfig
import com.shuai.retrofitrx.config.provider.NetRequestConfigProvider
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * @author changshuai
 */
abstract class AbstractRetrofitFactory : IRetrofitCreator {

    var ctx: Context
        private set
    var netRequestConfigProvider: NetRequestConfigProvider?
        private set

    constructor(ctx: Context) {
        this.ctx = ctx.applicationContext
        netRequestConfigProvider = NetConfig.config.requestConfigProvider
    }

    constructor(ctx: Context, netRequestConfigProvider: NetRequestConfigProvider?) {
        this.ctx = ctx.applicationContext
        this.netRequestConfigProvider = netRequestConfigProvider
    }

    override fun create(): Retrofit? {
        if (netRequestConfigProvider != null) {
            return Retrofit.Builder()
                    .client(client)
                    .baseUrl(netRequestConfigProvider!!.baseUrl)
                    .addConverterFactory(GsonConverterFactory.create(netRequestConfigProvider!!.gsonInstance))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build()
        }
        return null
    }

    /////////////// abstract ///////////////

    abstract val client: OkHttpClient
}