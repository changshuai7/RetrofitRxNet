package com.shuai.retrofitrx.example.app.net

import com.shuai.retrofitrx.example.app.MyApplication
import com.shuai.retrofitrx.example.app.api.interfaceapi.TestServiceApi
import com.shuai.retrofitrx.example.app.config.MyAppNetRequestConfig
import com.shuai.retrofitrx.example.app.net.core.AppDataCallback
import com.shuai.retrofitrx.example.app.net.core.AppObserver
import com.shuai.retrofitrx.example.app.net.core.BaseObserver
import com.shuai.retrofitrx.example.app.ui.bean.CheckRomBean
import com.shuai.retrofitrx.net.ApiFactory.Companion.getApiService
import com.shuai.retrofitrx.net.retrofit.AuthRetrofitFactory
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * MVP：统一Model层，用于存放所有网络请求，INetProvider的实现类
 *
 * @author changshuai
 */
class NetProvider {

    private val myAuthRetrofitFactory: AuthRetrofitFactory = AuthRetrofitFactory(MyApplication.instance, MyAppNetRequestConfig())

    /**
     * Test请求
     *
     * @param dataCallback
     */
    fun requestCheckRom(app_source: String, isCustomizeRom: Boolean, versionName: String, dataCallback: AppDataCallback<CheckRomBean>) {

        ////////////////////   默认使用Application中配置的AuthRetrofitFactory来请求网络  ////////////////////

        getApiService(TestServiceApi::class.java)
                ?.testRequestGet(app_source, isCustomizeRom, versionName)
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe(AppObserver(dataCallback))


        ////////////////////   使用自定义配置的配置的AuthRetrofitFactory来请求网络  ////////////////////


//        getApiService(myAuthRetrofitFactory,TestServiceApi::class.java)
//                ?.testRequestGet(app_source, isCustomizeRom, versionName)
//                ?.subscribeOn(Schedulers.io())
//                ?.observeOn(AndroidSchedulers.mainThread())
//                ?.subscribe(AppObserver(dataCallback))

//        ApiFactory.getApiService(myAuthRetrofitFactory,TestServiceApi.class)
//                .testRequestGet(app_source, isCustomizeRom, versionname)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new MyObserver<CheckRomBean>(dataCallback));
    }

    companion object {
        private var mInstance: NetProvider? = null
        val instance: NetProvider?
            get() {
                if (mInstance == null) {
                    mInstance = NetProvider()
                }
                return mInstance
            }
    }

}

