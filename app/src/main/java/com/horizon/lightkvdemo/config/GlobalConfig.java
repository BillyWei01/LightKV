package com.horizon.lightkvdemo.config;


import android.content.Context;

import com.horizon.lightkvdemo.BuildConfig;

public class GlobalConfig {
    public static final boolean DEBUG = BuildConfig.DEBUG;

    private static Context sAppContext;

    public static void setAppContext(Context context) {
        sAppContext = context;
    }

    public static Context getAppContext() {
        return sAppContext;
    }
}
