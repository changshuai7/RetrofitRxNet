package com.shuai.retrofitrx.example.app.net.core

import com.shuai.csnet.example.app.R
import com.shuai.retrofitrx.example.app.MyApplication

/**
 * 错误常量字段
 */
object MyErrorInfoConstant {

    const val CODE_LOGIN_EXPIRED = -1000// 登录过期
    const val CODE_RESPONSE_EMPTY = -10001 // 返回空
    const val CODE_UNKNOWN_HOST = -10002 // DNS解析失败
    const val CODE_REQUEST_TIMEOUT = -10003 // 请求超时
    const val CODE_RESPONSE_TIMEOUT = -10004// 响应超时
    const val CODE_OTHER_EXCEPTION = -11000 // 其他异常
    val MESSAGE_UNKNOWN_HOST = getString(R.string.error_network_unknown_host)
    val MESSAGE_REQUEST_TIMEOUT = getString(R.string.error_network_request_timeout)
    val MESSAGE_RESPONSE_TIMEOUT = getString(R.string.error_network_response_timeout)
    val MESSAGE_OTHER_EXCEPTION = getString(R.string.error_network_other_exception)
    val MESSAGE_EXCEPTION = getString(R.string.error_network_request)
    val MESSAGE_RESPONSE_EMPTY = getString(R.string.error_network_response_empty)
    private fun getString(resId: Int): String {
        return MyApplication.instance.getString(resId)
    }
}