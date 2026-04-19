// @ts-ignore
/* eslint-disable */
import request from '@/request'

/** 文件下载 GET /api/test/file/download */
export async function downloadFileUsingGet(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.downloadFileUsingGETParams,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseBoolean_>('/api/test/file/download', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  })
}

/** 文件上传 POST /api/test/file/upload */
export async function uploadFileUsingPost(
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

  return request<API.BaseResponseString_>('/api/test/file/upload', {
    method: 'POST',
    data: formData,
    requestType: 'form',
    ...(options || {}),
  })
}
