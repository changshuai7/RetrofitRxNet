package com.shuai.retrofitrx.net.Interceptor;

import android.text.TextUtils;

import com.shuai.retrofitrx.R;
import com.shuai.retrofitrx.config.NetConfig;
import com.shuai.retrofitrx.utils.Logger;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 对于需要缓存的接口，若服务端返回的 response 中没有设置缓存策略，
 * 则设置为1200s(20分钟)过期;
 */

public class CacheControlInterceptor implements Interceptor {

    private static final String TAG = CacheControlInterceptor.class.getSimpleName();
    private final int CACHE_TIME = 1200;

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response = chain.proceed(request);
        if (isCachedUrl(request.url().toString())) {
            if (response.cacheControl().maxAgeSeconds() == -1 || response.cacheControl().noCache()) {
                return response.newBuilder()
                        .addHeader(NetConfig.getApp().getString(R.string.config_header_cache_key),
                                NetConfig.getApp()
                                        .getString(R.string.config_header_cache_value, CACHE_TIME))
                        .build();
            }
        }
        return response;
    }

    private static boolean isCachedUrl(String url) {
        String[] mCachedUrls = null;
        if (NetConfig.getConfig().getResponseConfigProvider() != null) {
            mCachedUrls = NetConfig.getConfig().getResponseConfigProvider().getCacheUrls();
        }

        if (mCachedUrls != null && mCachedUrls.length > 0) {
            for (String cachedUrl : mCachedUrls) {
                if (!TextUtils.isEmpty(url) && url.contains(cachedUrl)) {
                    if (NetConfig.getConfig().getBaseConfig().isDebug()) {
                        Logger.i(TAG, "Cache One");
                    }
                    return true;
                }
            }
        }
        return false;
    }
}
