package com.horizon.lightkvdemo.util;


import com.horizon.lightkv.DataType;

public interface Keys {
    int B1 = 1 | DataType.BOOLEAN;
    int B2 = 2 | DataType.BOOLEAN;
    int B3 = 3 | DataType.BOOLEAN;
    int B4 = 4 | DataType.BOOLEAN;
    int B5 = 5 | DataType.BOOLEAN;

    int I1 = 1 | DataType.INT;
    int I2 = 2 | DataType.INT;
    int I3 = 3 | DataType.INT;
    int I4 = 4| DataType.INT;
    int I5 = 5 | DataType.INT;

    int F1 = 1 | DataType.FLOAT;
    int F2 = 2 | DataType.FLOAT;
    int F3 = 3 | DataType.FLOAT;
    int F4 = 4 | DataType.FLOAT;
    int F5 = 5 | DataType.FLOAT;

    int L1 = 1 | DataType.LONG;
    int L2 = 2 | DataType.LONG;
    int L3 = 3 | DataType.LONG;
    int L4 = 4 | DataType.LONG;
    int L5 = 5 | DataType.LONG;

    int D1 = 1 | DataType.DOUBLE;
    int D2 = 2 | DataType.DOUBLE;
    int D3 = 3 | DataType.DOUBLE;
    int D4 = 4 | DataType.DOUBLE;
    int D5 = 5 | DataType.DOUBLE;

    int A1 = 1 | DataType.ARRAY;
    int A2 = 2 | DataType.ARRAY;
    int A3 = 3 | DataType.ARRAY;
    int A4 = 4 | DataType.ARRAY;
    int A5 = 5 | DataType.ARRAY;

    int S1 = 1 | DataType.STRING;
    int S2 = 2 | DataType.STRING;
    int S3 = 3 | DataType.STRING;
    int S4 = 4 | DataType.STRING;
    int S5 = 5 | DataType.STRING;
}
