package com.shuai.retrofitrx.config.provider;

/**
 * 最基本配置
 */
public abstract class NetBaseConfigProvider {

    /**
     * 配置库的debug状态
     * 影响debug调试域名，请求日志输出，以及可能影响普通的log日志输出
     */
    public abstract boolean isDebug();

    /**
     * 配置库的log输出状态
     * 如果没有配置，随 {@link #isDebug()}状态走
     */
    public boolean isLogDebug() {
        return isDebug();
    }


}
