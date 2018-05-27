package com.horizon.lightkvdemo.config;


import android.os.AsyncTask;

import com.horizon.lightkv.DataType;
import com.horizon.lightkv.LightKV;
import com.horizon.lightkv.SyncKV;
import com.horizon.lightkvdemo.util.AppLogger;
import com.horizon.lightkvdemo.util.ConfuseEncoder;

public class AppData {
    private static final SyncKV DATA =
            new LightKV.Builder(GlobalConfig.getAppContext(), "app_data")
                    .logger(AppLogger.getInstance())
                    .executor(AsyncTask.THREAD_POOL_EXECUTOR)
                    .keys(Keys.class)
                    .encoder(new ConfuseEncoder())
                    .sync();

    // keys define
    public interface Keys {
        int SHOW_COUNT = 1 | DataType.INT;

        int ACCOUNT = 1 | DataType.STRING | DataType.ENCODE;
        int TOKEN = 2 | DataType.STRING | DataType.ENCODE;

        int SECRET = 1 | DataType.ARRAY | DataType.ENCODE;
    }

    public static SyncKV data() {
        return DATA;
    }

    public static String getString(int key) {
        return DATA.getString(key);
    }

    public static void putString(int key, String value) {
        DATA.putString(key, value);
        DATA.commit();
    }

    public static byte[] getArray(int key) {
        return DATA.getArray(key);
    }

    public static void putArray(int key, byte[] value) {
        DATA.putArray(key, value);
        DATA.commit();
    }

    public static int getInt(int key) {
        return DATA.getInt(key);
    }

    public static void putInt(int key, int value) {
        DATA.putInt(key, value);
        DATA.commit();
    }
}
