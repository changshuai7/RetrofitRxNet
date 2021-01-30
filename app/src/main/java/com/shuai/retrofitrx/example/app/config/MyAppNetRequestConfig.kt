package com.shuai.retrofitrx.example.app.config;

import com.google.gson.Gson;
import com.shuai.csnet.example.app.BuildConfig;
import com.shuai.retrofitrx.config.provider.NetRequestConfigProvider;
import com.shuai.retrofitrx.example.app.constants.MyConstants;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

import static com.shuai.retrofitrx.example.app.api.ServerAddress.ServerAddress1;
import static com.shuai.retrofitrx.example.app.api.ServerAddress.ServerAddress2;
import static com.shuai.retrofitrx.example.app.api.ServerAddress.ServerAddressDefault;

/**
 * 自定义NetRequestConfigProvider
 */
public class MyAppNetRequestConfig extends NetRequestConfigProvider {

    @NotNull
    @Override
    public Map<String,String> getHeaderMap() {
        HashMap<String, String> map = new HashMap<>();
        map.put(MyConstants.HeaderKey.HEADER_1, "header1-----my");
        map.put(MyConstants.HeaderKey.HEADER_2, "header2-----my");

        return map;
    }


    @NotNull
    @Override
    public Map<String,String> getParamsMap() {
        HashMap<String, String> map = new HashMap<>();
        map.put(MyConstants.ParamsKey.PARAMS_1, "tom-----my");
        map.put(MyConstants.ParamsKey.PARAMS_2, "jake-----my");

        return map;
    }


    @NotNull
    @Override
    public Map<String,String> getBodyMap() {
        //code...
        return null;
    }


    @NotNull
    @Override
    public String getBaseUrl() {
        // 建议baseUrl以斜杠结尾，避免Retrofit报错
        return BuildConfig.DEBUG ? ServerAddressDefault[0] : ServerAddressDefault[1];
    }

    @NotNull
    @Override
    public Map<String, String> getBaseUrls() {
        HashMap<String, String> map = new HashMap<>();
        map.put(MyConstants.ServerDomainKey.URL1, BuildConfig.DEBUG ? ServerAddress1[0] : ServerAddress1[1]);
        map.put(MyConstants.ServerDomainKey.URL2, BuildConfig.DEBUG ? ServerAddress2[0] : ServerAddress2[1]);

        return map;
    }

    @Override
    public Gson getGsonInstance() {
        return null;
    }
}
