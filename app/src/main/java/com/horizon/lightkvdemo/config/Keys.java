package com.horizon.lightkvdemo.config;

import com.horizon.lightkv.DataType;

// keys define
public interface Keys {
    int SHOW_COUNT = 1 | DataType.INT;

    int ACCOUNT = 2 | DataType.STRING | DataType.ENCODE;
    int TOKEN = 3 | DataType.STRING | DataType.ENCODE;

    int SECRET = 4 | DataType.ARRAY | DataType.ENCODE;
}
