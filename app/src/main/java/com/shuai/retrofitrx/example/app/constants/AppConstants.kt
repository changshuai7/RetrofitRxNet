package com.shuai.retrofitrx.example.app.constants

/**
 * 常量配置
 */
class AppConstants {
    object CommonKey {
        const val STATUS = "status"
        const val ERR_MSG = "err_msg"
        const val USER_ID = "user_id"
        const val APP_SOURCE = "app_source"
        const val NEW_HOST_NAME = "new_host_name"
    }

    object ParamsKey {
        const val PARAMS_1 = "params_1"
        const val PARAMS_2 = "params_2"
    }

    object HeaderKey {
        const val HEADER_1 = "my-header1"
        const val HEADER_2 = "my-header2"
    }

    object ServerDomainKey {
        const val URL1 = "url1"
        const val URL2 = "url2"
    }

    object Authority {
        const val FILE_AUTHORITY = "com.example.app.fileProvider"
    }
}