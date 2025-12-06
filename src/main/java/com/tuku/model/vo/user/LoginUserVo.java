package com.tuku.model.vo.user;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class LoginUserVo implements Serializable {
    private static final long serialVersionUID = -5401711494779716772L;
    /**
     * id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 用户昵称
     */
    @TableField("userName")
    private String userName;

    /**
     * 用户头像
     */
    @TableField("userAvatar")
    private String userAvatar;

    /**
     * 用户简介
     */
    @TableField("userProfile")
    private String userProfile;

    /**
     * 用户角色：user/admin
     */
    @TableField("userRole")
    private String userRole;

    /**
     * 会员过期时间
     */
    @TableField("vipExpireTime")
    private LocalDateTime vipExpireTime;

    /**
     * 会员兑换码
     */
    @TableField("vipCode")
    private String vipCode;

    /**
     * 会员编号
     */
    @TableField("vipNumber")
    private Long vipNumber;

    /**
     * 分享码
     */
    @TableField("shareCode")
    private String shareCode;

    /**
     * 邀请用户 id
     */
    @TableField("inviteUser")
    private Long inviteUser;

    /**
     * 编辑时间
     */
    @TableField("editTime")
    private LocalDateTime editTime;

    /**
     * 创建时间
     */
    @TableField("createTime")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField("updateTime")
    private LocalDateTime updateTime;


}
