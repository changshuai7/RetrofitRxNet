package com.shuai.retrofitrx.example.app.net

import android.widget.Toast
import com.shuai.retrofitrx.example.app.App
import com.shuai.retrofitrx.utils.Util

/**
 * 统一BaseObserver处理
 *
 */
class AppObserver<T>(private val dataCallback: AppDataCallback<T>) : BaseObserver<T>() {

    override fun handleError(errorCode: Int, errMsg: String?, err: Throwable) {
        if (!Util.isStrNullOrEmpty(errMsg)) {
            Toast.makeText(App.instance, "$errMsg($errorCode)", Toast.LENGTH_SHORT).show()
        } else if (errorCode != 0) {
            Toast.makeText(App.instance, errorCode.toString() + "", Toast.LENGTH_SHORT).show()
        }
        dataCallback.onError(errorCode, errMsg, err)
    }

    override fun handleData(response: BaseResponse<T>) {
        dataCallback.onSuccess(response.data)
    }

    override fun handleFinally() {
        dataCallback.onFinally()
    }
}