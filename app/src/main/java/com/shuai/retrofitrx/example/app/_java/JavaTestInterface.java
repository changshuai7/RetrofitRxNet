package com.shuai.retrofitrx.example.app._java;

import com.shuai.retrofitrx.constants.NetConstants;
import com.shuai.retrofitrx.example.app.api.ServerField;
import com.shuai.retrofitrx.example.app.constants.AppConstants;
import com.shuai.retrofitrx.example.app.net.BaseResponse;
import com.shuai.retrofitrx.example.app.bean.TestBean;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;


public interface JavaTestInterface {

    @Headers({NetConstants.HeaderKey.DomainHost + ":" + AppConstants.ServerDomainKey.URL1})
    @GET(ServerField.TEST_CHECK_ROM)
    Observable<BaseResponse<TestBean>> testRequestGet(
            @Query("app_source") String appSource,
            @Query("isCustomizeRom") boolean isCustomizeRom,
            @Query("versionname") String versionName);

}
