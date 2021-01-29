package com.shuai.retrofitrx.config

import android.app.Application
import com.shuai.retrofitrx.config.provider.NetBaseConfigProvider
import com.shuai.retrofitrx.config.provider.NetRequestConfigProvider
import com.shuai.retrofitrx.utils.Logger

/**
 * 网络请求库的统一配置
 *
 *
 * api:
 * 获取Application：NetConfig.getApp();
 * 获取Config：NetConfig.getConfig.getXXXConfig().getXXX()
 */
class NetConfig {

    companion object {

        @JvmStatic
        val config: Config by lazy {
            Config()
        }


        @JvmStatic
        lateinit var app: Application
            private set

        /**
         * 初始化
         *
         * @param app Application
         * @return 配置文件
         */
        @JvmStatic
        fun init(app: Application): Config {
            NetConfig.app = app
            return config
        }

        class Config {
            //基础配置：【必须配置】
            lateinit var baseConfigProvider: NetBaseConfigProvider
                private set

            //请求配置：【选择配置，也可以在单独请求里传入配置】
            var requestConfigProvider: NetRequestConfigProvider? = null
                private set

            fun baseConfig(baseConfigProvider: NetBaseConfigProvider): Config {
                this.baseConfigProvider = baseConfigProvider
                Logger.debug(baseConfigProvider.isLogDebug) //设置log日志模式
                return this
            }

            fun requestConfig(requestConfigProvider: NetRequestConfigProvider?): Config {
                this.requestConfigProvider = requestConfigProvider
                return this
            }
        }
    }

}