package com.tuku.tukuModel.request.picture;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class PictureUploadByBatchRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = 2984673511213539820L;

    @ApiModelProperty(value = "搜索词")
    private String searchText;

    @ApiModelProperty(value = "抓取数量")
    private Integer count = 10;
}
