package com.shuai.retrofitrx.example.app._kotlin

import com.google.gson.Gson
import com.shuai.retrofitrx.config.NetConfig
import com.shuai.retrofitrx.config.provider.NetBaseConfigProvider
import com.shuai.retrofitrx.config.provider.NetRequestConfigProvider
import com.shuai.retrofitrx.example.app.App
import com.shuai.retrofitrx.example.app.BuildConfig
import com.shuai.retrofitrx.example.app.api.ServerAddress
import com.shuai.retrofitrx.example.app.constants.AppConstants
import com.shuai.retrofitrx.example.app.constants.AppConstants.ParamsKey
import com.shuai.retrofitrx.example.app.constants.AppConstants.ServerDomainKey
import java.util.*

class KTApp : App() {

    override fun onCreate() {
        super.onCreate()
        initNet()
    }

    private fun initNet() {
        NetConfig.init(this)
                .baseConfig(object : NetBaseConfigProvider() {
                    /**
                     * 配置库的debug状态
                     * 影响debug调试域名，请求日志输出，以及可能影响普通的log日志输出
                     */
                    override val isDebug: Boolean
                        get() = BuildConfig.DEBUG

                    /**
                     * 配置库的log输出状态
                     * 如果没有配置，随 [.isDebug]状态走
                     */
                    override val isLogDebug: Boolean
                        get() = super.isLogDebug
                })
                .requestConfig(object : NetRequestConfigProvider() {
                    /**
                     * 配置默认Auth：公共请求header。（可选）
                     */
                    override val headerMap: Map<String, String>
                        get() {
                            val map = HashMap<String, String>()
                            map[AppConstants.HeaderKey.HEADER_1] = "header1"
                            map[AppConstants.HeaderKey.HEADER_2] = "header2"
                            return map
                        }

                    /**
                     * 配置默认Auth：公共get/post请求的url拼接参数。（可选）
                     */
                    override val paramsMap: Map<String, String>
                        get() {
                            val map = HashMap<String, String>()
                            map[ParamsKey.PARAMS_1] = "tom"
                            map[ParamsKey.PARAMS_2] = "jake"
                            return map
                        }

                    /**
                     * 配置默认Auth：公共post请求的通用参数。（可选）
                     */
                    override val bodyMap: Map<String, String>
                        get() = super.bodyMap // 建议baseUrl以斜杠结尾，避免Retrofit报错

                    /**
                     * 配置通用Auth默认baseUrl的domain
                     */
                    override val baseUrl: String
                        // 建议baseUrl以斜杠结尾，避免Retrofit报错
                        get() = if (BuildConfig.DEBUG) ServerAddress.ServerAddressDefault[0] else ServerAddress.ServerAddressDefault[1]

                    /**
                     * 可传入多个BaseUrl，通过制定Header中Domain-Host字段来区分。（可选）
                     * @return
                     */
                    override val baseUrls: Map<String, String>
                        get() {
                            val map = HashMap<String, String>()
                            map[ServerDomainKey.URL1] = if (BuildConfig.DEBUG) ServerAddress.ServerAddress1[0] else ServerAddress.ServerAddress1[1]
                            map[ServerDomainKey.URL2] = if (BuildConfig.DEBUG) ServerAddress.ServerAddress2[0] else ServerAddress.ServerAddress2[1]
                            return map
                        }

                    /**
                     * 传入Gson实例。（可选）
                     * @return
                     */
                    override val gsonInstance: Gson
                        get() = super.gsonInstance
                })
    }

}