package com.shuai.retrofitrx.example.app.api.interfaceapi

import com.shuai.retrofitrx.example.app.api.ServerField
import com.shuai.retrofitrx.example.app.constants.MyConstants.ServerDomainKey
import com.shuai.retrofitrx.example.app.net.core.BaseResponse
import com.shuai.retrofitrx.example.app.ui.bean.CheckRomBean
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface TestServiceApi {
    @Headers("Domain-Host:" + ServerDomainKey.URL1) // 加上 Domain-Host header
    @GET(ServerField.CHECK_ROM)
    fun testRequestGet(
            @Query("app_source") app_source: String,
            @Query("isCustomizeRom") isCustomizeRom: Boolean,
            @Query("versionname") versionName: String): Observable<BaseResponse<CheckRomBean>>
}