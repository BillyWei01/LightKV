package com.horizon.lightkvdemo.test;


import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.util.SparseArray;

import com.horizon.lightkv.AsyncKV;
import com.horizon.lightkv.DataType;
import com.horizon.lightkv.SyncKV;
import com.horizon.lightkvdemo.config.GlobalConfig;
import com.horizon.lightkvdemo.util.AsyncData;
import com.horizon.lightkvdemo.util.Keys;
import com.horizon.lightkvdemo.util.SyncData;
import com.horizon.lightkvdemo.util.Utils;

import java.lang.reflect.Field;
import java.util.Random;

public class BenchMarkTest extends BaseTestCase {
    private static Random random = new Random();

    private long takv;
    private long tskv1;
    private long tskv2;
    private long tsp1;
    private long tsp2;
    private long tspa;


    /**
     * just test write performance，cause they all read fast.
     */
    public void testSpeed() throws Exception {
        // prepare data
        Class keyDefineClass = Keys.class;
        Field[] fields = keyDefineClass.getDeclaredFields();
        int[] keys = new int[fields.length];
        int p = 0;
        for (Field field : fields) {
            keys[p++] = field.getInt(keyDefineClass);
        }

        int max = 10;
        SparseArray[] data = new SparseArray[max];
        for (int i = 0; i < max; i++) {
            data[i] = generateData(keys);
        }

        // test loading, first test has not data
        long t1 = System.nanoTime();
        AsyncKV asyncKV = AsyncData.newInstance().data();
        asyncKV.getBoolean(Keys.B1);
        long t2 = System.nanoTime();
        SyncKV syncKV = SyncData.newInstance().data();
        syncKV.getBoolean(Keys.B1);
        long t3 = System.nanoTime();
        SharedPreferences sp = GlobalConfig.getAppContext().getSharedPreferences("test_sp", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        sp.getBoolean(Integer.toString(Keys.B1), false);
        long t4 = System.nanoTime();

        Log.d(TAG, "loading time, \n"
                + "takv: " + getTime(t2 - t1)
                + "tkv: " + getTime(t3 - t2)
                + "tsp: " + getTime(t4 - t3));

        // test writing
        for (int i = 5; i <= max; i += 5) {
            takv = 0;
            tskv1 = 0;
            tskv2 = 0;
            tsp1 = 0;
            tsp2 = 0;
            tspa = 0;

            for (int j = 0; j < i; j++) {
                compare(keys, data[j], syncKV, asyncKV, editor);
            }

            takv /= i;
            tskv1 /= i;
            tskv2 /= i;
            tsp1 /= i;
            tsp2 /= i;
            tspa /= i;

            String msg = "run for " + i + " times. average time,\n" +
                    "takv:" + getTime(takv) +
                    "tskv1:" + getTime(tskv1) +
                    "tskv2:" + getTime(tskv2) +
                    "tsp1:" + getTime(tsp1) +
                    "tsp2:" + getTime(tsp2) +
                    "tspa:" + getTime(tspa);

            Log.d(TAG, msg);
        }


        // clear data and fill content, for next test
        asyncKV.clear();
        writeAkv(keys, asyncKV, data[0]);

        syncKV.clear();
        writeSkv2(keys, syncKV, data[0]);

        editor.clear();
        writeSp2(keys, editor, data[0]);
    }


    private String getTime(long t) {
        return (t / 1000000) + "." + (t / 10000 % 100) + "\n";
    }



    private void compare(int[] keys, SparseArray data, SyncKV syncKV, AsyncKV testKV, SharedPreferences.Editor editor) {
        long t1 = System.nanoTime();
        writeAkv(keys, testKV, data);
        long t2 = System.nanoTime();
        writeSkv1(keys, syncKV, data);
        long t3 = System.nanoTime();
        writeSkv2(keys, syncKV, data);
        long t4 = System.nanoTime();
        writeSp1(keys, editor, data);
        long t5 = System.nanoTime();
        writeSp2(keys, editor, data);
        long t6 = System.nanoTime();
        writeSpAync(keys, editor, data);
        long t7 = System.nanoTime();

        takv += t2 - t1;
        tskv1 += t3 - t2;
        tskv2 += t4 - t3;
        tsp1 += t5 - t4;
        tsp2 += t6 - t5;
        tspa += t7 - t6;
    }

    private SparseArray<Object> generateData(int[] keys) throws InterruptedException {
        SparseArray<Object> data = new SparseArray<>();
        for (int key : keys) {
            switch (key & DataType.MASK) {
                case DataType.BOOLEAN:
                    data.put(key, random.nextBoolean());
                    break;
                case DataType.STRING:
                    data.put(key, Utils.randomStr(random, 128));
                    break;
                case DataType.LONG:
                    data.put(key, random.nextLong());
                    break;
                case DataType.INT:
                    data.put(key, random.nextInt());
                    break;
                case DataType.FLOAT:
                    data.put(key, random.nextFloat());
                    break;
                case DataType.DOUBLE:
                    data.put(key, random.nextDouble());
                    break;
                case DataType.ARRAY:
                    data.put(key, Utils.randomBytes(random, 128));
                    break;
                default:
                    break;
            }
        }
        return data;
    }

    private void writeAkv(int[] keys, AsyncKV testKV, SparseArray data) {
        for (int key : keys) {
            switch (key & DataType.MASK) {
                case DataType.BOOLEAN:
                    testKV.putBoolean(key, (boolean) data.get(key));
                    break;
                case DataType.STRING:
                    testKV.putString(key, (String) data.get(key));
                    break;
                case DataType.LONG:
                    testKV.putLong(key, (long) data.get(key));
                    break;
                case DataType.INT:
                    testKV.putInt(key, (int) data.get(key));
                    break;
                case DataType.FLOAT:
                    testKV.putFloat(key, (float) data.get(key));
                    break;
                case DataType.DOUBLE:
                    testKV.putDouble(key, (double) data.get(key));
                    break;
                case DataType.ARRAY:
                    testKV.putArray(key, (byte[]) data.get(key));
                    break;
                default:
                    break;
            }
        }
    }

    private void writeSkv1(int[] keys, SyncKV testKV, SparseArray data) {
        for (int key : keys) {
            switch (key & DataType.MASK) {
                case DataType.BOOLEAN:
                    testKV.putBoolean(key, (boolean) data.get(key));
                    break;
                case DataType.STRING:
                    testKV.putString(key, (String) data.get(key));
                    break;
                case DataType.LONG:
                    testKV.putLong(key, (long) data.get(key));
                    break;
                case DataType.INT:
                    testKV.putInt(key, (int) data.get(key));
                    break;
                case DataType.FLOAT:
                    testKV.putFloat(key, (float) data.get(key));
                    break;
                case DataType.DOUBLE:
                    testKV.putDouble(key, (double) data.get(key));
                    break;
                case DataType.ARRAY:
                    testKV.putArray(key, (byte[]) data.get(key));
                    break;
                default:
                    break;
            }
            testKV.commit();
        }
    }

    private void writeSkv2(int[] keys, SyncKV testKV, SparseArray data) {
        for (int key : keys) {
            switch (key & DataType.MASK) {
                case DataType.BOOLEAN:
                    testKV.putBoolean(key, (boolean) data.get(key));
                    break;
                case DataType.STRING:
                    testKV.putString(key, (String) data.get(key));
                    break;
                case DataType.LONG:
                    testKV.putLong(key, (long) data.get(key));
                    break;
                case DataType.INT:
                    testKV.putInt(key, (int) data.get(key));
                    break;
                case DataType.FLOAT:
                    testKV.putFloat(key, (float) data.get(key));
                    break;
                case DataType.DOUBLE:
                    testKV.putDouble(key, (double) data.get(key));
                    break;
                case DataType.ARRAY:
                    testKV.putArray(key, (byte[]) data.get(key));
                    break;
                default:
                    break;
            }
        }
        testKV.commit();
    }

    private void writeSp1(int[] keys, SharedPreferences.Editor editor, SparseArray data) {
        for (int key : keys) {
            String keyStr = Integer.toString(key);
            switch (key & DataType.MASK) {
                case DataType.BOOLEAN:
                    editor.putBoolean(keyStr, (boolean) data.get(key));
                    break;
                case DataType.STRING:
                    editor.putString(keyStr, (String) data.get(key));
                    break;
                case DataType.LONG:
                    editor.putLong(keyStr, (long) data.get(key));
                    break;
                case DataType.INT:
                    editor.putInt(keyStr, (int) data.get(key));
                    break;
                case DataType.FLOAT:
                    editor.putFloat(keyStr, (float) data.get(key));
                    break;
                case DataType.DOUBLE:
                    editor.putString(keyStr, Double.toString((double) data.get(key)));
                    break;
                case DataType.ARRAY:
                    editor.putString(keyStr, Utils.bytesToHex((byte[]) data.get(key)));
                    break;
                default:
                    break;
            }
            editor.commit();
        }
    }

    private void writeSp2(int[] keys, SharedPreferences.Editor editor, SparseArray data) {
        for (int key : keys) {
            String keyStr = Integer.toString(key);
            switch (key & DataType.MASK) {
                case DataType.BOOLEAN:
                    editor.putBoolean(keyStr, (boolean) data.get(key));
                    break;
                case DataType.STRING:
                    editor.putString(keyStr, (String) data.get(key));
                    break;
                case DataType.LONG:
                    editor.putLong(keyStr, (long) data.get(key));
                    break;
                case DataType.INT:
                    editor.putInt(keyStr, (int) data.get(key));
                    break;
                case DataType.FLOAT:
                    editor.putFloat(keyStr, (float) data.get(key));
                    break;
                case DataType.DOUBLE:
                    editor.putString(keyStr, Double.toString((double) data.get(key)));
                    break;
                case DataType.ARRAY:
                    editor.putString(keyStr, Utils.bytesToHex((byte[]) data.get(key)));
                    break;
                default:
                    break;
            }
        }
        editor.commit();
    }


    private void writeSpAync(int[] keys, SharedPreferences.Editor editor, SparseArray data) {
        for (int key : keys) {
            String keyStr = Integer.toString(key);
            switch (key & DataType.MASK) {
                case DataType.BOOLEAN:
                    editor.putBoolean(keyStr, (boolean) data.get(key));
                    editor.apply();
                    break;
                case DataType.STRING:
                    editor.putString(keyStr, (String) data.get(key));
                    editor.apply();
                    break;
                case DataType.LONG:
                    editor.putLong(keyStr, (long) data.get(key));
                    editor.apply();
                    break;
                case DataType.INT:
                    editor.putInt(keyStr, (int) data.get(key));
                    editor.apply();
                    break;
                case DataType.FLOAT:
                    editor.putFloat(keyStr, (float) data.get(key));
                    editor.apply();
                    break;
                case DataType.DOUBLE:
                    editor.putString(keyStr, Double.toString((double) data.get(key)));
                    editor.apply();
                    break;
                case DataType.ARRAY:
                    editor.putString(keyStr, Utils.bytesToHex((byte[]) data.get(key)));
                    editor.apply();
                    break;
                default:
                    break;
            }
        }
    }


}
