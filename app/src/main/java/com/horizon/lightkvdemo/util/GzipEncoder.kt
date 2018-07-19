package com.horizon.lightkvdemo.util

import android.util.Log
import com.horizon.lightkv.LightKV

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.util.zip.GZIPInputStream
import java.util.zip.GZIPOutputStream

object GzipEncoder : LightKV.Encoder {

    override fun encode(src: ByteArray?): ByteArray? {
        if (src == null || src.isEmpty()) {
            return src
        }
        try {
            val out = ByteArrayOutputStream()
            val gzip = GZIPOutputStream(out)
            gzip.write(src)
            gzip.close()
            return out.toByteArray()
        } catch (e: IOException) {
            // should not be here
            Log.e("GzipEncoder", "encode failed", e)
        }

        return ByteArray(0)
    }

    override fun decode(des: ByteArray?): ByteArray? {
        if (des == null || des.isEmpty()) {
            return des
        }
        try {
            val out = ByteArrayOutputStream()
            val gzip = GZIPInputStream(ByteArrayInputStream(des))
            val buffer = ByteArray(4096)
            while (true) {
                val n = gzip.read(buffer, 0, buffer.size)
                if(n <= 0) break
                out.write(buffer, 0, n)
            }
            gzip.close()
            return out.toByteArray()
        } catch (e: IOException) {
            Log.e("GzipEncoder",  "decode failed", e)
        }

        return ByteArray(0)
    }
}
