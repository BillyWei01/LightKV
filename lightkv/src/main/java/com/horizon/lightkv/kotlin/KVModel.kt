package com.horizon.lightkv.kotlin

import com.horizon.lightkv.DataType
import com.horizon.lightkv.LightKV

/**
 * Kotlin support to LightKV, make it easier to use.
 * Just inherit this class, override [createInstance], and define keys.
 *
 */
abstract class KVModel {
    /**
     * auto commit flag for SYNC_MODE,
     * ASYNC_MODE will flush data by kernel
     */
    internal var autoCommit = true

    abstract fun createInstance() : LightKV

    val data: LightKV by lazy {
        createInstance()
    }

    protected fun boolean(key: Int) = KVProperty<Boolean>(key or DataType.BOOLEAN)
    protected fun int(key: Int) = KVProperty<Int>(key or DataType.INT)
    protected fun float(key: Int) = KVProperty<Float>(key or DataType.FLOAT)
    protected fun double(key: Int) = KVProperty<Double>(key or DataType.DOUBLE)
    protected fun long(key: Int) = KVProperty<Long>(key or DataType.LONG)
    protected fun string(key: Int) = KVProperty<String>(key or DataType.STRING)
    protected fun array(key: Int) = KVProperty<ByteArray>(key or DataType.ARRAY)

    /**
     * If you need
     */
    fun disableAutoCommit(){
        autoCommit = false
    }

    fun enableAutoCommit(){
        autoCommit = true
        data.commit()
    }
}