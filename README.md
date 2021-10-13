# LightKV

Note: <br/>
**LightKV is not good enough, I wrote a beffer component recently.** <br/>
https://github.com/BillyWei01/FastKV

LightKV is a Lightweight key-value storage component based on Android platform.

[SharedPreferences](https://developer.android.com/reference/android/content/SharedPreferences) is a convenient way to save key-value,
but it's no very efficient.

So we make this LightKV, to make saving data faster.


LightKV has two modes: SyncKV & AsyncKV, <br/>
Both of their API similar to SharePreferences（AsyncKV is unnecessary to commit).  <br/>
SyncKV submits data atomic (need to commit after updating data, which like SharePreferences-commit mode); <br/>
AsyncKV is faster, it will auto flush data to disk by system. <br/>

# Benchmark
We make a simple test case (you can see the code in project) to test their loading speed and writing speed.

Testing Device: Xiaomi Note 1 (run in 2018)

mode|writing time(ms）
--|--
AsyncKV | 2.25
SyncKV | 75.34
SharePreferences-apply | 6.90
SharePreferences-commit | 279.14

Testing Device: Huawei P30 Pro (run in 2020)

mode | writing time(ms）
--|--
AsyncKV | 0.31
SyncKV | 8.31
SharePreferences-apply | 1.9
SharePreferences-commit | 30.81

AsyncKV use mmap open mode, so it's slower when loading, but is much faster when writing. <br/>
SyncKV and SharePreferences-commit need to flush data to disk every commit, so they use more time in whole test case. <br/>
SyncKV is faster comparing with SharePreferences-commit.


For Reading, SharePreferences use HashMap, and LightKV us SparseArray, both of them are very fast, 
so we don't make their benchmark to compare.

# Download
```gradle
repositories {
    jcenter()
}

dependencies {
    implementation 'com.horizon.lightkv:lightkv:1.0.8'
}
```

# EXAMPLE
## Java case
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
### Use
```kotlin
val account = AppData.getString(AppData.Keys.ACCOUNT)
if(TextUtils.isEmpty(account)){
      AppData.putString(AppData.Keys.ACCOUNT, "foo@gmail.com")
}
```

## Kotlin case
### Define
```kotlin
object AppData : KVData() {
    override val data: LightKV by lazy {
       LightKV.Builder(GlobalConfig.appContext, "app_data")
                .logger(AppLogger)
                .executor(AsyncTask.THREAD_POOL_EXECUTOR)
                .encoder(GzipEncoder)
                .async()
    }

    var showCount by int(1)
    var account by string(2)
    var token by string(3)
    var secret by array(4 or DataType.ENCODE)
}
```

### Use
```kotlin
val account = AppData.account
if (TextUtils.isEmpty(account)) {
   AppData.account = "foo@gmail.com"
}
```

It's more simple with Kotlin's syntactic sugar.
Any way, LightKV is writing for improving storage efficiency,
not matter you program with Java or Kotlin, it worth trying.


# License
See the [LICENSE](LICENSE.md) file for license rights and limitations.
