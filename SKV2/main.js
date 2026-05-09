/**
 * 应用入口
 * 功能：创建 Vue 应用、注册 Pinia
 */
import { createSSRApp } from 'vue'
import App from './App.vue'
import pinia from './stores/index.js'

export function createApp() {
  const app = createSSRApp(App)
  app.use(pinia)

  app.config.errorHandler = (err, instance, info) => {
    console.error('全局错误:', err, info)
    uni.showToast({ title: '系统异常，请稍后重试', icon: 'none' })
  }

  return { app }
}

uni.onUnhandledRejection((event) => {
  console.error('未捕获的 Promise 错误:', event.reason)
})
