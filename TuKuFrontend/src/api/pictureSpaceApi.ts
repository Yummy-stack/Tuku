// @ts-ignore
/* eslint-disable */
import request from '@/request'

/** 个人空间或空间管理 - 添加图片 POST /api/picture-space/add-picture/one */
export async function addPictureSpaceOneUsingPost(
  body: API.PicSpaAddPicOneDto,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponsePicture_>('/api/picture-space/add-picture/one', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** 个人空间或空间管理 - 创建空间 POST /api/picture-space/create */
export async function createPictureSpaceUsingPost(
  body: API.PicSpaCreateDto,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponsePictureSpace_>('/api/picture-space/create', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** 空间管理 - 空间删除 POST /api/picture-space/delete */
export async function deletePictureSpaceUsingPost(
  body: API.PicSpaceDeleteDto,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseBoolean_>('/api/picture-space/delete', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** 个人空间或空间管理 - 删除图片 POST /api/picture-space/delete-picture/one */
export async function deletePictureSpaceOneUsingPost(
  body: API.PicSpaDeletePicOneDto,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseBoolean_>('/api/picture-space/delete-picture/one', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** 个人空间或空间管理 - 查看空间详情 POST /api/picture-space/detail */
export async function detailPictureSpaceUsingPost(
  body: API.PicSpaDetailDto,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponsePicSpaDetailVO_>('/api/picture-space/detail', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** 空间管理 - 空间编辑 POST /api/picture-space/edit */
export async function editPictureSpaceUsingPost(options?: { [key: string]: any }) {
  return request<API.BaseResponseBoolean_>('/api/picture-space/edit', {
    method: 'POST',
    ...(options || {}),
  })
}

/** 空间管理 - 查询空间列表 POST /api/picture-space/list/page */
export async function listPictureSpaceByPageUsingPost(
  body: API.PicSpaPageListDto,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponsePagePictureSpace_>('/api/picture-space/list/page', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** 个人空间或空间管理 - 获取所有空间级别 POST /api/picture-space/picture-space/levels */
export async function pictureSpaceLevelsUsingPost(options?: { [key: string]: any }) {
  return request<API.BaseResponseListPicSpaLevelVO_>('/api/picture-space/picture-space/levels', {
    method: 'POST',
    ...(options || {}),
  })
}
