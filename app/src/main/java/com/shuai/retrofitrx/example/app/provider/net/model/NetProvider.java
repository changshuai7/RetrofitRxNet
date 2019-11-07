package com.shuai.retrofitrx.example.app.provider.net.model;



import com.shuai.retrofitrx.example.app.api.interfaceapi.TestServiceApi;
import com.shuai.retrofitrx.example.app.provider.net.INetProvider;
import com.shuai.retrofitrx.example.app.provider.net.core.HttpInterface;
import com.shuai.retrofitrx.example.app.provider.net.core.observer.MyObserver;
import com.shuai.retrofitrx.example.app.ui.bean.CheckRomBean;
import com.shuai.retrofitrx.net.ApiFactory;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


/**
 * MVP：统一Model层，用于存放所有网络请求，INetProvider的实现类
 *
 * @author changshuai
 */

public class NetProvider implements INetProvider {

    /**
     * Test请求
     *
     * @param dataCallback
     */
    @Override
    public void requestCheckRom(String app_source, boolean isCustomizeRom, String versionname, HttpInterface.DataCallback dataCallback) {

        ApiFactory.getApiService(TestServiceApi.class)
                .testRequestGet(app_source, isCustomizeRom, versionname)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MyObserver<CheckRomBean>(dataCallback));


//        AuthRetrofitFactory factory = new AuthRetrofitFactory(MyApplication.getInstance());
//        TestServiceApi testServiceApi = factory.create("http://123.345.345.234:9999").create(TestServiceApi.class);
//
//        testServiceApi
//                .testRequestGet(app_source, isCustomizeRom, versionname)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new MyObserver<CheckRomBean>(dataCallback));



    }


}
