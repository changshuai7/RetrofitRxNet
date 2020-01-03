package com.shuai.retrofitrx.config;

import android.app.Application;
import android.support.annotation.NonNull;

import com.shuai.retrofitrx.config.provider.NetBaseConfigProvider;
import com.shuai.retrofitrx.config.provider.NetRequestConfigProvider;
import com.shuai.retrofitrx.utils.Logger;


/**
 * 网络请求库的统一配置
 * <p>
 * api:
 * 获取Application：NetConfig.getApp();
 * 获取Config：NetConfig.getConfig.getXXXConfig().getXXX()
 */
public class NetConfig {
    private static NetConfig.Config sConfig;
    private static Application sApp;

    public static Config getConfig() {
        return sConfig;
    }

    public static Application getApp() {
        return sApp;
    }

    /**
     * 初始化
     *
     * @param app Application
     * @return 配置文件
     */
    public static NetConfig.Config init(@NonNull final Application app) {
        sApp = app;
        if (sConfig == null) {
            sConfig = new NetConfig.Config();
        }
        return sConfig;
    }

    public static class Config {

        private NetBaseConfigProvider baseConfig = null;                    //基础配置：【必须配置】
        private NetRequestConfigProvider requestConfigProvider = null;      //请求配置：【必须配置】

        public Config baseConfig(NetBaseConfigProvider baseConfig) {
            this.baseConfig = baseConfig;
            Logger.debug(baseConfig.isLogDebug());//设置log日志模式
            return this;
        }

        public Config requestConfig(NetRequestConfigProvider requestConfigProvider) {
            this.requestConfigProvider = requestConfigProvider;
            return this;
        }



        //////////////////////////////////GET AND SET////////////////////////////////////////

        public NetBaseConfigProvider getBaseConfig() {
            return baseConfig;
        }

        public NetRequestConfigProvider getRequestConfigProvider() {
            return requestConfigProvider;
        }


        //////////////////////////////////GET AND SET////////////////////////////////////////


    }
}
