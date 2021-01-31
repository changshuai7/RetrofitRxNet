package com.shuai.retrofitrx.net.client

import android.content.Context
import com.shuai.retrofitrx.config.NetConfig
import com.shuai.retrofitrx.config.provider.NetRequestConfigProvider
import com.shuai.retrofitrx.constants.NetConstants
import com.shuai.retrofitrx.net.interceptor.AuthParamsInterceptor
import com.shuai.retrofitrx.net.interceptor.HttpLoggingInterceptor
import com.shuai.retrofitrx.net.interceptor.MoreBaseUrlInterceptor
import okhttp3.Cache
import okhttp3.OkHttpClient
import java.io.File
import java.util.logging.Level
import java.util.concurrent.TimeUnit


class AuthOkHttpClientBuilder : AbstractOkHttpClientBuilder {

    private val context: Context
    private var netRequestConfigProvider: NetRequestConfigProvider?

    constructor(context: Context) {
        this.context = context
        //采用公共配置的NetRequestConfigProvider
        netRequestConfigProvider = NetConfig.config.requestConfigProvider
    }

    constructor(context: Context, netRequestConfigProvider: NetRequestConfigProvider?) {
        this.context = context
        //采用传入配置的NetRequestConfigProvider
        this.netRequestConfigProvider = netRequestConfigProvider
    }

    override val okHttpClientBuilder: OkHttpClient.Builder
        get() = OkHttpClient().newBuilder()

    override fun setTimeOut(builder: OkHttpClient.Builder) {
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

    override fun setRequestInterceptor(builder: OkHttpClient.Builder) {

        /**URL拦截解决多BaseURL的问题。*/
        builder.addInterceptor(MoreBaseUrlInterceptor(netRequestConfigProvider))

        /** 增加默认的全局配置参数*/
        builder.addInterceptor(AuthParamsInterceptor(netRequestConfigProvider))

        /**日志拦截器*/
        val logInterceptor = HttpLoggingInterceptor(NetConstants.TAG)
        //log打印级别，决定了log显示的详细程度
        logInterceptor.setPrintLevel(if (NetConfig.config.baseConfigProvider.isLogDebug) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE)
        //log颜色级别，决定了log在控制台显示的颜色
        logInterceptor.setColorLevel(Level.INFO)
        builder.addInterceptor(logInterceptor)
    }

    override fun setCache(builder: OkHttpClient.Builder) {
        val cacheDirectory = File(context.externalCacheDir, OK_HTTP_CACHE_FILE_NAME)
        builder.cache(Cache(cacheDirectory, OK_HTTP_CACHE_SIZE.toLong()))
    }

    companion object {
        const val OK_HTTP_CACHE_SIZE = 10 * 1024 * 1024
        const val OK_HTTP_CACHE_FILE_NAME = "OKHttpCache"
        const val TIMEOUT_DEBUG: Long = 20
        const val TIMEOUT_REAL: Long = 15
    }

}