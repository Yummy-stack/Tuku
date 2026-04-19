package com.tuku.tukucommon.utils.redis;

import static com.tuku.tukucommon.constant.redis.KeyConstant.USER_SIGN_IN_KEY;

public class MergeKey {
    public static String getUserSignInKey(Long userId,Integer year) {
        return String.format("%s:%s:%s",USER_SIGN_IN_KEY ,year,userId);
    }

}
