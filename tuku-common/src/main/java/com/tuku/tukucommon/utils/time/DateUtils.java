package com.tuku.tukucommon.utils.time;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 日期格式化工具类（线程安全）
 */
public class DateUtils {
    private static final DateTimeFormatter DEFAULT_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * 将LocalDateTime格式化为标准日期字符串
     *
     * @param localDateTime 需要格式化的时间对象
     * @return 格式化后的日期字符串
     */
    public static String toFormatter(LocalDateTime localDateTime) {
        return localDateTime.format(DEFAULT_FORMATTER);
    }

    /**
     * 将日期字符串解析为LocalDateTime对象
     *
     * @param dateString 符合yyyy-MM-dd HH:mm:ss格式的日期字符串
     * @return 解析后的时间对象
     */
    public static LocalDateTime parseStringToDateTime(String dateString) {
        return LocalDateTime.parse(dateString, DEFAULT_FORMATTER);
    }

//    public static void main(String[] args) {
//        String formatted = DateUtils.toFormatter(LocalDateTime.now());
//        System.out.println("Formatted: " + formatted);
//
//        LocalDateTime parsed = parseStringToDateTime(formatted);
//        System.out.println("Parsed: " + parsed);
//    }
}

