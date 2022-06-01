package com.fsoc.sheathcare.presentation

import android.app.Application
import com.fsoc.sheathcare.common.di.AppComponent
import com.fsoc.sheathcare.common.di.DaggerAppComponent
import com.fsoc.sheathcare.common.di.module.ApiModule
import com.fsoc.sheathcare.common.di.module.CommonModule
import com.fsoc.sheathcare.common.di.module.DaoModule
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger

class MyApplication : Application() {

    private lateinit var appComponent: AppComponent

    companion object {
        lateinit var instance: MyApplication
            private set
    }

    fun getAppComponent(): AppComponent {
        return appComponent
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        initDI()
        initLogger()
    }


    private fun initDI() {
        appComponent = DaggerAppComponent.builder()
            .daoModule(DaoModule(this))
            .apiModule(ApiModule(this))
            .commonModule(CommonModule(this))
            .build()
    }

    private fun initLogger() {
        Logger.addLogAdapter(AndroidLogAdapter())
//        Logger.addLogAdapter(object : AndroidLogAdapter() {
//            override fun isLoggable(priority: Int, tag: String?): Boolean {
//                return BuildConfig.DEBUG
//            }
//        })
    }
}