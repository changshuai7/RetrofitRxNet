package com.shuai.retrofitrx.example.app.net;

import com.shuai.retrofitrx.example.app.MyApplication;
import com.shuai.retrofitrx.example.app.api.interfaceapi.TestServiceApi;
import com.shuai.retrofitrx.example.app.config.MyAppNetRequestConfig;
import com.shuai.retrofitrx.example.app.net.core.AppDataCallback;
import com.shuai.retrofitrx.example.app.net.core.AppObserver;
import com.shuai.retrofitrx.example.app.ui.bean.CheckRomBean;
import com.shuai.retrofitrx.net.ApiFactory;
import com.shuai.retrofitrx.net.retrofit.AuthRetrofitFactory;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


/**
 * MVP：统一Model层，用于存放所有网络请求，INetProvider的实现类
 *
 * @author changshuai
 */

public class NetProvider  {


    private static NetProvider mInstance;
    private AuthRetrofitFactory myAuthRetrofitFactory;


    public NetProvider() {
        myAuthRetrofitFactory = new AuthRetrofitFactory(MyApplication.getInstance(), new MyAppNetRequestConfig());
    }

    public static NetProvider getInstance() {
        if (mInstance == null) {
            mInstance = new NetProvider();
        }
        return mInstance;
    }


    /**
     * Test请求
     *
     * @param dataCallback
     */

    public void requestCheckRom(String app_source, boolean isCustomizeRom, String versionname, AppDataCallback dataCallback) {

        ////////////////////   默认使用Application中配置的AuthRetrofitFactory来请求网络  ////////////////////

        ApiFactory.getApiService(TestServiceApi.class)
                .testRequestGet(app_source, isCustomizeRom, versionname)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new AppObserver<CheckRomBean>(dataCallback));


        ////////////////////   使用自定义配置的配置的AuthRetrofitFactory来请求网络  ////////////////////

//        ApiFactory.getApiService(myAuthRetrofitFactory,TestServiceApi.class)
//                .testRequestGet(app_source, isCustomizeRom, versionname)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new MyObserver<CheckRomBean>(dataCallback));


    }


}