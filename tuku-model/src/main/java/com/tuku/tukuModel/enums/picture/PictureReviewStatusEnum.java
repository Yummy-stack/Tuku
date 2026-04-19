package com.tuku.tukuModel.enums.picture;

import cn.hutool.core.util.ObjUtil;
import lombok.Getter;

/**
 * 图片审核状态的枚举类
 */
@Getter
public enum PictureReviewStatusEnum {
    REVIEW("待审核", 0),
    PASS("通过", 1),
    FAIL("不通过", 2);

    private final String text;

    private final Integer value;

    PictureReviewStatusEnum(String text, int value) {
        this.text = text;
        this.value = value;
    }

    /**
     * 根据 value 获取枚举
     *
     * @param value 枚举值的value
     * @return 枚举值
     */
    public static PictureReviewStatusEnum getEnumByValue(int value) {
        if (ObjUtil.isEmpty(value)) {
            return null;
        }
        for (PictureReviewStatusEnum anEnum : PictureReviewStatusEnum.values()) {
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
        PictureReviewStatusEnum[] values = PictureReviewStatusEnum.values();
        for (PictureReviewStatusEnum anEnum : values) {
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
        PictureReviewStatusEnum[] values = PictureReviewStatusEnum.values();
        for (PictureReviewStatusEnum anEnum : values) {
            if (anEnum.text.equals(text)) {
                return anEnum.value;
            }
        }
        throw new RuntimeException("Text所对应的Value不存在");
    }
}
