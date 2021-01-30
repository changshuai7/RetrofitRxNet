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
import java.util.*

/**
 * 针对多个BaseUrl的拦截器
 */
class MoreBaseUrlInterceptor : Interceptor {

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
                    val oldBaseUrl = HttpUrl.parse(netRequestConfigProvider?.baseUrl ?: "")
                    var newBaseUrl: HttpUrl? = null
                    for ((key, value) in baseUrls) {
                        if (key == domainName) {
                            if (Util.checkUrl(value)) {
                                newBaseUrl = HttpUrl.parse(value ?: "")
                                break
                            }
                        }
                    }
                    if (oldBaseUrl != null && newBaseUrl != null) {
                        val originalPathSegments = Util.removeStrListEmpty(originalUrl.encodedPathSegments())
                        val newBaseUrlPathSegments = Util.removeStrListEmpty(newBaseUrl.encodedPathSegments())
                        val oldBaseUrlPathSegments = Util.removeStrListEmpty(oldBaseUrl.encodedPathSegments())
                        if (!originalPathSegments.containsAll(oldBaseUrlPathSegments)) {
                            NetLogger.e("""
                                        -->>>>>>
                                        框架发现，你请求的完整URL：$originalUrl
                                        其中未包含BaseUrl的部分segments字段内容：$oldBaseUrl
                                        可能您的path路径使用了相对路径（使用/开头的路径）或者其他路径的错误
                                        我们强烈建议您的path采用绝对路径，以保证框架的正常运行
                                        此处baseUrl拦截已中断，使用了默认的baseUrl！
                                        不了解baseUrl相对/绝对路径的点击这里：https://www.jianshu.com/p/d6b8b6bc6209 学习使你快乐
                                        """.trimIndent()
                            )
                            return chain.proceed(originalRequest)
                        }
                        val resultPathSegments = ArrayList<String?>()
                        resultPathSegments.addAll(newBaseUrlPathSegments) // 添加新的newBaseUrlPathSegments     /cccc/dddd
                        for (i in oldBaseUrlPathSegments.indices) {
                            originalPathSegments.removeAt(0) // 删除originalPathSegments中包含oldBaseUrlPathSegments的部分  aaaa/bbbb/sales/v1 -> /sales/v1
                        }
                        resultPathSegments.addAll(originalPathSegments) // 添加修改后的originalPathSegments  /cccc/dddd/sales/v1

                        //old中的PathSegments全部删除
                        val oldBuilder = originalUrl.newBuilder()
                        for (i in 0 until originalUrl.pathSize()) {
                            //当删除了上一个 index, PathSegment 的 item 会自动前进一位, 所以 remove(0) 就好
                            oldBuilder.removePathSegment(0)
                        }

                        //重新添加
                        for (segment in resultPathSegments) {
                            oldBuilder.addEncodedPathSegment(segment ?: "")
                        }

                        //重建新的HttpUrl，需要重新设置的url部分
                        val newHttpUrl = oldBuilder
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