package com.shuai.retrofitrx.net.retrofit

import android.content.Context
import com.shuai.retrofitrx.config.provider.NetRequestConfigProvider
import com.shuai.retrofitrx.net.client.AuthOkHttpClientBuilder
import okhttp3.OkHttpClient

/**
 * 需要使用默认添加公共参数以及token的网络请求 使用AuthRetrofitFactory构建Api
 */
class AuthRetrofitFactory : AbstractRetrofitFactory {

    constructor(ctx: Context, netRequestConfigProvider: NetRequestConfigProvider?) : super(ctx, netRequestConfigProvider)
    constructor(ctx: Context) : super(ctx)

    override val client: OkHttpClient
        get() = AuthOkHttpClientBuilder(ctx, netRequestConfigProvider).build()
}