package com.tuku.tukuModel.enums;

import cn.hutool.core.util.ObjUtil;
import lombok.Getter;

@Getter
public enum ReviewStatusEnum {
    REVIEW("待审核", 0),
    PASS("通过", 1),
    FAIL("不通过", 2);

    private final String text;

    private final Integer value;

    ReviewStatusEnum(String text, int value) {
        this.text = text;
        this.value = value;
    }

    /**
     * 根据 value 获取枚举
     *
     * @param value 枚举值的value
     * @return 枚举值
     */
    public static ReviewStatusEnum getEnumByValue(int value) {
        if (ObjUtil.isEmpty(value)) {
            throw new RuntimeException("value所对应的Enum不存在");
        }
        for (ReviewStatusEnum anEnum : ReviewStatusEnum.values()) {
            if (anEnum.value.equals(value)) {
                return anEnum;
            }
        }
        throw new RuntimeException("value所对应的Enum不存在");
    }

    /**
     * 根据Value来获取Text的值
     */
    public static String getTextByValue(int value) {
        if (ObjUtil.isEmpty(value)) {
            return null;
        }
        ReviewStatusEnum[] values = ReviewStatusEnum.values();
        for (ReviewStatusEnum anEnum : values) {
            if (anEnum.value.equals(value)) {
                return anEnum.text;
            }
        }
        throw new RuntimeException("Value所对应的Text不存在");
    }


    /**
     * 根据Text来获取Value的值
     */
    public static Integer getValueByText(String text) {
        if (ObjUtil.isEmpty(text)) {
            return null;
        }
        ReviewStatusEnum[] values = ReviewStatusEnum.values();
        for (ReviewStatusEnum anEnum : values) {
            if (anEnum.text.equals(text)) {
                return anEnum.value;
            }
        }
        throw new RuntimeException("Text所对应的Value不存在");
    }
}
