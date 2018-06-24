# LightKV
![Bintray](https://img.shields.io/bintray/v/horizon757/maven/LightKV.svg)

LightKV is a Lightweight key-value storage component based on Android platform.

[SharedPreferences](https://developer.android.com/reference/android/content/SharedPreferences) is a convenient way to save key-value,
but it's no very efficient.

So we make this LightKV, to make saving data faster.


LightKV has two modes: SyncKV & AsyncKV, <br/>
Both of thier API similar to SharePreferences（AsyncKV no need to commit).
SyncKV submits data atomic (need to commit after updating data, whick like SharePreferences-commit mode);
AsyncKV faster, cause it will auto commit the update by system.

# Benchmark
We make a simple test case (you can see the code in project) to test their loading speed and writing speed.

mode |loading time(ms）
--|--
AsyncKV | 10.46
SyncKV | 1.56
SharePreferences | 4.99

mode|writing time(ms）
--|--
AsyncKV | 2.25
SyncKV | 75.34
SharePreferences-apply | 6.90
SharePreferences-commit | 279.14

AsyncKV use mmap open mode, so it's slower when loading, but is much faster when writing. <br/>
SyncKV and SharePreferences-commit need to flush data to disk every commit, so they use more time in whole test case. <br/>
SyncKV is faster comparing with SharePreferences-commit.

# Download
```gradle
repositories {
    jcenter()
}

dependencies {
    implementation 'com.horizon.lightkv:lightkv:1.0.1'
}
```

# EXAMPLE
### Define
```java

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


```
### Use case
```kotlin
val showCount = AppData.getInt(AppData.Keys.SHOW_COUNT)
AppData.putInt(AppData.Keys.SHOW_COUNT, showCount + 1)
```

# MORE
LightKV has more feature, for more infomation, 
see https://www.jianshu.com/p/37992580f3d5

# License
See the [LICENSE](LICENSE.md) file for license rights and limitations.
