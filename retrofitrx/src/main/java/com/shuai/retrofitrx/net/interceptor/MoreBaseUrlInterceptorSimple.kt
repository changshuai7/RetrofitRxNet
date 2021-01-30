package com.shuai.retrofitrx.net.interceptor

import com.shuai.retrofitrx.config.NetConfig
import com.shuai.retrofitrx.config.provider.NetRequestConfigProvider
import com.shuai.retrofitrx.constants.NetConstants
import com.shuai.retrofitrx.utils.NetLogger
import com.shuai.retrofitrx.utils.Util
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

/**
 * 针对多个BaseUrl的拦截器
 * 此拦截器只能拦截被替换的baseUrl的host、port部分。segments无法识别
 */
class MoreBaseUrlInterceptorSimple : Interceptor {
    private var netRequestConfigProvider: NetRequestConfigProvider?

    constructor(netRequestConfigProvider: NetRequestConfigProvider?) {
        this.netRequestConfigProvider = netRequestConfigProvider
    }

    constructor() {
        netRequestConfigProvider = NetConfig.config.requestConfigProvider
    }

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        //获取原始的originalRequest
        val originalRequest = chain.request()
        //获取老的url
        val originalUrl = originalRequest.url()
        //获取originalRequest的创建者builder
        val builder = originalRequest.newBuilder()
        //获取头信息的集合如
        val domainHostList = originalRequest.headers(NetConstants.HeaderKey.DomainHost)
        NetLogger.d("未拦截替换baseUrl前的原始URL:$originalUrl")
        if (domainHostList.size > 0) {
            //删除原有配置中的值,就是namesAndValues集合里的值
            builder.removeHeader(NetConstants.HeaderKey.DomainHost)
            //获取头信息中配置的value
            val domainName = domainHostList[0]
            if (netRequestConfigProvider != null) {
                val baseUrls = netRequestConfigProvider!!.baseUrls
                if (baseUrls.isNotEmpty() && baseUrls.keys.contains(domainName) && Util.checkUrl(netRequestConfigProvider!!.baseUrl)) {
                    val oldBaseUrl = HttpUrl.parse(netRequestConfigProvider!!.baseUrl)
                    var newBaseUrl: HttpUrl? = null
                    for ((key, value) in baseUrls) {
                        if (key == domainName) {
                            if (Util.checkUrl(value)) {
                                newBaseUrl = HttpUrl.parse(value)
                                break
                            }
                        }
                    }
                    if (oldBaseUrl != null && newBaseUrl != null) {
                        //重建新的HttpUrl，需要重新设置的url部分
                        val newHttpUrl = originalUrl.newBuilder()
                                .scheme(newBaseUrl.scheme()) //http协议如：http或者https
                                .host(newBaseUrl.host()) //主机地址
                                .port(newBaseUrl.port()) //端口
                                .build()

                        //获取处理后的新newRequest
                        val newRequest = builder.url(newHttpUrl).build()
                        return chain.proceed(newRequest)
                    }
                }
            }
        }
        return chain.proceed(originalRequest)
    }
}