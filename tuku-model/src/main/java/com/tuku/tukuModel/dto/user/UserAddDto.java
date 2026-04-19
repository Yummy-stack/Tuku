package com.tuku.tukuModel.dto.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class UserAddDto implements Serializable {

    @ApiModelProperty(value = "用户昵称")
    private String userName;

    @ApiModelProperty(value = "账号")
    private String userAccount;

    @ApiModelProperty(value = "用户头像")
    private String userAvatar;

    @ApiModelProperty(value = "用户简介")
    private String userProfile;

    @ApiModelProperty(value = "用户角色: user, admin")
    private String userRole;

    private static final long serialVersionUID = 1L;
}
