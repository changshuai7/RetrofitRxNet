package com.shuai.retrofitrx.example.app._kotlin

import com.shuai.retrofitrx.constants.NetConstants
import com.shuai.retrofitrx.example.app.api.ServerField
import com.shuai.retrofitrx.example.app.constants.AppConstants
import com.shuai.retrofitrx.example.app.net.BaseResponse
import com.shuai.retrofitrx.example.app.bean.TestBean
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface KTTestInterface {

    @Headers("${NetConstants.HeaderKey.DomainHost}:" + AppConstants.ServerDomainKey.URL1)
    @GET(ServerField.TEST_CHECK_ROM)
    fun testRequestGet(
            @Query("app_source") app_source: String,
            @Query("isCustomizeRom") isCustomizeRom: Boolean,
            @Query("versionname") versionName: String): Observable<BaseResponse<TestBean>>
}