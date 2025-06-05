import { defineStore } from 'pinia'
import { ref } from 'vue'
import Cookies from 'js-cookie'
import { login as loginApi, getUserInfo } from '@/api/user'

export const useUserStore = defineStore('user', () => {
  const token = ref(Cookies.get('token') || '')
  const userInfo = ref({})

  // 登录
  const login = async (loginForm) => {
    try {
      const response = await loginApi(loginForm)
      if (response.data.code === 200) {
        token.value = response.data.data.token
        Cookies.set('token', token.value, { expires: 7 })
        return { success: true }
      } else {
        return { success: false, message: response.data.message }
      }
    } catch (error) {
      return { success: false, message: '登录失败' }
    }
  }

  // 登出
  const logout = () => {
    token.value = ''
    userInfo.value = {}
    Cookies.remove('token')
  }

  // 获取用户信息
  const fetchUserInfo = async () => {
    try {
      const response = await getUserInfo()
      if (response.data.code === 200) {
        userInfo.value = response.data.data
      }
    } catch (error) {
      console.error('获取用户信息失败:', error)
    }
  }

  // 初始化
  const init = () => {
    if (token.value) {
      fetchUserInfo()
    }
  }

  return {
    token,
    userInfo,
    login,
    logout,
    fetchUserInfo,
    init
  }
})