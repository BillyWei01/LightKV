package com.horizon.lightkvdemo.config


import android.annotation.SuppressLint
import android.content.Context

import com.horizon.lightkvdemo.BuildConfig

@SuppressLint("StaticFieldLeak")
object GlobalConfig {
    val DEBUG = BuildConfig.DEBUG

    var appContext: Context? = null
}
