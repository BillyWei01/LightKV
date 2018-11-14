package com.horizon.lightkvdemo.config

import android.os.AsyncTask
import com.horizon.lightkv.DataType
import com.horizon.lightkv.KVData
import com.horizon.lightkv.LightKV
import com.horizon.lightkvdemo.util.AppLogger
import com.horizon.lightkvdemo.util.GzipEncoder


object AppData : KVData() {
    override val data: LightKV by lazy {
       LightKV.Builder(GlobalConfig.appContext, "app_data")
                .logger(AppLogger)
                .executor(AsyncTask.THREAD_POOL_EXECUTOR)
                .encoder(GzipEncoder)
                .async()
    }

    var showCount by int(1)
    var account by string(2)
    var token by string(3)
    var secret by array(4 or DataType.ENCODE)
}
