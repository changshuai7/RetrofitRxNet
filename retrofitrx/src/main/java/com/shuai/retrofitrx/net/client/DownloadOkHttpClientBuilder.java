package com.shuai.retrofitrx.net.client;

import android.text.TextUtils;

import com.shuai.retrofitrx.net.download.progress.DownloadManagerResponseListener;
import com.shuai.retrofitrx.net.download.progress.ProgressResponseBody;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class DownloadOkHttpClientBuilder extends AbstractOkHttpClientBuilder {

    private DownloadManagerResponseListener listener;

    public DownloadOkHttpClientBuilder(DownloadManagerResponseListener listener) {
        this.listener = listener;
    }

    @Override
    public void setRequestInterceptor(OkHttpClient.Builder builder) {
        //拦截返回数据增加下载监听
        builder.addNetworkInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                String url = request.url().toString();
                Response orginalResponse = chain.proceed(request);
                String range = request.header("RANGE");
                long startPos = -1;
                long endPos = -1;
                if (!TextUtils.isEmpty(range)) {
                    String[] posValues = range.replace("bytes=", "").split("-");
                    if (posValues.length == 1) {
                        startPos = Long.valueOf(posValues[0]);
                    } else if (posValues.length == 2) {
                        startPos = Long.valueOf(posValues[0]);
                        endPos = Long.valueOf(posValues[1]);
                    }
                }
                return orginalResponse.newBuilder().body(new ProgressResponseBody(url, startPos, orginalResponse.body(), listener)).build();
            }
        });
    }

    @Override
    public void setCache(OkHttpClient.Builder builder) {
        //do nothing
    }
}
