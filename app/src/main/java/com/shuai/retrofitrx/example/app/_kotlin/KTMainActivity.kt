package com.shuai.retrofitrx.example.app._kotlin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.TextView
import com.shuai.retrofitrx.example.app.App
import com.shuai.retrofitrx.example.app.R
import com.shuai.retrofitrx.example.app.net.AppDataCallback
import com.shuai.retrofitrx.example.app.net.AppObserver
import com.shuai.retrofitrx.example.app.bean.TestBean
import com.shuai.retrofitrx.net.ApiFactory
import com.shuai.retrofitrx.net.retrofit.AuthRetrofitFactory
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class KTMainActivity : AppCompatActivity() {

    private lateinit var btn: Button
    private lateinit var tv: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        btn = findViewById(R.id.btn_request_net)
        tv = findViewById(R.id.tv_result)

        btn.setOnClickListener {
            requestGetDefault(true,
                    object : AppDataCallback<TestBean>() {
                        override fun onSuccess(data: TestBean?) {
                            tv.text = data?.result
                        }

                        override fun onError(errorCode: Int, errorMsg: String?, throwable: Throwable) {

                        }

                        override fun onFinally() {

                        }

                    }.setDisposable(null)
            )
        }
    }


    private fun requestGetDefault(useConfig: Boolean, dataCallback: AppDataCallback<TestBean>) {
        val myAuthRetrofitFactory = AuthRetrofitFactory(App.instance, KTNetRequestConfig())

        if (useConfig) {
            ApiFactory.getApiService(myAuthRetrofitFactory, KTTestInterface::class.java)
                    ?.testRequestGet("salesorder", false, "v2.6.2.1102-debug")
                    ?.subscribeOn(Schedulers.io())
                    ?.observeOn(AndroidSchedulers.mainThread())
                    ?.subscribe(AppObserver(dataCallback))
        } else {
            ApiFactory.getApiService(KTTestInterface::class.java)
                    ?.testRequestGet("salesorder", false, "v2.6.2.1102-debug")
                    ?.subscribeOn(Schedulers.io())
                    ?.observeOn(AndroidSchedulers.mainThread())
                    ?.subscribe(AppObserver(dataCallback))
        }

    }

}