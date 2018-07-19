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

        val account = AppData.account
        Log.d(TAG, "account:$account")
        if (TextUtils.isEmpty(account)) {
            AppData.account = "foo@gmail.com"
        }

        val token = AppData.token
        Log.d(TAG, "token:$token")
        if (TextUtils.isEmpty(token)) {
            AppData.token = "f5f5f5f5"
        }

        val secret = AppData.secret
        if (secret.isNotEmpty()) {
            Log.d(TAG, "secret:" + String(secret))
        } else {
            AppData.secret = "I love you baby".toByteArray()
        }
    }

    private fun printShowTime() {
        val showCount = AppData.showCount
        helloTv.text = getString(R.string.main_tips, showCount + 1, AppData.data.toString())
        AppData.showCount = showCount + 1
    }
}
