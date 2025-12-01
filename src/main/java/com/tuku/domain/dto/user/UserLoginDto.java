package com.tuku.domain.dto.user;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class UserLoginDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 6104423073367378703L;

    @ApiModelProperty(value = "用户账号")
    private String userAccount;

    @ApiModelProperty(value = "用户密码")
    private String userPassword;
}
