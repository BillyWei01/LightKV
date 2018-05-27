package com.horizon.lightkvdemo.test;


import android.os.AsyncTask;
import android.util.Log;
import android.util.SparseArray;

import com.horizon.lightkv.AsyncKV;
import com.horizon.lightkv.DataType;
import com.horizon.lightkvdemo.util.AsyncData;
import com.horizon.lightkvdemo.util.Keys;
import com.horizon.lightkvdemo.util.Utils;

import java.lang.reflect.Field;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

public class AsyncKVTest extends BaseTestCase {
    private static Random RANDOM = new Random();

    public void testAsyncKV() throws Exception {
        Class keyDefineClass = Keys.class;
        Field[] fields = keyDefineClass.getDeclaredFields();
        int[] keys = new int[fields.length];
        int p = 0;
        for (Field field : fields) {
            keys[p++] = field.getInt(keyDefineClass);
        }

        SparseArray<Object> data = testWriteAndRead(keys);
        testConcurrentModify(keys, data);
        testTransaction(keys, data);
        testGC(keys, data);
    }

    private SparseArray<Object> testWriteAndRead(int[] keys) throws InterruptedException {
        AsyncKV testKV = AsyncData.newInstance().data();
        testKV.clear();
        SparseArray<Object> data = new SparseArray<>();
        for (int key : keys) {
            switch (key & DataType.MASK) {
                case DataType.BOOLEAN:
                    boolean b = RANDOM.nextBoolean();
                    data.put(key, b);
                    testKV.putBoolean(key, b);
                    break;
                case DataType.STRING:
                    String s = Utils.randomStr(RANDOM, 1024);
                    data.put(key, s);
                    testKV.putString(key, s);
                    break;
                case DataType.LONG:
                    long l = RANDOM.nextLong();
                    data.put(key, l);
                    testKV.putLong(key, l);
                    break;
                case DataType.INT:
                    int i = RANDOM.nextInt();
                    data.put(key, i);
                    testKV.putInt(key, i);
                    break;
                case DataType.FLOAT:
                    float f = RANDOM.nextFloat();
                    data.put(key, f);
                    testKV.putFloat(key, f);
                    break;
                case DataType.DOUBLE:
                    double d = RANDOM.nextDouble();
                    data.put(key, d);
                    testKV.putDouble(key, d);
                    break;
                case DataType.ARRAY:
                    byte[] a = Utils.randomBytes(RANDOM, 1024);
                    data.put(key, a);
                    testKV.putArray(key, a);
                    break;
                default:
                    break;
            }
        }

        compare(keys, data, testKV);

        testRead(keys, data);

        return data;
    }

    private void testRead(int[] keys, SparseArray data) {
        compare(keys, data, AsyncData.newInstance().data());
    }

    private void testConcurrentModify(final int[] keys, final SparseArray data) throws InterruptedException {
        final AsyncKV testKV = AsyncData.newInstance().data();
        testKV.clear();
        int n = 4;
        long t1 = System.currentTimeMillis();
        final CountDownLatch downLatch = new CountDownLatch(n);
        for (int i = 0; i < n; i++) {
            AsyncTask.THREAD_POOL_EXECUTOR.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        writeData(keys, testKV, data);
                    } finally {
                        downLatch.countDown();
                    }
                }
            });
        }
        downLatch.await();
        long t2 = System.currentTimeMillis();
        Log.d(TAG, "concurrent modify, use time:" + (t2 - t1) + "ms");

        compare(keys, data, testKV);

        testRead(keys, data);
    }

    private void writeData(int[] keys, AsyncKV testKV, SparseArray data) {
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

    /**
     * operate like testKV.putBoolean(key, !testKV.getBoolean(key)) is not atomic
     * if need to be atomic, synchronized the object like this:
     * synchronized (testKV){
     * testKV.putBoolean(key, !testKV.getBoolean(key));
     * }
     * <p>
     * this test case is design to check this
     */
    private void testTransaction(final int[] keys, SparseArray data) throws InterruptedException {
        final AsyncKV testKV = AsyncData.newInstance().data();
        // n must be even
        int n = 4;
        long t1 = System.nanoTime();
        final CountDownLatch downLatch = new CountDownLatch(n);
        for (int i = 0; i < n; i++) {
            AsyncTask.THREAD_POOL_EXECUTOR.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        reverse(keys, testKV);
                    } finally {
                        downLatch.countDown();
                    }
                }
            });
        }
        downLatch.await();
        long t2 = System.nanoTime();

        Log.d(TAG, "modify by transaction, use time:" + (t2 - t1) / 1000000L + "ms");

        compare(keys, data, testKV);

        testRead(keys, data);
    }

    @SuppressWarnings("SynchronizationOnLocalVariableOrMethodParameter")
    private void reverse(int[] keys, AsyncKV testKV) {
        for (int key : keys) {
            switch (key & DataType.MASK) {
                case DataType.BOOLEAN:
                    synchronized (testKV) {
                        testKV.putBoolean(key, !testKV.getBoolean(key));
                    }
                    break;
                case DataType.STRING:
                    synchronized (testKV) {
                        testKV.putString(key, Utils.reverseString(testKV.getString(key)));
                    }
                    break;
                case DataType.LONG:
                    synchronized (testKV) {
                        testKV.putLong(key, -testKV.getLong(key));
                    }
                    break;
                case DataType.INT:
                    synchronized (testKV) {
                        testKV.putInt(key, -testKV.getInt(key));
                    }
                    break;
                case DataType.FLOAT:
                    synchronized (testKV) {
                        testKV.putFloat(key, -testKV.getFloat(key));
                    }
                    break;
                case DataType.DOUBLE:
                    synchronized (testKV) {
                        testKV.putDouble(key, -testKV.getDouble(key));
                    }
                    break;
                case DataType.ARRAY:
                    synchronized (testKV) {
                        testKV.putArray(key, Utils.reverseBytes(testKV.getArray(key)));
                    }
                    break;
                default:
                    break;
            }
        }
    }


    /**
     * We RANDOM remove item by 50% possibility, enough to trigger gc.
     * For release version we remove the log code of gc in {@link AsyncKV}
     */
    private void testGC(int[] keys, SparseArray<Object> data) throws InterruptedException {
        AsyncKV kv1 = AsyncData.newInstance().data();

        for (int key : keys) {
            boolean delete = RANDOM.nextBoolean();
            if (delete) {
                kv1.remove(key);
                data.remove(key);
                continue;
            }
            switch (key & DataType.MASK) {
                case DataType.BOOLEAN:
                    boolean b = !kv1.getBoolean(key);
                    kv1.putBoolean(key, b);
                    data.put(key, b);
                    break;
                case DataType.STRING:
                    String s = Utils.randomStr(RANDOM, 1024);
                    kv1.putString(key, s);
                    data.put(key, s);
                    break;
                case DataType.LONG:
                    long l = RANDOM.nextLong();
                    kv1.putLong(key, l);
                    data.put(key, l);
                    break;
                case DataType.INT:
                    int i = RANDOM.nextInt();
                    kv1.putInt(key, i);
                    data.put(key, i);
                    break;
                case DataType.FLOAT:
                    float f = RANDOM.nextFloat();
                    kv1.putFloat(key, f);
                    data.put(key, f);
                    break;
                case DataType.DOUBLE:
                    double d = RANDOM.nextDouble();
                    kv1.putDouble(key, d);
                    data.put(key, d);
                    break;
                case DataType.ARRAY:
                    byte[] a = Utils.randomBytes(RANDOM, 1024);
                    kv1.putArray(key, a);
                    data.put(key, a);
                    break;
                default:
                    break;
            }
        }

        compare(keys, data, kv1);

        testRead(keys, data);
    }

    private void compare(int[] keys, SparseArray data, AsyncKV testKV) {
        for (int key : keys) {
            switch (key & DataType.MASK) {
                case DataType.BOOLEAN:
                    assertEquals(data.get(key) != null ? data.get(key) : false, testKV.getBoolean(key));
                    break;
                case DataType.STRING:
                    assertEquals(data.get(key) != null ? data.get(key) : "", testKV.getString(key));
                    break;
                case DataType.LONG:
                    assertEquals(data.get(key) != null ? data.get(key) : 0L, testKV.getLong(key));
                    break;
                case DataType.INT:
                    assertEquals(data.get(key) != null ? data.get(key) : 0, testKV.getInt(key));
                    break;
                case DataType.FLOAT:
                    assertEquals(data.get(key) != null ? data.get(key) : 0F, testKV.getFloat(key));
                    break;
                case DataType.DOUBLE:
                    assertEquals(data.get(key) != null ? data.get(key) : 0D, testKV.getDouble(key));
                    break;
                case DataType.ARRAY:
                    String aString = data.get(key) != null ? Utils.bytesToHex((byte[]) data.get(key)) : "";
                    String bString = Utils.bytesToHex(testKV.getArray(key));
                    assertEquals(aString, bString);
                    break;
                default:
                    break;
            }
        }
    }
}
