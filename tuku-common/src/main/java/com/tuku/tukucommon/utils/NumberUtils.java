package com.tuku.tukucommon.utils;

public class NumberUtils {
    public static Long ifNullToZero(Long value) {
        if (value == null) return 0L;
        return value;
    }
}
