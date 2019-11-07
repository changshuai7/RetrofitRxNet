package com.shuai.retrofitrx.net.Interceptor;

import com.shuai.retrofitrx.config.NetConfig;
import com.shuai.retrofitrx.config.provider.NetRequestConfigProvider;
import com.shuai.retrofitrx.utils.Util;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;


public class AuthParamsInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originRequest = chain.request();

        // 添加公共参数
        HttpUrl.Builder newUrlBuilder = originRequest.url().newBuilder();
        Map<String, String> defaultParams = getDefaultUrlParams();
        if (defaultParams != null && !defaultParams.isEmpty()) {
            for (Map.Entry<String, String> param : defaultParams.entrySet()) {
                newUrlBuilder.addQueryParameter(param.getKey(), param.getValue());
            }
        }

        // 添加公共 Header
        Request.Builder newRequestBuilder = originRequest.newBuilder()
                .url(newUrlBuilder.build());
        Map<String, String> defaultHeaders = getDefaultHeaders();
        if (defaultHeaders != null && !defaultHeaders.isEmpty()) {
            for (Map.Entry<String, String> entry : defaultHeaders.entrySet()) {
                newRequestBuilder.addHeader(entry.getKey(), entry.getValue());
            }
        }


        // Post 请求添加统一Body
        if ("POST".equalsIgnoreCase(originRequest.method())) {
            if (originRequest.body() instanceof FormBody) {
                Map<String, String> defaultBodyParams = getDefaultBodyParams();
                if (!Util.MapIsEmpty(defaultBodyParams)) {
                    FormBody.Builder newFormBody = new FormBody.Builder();
                    FormBody oldFormBody = (FormBody) originRequest.body();
                    if (oldFormBody != null && oldFormBody.size() > 0) {
                        for (int i = 0; i < oldFormBody.size(); i++) {
                            newFormBody.addEncoded(oldFormBody.encodedName(i), oldFormBody.encodedValue(i));
                        }
                    }
                    for (Map.Entry<String, String> entry : defaultBodyParams.entrySet()) {
                        if (entry != null && entry.getKey() != null && entry.getValue() != null) {
                            newFormBody.add(entry.getKey(), entry.getValue());
                        }
                    }
                    newRequestBuilder.method(originRequest.method(), newFormBody.build());
                }
            }
        }

        Request newRequest = newRequestBuilder.build();
        return chain.proceed(newRequest);
    }

    private Map getDefaultUrlParams() {
        NetRequestConfigProvider provider = NetConfig.getConfig().getRequestConfigProvider();
        if (provider != null) {
            return provider.getParamsMap();
        }
        return Collections.EMPTY_MAP;
    }

    private Map getDefaultHeaders() {
        NetRequestConfigProvider provider = NetConfig.getConfig().getRequestConfigProvider();
        if (provider != null) {
            return provider.getHeaderMap();
        }
        return Collections.EMPTY_MAP;
    }

    private Map getDefaultBodyParams() {
        NetRequestConfigProvider provider = NetConfig.getConfig().getRequestConfigProvider();
        if (provider != null) {
            return provider.getBodyMap();
        }
        return Collections.EMPTY_MAP;
    }

}
