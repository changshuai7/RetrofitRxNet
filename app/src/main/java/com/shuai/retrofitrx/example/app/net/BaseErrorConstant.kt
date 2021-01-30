package com.shuai.retrofitrx.example.app.net

import com.shuai.retrofitrx.example.app.App
import com.shuai.retrofitrx.example.app.R

/**
 * 错误常量字段
 */
object BaseErrorConstant {

    // 登录过期 需要和后端确定登录过期的code码是多少
    const val CODE_LOGIN_EXPIRED = -1000

    // DNS解析失败
    const val CODE_UNKNOWN_HOST = -10002
    val MESSAGE_UNKNOWN_HOST: String = getString(R.string.error_network_unknown_host)

    // 请求超时
    const val CODE_REQUEST_TIMEOUT = -10003
    val MESSAGE_REQUEST_TIMEOUT: String = getString(R.string.error_network_request_timeout)

    // 响应超时
    const val CODE_RESPONSE_TIMEOUT = -10004
    val MESSAGE_RESPONSE_TIMEOUT: String = getString(R.string.error_network_response_timeout)

    // 其他异常
    const val CODE_OTHER_EXCEPTION = -11000
    val MESSAGE_OTHER_EXCEPTION: String = getString(R.string.error_network_other_exception)

    val MESSAGE_EXCEPTION: String = getString(R.string.error_network_request)

    private fun getString(resId: Int): String {
        return App.instance.getString(resId)
    }
}