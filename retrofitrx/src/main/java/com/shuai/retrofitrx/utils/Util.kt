package com.shuai.retrofitrx.utils

import okhttp3.HttpUrl
import java.util.*

/**
 * 提取出一些框架中可能用到的工具类
 */
class Util {
    companion object {

        /**
         * Map是否为空
         *
         * @param map
         * @return
         */
        fun mapIsEmpty(map: Map<*, *>?): Boolean {
            return map == null || map.isEmpty()
        }

        /**
         * 检查URL是否合法
         *
         * @param url
         * @return
         */
        fun checkUrl(url: String?): Boolean {
            if (url == null) {
                NetLogger.e("URL检查出异常：", "url为空")
                return false
            }
            val httpUrl = HttpUrl.parse(url)
            if (httpUrl == null) {
                NetLogger.e("URL检查出异常：", "$url->为非法URL")
                return false
            }
            return true
        }

        /**
         * 去除String类型List中的空
         * @param list
         * @return
         */
        fun removeStrListEmpty(list: List<String?>): MutableList<String?> {
            val result: ArrayList<String?> = ArrayList<String?>()
            for (str in list) {
                if (!isStrNullOrEmpty(str)) {
                    result.add(str)
                }
            }
            return result
        }

        /**
         * 判断CharSequence是否为空
         *
         * @param string
         * @return
         */
        fun isNullOrEmpty(string: CharSequence?): Boolean {
            return string == null || string.isEmpty()
        }

        /**
         * 判断String是否为空（内部trim校验）
         *
         * @param string
         * @return
         */
        @JvmStatic
        fun isStrNullOrEmpty(string: String?): Boolean {
            return string == null || string.trim { it <= ' ' }.isEmpty()
        }
    }


}