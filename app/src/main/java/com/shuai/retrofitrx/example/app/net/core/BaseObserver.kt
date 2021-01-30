package com.shuai.retrofitrx.example.app.net.core

import android.text.TextUtils
import com.shuai.csnet.example.app.R
import com.shuai.retrofitrx.config.NetConfig
import com.shuai.retrofitrx.example.app.constants.MyConstants.CommonKey
import com.shuai.retrofitrx.net.exception.RequestException
import io.reactivex.Observer
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import org.json.JSONObject
import retrofit2.HttpException
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * 最底层Observer
 *
 * @param <T>
</T> */
abstract class BaseObserver<T> : Observer<BaseResponse<T>> {

    private var compositeDisposable: CompositeDisposable? = null

    constructor()

    constructor(compositeDisposable: CompositeDisposable?) {
        this.compositeDisposable = compositeDisposable
    }

    // 错误处理
    abstract fun handleError(errorCode: Int, errMsg: String?, err: Throwable)

    // 正常处理
    abstract fun handleData(response: BaseResponse<T>)

    // 无论正常还是异常返回，都会走到这里
    abstract fun handleFinally()


    // onComplete 调用之后再调用onNext或者onError都不会生效
    override fun onComplete() {
        handleFinally()
    }

    override fun onError(e: Throwable) {
        parseError(e)
        e.printStackTrace()
        handleFinally()
    }

    override fun onSubscribe(d: Disposable) {
        compositeDisposable?.add(d)
    }

    override fun onNext(baseResponse: BaseResponse<T>) {
        when {
            baseResponse.isSuccess -> { // 成功
                handleData(baseResponse)
            }
            baseResponse.status == MyErrorInfoConstant.CODE_LOGIN_EXPIRED -> { //登陆过期
                handleLogout()
            }
            else -> { //其他错误
                val errorCode = baseResponse.status
                val errMsg = if (!TextUtils.isEmpty(baseResponse.errMsg)) baseResponse.errMsg else MyErrorInfoConstant.MESSAGE_EXCEPTION
                handleError(errorCode, errMsg, RequestException(errMsg!!))
            }
        }
    }

    private fun parseError(ex: Throwable) {
        val errorCode: Int
        val errMsg: String?
        if (ex is HttpException) {
            errorCode = ex.code()
            errMsg = ex.message()
            if (ex.response() != null) {
                val responseBody = ex.response().errorBody()
                try {
                    if (responseBody != null) {
                        val data = responseBody.string()
                        checkLoginExpired(data)
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        } else if (ex is UnknownHostException) {
            errorCode = MyErrorInfoConstant.CODE_UNKNOWN_HOST
            errMsg = MyErrorInfoConstant.MESSAGE_UNKNOWN_HOST
        } else if (ex is ConnectException) {
            errorCode = MyErrorInfoConstant.CODE_REQUEST_TIMEOUT
            errMsg = MyErrorInfoConstant.MESSAGE_REQUEST_TIMEOUT
        } else if (ex is SocketTimeoutException) {
            errorCode = MyErrorInfoConstant.CODE_RESPONSE_TIMEOUT
            errMsg = MyErrorInfoConstant.MESSAGE_RESPONSE_TIMEOUT
        } else {
            errorCode = MyErrorInfoConstant.CODE_OTHER_EXCEPTION
            errMsg = NetConfig.app.getString(R.string.error_network_other_exception, ex.localizedMessage)
        }
        handleError(errorCode, errMsg, ex)
    }


    private fun checkLoginExpired(response: String): Boolean {
        return if (TextUtils.isEmpty(response)) {
            false
        } else try {
            val jsonObject = JSONObject(response)
            val status = jsonObject.getInt(CommonKey.STATUS)
            if (status == MyErrorInfoConstant.CODE_LOGIN_EXPIRED) {
                handleLogout()
                return true
            }
            false
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    private fun handleLogout() {

        // TODO :登录过期的方法
    }

}