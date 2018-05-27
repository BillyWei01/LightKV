package com.horizon.lightkvdemo.application


import android.content.Context
import com.horizon.lightkvdemo.config.AppData
import com.horizon.lightkvdemo.config.GlobalConfig

internal object AppIntiHelper {
    fun inti(context: Context) {
        GlobalConfig.setAppContext(context)

        // just trigger loading
        AppData.data()
    }
}
