/**
 * 应用入口文件
 * 功能：Vue3 应用初始化、插件注册、全局配置
 * 参考：frontend/src/main.js
 */

// 1. Vue 官方 API
import { createApp } from 'vue'

// 2. Pinia Store
import { createPinia } from 'pinia'

// 3. Vue Router
import router from './router'

// 4. 根组件
import App from './App.vue'

// 5. 工具函数
import { getBestApiURL, getApiBaseURL } from './config/api'
import { setupAxios } from './utils/request'

// 6. 全局样式
import './assets/css/main.css'

// 7. FontAwesome 图标
import '@fortawesome/fontawesome-free/css/all.min.css'

// 8. OpenLayers 样式
import 'ol/ol.css'

/**
 * 智能配置 API 地址
 * 根据访问环境自动选择最佳 API 地址
 */
const initApiConfig = async () => {
  try {
    console.log('开始智能选择API地址...')
    const apiBaseURL = await getBestApiURL()
    console.log('✅ 智能选择的API Base URL:', apiBaseURL)
    console.log('📍 当前访问地址:', window.location.href)
    console.log('🌐 主机名:', window.location.hostname)
    
    // 配置 Axios
    setupAxios(apiBaseURL)
    
    console.log('🎉 API配置成功')
  } catch (error) {
    console.error('❌ API配置失败，使用默认配置:', error)
    const fallbackURL = getApiBaseURL()
    console.log('🔄 使用备用API Base URL:', fallbackURL)
    setupAxios(fallbackURL)
  }
}

/**
 * 应用启动函数
 * 异步初始化 API 配置后挂载应用
 */
const bootstrap = async () => {
  try {
    // 初始化 API 配置（关键：必须等待完成）
    await initApiConfig()
    console.log('✅ API配置初始化完成，开始挂载应用')
    
    // 创建 Vue 应用
    const app = createApp(App)
    
    // 注册 Pinia
    const pinia = createPinia()
    app.use(pinia)
    
    // 注册路由
    app.use(router)
    
    // 挂载应用
    app.mount('#app')
    console.log('✅ 应用挂载完成')
  } catch (error) {
    console.error('❌ 应用启动失败:', error)
    // 显示错误提示
    document.body.innerHTML = `
      <div style="display: flex; justify-content: center; align-items: center; height: 100vh; font-family: sans-serif;">
        <div style="text-align: center;">
          <h2 style="color: #e53e3e;">应用启动失败</h2>
          <p style="color: #718096;">请检查网络连接或联系管理员</p>
          <button onclick="location.reload()" style="margin-top: 20px; padding: 10px 20px; background: #3182ce; color: white; border: none; border-radius: 4px; cursor: pointer;">
            重新加载
          </button>
        </div>
      </div>
    `
  }
}

// 启动应用
bootstrap()
