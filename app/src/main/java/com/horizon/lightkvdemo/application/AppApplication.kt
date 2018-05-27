package com.horizon.lightkvdemo.application


import android.app.Application

class AppApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // if app has multiple processes,
        // you need to filter other processes,
        // in case files damaged in multiple processes mode
        AppIntiHelper.inti(this)
    }
}
