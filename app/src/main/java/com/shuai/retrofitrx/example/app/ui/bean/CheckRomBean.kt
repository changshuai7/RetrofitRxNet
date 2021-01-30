package com.shuai.retrofitrx.example.app.ui.bean

import java.io.Serializable

/**
 * @author changshuai
 */
class CheckRomBean : Serializable {
    var result: String = ""
    override fun toString(): String {
        return "CheckRomBean(result='$result')"
    }

}