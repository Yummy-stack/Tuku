package com.tuku.tukuModel.entity.user;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serial;
import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;


@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("user")
public class User implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 账号
     */
    private String userAccount;

    /**
     * 密码
     */
    private String userPassword;

    /**
     * 用户昵称
     */
    private String userName;

    /**
     * 用户头像
     */
    private String userAvatar;

    /**
     * 用户简介
     */
    private String userProfile;

    /**
     * 用户角色：user/admin
     */
    private String userRole;

    /**
     * 会员过期时间
     */
    @JsonFormat(pattern = "YYYY-MM-DD hh:mm:ss",timezone = "GMT+8")
    private Date vipExpireTime;

    /**
     * 会员兑换码
     */
    private String vipCode;

    /**
     * 会员编号
     */
    private Long vipNumber;

    /**
     * 分享码
     */
    private String shareCode;

    /**
     * 邀请用户 id
     */
    private Long inviteUser;

    /**
     * 编辑时间
     */
    @JsonFormat(pattern = "YYYY-MM-DD hh:mm:ss",timezone = "GMT+8")
    private Date editTime;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "YYYY-MM-DD hh:mm:ss",timezone = "GMT+8")
    private Date createTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "YYYY-MM-DD hh:mm:ss",timezone = "GMT+8")
    private Date updateTime;

    /**
     * 是否删除
     */
    @TableLogic
    private Integer isDelete;


}
