package com.shuai.retrofitrx.example.app

import android.app.Application

open class App : Application() {

    companion object {
        //获取单例对象  //单例
        @JvmStatic
        lateinit var instance: App
            private set

    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}