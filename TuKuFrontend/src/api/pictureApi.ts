// @ts-ignore
/* eslint-disable */
import request from '@/request'

/** 删除图片 POST /api/picture/delete */
export async function deletePictureUsingPost1(
  body: API.DeleteRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseBoolean_>('/api/picture/delete', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    data: body,
    ...(options || {})
  })
}

/** 图片下载 GET /api/picture/download */
export async function downloadPictureUsingGet1(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.downloadPictureUsingGET1Params,
  options?: { [key: string]: any }
) {
  return request<any>('/api/picture/download', {
    method: 'GET',
    params: {
      ...params
    },
    ...(options || {})
  })
}

/** 图片下载 v2 GET /api/picture/download/v2 */
export async function downloadPictureV2UsingGet(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.downloadPictureV2UsingGETParams,
  options?: { [key: string]: any }
) {
  return request<any>('/api/picture/download/v2', {
    method: 'GET',
    params: {
      ...params
    },
    ...(options || {})
  })
}

/** 编辑图片（给用户使用） POST /api/picture/edit */
export async function editPictureUsingPost(
  body: API.PictureEditRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseBoolean_>('/api/picture/edit', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    data: body,
    ...(options || {})
  })
}

/** 根据 id 获取图片（仅管理员可用） GET /api/picture/get */
export async function getPictureByIdUsingGet(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getPictureByIdUsingGETParams,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponsePicture_>('/api/picture/get', {
    method: 'GET',
    params: {
      ...params
    },
    ...(options || {})
  })
}

/**  根据 id 获取图片（封装类） GET /api/picture/get/vo */
export async function getPictureVoByIdUsingGet(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getPictureVOByIdUsingGETParams,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponsePictureVO_>('/api/picture/get/vo', {
    method: 'GET',
    params: {
      ...params
    },
    ...(options || {})
  })
}

/**  根据 id 获取图片（封装类） -- 缓存版本 GET /api/picture/get/vo/with/cache */
export async function getPictureVoByIdWithCacheUsingGet(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getPictureVOByIdWithCacheUsingGETParams,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponsePictureVO_>('/api/picture/get/vo/with/cache', {
    method: 'GET',
    params: {
      ...params
    },
    ...(options || {})
  })
}

/** 分页获取图片列表（仅管理员可用） POST /api/picture/list/page */
export async function listPictureByPageUsingPost(
  body: API.PictureQueryRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponsePagePicture_>('/api/picture/list/page', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    data: body,
    ...(options || {})
  })
}

/** 分页获取图片列表（封装类） POST /api/picture/list/page/vo */
export async function listPictureVoByPageUsingPost(
  body: API.PictureQueryRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponsePagePictureVO_>('/api/picture/list/page/vo', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    data: body,
    ...(options || {})
  })
}

/** 分页获取审核通过的图片列表（封装类） POST /api/picture/list/page/vo/v2 */
export async function listPictureVoByPageV2UsingPost(
  body: API.PictureQueryRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponsePagePictureVO_>('/api/picture/list/page/vo/v2', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    data: body,
    ...(options || {})
  })
}

/** 分页获取审核通过的图片列表（封装类）-- 缓存优化版本 POST /api/picture/list/page/vo/v2/with/cache */
export async function listPictureVoByPageV2WithCacheUsingPost(
  body: API.PictureQueryRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponsePagePictureVO_>('/api/picture/list/page/vo/v2/with/cache', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    data: body,
    ...(options || {})
  })
}

/** 图片审核 POST /api/picture/review */
export async function reviewPictureUsingPost(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.reviewPictureUsingPOSTParams,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseBoolean_>('/api/picture/review', {
    method: 'POST',
    params: {
      ...params
    },
    ...(options || {})
  })
}

/** 图片种类和标签 GET /api/picture/tag_category */
export async function listPictureTagCategoryUsingGet(options?: { [key: string]: any }) {
  return request<API.BaseResponsePictureTagCategory_>('/api/picture/tag_category', {
    method: 'GET',
    ...(options || {})
  })
}

/** 更新图片（仅管理员可用） POST /api/picture/update */
export async function updatePictureUsingPost(
  body: API.PictureUpdateRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseBoolean_>('/api/picture/update', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    data: body,
    ...(options || {})
  })
}

/** 批量抓取图片 POST /api/picture/upload/batch */
export async function uploadPictureByBatchUsingPost(
  body: API.PictureUploadByBatchRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseInt_>('/api/picture/upload/batch', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    data: body,
    ...(options || {})
  })
}

/** 图片上传 v1 POST /api/picture/upload/v1 */
export async function uploadPictureUsingPost1(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.uploadPictureUsingPOST1Params,
  body: {},
  file?: File,
  options?: { [key: string]: any }
) {
  const formData = new FormData()

  if (file) {
    formData.append('file', file)
  }

  Object.keys(body).forEach((ele) => {
    const item = (body as any)[ele]

    if (item !== undefined && item !== null) {
      if (typeof item === 'object' && !(item instanceof File)) {
        if (item instanceof Array) {
          item.forEach((f) => formData.append(ele, f || ''))
        } else {
          formData.append(ele, new Blob([JSON.stringify(item)], { type: 'application/json' }))
        }
      } else {
        formData.append(ele, item)
      }
    }
  })

  return request<API.BaseResponsePictureVO_>('/api/picture/upload/v1', {
    method: 'POST',
    params: {
      ...params
    },
    data: formData,
    // requestType: 'form',
    ...(options || {})
  })
}

/** 图片上传 v1 v2 POST /api/picture/upload/v1/v2 */
export async function uploadPictureV2UsingPost(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.uploadPictureV2UsingPOSTParams,
  body: {},
  file?: File,
  options?: { [key: string]: any }
) {
  const formData = new FormData()

  if (file) {
    formData.append('file', file)
  }

  Object.keys(body).forEach((ele) => {
    const item = (body as any)[ele]

    if (item !== undefined && item !== null) {
      if (typeof item === 'object' && !(item instanceof File)) {
        if (item instanceof Array) {
          item.forEach((f) => formData.append(ele, f || ''))
        } else {
          formData.append(ele, new Blob([JSON.stringify(item)], { type: 'application/json' }))
        }
      } else {
        formData.append(ele, item)
      }
    }
  })

  return request<API.BaseResponsePictureVO_>('/api/picture/upload/v1/v2', {
    method: 'POST',
    params: {
      ...params
    },
    data: formData,
    // requestType: 'form',
    ...(options || {})
  })
}

/** 图片上传（Url）v2 POST /api/picture/upload/v2 */
export async function uploadPictureByUrlUsingPost(
  body: API.PictureUploadRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponsePictureVO_>('/api/picture/upload/v2', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    data: body,
    ...(options || {})
  })
}
