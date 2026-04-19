package com.tuku.tukuModel.dto.user;

import com.tuku.tukuModel.request.PageRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
public class UserQueryDto extends PageRequest implements Serializable {

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "用户昵称")
    private String userName;

    @ApiModelProperty(value = "账号")
    private String userAccount;

    @ApiModelProperty(value = "简介")
    private String userProfile;

    @ApiModelProperty(value = "用户角色：user/admin/ban")
    private String userRole;

    @Serial
    private static final long serialVersionUID = 1L;
}
