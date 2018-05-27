package com.horizon.lightkvdemo.base


import android.support.v7.app.AppCompatActivity

abstract class BaseActivity : AppCompatActivity() {
    protected val TAG = this.javaClass.simpleName
}
