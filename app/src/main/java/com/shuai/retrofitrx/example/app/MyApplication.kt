package com.shuai.retrofitrx.example.app;

import android.app.Application;
import android.os.Handler;

import com.shuai.csnet.example.app.BuildConfig;
import com.shuai.retrofitrx.example.app.constants.MyConstants;
import com.google.gson.Gson;
import com.shuai.retrofitrx.config.NetConfig;
import com.shuai.retrofitrx.config.provider.NetBaseConfigProvider;
import com.shuai.retrofitrx.config.provider.NetRequestConfigProvider;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

import static com.shuai.retrofitrx.example.app.api.ServerAddress.ServerAddress1;
import static com.shuai.retrofitrx.example.app.api.ServerAddress.ServerAddress2;
import static com.shuai.retrofitrx.example.app.api.ServerAddress.ServerAddressDefault;


public class MyApplication extends Application {

    private static MyApplication sInstance;//单例
    private static int mainTid;//主线程ID
    private static Handler baseHandler;//全局Handler


    //获取单例对象
    public static MyApplication getInstance() {
        return sInstance;
    }

    //获取主线程id
    public static int getMainTid() {
        return mainTid;
    }

    //获取Handler
    public static Handler getHandler() {
        return baseHandler;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        mainTid = android.os.Process.myTid();
        baseHandler = new Handler();
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
                        map.put(MyConstants.HeaderKey.HEADER_1, "header1");
                        map.put(MyConstants.HeaderKey.HEADER_2, "header2");

                        return map;
                    }

                    /**
                     * 配置默认Auth：公共get/post请求的url拼接参数。（可选）
                     */
                    @NotNull
                    @Override
                    public Map<String, String> getParamsMap() {
                        HashMap<String, String> map = new HashMap<>();
                        map.put(MyConstants.ParamsKey.PARAMS_1, "tom");
                        map.put(MyConstants.ParamsKey.PARAMS_2, "jake");

                        return map;
                    }

                    /**
                     * 配置默认Auth：公共post请求的通用参数。（可选）
                     */
                    @NotNull
                    @Override
                    public Map<String, String> getBodyMap() {
                        //code...
                        return null;
                    }


                    /**
                     * 配置通用Auth默认baseUrl的domain
                     */
                    @NotNull
                    @Override
                    public String getBaseUrl() {
                        // 建议baseUrl以斜杠结尾，避免Retrofit报错
                        return BuildConfig.DEBUG ? ServerAddressDefault[0] : ServerAddressDefault[1];
                    }

                    /**
                     * 可传入多个BaseUrl，通过制定Header中Domain-Host字段来区分。（可选）
                     * @return
                     */
                    @NotNull
                    @Override
                    public Map<String, String> getBaseUrls() {
                        HashMap<String, String> map = new HashMap<>();
                        map.put(MyConstants.ServerDomainKey.URL1, BuildConfig.DEBUG ? ServerAddress1[0] : ServerAddress1[1]);
                        map.put(MyConstants.ServerDomainKey.URL2, BuildConfig.DEBUG ? ServerAddress2[0] : ServerAddress2[1]);

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
