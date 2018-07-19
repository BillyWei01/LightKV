package com.horizon.lightkvdemo.util;

import com.horizon.lightkv.AsyncKV;
import com.horizon.lightkv.LightKV;
import com.horizon.lightkvdemo.config.GlobalConfig;

public class AsyncDataB {
    private final AsyncKV DATA;

    private AsyncDataB() {
        DATA = new LightKV.Builder(GlobalConfig.INSTANCE.getAppContext(), "async_data_b")
                .keys(Keys.class)
                .logger(AppLogger.INSTANCE)
                .async();
    }

    public AsyncKV data() {
        return DATA;
    }

    public static AsyncDataB newInstance(){
        return new AsyncDataB();
    }
}
