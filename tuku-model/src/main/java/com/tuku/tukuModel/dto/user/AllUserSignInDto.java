package com.tuku.tukuModel.dto.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class AllUserSignInDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 8292374776812657409L;

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "年份")
    private Integer year;

}
