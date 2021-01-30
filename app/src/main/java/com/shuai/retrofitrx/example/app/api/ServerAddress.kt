package com.shuai.retrofitrx.example.app.api

/**
 * 配置服务器地址
 * String[0] ：Release地址
 * String[1] ：Debug地址
 */
object ServerAddress {
    val ServerAddressDefault = arrayOf("http://123.123.123.123:8001/aaaa/bbbb/cccc/dddd/", "http://www.a.com")
    val ServerAddress1 = arrayOf<String>("http://39.105.99.196:8001/qqqq/rrrr/ssss/gggg/", "http://www.b.com")
    val ServerAddress2 = arrayOf<String>("http://111.222.333.444:8001", "http://www.c.com")
}