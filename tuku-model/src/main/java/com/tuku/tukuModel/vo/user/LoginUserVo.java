package com.tuku.tukuModel.vo.user;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class LoginUserVo implements Serializable {
    @Serial
    private static final long serialVersionUID = -5401711494779716772L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    @ApiModelProperty(value = "用户昵称")
    private String userName;

    @ApiModelProperty(value = "用户头像")
    private String userAvatar;

    @ApiModelProperty(value = "用户简介")
    private String userProfile;

    @ApiModelProperty(value = "用户角色：user/admin")
    private String userRole;

    @ApiModelProperty(value = "会员过期时间")
    private LocalDateTime vipExpireTime;

    @ApiModelProperty(value = "会员兑换码")
    private String vipCode;

    @ApiModelProperty(value = "会员编号")
    private Long vipNumber;

    @ApiModelProperty(value = "分享码")
    private String shareCode;

    @ApiModelProperty(value = "邀请用户 id")
    private Long inviteUser;

    @ApiModelProperty(value = "编辑时间")
    private LocalDateTime editTime;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;


}
