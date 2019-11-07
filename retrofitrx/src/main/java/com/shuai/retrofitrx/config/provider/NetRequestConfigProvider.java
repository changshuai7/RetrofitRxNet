package com.shuai.retrofitrx.config.provider;


import com.google.gson.Gson;

import java.util.Collections;
import java.util.Map;


/**
 * 请求配置
 */
public abstract class NetRequestConfigProvider {

    /**
     * 配置默认Auth：公共请求header。（可选）
     */
    public Map getHeaderMap(){
        return Collections.EMPTY_MAP;
    }

    /**
     * 配置默认Auth：公共post请求的通用参数。（可选）
     */
    public Map getBodyMap() {
        return Collections.EMPTY_MAP;
    }

    /**
     * 配置默认Auth：公共get/post请求的url拼接参数。（可选）
     */
    public Map getParamsMap() {
        return Collections.EMPTY_MAP;
    }

    /**
     * 配置通用Auth默认baseUrl的domain
     */
    public abstract String getBaseUrl();

    /**
     * 可传入多个BaseUrl，通过制定Header中Domain-Host字段来区分。（可选）
     * @return
     */
    public Map<String,String> getBaseUrls(){
        return null;
    }

    /**
     * 传入Gson实例。（可选）
     * @return
     */
    public Gson getGsonInstance(){
        return null;
    }

}
