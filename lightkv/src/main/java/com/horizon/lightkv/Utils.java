package com.horizon.lightkv;


import java.io.File;
import java.io.IOException;

class Utils {
    /**
     * check if file exist, create new file if not.
     * if there is a directory with a name same as the file, return false.
     */
    static boolean existFile(File file) throws IOException {
        if (file.isFile()) {
            return true;
        } else {
            File parent = file.getParentFile();
            return parent != null && (parent.isDirectory() || parent.mkdirs()) && file.createNewFile();
        }
    }

    /**
     * murmurhash
     *
     * see
     * https://sites.google.com/site/murmurhash/
     * https://github.com/tnm/murmurhash-java
     * https://github.com/tamtam180/MurmurHash-For-Java/
     *
     * @param data bytes
     * @return 64 bit hash
     */
    static long hash64(final byte[] data, int len) {
        if (data == null || data.length == 0) {
            return 0L;
        }
        final long m = 0xc6a4a7935bd1e995L;
        final long seed = 0xe17a1465L;
        final int r = 47;

        long h = seed ^ (len * m);
        int remain = len & 7;
        int size = len - remain;

        for (int i = 0; i < size; i += 8) {
            long k = ((long) data[i] << 56) +
                    ((long) (data[i + 1] & 0xFF) << 48) +
                    ((long) (data[i + 2] & 0xFF) << 40) +
                    ((long) (data[i + 3] & 0xFF) << 32) +
                    ((long) (data[i + 4] & 0xFF) << 24) +
                    ((data[i + 5] & 0xFF) << 16) +
                    ((data[i + 6] & 0xFF) << 8) +
                    ((data[i + 7] & 0xFF));
            k *= m;
            k ^= k >>> r;
            k *= m;
            h ^= k;
            h *= m;
        }

        switch (remain) {
            case 7:
                h ^= (long) (data[size + 6] & 0xFF) << 48;
            case 6:
                h ^= (long) (data[size + 5] & 0xFF) << 40;
            case 5:
                h ^= (long) (data[size + 4] & 0xFF) << 32;
            case 4:
                h ^= (long) (data[size + 3] & 0xFF) << 24;
            case 3:
                h ^= (data[size + 2] & 0xFF) << 16;
            case 2:
                h ^= (data[size + 1] & 0xFF) << 8;
            case 1:
                h ^= (data[size] & 0xFF);
                h *= m;
        }

        h ^= h >>> r;
        h *= m;
        h ^= h >>> r;

        return h;
    }
}
