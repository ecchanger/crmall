import { createApp } from 'vue'
import { createPinia } from 'pinia'
import router from './router'
import App from './App.vue'

// Element Plus
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import 'element-plus/theme-chalk/dark/css-vars.css'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'

// 样式
import './styles/index.scss'

// NProgress
import NProgress from 'nprogress'
import 'nprogress/nprogress.css'

const app = createApp(App)

// 注册所有图标
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component)
}

// 全局配置
app.use(createPinia())
app.use(router)
app.use(ElementPlus)

// NProgress配置
NProgress.configure({ showSpinner: false })

app.mount('#app')