package com.tuku.tukuModel.dto.pictureSpace;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class PicSpaceDeleteDto implements Serializable {
    @Serial
    private static final long serialVersionUID = -8425545407952512388L;

    @ApiModelProperty(value = "空间的ID")
    private Long id;

}
