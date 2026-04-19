declare namespace API {
  type BaseResponseBoolean_ = {
    code?: number
    data?: boolean
    message?: string
  }

  type BaseResponseInt_ = {
    code?: number
    data?: number
    message?: string
  }

  type BaseResponseListPicSpaLevelVO_ = {
    code?: number
    data?: PicSpaLevelVO[]
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

  type BaseResponsePagePicture_ = {
    code?: number
    data?: PagePicture_
    message?: string
  }

  type BaseResponsePagePictureSpace_ = {
    code?: number
    data?: PagePictureSpace_
    message?: string
  }

  type BaseResponsePagePictureVO_ = {
    code?: number
    data?: PagePictureVO_
    message?: string
  }

  type BaseResponsePageUser_ = {
    code?: number
    data?: PageUser_
    message?: string
  }

  type BaseResponsePicSpaDetailVO_ = {
    code?: number
    data?: PicSpaDetailVO
    message?: string
  }

  type BaseResponsePicture_ = {
    code?: number
    data?: Picture
    message?: string
  }

  type BaseResponsePictureSpace_ = {
    code?: number
    data?: PictureSpace
    message?: string
  }

  type BaseResponsePictureTagCategory_ = {
    code?: number
    data?: PictureTagCategory
    message?: string
  }

  type BaseResponsePictureVO_ = {
    code?: number
    data?: PictureVO
    message?: string
  }

  type BaseResponseString_ = {
    code?: number
    data?: string
    message?: string
  }

  type DeleteRequest = {
    id?: number
  }

  type downloadFileUsingGETParams = {
    /** fileName */
    fileName: string
  }

  type downloadPictureUsingGET1Params = {
    /** 下载的文件名 */
    filePath: string
  }

  type downloadPictureUsingGETParams = {
    /** fileName */
    fileName: string
  }

  type downloadPictureV2UsingGETParams = {
    /** 下载的文件名 */
    filePath: string
  }

  type downloadUsingGETParams = {
    /** fileName */
    fileName: string
  }

  type getPictureByIdUsingGETParams = {
    /** 图片ID */
    id: number
  }

  type getPictureVOByIdUsingGETParams = {
    /** 图片ID */
    id: number
  }

  type getPictureVOByIdWithCacheUsingGETParams = {
    /** 图片ID */
    id: number
  }

  type LoginUserVo = {
    /** 创建时间 */
    createTime?: string
    /** 编辑时间 */
    editTime?: string
    id?: number
    /** 邀请用户 id */
    inviteUser?: number
    /** 分享码 */
    shareCode?: string
    /** 更新时间 */
    updateTime?: string
    /** 用户头像 */
    userAvatar?: string
    /** 用户昵称 */
    userName?: string
    /** 用户简介 */
    userProfile?: string
    /** 用户角色：user/admin */
    userRole?: string
    /** 会员兑换码 */
    vipCode?: string
    /** 会员过期时间 */
    vipExpireTime?: string
    /** 会员编号 */
    vipNumber?: number
  }

  type OrderItem = {
    asc?: boolean
    column?: string
  }

  type PagePicture_ = {
    countId?: string
    current?: number
    maxLimit?: number
    optimizeCountSql?: boolean
    orders?: OrderItem[]
    pages?: number
    records?: Picture[]
    searchCount?: boolean
    size?: number
    total?: number
  }

  type PagePictureSpace_ = {
    countId?: string
    current?: number
    maxLimit?: number
    optimizeCountSql?: boolean
    orders?: OrderItem[]
    pages?: number
    records?: PictureSpace[]
    searchCount?: boolean
    size?: number
    total?: number
  }

  type PagePictureVO_ = {
    countId?: string
    current?: number
    maxLimit?: number
    optimizeCountSql?: boolean
    orders?: OrderItem[]
    pages?: number
    records?: PictureVO[]
    searchCount?: boolean
    size?: number
    total?: number
  }

  type PageUser_ = {
    countId?: string
    current?: number
    maxLimit?: number
    optimizeCountSql?: boolean
    orders?: OrderItem[]
    pages?: number
    records?: User[]
    searchCount?: boolean
    size?: number
    total?: number
  }

  type PicSpaAddPicOneDto = {
    /** 图片空间id */
    id?: number
    /** 图片id */
    pictureId?: number
  }

  type PicSpaceDeleteDto = {
    /** 空间的ID */
    id?: number
  }

  type PicSpaCreateDto = {
    /** 空间级别： 0 普通版 1专业版 2旗舰版 */
    spaceLevel?: number
    /** 空间名称 */
    spaceName?: string
  }

  type PicSpaDeletePicOneDto = {
    /** 图片空间id */
    id?: number
    /** 图片id */
    pictureId?: number
  }

  type PicSpaDetailDto = {
    /** 图片空间id */
    id?: number
  }

  type PicSpaDetailVO = {
    /** 当前空间下图片的总数量 */
    currentCount?: number
    /** 当前空间下图片的总大小 */
    currentSize?: number
    /** 主键ID */
    id?: number
    /** 空间里的图片信息 */
    pictureVOList?: PictureVO[]
    /** 空间级别： 0 普通版 1专业版 2旗舰版 */
    spaceLevel?: number
    /** 空间的图片容量 */
    spaceMaxNumber?: number
    /** 空间的大小 */
    spaceMaxSize?: number
    /** 空间名称 */
    spaceName?: string
  }

  type PicSpaLevelVO = {
    size?: number
    text?: string
    total?: number
    value?: number
  }

  type PicSpaPageListDto = {
    /** 最早创建时间 */
    leftCreateTime?: string
    pageNum?: number
    pageSize?: number
    /** 最晚创建时间 */
    rightCreateTime?: string
    sortField?: string
    sortOrder?: string
    /** 空间名称 */
    spaceName?: string
  }

  type Picture = {
    /** 图片创建时间 */
    createTime?: string
    /** 图片创建人id */
    createUser?: number
    /** 图片编辑时间 */
    editTime?: string
    /** 主键ID */
    id?: number
    /** 是否删除 */
    isDelete?: number
    /** 图片种类 */
    picCategory?: string
    /** 图片格式 */
    picFormat?: string
    /** 图片高度 */
    picHeight?: number
    /** 图片简介 */
    picIntroduction?: string
    /** 图片名称 */
    picName?: string
    /** 图片宽高比例 */
    picScale?: number
    /** 图片体积 */
    picSize?: number
    /** 图片标签 */
    picTags?: string
    /** 图片URL路径 */
    picUrl?: string
    /** 图片宽度 */
    picWidth?: number
    /** 审核信息 */
    reviewMessage?: string
    /** 审核状态 0待审核 1通过 2不通过 */
    reviewStatus?: number
    /** 审核时间 */
    reviewTime?: string
    /** 审核人 */
    reviewUser?: number
    /** 图片更新时间 */
    updateTime?: string
    /** 图片修改人 */
    updateUser?: number
  }

  type PictureEditRequest = {
    /** 分类 */
    category?: string
    /** id */
    id?: number
    /** 简介 */
    introduction?: string
    /** 图片名称 */
    name?: string
    /** 标签 */
    tags?: string[]
  }

  type PictureQueryRequest = {
    /** 分类 */
    category?: string
    /** id */
    id?: number
    /** 简介 */
    introduction?: string
    /** 图片名称 */
    name?: string
    pageNum?: number
    pageSize?: number
    /** 图片格式 */
    picFormat?: string
    /** 图片高度 */
    picHeight?: number
    /** 图片比例 */
    picScale?: number
    /** 文件体积 */
    picSize?: number
    /** 图片宽度 */
    picWidth?: number
    /** 搜索词（同时搜名称、简介等） */
    searchText?: string
    sortField?: string
    sortOrder?: string
    /** 标签 */
    tags?: string[]
    /** 用户 id */
    userId?: number
  }

  type PictureSpace = {
    /** 创建时间 */
    createTime?: string
    /** 创建用户id */
    createUser?: number
    /** 当前空间下图片的总数量 */
    currentCount?: number
    /** 当前空间下图片的总大小 */
    currentSize?: number
    /** 编辑时间 */
    editTime?: string
    /** 主键ID */
    id?: number
    /** 空间级别： 0 普通版 1专业版 2旗舰版 */
    spaceLevel?: number
    /** 空间的图片容量 */
    spaceMaxNumber?: number
    /** 空间的大小 */
    spaceMaxSize?: number
    /** 空间名称 */
    spaceName?: string
    /** 修改时间 */
    updateTime?: string
    /** 修改用户id */
    updateUser?: number
  }

  type PictureTagCategory = {
    categoryList?: string[]
    tagList?: string[]
  }

  type PictureUpdateRequest = {
    category?: string
    id?: number
    introduction?: string
    name?: string
    tags?: string[]
  }

  type PictureUploadByBatchRequest = {
    /** 抓取数量 */
    count?: number
    /** 搜索词 */
    searchText?: string
  }

  type PictureUploadRequest = {
    fileUrl?: string
    /** 图片 id（用于修改） */
    id?: number
  }

  type PictureVO = {
    /** 图片创建时间 */
    createTime?: string
    /** 图片创建人id */
    createUser?: number
    /** 图片编辑时间 */
    editTime?: string
    /** 主键ID */
    id?: number
    /** 是否删除 0存在 1删除 */
    isDelete?: number
    /** 图片种类 */
    picCategory?: string
    /** 图片格式 */
    picFormat?: string
    /** 图片高度 */
    picHeight?: number
    /** 图片简介 */
    picIntroduction?: string
    /** 图片名称 */
    picName?: string
    /** 图片宽高比例 */
    picScale?: number
    /** 图片体积 */
    picSize?: number
    /** 图片标签 */
    picTags?: string[]
    /** 图片URL路径 */
    picUrl?: string
    /** 图片宽度 */
    picWidth?: number
    /** 审核信息 */
    reviewMessage?: string
    /** 待审核 通过 不通过 */
    reviewStatusText?: string
    /** 审核时间 */
    reviewTime?: string
    /** 审核人 */
    reviewUser?: number
    /** 图片更新时间 */
    updateTime?: string
    /** 图片修改人 */
    updateUser?: number
  }

  type reviewPictureUsingPOSTParams = {
    /** 图片id */
    id?: number
    /** 审核信息 */
    reviewMessage?: string
    /** 审核状态 0待审核 1通过 2不通过 */
    reviewStatus?: number
  }

  type uploadPictureUsingPOST1Params = {
    /** 图片 id（用于修改） */
    id?: number
    fileUrl?: string
  }

  type uploadPictureV2UsingPOSTParams = {
    /** 图片 id（用于修改） */
    id?: number
    fileUrl?: string
  }

  type User = {
    createTime?: string
    editTime?: string
    id?: number
    inviteUser?: number
    isDelete?: number
    shareCode?: string
    updateTime?: string
    userAccount?: string
    userAvatar?: string
    userName?: string
    userPassword?: string
    userProfile?: string
    userRole?: string
    vipCode?: string
    vipExpireTime?: string
    vipNumber?: number
  }

  type userAddUsingPOSTParams = {
    /** 账号 */
    userAccount?: string
    /** 用户头像 */
    userAvatar?: string
    /** 用户昵称 */
    userName?: string
    /** 用户简介 */
    userProfile?: string
    /** 用户角色: user, admin */
    userRole?: string
  }

  type UserLoginDto = {
    /** 用户账号 */
    userAccount?: string
    /** 用户密码 */
    userPassword?: string
  }

  type UserQueryDto = {
    /** id */
    id?: number
    pageNum?: number
    pageSize?: number
    sortField?: string
    sortOrder?: string
    /** 账号 */
    userAccount?: string
    /** 用户昵称 */
    userName?: string
    /** 简介 */
    userProfile?: string
    /** 用户角色：user/admin/ban */
    userRole?: string
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
