package com.shuai.retrofitrx.example.app.provider.net.core.observer;

import android.widget.Toast;

import com.shuai.retrofitrx.example.app.MyApplication;
import com.shuai.retrofitrx.example.app.provider.net.core.HttpInterface;
import com.shuai.retrofitrx.example.app.provider.net.core.bean.BaseResponse;
import com.shuai.retrofitrx.utils.Util;


/**
 * 统一BaseObserver处理
 *
 * @author changshuai
 */

public class MyObserver<T> extends BaseObserver<T> {

    private HttpInterface.DataCallback dataCallback;

    public HttpInterface.DataCallback getDataCallback() {
        return dataCallback;
    }

    public MyObserver(HttpInterface.DataCallback dataCallback) {
        super(MyApplication.getInstance());
        this.dataCallback = dataCallback;
    }

    @Override
    public void handleError(final int i, final String s, Throwable throwable) {

        // TODO :这里考虑一下异步线程Toast可能会引发的异常。
        if (!Util.isStrNullOrEmpty(s)) {
            Toast.makeText(MyApplication.getInstance(), s + "(" + i + ")", Toast.LENGTH_SHORT).show();
        } else if (i != 0) {
            Toast.makeText(MyApplication.getInstance(), i + "", Toast.LENGTH_SHORT).show();
        }
        dataCallback.onError(i, s, throwable);
    }

    @Override
    public void handleData(BaseResponse<T> baseResponse) {
        dataCallback.onSuccess(baseResponse.getData());
    }

    @Override
    public void onFinally() {
        dataCallback.onFinally();
    }
}
