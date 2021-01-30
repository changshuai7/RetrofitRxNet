package com.shuai.retrofitrx.example.app._java;

import com.google.gson.Gson;
import com.shuai.retrofitrx.config.NetConfig;
import com.shuai.retrofitrx.config.provider.NetBaseConfigProvider;
import com.shuai.retrofitrx.config.provider.NetRequestConfigProvider;
import com.shuai.retrofitrx.example.app.App;
import com.shuai.retrofitrx.example.app.BuildConfig;
import com.shuai.retrofitrx.example.app.api.ServerAddress;
import com.shuai.retrofitrx.example.app.constants.AppConstants;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class JavaApp extends App {

    @Override
    public void onCreate() {
        super.onCreate();

        initNet();
    }

    private void initNet() {
        NetConfig.init(this)
                .baseConfig(new NetBaseConfigProvider() {

                    /**
                     * 配置库的debug状态
                     * 影响debug调试域名，请求日志输出，以及可能影响普通的log日志输出
                     */
                    @Override
                    public boolean isDebug() {
                        return BuildConfig.DEBUG;
                    }

                    /**
                     * 配置库的log输出状态
                     * 如果没有配置，随 {@link #isDebug()}状态走
                     */
                    @Override
                    public boolean isLogDebug() {
                        return super.isLogDebug();
                    }

                })
                .requestConfig(new NetRequestConfigProvider() {

                    /**
                     * 配置默认Auth：公共请求header。（可选）
                     */
                    @NotNull
                    @Override
                    public Map<String, String> getHeaderMap() {
                        HashMap<String, String> map = new HashMap<>();
                        map.put(AppConstants.HeaderKey.HEADER_1, "header1");
                        map.put(AppConstants.HeaderKey.HEADER_2, "header2");

                        return map;
                    }

                    /**
                     * 配置默认Auth：公共get/post请求的url拼接参数。（可选）
                     */
                    @NotNull
                    @Override
                    public Map<String, String> getParamsMap() {
                        HashMap<String, String> map = new HashMap<>();
                        map.put(AppConstants.ParamsKey.PARAMS_1, "tom");
                        map.put(AppConstants.ParamsKey.PARAMS_2, "jake");

                        return map;
                    }

                    /**
                     * 配置默认Auth：公共post请求的通用参数。（可选）
                     */
                    @NotNull
                    @Override
                    public Map<String, String> getBodyMap() {
                        //code...
                        return super.getBodyMap();
                    }


                    /**
                     * 配置通用Auth默认baseUrl的domain
                     */
                    @NotNull
                    @Override
                    public String getBaseUrl() {
                        // 建议baseUrl以斜杠结尾，避免Retrofit报错
                        return BuildConfig.DEBUG ? ServerAddress.INSTANCE.getServerAddressDefault()[0] : ServerAddress.INSTANCE.getServerAddressDefault()[1];
                    }

                    /**
                     * 可传入多个BaseUrl，通过制定Header中Domain-Host字段来区分。（可选）
                     * @return
                     */
                    @NotNull
                    @Override
                    public Map<String, String> getBaseUrls() {
                        HashMap<String, String> map = new HashMap<>();
                        map.put(AppConstants.ServerDomainKey.URL1, BuildConfig.DEBUG ? ServerAddress.INSTANCE.getServerAddress1()[0] : ServerAddress.INSTANCE.getServerAddress1()[1]);
                        map.put(AppConstants.ServerDomainKey.URL2, BuildConfig.DEBUG ? ServerAddress.INSTANCE.getServerAddress2()[0] : ServerAddress.INSTANCE.getServerAddress2()[1]);

                        return map;
                    }

                    /**
                     * 传入Gson实例。（可选）
                     * @return
                     */
                    @Override
                    public Gson getGsonInstance() {
                        return null;
                    }

                });
    }
}
