declare namespace API {
  type BaseResponseBoolean_ = {
    code?: number
    data?: boolean
    message?: string
  }

  type BaseResponseLoginUserVo_ = {
    code?: number
    data?: LoginUserVo
    message?: string
  }

  type BaseResponseLong_ = {
    code?: number
    data?: number
    message?: string
  }

  type downloadPictureUsingGETParams = {
    /** fileName */
    fileName: string
  }

  type LoginUserVo = {
    createTime?: string
    editTime?: string
    id?: number
    inviteUser?: number
    shareCode?: string
    updateTime?: string
    userAvatar?: string
    userName?: string
    userProfile?: string
    userRole?: string
    vipCode?: string
    vipExpireTime?: string
    vipNumber?: number
  }

  type userAddUsingPOSTParams = {
    userAccount?: string
    userAvatar?: string
    userName?: string
    userProfile?: string
    userRole?: string
  }

  type UserLoginDto = {
    /** 用户账号 */
    userAccount?: string
    /** 用户密码 */
    userPassword?: string
  }

  type UserRegisterDto = {
    /** 用户账号 */
    userAccount?: string
    /** 用户确认密码 */
    userConfirmPassword?: string
    /** 用户密码 */
    userPassword?: string
    /** 账号身份 */
    userRole?: string
  }
}
