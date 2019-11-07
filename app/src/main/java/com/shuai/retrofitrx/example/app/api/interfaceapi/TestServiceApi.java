package com.shuai.retrofitrx.example.app.api.interfaceapi;



import com.shuai.retrofitrx.example.app.api.ServerField;
import com.shuai.retrofitrx.example.app.constants.MyConstants;
import com.shuai.retrofitrx.example.app.provider.net.core.bean.BaseResponse;
import com.shuai.retrofitrx.example.app.ui.bean.CheckRomBean;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface TestServiceApi {


    @Headers({"Domain-Host:" + MyConstants.ServerDomainKey.URL1})// 加上 Domain-Host header
    @GET(ServerField.CHECK_ROM)
    Observable<BaseResponse<CheckRomBean>> testRequestGet(@Query("app_source") String app_source, @Query("isCustomizeRom") boolean isCustomizeRom, @Query("versionname") String versionname);


}