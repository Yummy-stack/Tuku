package com.tuku.tukuModel.enums.pictureSpace;

import lombok.Getter;



@Getter
public enum PictureSpaceLevelEnum {
    BASIC(0,"普通版",100 * 1024L * 1024L,100L),
    PROFESSION(1,"专业版",1000 * 1024L * 1024L,1000L),
    PREMIUM(2,"旗舰版",10000 * 1024L * 1024L,10000L);

    private final Integer value;

    private final String text;

    private final Long size;

    private final Long total;

    PictureSpaceLevelEnum(int value, String text, Long size, Long total) {
        this.value = value;
        this.text = text;
        this.size = size;
        this.total = total;
    }

    public static PictureSpaceLevelEnum getEnumByValue(Integer value) {
        PictureSpaceLevelEnum[] pictureSpaceLevelEnums = PictureSpaceLevelEnum.values();
        for (PictureSpaceLevelEnum pictureSpaceLevelEnum : pictureSpaceLevelEnums) {
            if (value.equals(pictureSpaceLevelEnum.getValue())) {
                return pictureSpaceLevelEnum;
            }
        }
        return null;
    }

}
