package com.shuai.retrofitrx.example.app.net.core

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * @author changshuai
 */
class BaseResponse<T> : Serializable {
    @Expose
    @SerializedName("status")
    var status = -99

    @Expose
    @SerializedName("err_msg")
    val errMsg: String? = null

    @Expose
    @SerializedName("data")
    val data: T? = null

    val isSuccess: Boolean
        get() = status == SUCCESS_CODE

    companion object {
        const val SUCCESS_CODE = 0
    }
}