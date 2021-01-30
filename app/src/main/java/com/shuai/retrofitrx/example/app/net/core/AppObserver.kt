package com.shuai.retrofitrx.example.app.net.core;

import android.widget.Toast;

import com.shuai.retrofitrx.example.app.MyApplication;
import com.shuai.retrofitrx.utils.Util;


/**
 * 统一BaseObserver处理
 *
 * @author changshuai
 */

public class AppObserver<T> extends BaseObserver<T> {

    private final AppDataCallback<T> dataCallback;

    public AppObserver(AppDataCallback<T> dataCallback) {
        super(MyApplication.getInstance());
        this.dataCallback = dataCallback;
    }

    @Override
    public void handleError(final int i, final String s, Throwable throwable) {

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
    public void handleFinally() {
        dataCallback.onFinally();
    }
}
