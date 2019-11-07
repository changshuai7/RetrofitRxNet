package com.shuai.retrofitrx.example.app.provider.net.core.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * @author changshuai
 */
public class BaseResponse<T> extends MyResponse implements Serializable {

    @Expose
    @SerializedName("data")
    private T data;

    public T getData() {
        return data;
    }

}
