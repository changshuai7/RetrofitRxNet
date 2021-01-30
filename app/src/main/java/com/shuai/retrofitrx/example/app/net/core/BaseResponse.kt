package com.shuai.retrofitrx.example.app.net.core;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * @author changshuai
 */
public class BaseResponse<T> implements Serializable {

    public static final int SUCCESS_CODE = 0;

    @Expose
    @SerializedName("status")
    private int status = -99;

    @Expose
    @SerializedName("err_msg")
    private String errMsg;

    @Expose
    @SerializedName("data")
    private T data;

    public T getData() {
        return data;
    }


    public boolean isSuccess() {
        return this.status == SUCCESS_CODE;
    }

    public int getStatus() {
        return status;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
