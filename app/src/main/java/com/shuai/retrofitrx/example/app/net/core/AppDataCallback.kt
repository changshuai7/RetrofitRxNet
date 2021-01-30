package com.shuai.retrofitrx.example.app.net.core

import io.reactivex.disposables.CompositeDisposable

/**
 * MVP-回调接口
 * Model->Presenter回调接口
 *
 */
abstract class AppDataCallback<T> {

    //用户控制RxJava对网络请求的取消订阅
    var disposable: CompositeDisposable? = null
        private set

    fun setDisposable(disposable: CompositeDisposable?): AppDataCallback<T> {
        this.disposable = disposable
        return this
    }

    abstract fun onSuccess(data: T?)
    abstract fun onError(errorCode: Int, errorMsg: String?, throwable: Throwable)
    abstract fun onFinally()

}