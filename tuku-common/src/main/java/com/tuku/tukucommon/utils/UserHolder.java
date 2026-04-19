package com.tuku.tukucommon.utils;


public class UserHolder {
    public static final ThreadLocal<Long> userThreadLocal = new ThreadLocal<>();

    public static Long getUser() {
        return userThreadLocal.get();
    }

    public static void setUser(Long userId) {
        userThreadLocal.set(userId);
    }

    public static void removeUser() {
        userThreadLocal.remove();
    }
}
