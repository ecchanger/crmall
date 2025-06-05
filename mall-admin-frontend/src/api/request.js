import axios from 'axios'
import { ElMessage } from 'element-plus'
import { useUserStore } from '@/stores/user'
import Cookies from 'js-cookie'

// 创建axios实例
const request = axios.create({
  baseURL: '/api',
  timeout: 30000
})

// 请求拦截器
request.interceptors.request.use(
  config => {
    const token = Cookies.get('token')
    if (token) {
      config.headers['Authorization'] = `Bearer ${token}`
    }
    return config
  },
  error => {
    return Promise.reject(error)
  }
)

// 响应拦截器
request.interceptors.response.use(
  response => {
    const res = response.data
    
    // 如果是下载文件类型的响应，直接返回
    if (response.config.responseType === 'blob') {
      return response
    }
    
    // 统一处理错误
    if (res.code !== 200) {
      ElMessage.error(res.message || '请求失败')
      
      // 401 token过期
      if (res.code === 401) {
        const userStore = useUserStore()
        userStore.logout()
        window.location.href = '/login'
      }
      
      return Promise.reject(new Error(res.message || '请求失败'))
    }
    
    return response
  },
  error => {
    let message = '请求失败'
    
    if (error.response) {
      switch (error.response.status) {
        case 401:
          message = '未授权，请重新登录'
          const userStore = useUserStore()
          userStore.logout()
          window.location.href = '/login'
          break
        case 403:
          message = '拒绝访问'
          break
        case 404:
          message = '请求地址出错'
          break
        case 500:
          message = '服务器内部错误'
          break
        default:
          message = error.response.data?.message || message
      }
    } else if (error.code === 'ECONNABORTED') {
      message = '请求超时'
    } else if (error.message) {
      message = error.message
    }
    
    ElMessage.error(message)
    return Promise.reject(error)
  }
)

export default request