/**
 * Vite 配置文件
 * 功能：Vue3 项目构建配置
 * 参考：frontend/vite.config.js + frontend/vue.config.js
 */
import vue from '@vitejs/plugin-vue'
import { resolve } from 'path'

export default {
  plugins: [
    vue()
  ],
  resolve: {
    alias: {
      '@': resolve(__dirname, 'src'),
    },
    extensions: ['.mjs', '.js', '.ts', '.jsx', '.tsx', '.json', '.vue'],
  },
  server: {
    host: '0.0.0.0', // 允许所有IP访问
    port: 8084, // 修改端口为8084
    cors: true, // 允许跨域
    proxy: {
      // 代理所有 /api 请求到后端V2（保留 /api 前缀，后端Controller已包含 /api）
      '/api': {
        target: 'http://192.168.20.3:8081',
        changeOrigin: true,
        timeout: 10000
      },
      // 代理公网API请求
      '/public-api': {
        target: 'http://111.4.68.108:8081',
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/public-api/, ''),
        timeout: 10000
      }
    },
    headers: {
      'Access-Control-Allow-Origin': '*',
      'Access-Control-Allow-Methods': 'GET, POST, PUT, DELETE, PATCH, OPTIONS',
      'Access-Control-Allow-Headers': 'X-Requested-With, content-type, Authorization'
    }
  }
}
