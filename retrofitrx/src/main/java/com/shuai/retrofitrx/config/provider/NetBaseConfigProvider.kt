package com.shuai.retrofitrx.config.provider

/**
 * 最基本配置
 */
abstract class NetBaseConfigProvider {
    /**
     * 配置库的debug状态
     * 影响debug调试域名，请求日志输出，以及可能影响普通的log日志输出
     */
    abstract val isDebug: Boolean

    /**
     * 配置库的log输出状态
     * 如果没有配置，随 [.isDebug]状态走
     */
    open val isLogDebug: Boolean
        get() = isDebug
}