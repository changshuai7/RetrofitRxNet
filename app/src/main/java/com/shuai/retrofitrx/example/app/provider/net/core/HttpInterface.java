package com.shuai.retrofitrx.example.app.provider.net.core;

import io.reactivex.disposables.CompositeDisposable;

/**
 * MVP-回调接口
 * Model->Presenter回调接口
 *
 * @author changshuai
 */
public class HttpInterface {

    /**
     * 此回调遵从标准onSuccess、onFail、onFinally回调。tagData为扩展字段
     *
     * @param <T>
     */
    public static abstract class DataCallback<T> {
        private CompositeDisposable mDisposable;

        public DataCallback(CompositeDisposable disposable) {
            this.mDisposable = disposable;
        }

        public CompositeDisposable getDisposable() {
            return mDisposable;
        }

        public abstract void onSuccess(T data);

        public abstract void onError(int errorCode, String errorMsg, Throwable throwable);

        public abstract void onFinally();
    }


}
