package com.shuai.retrofitrx.example.app.ui.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 @author changshuai
 */

public class CheckRomBean implements Serializable {

    @SerializedName("result")
    private String result;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "CheckRomBean{" +
                "result='" + result + '\'' +
                '}';
    }
}
