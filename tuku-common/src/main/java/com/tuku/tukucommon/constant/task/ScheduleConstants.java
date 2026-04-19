package com.tuku.tukucommon.constant.task;

public interface ScheduleConstants {
    //task状态
    int SCHEDULED = 0;   //初始化状态

    int EXECUTED = 1;       //已执行状态

    int CANCELLED = 2;   //已取消状态

    String FUTURE = "future:";   //未来数据key前缀

    String TOPIC = "topic:";     //当前数据key前缀
}