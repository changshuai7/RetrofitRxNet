package com.shuai.retrofitrx.net.retrofit

import retrofit2.Retrofit

/**
 * @author changshuai
 */
interface IRetrofitCreator {
    fun create(): Retrofit?
}