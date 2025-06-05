import request from './request'

// 登录
export const login = (data) => {
  return request({
    url: '/admin/login',
    method: 'post',
    data
  })
}

// 获取用户信息
export const getUserInfo = () => {
  return request({
    url: '/admin/info',
    method: 'get'
  })
}

// 登出
export const logout = () => {
  return request({
    url: '/admin/logout',
    method: 'post'
  })
}

// 修改密码
export const updatePassword = (data) => {
  return request({
    url: '/admin/updatePassword',
    method: 'post',
    data
  })
}