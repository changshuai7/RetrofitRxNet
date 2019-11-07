package com.shuai.retrofitrx.config.provider;


import com.shuai.retrofitrx.net.Interceptor.ResponseHeaderInterceptor;


/**
 * 响应配置
 */
public abstract class NetResponseConfigProvider {

    /**
     * 定义一个对ResponseHeader进行处理的Handler类
     * 可以对返回的{@link okhttp3.Response}header 进行处理
     */
    public ResponseHeaderInterceptor.ResponseHeaderHandler getHeaderHandler() {
        return null;
    }

    /**
     * 实际网络请求中，可能有些GET 请求的结果，客户端需要缓存一份，以节约请求成本，同时如果服务端在响应头中未做缓存处理，则客户端可配置是否需要缓存的URL：
     * 特定请求的缓存配置
     *
     * @return
     */
    public String[] getCacheUrls() {
        return null;
    }
}
