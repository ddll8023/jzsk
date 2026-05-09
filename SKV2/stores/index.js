/**
 * Pinia 状态管理入口
 * 功能：创建 Pinia 实例并注册持久化插件
 */
import { createPinia } from 'pinia'
import piniaPluginPersistedstate from 'pinia-plugin-persistedstate'

const pinia = createPinia()
pinia.use(piniaPluginPersistedstate)

export default pinia
