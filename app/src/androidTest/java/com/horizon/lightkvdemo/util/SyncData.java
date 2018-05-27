package com.horizon.lightkvdemo.util;


import com.horizon.lightkv.LightKV;
import com.horizon.lightkv.SyncKV;
import com.horizon.lightkvdemo.config.GlobalConfig;

public class SyncData {
    private final SyncKV DATA;

    private SyncData() {
        DATA = new LightKV.Builder(GlobalConfig.getAppContext(), "sync_data")
                .keys(Keys.class)
                .logger(AppLogger.getInstance())
                .sync();
    }

    public SyncKV data() {
        return DATA;
    }

    public static SyncData newInstance(){
        return new SyncData();
    }
}
