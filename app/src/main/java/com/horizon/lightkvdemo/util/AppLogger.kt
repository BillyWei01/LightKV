package com.horizon.lightkvdemo.util


import com.horizon.lightkv.LightKV

object AppLogger  : LightKV.Logger {
    override fun e(tag: String, e: Throwable) {
        LogUtil.e(tag, e)
    }
}
