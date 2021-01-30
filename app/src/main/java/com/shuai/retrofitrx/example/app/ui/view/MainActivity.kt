package com.shuai.retrofitrx.example.app.ui.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.shuai.csnet.example.app.R
import com.shuai.retrofitrx.example.app.net.NetProvider
import com.shuai.retrofitrx.example.app.net.core.AppDataCallback
import com.shuai.retrofitrx.example.app.ui.bean.CheckRomBean

class MainActivity : AppCompatActivity() {

    private lateinit var btn: Button
    private lateinit var tv: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        btn = findViewById(R.id.btn_request_net)
        tv = findViewById(R.id.tv_result)

        btn.setOnClickListener { requestGet() }
    }

    @SuppressLint("SetTextI18n")
    private fun requestGet() {
        NetProvider.instance?.requestCheckRom("salesorder", false, "v2.6.2.1102-debug",
                object : AppDataCallback<CheckRomBean>() {


                    override fun onSuccess(data: CheckRomBean?) {
                        tv!!.text = "请求结果：$data"
                    }

                    override fun onError(errorCode: Int, errorMsg: String?, throwable: Throwable) {
                        tv!!.text = "请求错误：code = $errorCode,errorMsg = $errorMsg"
                    }

                    override fun onFinally() {}
                }.setDisposable(null))


    }
}