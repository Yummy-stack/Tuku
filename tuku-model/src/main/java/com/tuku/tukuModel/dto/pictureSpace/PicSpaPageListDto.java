package com.tuku.tukuModel.dto.pictureSpace;

import com.tuku.tukuModel.request.PageRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
public class PicSpaPageListDto extends PageRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = 8468509662022346066L;

    @ApiModelProperty(value = "空间名称")
    private String spaceName;

    @ApiModelProperty(value = "最早创建时间")
    private Date leftCreateTime;

    @ApiModelProperty(value = "最晚创建时间")
    private Date rightCreateTime;
}
