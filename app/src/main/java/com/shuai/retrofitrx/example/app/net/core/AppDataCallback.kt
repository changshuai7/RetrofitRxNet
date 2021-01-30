package com.shuai.retrofitrx.example.app.net.core;

import io.reactivex.disposables.CompositeDisposable;

/**
 * MVP-回调接口
 * Model->Presenter回调接口
 *
 */
public abstract class AppDataCallback<T> {

    private CompositeDisposable disposable;//用户控制RxJava对网络请求的取消订阅

    public CompositeDisposable getDisposable() {
        return disposable;
    }

    public AppDataCallback<T> setDisposable(CompositeDisposable disposable) {
        this.disposable = disposable;
        return this;
    }

    public abstract void onSuccess(T data);

    public abstract void onError(int errorCode, String errorMsg, Throwable throwable);

    public abstract void onFinally();
}