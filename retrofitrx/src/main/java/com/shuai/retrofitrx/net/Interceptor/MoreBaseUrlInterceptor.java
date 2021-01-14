package com.shuai.retrofitrx.net.Interceptor;


import com.shuai.retrofitrx.config.NetConfig;
import com.shuai.retrofitrx.config.provider.NetRequestConfigProvider;
import com.shuai.retrofitrx.constants.NetConstants;
import com.shuai.retrofitrx.utils.Logger;
import com.shuai.retrofitrx.utils.Util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;


/**
 * 针对多个BaseUrl的拦截器
 */

public class MoreBaseUrlInterceptor implements Interceptor {


    private NetRequestConfigProvider netRequestConfigProvider;

    public MoreBaseUrlInterceptor(NetRequestConfigProvider netRequestConfigProvider) {
        this.netRequestConfigProvider = netRequestConfigProvider;
    }

    public MoreBaseUrlInterceptor() {
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

            if (netRequestConfigProvider!= null) {

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

                        List<String> originalPathSegments = Util.removeStrListEmpty(originalUrl.encodedPathSegments());
                        List<String> newBaseUrlPathSegments = Util.removeStrListEmpty(newBaseUrl.encodedPathSegments());
                        List<String> oldBaseUrlPathSegments = Util.removeStrListEmpty(oldBaseUrl.encodedPathSegments());

                        if (!originalPathSegments.containsAll(oldBaseUrlPathSegments)){
                            Logger.e("-->>>>>>"+
                                    "\n框架发现，你请求的完整URL："+originalUrl.toString()+
                                    "\n其中未包含BaseUrl的部分segments字段内容："+oldBaseUrl.toString()+
                                    "\n可能您的path路径使用了相对路径（使用/开头的路径）或者其他路径的错误" +
                                    "\n我们强烈建议您的path采用绝对路径，以保证框架的正常运行" +
                                    "\n此处baseUrl拦截已中断，使用了默认的baseUrl！"+
                                    "\n不了解baseUrl相对/绝对路径的点击这里：https://www.jianshu.com/p/d6b8b6bc6209 学习使你快乐"
                            );
                            return chain.proceed(originalRequest);
                        }

                        ArrayList<String> resultPathSegments = new ArrayList<>();

                        resultPathSegments.addAll(newBaseUrlPathSegments);                  // 添加新的newBaseUrlPathSegments     /cccc/dddd

                        for (int i = 0 ;i <oldBaseUrlPathSegments.size();i++){
                            originalPathSegments.remove(0);                          // 删除originalPathSegments中包含oldBaseUrlPathSegments的部分  aaaa/bbbb/sales/v1 -> /sales/v1
                        }
                        resultPathSegments.addAll(originalPathSegments);                    // 添加修改后的originalPathSegments  /cccc/dddd/sales/v1

                        //old中的PathSegments全部删除
                        HttpUrl.Builder oldBuilder = originalUrl.newBuilder();
                        for (int i = 0; i < originalUrl.pathSize(); i++) {
                            //当删除了上一个 index, PathSegment 的 item 会自动前进一位, 所以 remove(0) 就好
                            oldBuilder.removePathSegment(0);
                        }

                        //重新添加
                        for (String segment : resultPathSegments) {
                            oldBuilder.addEncodedPathSegment(segment);
                        }

                        //重建新的HttpUrl，需要重新设置的url部分
                        HttpUrl newHttpUrl = oldBuilder
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