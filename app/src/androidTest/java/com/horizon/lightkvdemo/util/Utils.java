package com.horizon.lightkvdemo.util;


import java.util.Random;

public class Utils {
    private static final String[] CN = new String[]{"一", "二", "三", "四五", "六七", "八九十"};

    private static final char[] HEX_DIGITS = {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'a', 'b', 'c', 'd', 'e', 'f'};

    public static String bytesToHex(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return "";
        }
        int len = bytes.length;
        char[] buf = new char[len << 1];
        for (int i = 0; i < len; i++) {
            int index = i << 1;
            byte b = bytes[i];
            buf[index] = HEX_DIGITS[(b & 0xF0) >> 4];
            buf[index + 1] = HEX_DIGITS[b & 0xF];
        }
        return new String(buf);
    }

    public static String randomStr(Random random, int maxLen) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < maxLen; i++) {
            int r = random.nextInt(Integer.MAX_VALUE);
            int a = r % 4;
            if (a == 0) {
                builder.append(CN[r % CN.length]);
            } else if (a == 1) {
                builder.append((char) ('0' + r % 10));
            } else if (a == 2) {
                builder.append((char) ('a' + r % 26));
            } else {
                builder.append((char) ('A' + r % 26));
            }
        }
        return builder.toString();
    }

    public static byte[] randomBytes(Random random, int maxLen) {
        int len = random.nextInt(maxLen);
        byte[] a = new byte[len];
        random.nextBytes(a);
        return a;
    }

    public static String reverseString(String str){
        int len = str.length();
        StringBuilder builder = new StringBuilder(len);
        for (int i = len -1; i >= 0; i--) {
            builder.append(str.charAt(i));
        }
        return builder.toString();
    }

    public static byte[] reverseBytes(byte[] bytes) {
        int len = bytes.length;
        byte[] newBytes = new byte[len];
        for (int i = 0; i < len; i++) {
            newBytes[i] = (byte) ~bytes[i];
        }
        return newBytes;
    }


}
