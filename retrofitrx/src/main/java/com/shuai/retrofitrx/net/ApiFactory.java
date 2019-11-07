package com.shuai.retrofitrx.net;

import com.shuai.retrofitrx.config.NetConfig;
import com.shuai.retrofitrx.net.retrofit.AuthRetrofitFactory;

import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * 获取网络请求的server实例
 * @author changshuai
 */

public class ApiFactory {

    private static Map<Class, WeakReference<Object>> apis = new ConcurrentHashMap<>();
    private static AuthRetrofitFactory authRetrofitFactory = new AuthRetrofitFactory(null, NetConfig.getApp());

    public static <T> T getApiService(Class<T> clazz) {
        if (apis.get(clazz) != null && apis.get(clazz).get() != null) {
            return (T) apis.get(clazz).get();
        }
        T t = authRetrofitFactory.create().create(clazz);
        apis.put(clazz, new WeakReference<Object>(t));
        return t;
    }

}