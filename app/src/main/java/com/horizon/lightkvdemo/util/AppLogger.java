package com.horizon.lightkvdemo.util;


import com.horizon.lightkv.LightKV;

public class AppLogger implements LightKV.Logger{
    private static final AppLogger INSTANCE = new AppLogger();

    private AppLogger(){
    }

    public static AppLogger getInstance(){
        return INSTANCE;
    }

    @Override
    public void e(String tag, Throwable e) {
        LogUtil.INSTANCE.e(tag, e);
    }
}
