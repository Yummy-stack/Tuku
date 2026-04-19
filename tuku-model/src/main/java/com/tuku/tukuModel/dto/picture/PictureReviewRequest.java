package com.tuku.tukuModel.dto.picture;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class PictureReviewRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = 7465907976434758583L;

    @ApiModelProperty(value = "图片id")
    private Long id;

    @ApiModelProperty(value = "审核状态 0待审核 1通过 2不通过")
    private Integer reviewStatus;

    @ApiModelProperty(value = "审核信息")
    private String reviewMessage;
}
