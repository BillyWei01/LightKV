package com.horizon.lightkvdemo.config;


import android.os.AsyncTask;

import com.horizon.lightkv.LightKV;
import com.horizon.lightkvdemo.util.GzipEncoder;
import com.horizon.lightkvdemo.util.AppLogger;

/**
 * Fava version, for compare
 */
public class AppData2 {
    private static final LightKV DATA =
            new LightKV.Builder(GlobalConfig.INSTANCE.getAppContext(), "app_data_2")
                    .logger(AppLogger.INSTANCE)
                    .executor(AsyncTask.THREAD_POOL_EXECUTOR)
                    .keys(Keys.class)
                    .encoder(GzipEncoder.INSTANCE)
                    .async();

    public static LightKV data() {
        return DATA;
    }

    public static String getString(int key) {
        return DATA.getString(key);
    }

    public static void putString(int key, String value) {
        DATA.putString(key, value);
    }

    public static byte[] getArray(int key) {
        return DATA.getArray(key);
    }

    public static void putArray(int key, byte[] value) {
        DATA.putArray(key, value);
    }

    public static int getInt(int key) {
        return DATA.getInt(key);
    }

    public static void putInt(int key, int value) {
        DATA.putInt(key, value);
    }
}
