package com.shuai.retrofitrx.example.app.net.core;


import com.shuai.retrofitrx.example.app.MyApplication;
import com.shuai.csnet.example.app.R;

/**
 * 错误常量字段
 */
public class MyErrorInfoConstant {

    // 登录过期
    public static final int CODE_LOGIN_EXPIRED = -1000;
    // 返回空
    public static final int CODE_RESPONSE_EMPTY = -10001;
    // DNS解析失败
    public static final int CODE_UNKNOWN_HOST = -10002;
    // 请求超时
    public static final int CODE_REQUEST_TIMEOUT = -10003;
    // 响应超时
    public static final int CODE_RESPONSE_TIMEOUT = -10004;
    // 其他异常
    public static final int CODE_OTHER_EXCEPTION = -11000;

    public static final String MESSAGE_UNKNOWN_HOST = getString(R.string.error_network_unknown_host);

    public static final String MESSAGE_REQUEST_TIMEOUT = getString(R.string.error_network_request_timeout);

    public static final String MESSAGE_RESPONSE_TIMEOUT = getString(R.string.error_network_response_timeout);

    public static final String MESSAGE_OTHER_EXCEPTION = getString(R.string.error_network_other_exception);

    public static final String MESSAGE_EXCEPTION = getString(R.string.error_network_request);

    public static final String MESSAGE_RESPONSE_EMPTY = getString(R.string.error_network_response_empty);

    public static String getString(int resId) {
        return MyApplication.getInstance().getString(resId);
    }
}
