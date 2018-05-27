package com.horizon.lightkvdemo.util;


import com.horizon.lightkv.LightKV;

import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class EncryptEncoder implements LightKV.Encoder {
    private static final String TRANSFORMATION = "AES/CBC/PKCS5Padding";
    private static final String ALGORITHM = "AES";
    private static final byte[] MASK = new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 100, 105, 110, 115, 120, 125};
    private static final Random RANDOM = new Random();

    public  byte[] encode(byte[] src) {
        if(src == null || src.length == 0){
            return src;
        }
        byte[] key = new byte[16];
        byte[] iv = new byte[16];
        RANDOM.nextBytes(key);
        RANDOM.nextBytes(iv);
        byte[] cipherText = code(src, key, iv, Cipher.ENCRYPT_MODE);
        byte[] des = new byte[32 + cipherText.length];
        mask(key);
        System.arraycopy(key, 0, des, 0, 16);
        System.arraycopy(iv, 0, des, 16, 16);
        System.arraycopy(cipherText, 0, des, 32, cipherText.length);
        return des;
    }

    public  byte[] decode(byte[] des){
        if(des == null || des.length == 0){
            return des;
        }
        byte[] key = new byte[16];
        byte[] iv = new byte[16];
        byte[] cipherText = new byte[des.length - 32];
        System.arraycopy(des, 0, key, 0, 16);
        System.arraycopy(des, 16, iv, 0, 16);
        System.arraycopy(des, 32, cipherText, 0, cipherText.length);
        mask(key);
        return code(cipherText, key, iv, Cipher.DECRYPT_MODE);
    }

    private static void mask(byte[] key) {
        for (int i = 0; i < 16; i++) {
            key[i] ^= MASK[i];
        }
    }

    private static byte[] code(byte[] bytes, byte[] key, byte[] iv, int mode) {
        try {
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            SecretKeySpec keySpec = new SecretKeySpec(key, ALGORITHM);
            IvParameterSpec ivSpec = new IvParameterSpec(iv);
            cipher.init(mode, keySpec, ivSpec);
            return cipher.doFinal(bytes);
        }catch (Exception e){
            LogUtil.INSTANCE.e("Encode", e);
        }
        return new byte[0];
    }
}
