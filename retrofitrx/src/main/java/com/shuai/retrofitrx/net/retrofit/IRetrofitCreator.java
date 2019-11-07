package com.shuai.retrofitrx.net.retrofit;


import retrofit2.Retrofit;

/**
 * @author changshuai
 */
public interface IRetrofitCreator {

    Retrofit create();

    Retrofit create(String baseUrl);

}
