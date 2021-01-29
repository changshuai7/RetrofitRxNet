package com.shuai.retrofitrx.config.provider

import com.google.gson.Gson
import java.util.*

/**
 * 请求配置
 */
abstract class NetRequestConfigProvider {
    /**
     * 配置默认Auth：公共请求header。（可选）
     */
    open val headerMap: Map<String, String>
        get() = emptyMap()

    /**
     * 配置默认Auth：公共post请求的通用参数。（可选）
     */
    open val bodyMap: Map<String, String>
        get() = emptyMap()

    /**
     * 配置默认Auth：公共get/post请求的url拼接参数。（可选）
     */
    open val paramsMap: Map<String, String>
        get() = emptyMap()

    /**
     * 配置通用Auth默认baseUrl的domain
     */
    abstract val baseUrl: String

    /**
     * 可传入多个BaseUrl，通过制定Header中Domain-Host字段来区分。（可选）
     * @return
     */
    open val baseUrls: Map<String, String>
        get() = emptyMap()

    /**
     * 传入Gson实例。（可选）
     * @return
     */
    open val gsonInstance: Gson?
        get() = null
}
