package com.shuai.retrofitrx.net.interceptor

import com.shuai.retrofitrx.config.NetConfig
import com.shuai.retrofitrx.config.provider.NetRequestConfigProvider
import com.shuai.retrofitrx.utils.Util
import okhttp3.FormBody
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class AuthParamsInterceptor : Interceptor {

    private var netRequestConfigProvider: NetRequestConfigProvider?

    constructor(netRequestConfigProvider: NetRequestConfigProvider?) {
        this.netRequestConfigProvider = netRequestConfigProvider
    }

    constructor() {
        netRequestConfigProvider = NetConfig.config.requestConfigProvider
    }

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val originRequest = chain.request()

        // 添加公共参数
        val newUrlBuilder = originRequest.url().newBuilder()
        val defaultParams: Map<String, String> = defaultUrlParams
        if (defaultParams.isNotEmpty()) {
            for ((key, value) in defaultParams) {
                newUrlBuilder.addQueryParameter(key, value)
            }
        }

        // 添加公共 Header
        val newRequestBuilder = originRequest.newBuilder().url(newUrlBuilder.build())
        val defaultHeaders: Map<String, String> = defaultHeaders
        if (defaultHeaders.isNotEmpty()) {
            for ((key, value) in defaultHeaders) {
                newRequestBuilder.addHeader(key, value)
            }
        }

        // Post 请求添加统一Body
        if ("POST".equals(originRequest.method(), ignoreCase = true)) {
            if (originRequest.body() is FormBody) {
                val defaultBodyParams: Map<String, String> = defaultBodyParams
                if (!Util.mapIsEmpty(defaultBodyParams)) {
                    val newFormBody = FormBody.Builder()
                    val oldFormBody = originRequest.body() as FormBody?
                    if (oldFormBody != null && oldFormBody.size() > 0) {
                        for (i in 0 until oldFormBody.size()) {
                            newFormBody.addEncoded(oldFormBody.encodedName(i), oldFormBody.encodedValue(i))
                        }
                    }
                    for (entry in defaultBodyParams.entries) {
                        newFormBody.add(entry.key, entry.value)
                    }
                    newRequestBuilder.method(originRequest.method(), newFormBody.build())
                }
            }
        }
        val newRequest = newRequestBuilder.build()
        return chain.proceed(newRequest)
    }

    private val defaultUrlParams: Map<String, String>
        get() = if (netRequestConfigProvider != null) netRequestConfigProvider!!.paramsMap else emptyMap()

    private val defaultHeaders: Map<String, String>
        get() = if (netRequestConfigProvider != null) netRequestConfigProvider!!.headerMap else emptyMap()

    private val defaultBodyParams: Map<String, String>
        get() = if (netRequestConfigProvider != null) netRequestConfigProvider!!.bodyMap else emptyMap()

}