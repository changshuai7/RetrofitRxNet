package com.shuai.retrofitrx.example.app.net.core

import android.widget.Toast
import com.shuai.retrofitrx.example.app.MyApplication
import com.shuai.retrofitrx.utils.Util.Companion.isStrNullOrEmpty

/**
 * 统一BaseObserver处理
 *
 * @author changshuai
 */
class AppObserver<T>(private val dataCallback: AppDataCallback<T>) : BaseObserver<T>() {

    override fun handleError(errorCode: Int, errMsg: String?, err: Throwable) {
        if (!isStrNullOrEmpty(errMsg)) {
            Toast.makeText(MyApplication.instance, "$errMsg($errorCode)", Toast.LENGTH_SHORT).show()
        } else if (errorCode != 0) {
            Toast.makeText(MyApplication.instance, errorCode.toString() + "", Toast.LENGTH_SHORT).show()
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