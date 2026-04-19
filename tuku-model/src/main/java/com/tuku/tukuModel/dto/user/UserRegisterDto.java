package com.tuku.tukuModel.dto.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class UserRegisterDto implements Serializable {
    @Serial
    private static final long serialVersionUID = -7310350553292713752L;

    @ApiModelProperty(value = "用户账号")
    private String userAccount;

    @ApiModelProperty(value = "用户密码")
    private String userPassword;

    @ApiModelProperty(value = "用户确认密码")
    private String userConfirmPassword;

    @ApiModelProperty(value = "账号身份")
    private String userRole;
}
