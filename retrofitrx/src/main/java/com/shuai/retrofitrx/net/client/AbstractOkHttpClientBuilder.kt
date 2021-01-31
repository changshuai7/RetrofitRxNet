package com.shuai.retrofitrx.net.client

import okhttp3.OkHttpClient

abstract class AbstractOkHttpClientBuilder : IClientBuilder<OkHttpClient> {

    override fun build(): OkHttpClient {
        val builder = okHttpClientBuilder
        setRequestInterceptor(builder)
        setCache(builder)
        setTimeOut(builder)
        return builder.build()
    }

    /////////////// abstract ///////////////

    abstract val okHttpClientBuilder: OkHttpClient.Builder
    abstract fun setTimeOut(builder: OkHttpClient.Builder)
    abstract fun setRequestInterceptor(builder: OkHttpClient.Builder)
    abstract fun setCache(builder: OkHttpClient.Builder)
}