package com.tuku.tukuModel.dto.pictureSpace;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class PicSpaCreateDto implements Serializable {
    @Serial
    private static final long serialVersionUID = -7577309180123141250L;

    @ApiModelProperty(value = "空间名称")
    private String spaceName;

    @ApiModelProperty(value = "空间级别： 0 普通版 1专业版 2旗舰版")
    private Integer spaceLevel;

}
