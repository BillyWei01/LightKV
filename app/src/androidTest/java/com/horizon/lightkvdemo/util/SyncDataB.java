package com.horizon.lightkvdemo.util;

import com.horizon.lightkv.LightKV;
import com.horizon.lightkv.SyncKV;
import com.horizon.lightkvdemo.config.GlobalConfig;

public class SyncDataB {
    private final SyncKV DATA;

    private SyncDataB() {
        DATA = new LightKV.Builder(GlobalConfig.INSTANCE.getAppContext(), "sync_data_b")
                .keys(Keys.class)
                .logger(AppLogger.INSTANCE)
                .sync();
    }

    public SyncKV data() {
        return DATA;
    }

    public static SyncDataB newInstance(){
        return new SyncDataB();
    }
}
