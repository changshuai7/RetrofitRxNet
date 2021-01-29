package com.shuai.retrofitrx.net.client

import com.shuai.retrofitrx.config.NetConfig
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

abstract class AbstractOkHttpClientBuilder : IClientBuilder<OkHttpClient> {

    override fun build(): OkHttpClient {
        val builder = okHttpClientBuilder
        setRequestInterceptor(builder)
        setCache(builder)
        setTimeOut(builder)
        return builder.build()
    }

    open val okHttpClientBuilder: OkHttpClient.Builder
        get() = OkHttpClient().newBuilder()

    abstract fun setRequestInterceptor(builder: OkHttpClient.Builder)
    abstract fun setCache(builder: OkHttpClient.Builder)

    private fun setTimeOut(builder: OkHttpClient.Builder) {
        if (NetConfig.config.baseConfigProvider.isDebug) {
            builder.connectTimeout(TIMEOUT_DEBUG, TimeUnit.SECONDS)
                    .readTimeout(TIMEOUT_DEBUG, TimeUnit.SECONDS)
                    .writeTimeout(TIMEOUT_DEBUG, TimeUnit.SECONDS)
        } else {
            builder.connectTimeout(TIMEOUT_REAL, TimeUnit.SECONDS)
                    .readTimeout(TIMEOUT_REAL, TimeUnit.SECONDS)
                    .writeTimeout(TIMEOUT_REAL, TimeUnit.SECONDS)
        }
    }

    companion object {
        const val TIMEOUT_DEBUG: Long = 20
        const val TIMEOUT_REAL: Long = 15
    }
}