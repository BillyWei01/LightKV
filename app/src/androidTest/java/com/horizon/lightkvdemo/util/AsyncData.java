package com.horizon.lightkvdemo.util;


import com.horizon.lightkv.AsyncKV;
import com.horizon.lightkv.LightKV;
import com.horizon.lightkvdemo.config.GlobalConfig;

public class AsyncData {
    private final AsyncKV DATA;

    private AsyncData() {
        DATA = new LightKV.Builder(GlobalConfig.getAppContext(), "async_data")
                .keys(Keys.class)
                .logger(AppLogger.getInstance())
                .async();
    }

    public AsyncKV data() {
        return DATA;
    }

    public static AsyncData newInstance(){
        return new AsyncData();
    }
}
