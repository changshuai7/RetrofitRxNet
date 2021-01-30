package com.shuai.retrofitrx.example.app.api

/**
 * 配置服务器地址
 * String[0] ：Release地址
 * String[1] ：Debug地址
 */
object ServerAddress {

    //默认服务器地址
    val ServerAddressDefault = arrayOf("http://123.123.123.123:8001/aaaa/bbbb/cccc/dddd/", "http://www.a.com")

    //第一类服务器地址
    val ServerAddress1 = arrayOf("http://47.95.43.1/qqqq/rrrr/ssss/gggg", "http://www.b.com")

    //第二类服务器地址
    val ServerAddress2 = arrayOf("http://111.222.333.444:8001", "http://www.c.com")
}