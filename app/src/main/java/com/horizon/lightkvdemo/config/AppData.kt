package com.horizon.lightkvdemo.config

import android.os.AsyncTask
import com.horizon.lightkv.DataType
import com.horizon.lightkv.LightKV
import com.horizon.lightkv.KVModel
import com.horizon.lightkvdemo.util.AppLogger
import com.horizon.lightkvdemo.util.GzipEncoder


object AppData : KVModel() {
    override fun createInstance(): LightKV {
        return LightKV.Builder(GlobalConfig.appContext, "app_data")
                .logger(AppLogger)
                .executor(AsyncTask.THREAD_POOL_EXECUTOR)
                // just for demo，it's unnecessary to define keys if we don't need to print data
                .keys(Keys::class.java)
                .encoder(GzipEncoder)
                .async()
    }

    var showCount by int(1)

    // just encode sample, in fact, just long text like json or xml(or else) should be compress
    var account by string(2 or DataType.ENCODE)
    var token by string(3)

    var secret by array(4 or DataType.ENCODE)
}