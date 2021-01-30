package com.shuai.retrofitrx.example.app._java;

import com.google.gson.Gson;
import com.shuai.retrofitrx.config.provider.NetRequestConfigProvider;
import com.shuai.retrofitrx.example.app.BuildConfig;
import com.shuai.retrofitrx.example.app.api.ServerAddress;
import com.shuai.retrofitrx.example.app.constants.AppConstants;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class JavaNetRequestConfig extends NetRequestConfigProvider {

    @NotNull
    @Override
    public Map<String, String> getHeaderMap() {
        HashMap<String, String> map = new HashMap<>();
        map.put(AppConstants.HeaderKey.HEADER_1, "header1-----my");
        map.put(AppConstants.HeaderKey.HEADER_2, "header2-----my");

        return map;
    }

    @NotNull
    @Override
    public Map<String, String> getParamsMap() {
        HashMap<String, String> map = new HashMap<>();
        map.put(AppConstants.ParamsKey.PARAMS_1, "tom-----my");
        map.put(AppConstants.ParamsKey.PARAMS_2, "jake-----my");

        return map;
    }


    @NotNull
    @Override
    public Map<String, String> getBodyMap() {
        //code...
        return super.getBodyMap();
    }


    @NotNull
    @Override
    public String getBaseUrl() {
        // 建议baseUrl以斜杠结尾，避免Retrofit报错
        return BuildConfig.DEBUG ? ServerAddress.INSTANCE.getServerAddressDefault()[0] : ServerAddress.INSTANCE.getServerAddressDefault()[1];
    }

    @NotNull
    @Override
    public Map<String, String> getBaseUrls() {
        HashMap<String, String> map = new HashMap<>();
        map.put(AppConstants.ServerDomainKey.URL1, BuildConfig.DEBUG ? ServerAddress.INSTANCE.getServerAddress1()[0] : ServerAddress.INSTANCE.getServerAddress1()[1]);
        map.put(AppConstants.ServerDomainKey.URL2, BuildConfig.DEBUG ? ServerAddress.INSTANCE.getServerAddress2()[0] : ServerAddress.INSTANCE.getServerAddress2()[1]);

        return map;
    }

    @Override
    public Gson getGsonInstance() {
        return null;
    }
}
