// @ts-ignore
/* eslint-disable */
import request from '@/request'

/** 管理员添加用户 POST /api/user/add */
export async function userAddUsingPost(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.userAddUsingPOSTParams,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseBoolean_>('/api/user/add', {
    method: 'POST',
    params: {
      ...params,
    },
    ...(options || {}),
  })
}

/** 管理员获取所有用户信息 POST /api/user/list */
export async function listUserVoByPageUsingPost(
  body: API.UserQueryDto,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponsePageUser_>('/api/user/list', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** 用户登录 POST /api/user/login */
export async function userLoginUsingPost(body: API.UserLoginDto, options?: { [key: string]: any }) {
  return request<API.BaseResponseLong_>('/api/user/login', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** 获取当前登录用户信息 GET /api/user/login-user */
export async function getLoginUserUsingGet(options?: { [key: string]: any }) {
  return request<API.BaseResponseLoginUserVo_>('/api/user/login-user', {
    method: 'GET',
    ...(options || {}),
  })
}

/** 用户退出登录 POST /api/user/logout */
export async function userLogoutUsingPost(options?: { [key: string]: any }) {
  return request<API.BaseResponseBoolean_>('/api/user/logout', {
    method: 'POST',
    ...(options || {}),
  })
}

/** 用户注册 POST /api/user/register */
export async function userRegisterUsingPost(
  body: API.UserRegisterDto,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseBoolean_>('/api/user/register', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}
