package com.tuku.tukuModel.enums.task;

import cn.hutool.core.util.ObjUtil;
import com.tuku.tukuModel.enums.picture.PictureReviewStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
public enum TaskStatusEnum {
    SCHEDULED("初始化", 0),
    EXECUTED("已执行", 1),
    CANCELLED("已取消", 2);

    private final String text;

    private final Integer value;

    /**
     * 根据 value 获取枚举
     *
     * @param value 枚举值的value
     * @return 枚举值
     */
    public static TaskStatusEnum getEnumByValue(int value) {
        if (ObjUtil.isEmpty(value)) {
            return null;
        }
        for (TaskStatusEnum anEnum : TaskStatusEnum.values()) {
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
        TaskStatusEnum[] values = TaskStatusEnum.values();
        for (TaskStatusEnum anEnum : values) {
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
        TaskStatusEnum[] values = TaskStatusEnum.values();
        for (TaskStatusEnum anEnum : values) {
            if (anEnum.text.equals(text)) {
                return anEnum.value;
            }
        }
        throw new RuntimeException("Text所对应的Value不存在");
    }
}
