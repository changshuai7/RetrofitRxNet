package com.shuai.retrofitrx.example.app._kotlin

import com.google.gson.Gson
import com.shuai.retrofitrx.config.provider.NetRequestConfigProvider
import com.shuai.retrofitrx.example.app.BuildConfig
import com.shuai.retrofitrx.example.app.api.ServerAddress
import com.shuai.retrofitrx.example.app.constants.AppConstants
import com.shuai.retrofitrx.example.app.constants.AppConstants.ParamsKey
import com.shuai.retrofitrx.example.app.constants.AppConstants.ServerDomainKey
import java.util.*

/**
 * 自定义NetRequestConfigProvider
 */
class KTNetRequestConfig : NetRequestConfigProvider() {

    override val headerMap: Map<String, String>
        get() {
            val map = HashMap<String, String>()
            map[AppConstants.HeaderKey.HEADER_1] = "header1-----my"
            map[AppConstants.HeaderKey.HEADER_2] = "header2-----my"
            return map
        }

    override val paramsMap: Map<String, String>
        get() {
            val map = HashMap<String, String>()
            map[ParamsKey.PARAMS_1] = "tom-----my"
            map[ParamsKey.PARAMS_2] = "jake-----my"
            return map
        }

    override val bodyMap: Map<String, String>
        get() = super.bodyMap

    override val baseUrl: String // 建议baseUrl以斜杠结尾，避免Retrofit报错
        get() = if (BuildConfig.DEBUG) ServerAddress.ServerAddressDefault[0] else ServerAddress.ServerAddressDefault[1]

    override val baseUrls: Map<String, String>
        get() {
            val map = HashMap<String, String>()
            map[ServerDomainKey.URL1] = if (BuildConfig.DEBUG) ServerAddress.ServerAddress1[0] else ServerAddress.ServerAddress1[1]
            map[ServerDomainKey.URL2] = if (BuildConfig.DEBUG) ServerAddress.ServerAddress2[0] else ServerAddress.ServerAddress2[1]
            return map
        }

    override val gsonInstance: Gson?
        get() = null
}