package com.horizon.lightkvdemo.ui

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import com.horizon.lightkvdemo.R
import com.horizon.lightkvdemo.base.BaseActivity
import com.horizon.lightkvdemo.config.AppData
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        printShowTime()

        val account = AppData.getString(AppData.Keys.ACCOUNT)
        Log.d(TAG, "account:" + account)

        if(TextUtils.isEmpty(account)){
            AppData.putString(AppData.Keys.ACCOUNT, "foo@gmail.com")
        }

        val token = AppData.getString(AppData.Keys.TOKEN)
        Log.d(TAG, "token:" + token)

        if(TextUtils.isEmpty(token)){
            AppData.putString(AppData.Keys.TOKEN, "f5f5f5f5")
        }

        val secret = AppData.getArray(AppData.Keys.SECRET)
        if(secret != null){
            Log.d(TAG, "secret:" + String(secret))
        }else{
            AppData.putArray(AppData.Keys.SECRET, "I love you baby".toByteArray())
        }
    }

    private fun printShowTime() {
        val showTime = AppData.getInt(AppData.Keys.SHOW_COUNT) + 1
        helloTv.text = getString(R.string.main_tips, showTime, AppData.data().toString())
        Log.d(TAG, AppData.data().toString());
        AppData.putInt(AppData.Keys.SHOW_COUNT, showTime)
    }
}
