package com.shuai.retrofitrx.net.Interceptor;


import com.shuai.retrofitrx.config.NetConfig;

import java.io.IOException;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * 响应头拦截
 */

public class ResponseHeaderInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response response = chain.proceed(chain.request());
        handleHeader(response.headers());
        return response;
    }

    private void handleHeader(Headers headers) {
        if (NetConfig.getConfig().getResponseConfigProvider() != null && NetConfig.getConfig().getResponseConfigProvider().getHeaderHandler() != null) {
            ResponseHeaderHandler headerHandler = NetConfig.getConfig().getResponseConfigProvider().getHeaderHandler();
            headerHandler.handleHeader(headers);
        }
    }

    public interface ResponseHeaderHandler {
        void handleHeader(Headers headers);
    }

}
