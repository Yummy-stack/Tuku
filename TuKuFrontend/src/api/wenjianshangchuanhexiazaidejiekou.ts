// @ts-ignore
/* eslint-disable */
import request from '@/request'

/** 文件删除 POST /api/file/delete */
export async function deletePictureUsingPost(options?: { [key: string]: any }) {
  return request<boolean>('/api/file/delete', {
    method: 'POST',
    ...(options || {}),
  })
}

/** 文件下载 GET /api/file/download */
export async function downloadPictureUsingGet(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.downloadPictureUsingGETParams,
  options?: { [key: string]: any }
) {
  return request<boolean>('/api/file/download', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  })
}

/** 文件上传 POST /api/file/upload */
export async function uploadPictureUsingPost(
  body: {},
  multipartFile?: File,
  options?: { [key: string]: any }
) {
  const formData = new FormData()

  if (multipartFile) {
    formData.append('multipartFile', multipartFile)
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

  return request<boolean>('/api/file/upload', {
    method: 'POST',
    data: formData,
    requestType: 'form',
    ...(options || {}),
  })
}
