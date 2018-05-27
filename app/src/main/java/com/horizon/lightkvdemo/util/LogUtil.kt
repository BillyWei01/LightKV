package com.horizon.lightkvdemo.util


import android.util.Log

import com.horizon.lightkvdemo.config.GlobalConfig

object LogUtil {
    fun e(tag: String, e: Throwable) {
        Log.e(tag, e.message, e)
    }

    fun d(tag: String, msg: String) {
        if (GlobalConfig.DEBUG) {
            Log.d(tag, msg)
        }
    }
}
