package com.shuai.retrofitrx.net.Interceptor;



import com.shuai.retrofitrx.config.NetConfig;
import com.shuai.retrofitrx.config.provider.NetRequestConfigProvider;
import com.shuai.retrofitrx.constants.NetConstants;
import com.shuai.retrofitrx.utils.Logger;
import com.shuai.retrofitrx.utils.Util;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;


/**
 * 针对多个BaseUrl的拦截器
 * 此拦截器只能拦截被替换的baseUrl的host、port部分。segments无法识别
 */

public class MoreBaseUrlInterceptorSimple implements Interceptor {


    private NetRequestConfigProvider netRequestConfigProvider;

    public MoreBaseUrlInterceptorSimple(NetRequestConfigProvider netRequestConfigProvider) {
        this.netRequestConfigProvider = netRequestConfigProvider;
    }

    public MoreBaseUrlInterceptorSimple() {
        this.netRequestConfigProvider = NetConfig.getConfig().getRequestConfigProvider();
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        //获取原始的originalRequest
        Request originalRequest = chain.request();
        //获取老的url
        HttpUrl originalUrl = originalRequest.url();
        //获取originalRequest的创建者builder
        Request.Builder builder = originalRequest.newBuilder();
        //获取头信息的集合如
        List<String> domainHostList = originalRequest.headers(NetConstants.HeaderKey.DomainHost);

        Logger.d("未拦截替换baseUrl前的原始URL:"+originalUrl.toString());

        if (domainHostList != null && domainHostList.size() > 0) {
            //删除原有配置中的值,就是namesAndValues集合里的值
            builder.removeHeader(NetConstants.HeaderKey.DomainHost);
            //获取头信息中配置的value
            String domainName = domainHostList.get(0);

            if (netRequestConfigProvider != null) {

                Map<String, String> baseUrls = netRequestConfigProvider.getBaseUrls();

                if (baseUrls != null && baseUrls.size() > 0 && baseUrls.keySet().contains(domainName) && Util.checkUrl(netRequestConfigProvider.getBaseUrl())) {
                    HttpUrl oldBaseUrl = HttpUrl.parse(netRequestConfigProvider.getBaseUrl());
                    HttpUrl newBaseUrl = null;

                    for (Map.Entry<String, String> entry : baseUrls.entrySet()) {
                        String key = entry.getKey();
                        String value = entry.getValue();
                        if (key.equals(domainName)) {
                            if (Util.checkUrl(value)){
                                newBaseUrl = HttpUrl.parse(value);
                                break;
                            }
                        }
                    }

                    if (oldBaseUrl != null && newBaseUrl != null){
                        //重建新的HttpUrl，需要重新设置的url部分
                        HttpUrl newHttpUrl = originalUrl.newBuilder()
                                .scheme(newBaseUrl.scheme())//http协议如：http或者https
                                .host(newBaseUrl.host())//主机地址
                                .port(newBaseUrl.port())//端口
                                .build();

                        //获取处理后的新newRequest
                        Request newRequest = builder.url(newHttpUrl).build();
                        return chain.proceed(newRequest);
                    }

                }

            }

        }
        return chain.proceed(originalRequest);

    }
}