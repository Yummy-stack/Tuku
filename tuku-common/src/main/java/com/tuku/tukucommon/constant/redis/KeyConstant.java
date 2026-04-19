package com.tuku.tukucommon.constant.redis;

public interface KeyConstant {
    // 分页查询图片列表的Key
    String PICTURE_ALL = "Tuku:pictureVo:all:";

    // 获取图片详情
    String PICTURE_DETAIL = "Tuku:pictureVo:one:";

    //Spring-Data-Session
    String SPRING_SESSION = "spring:session:sessions:f9bc94f1-11d1-4fe7-bac7-20af50b705f0";

    // 布隆过滤器名称
    String BLOOM_FILTER_NAME = "tuku:bloom:filter";

    // 热Key计数器名称
    String HOT_KEY_COUNTER_PREFIX = "tuku:hot:key:";

    // 热Key阈值
    int HOT_KEY_THRESHOLD = 100;

    // 用户签到表的Key前缀
    String USER_SIGN_IN_KEY = "user:sign-in:";
}
