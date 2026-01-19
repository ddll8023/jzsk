<template>
  <!-- 巡检APP下载页面 -->
  <main class="min-h-screen bg-gray-50 flex items-center justify-center p-4">
    <Card variant="default" padding="lg" rounded="xl" shadow="lg" class="w-full max-w-md">
      <!-- 标题区 -->
      <header class="text-center mb-8">
        <h1 class="text-2xl font-semibold text-slate-900 mb-2">巡检APP下载</h1>
        <p class="text-sm text-slate-600">扫描二维码或点击按钮下载安装</p>
      </header>

      <!-- 二维码区域 -->
      <section class="flex flex-col items-center mb-8">
        <div class="w-48 h-48 bg-white border-2 border-gray-200 rounded-lg p-4 mb-4 flex items-center justify-center">
          <img 
            :src="qrcodeUrl" 
            alt="巡检APP下载二维码"
            class="w-full h-full object-contain"
            @error="handleImageError"
          />
        </div>
        <p class="text-xs text-slate-500 text-center">
          <i class="fa fa-mobile text-primary-600 mr-1" aria-hidden="true"></i>
          使用手机扫描二维码下载
        </p>
      </section>

      <!-- 下载信息 -->
      <section class="bg-slate-50 rounded-lg p-4 mb-6">
        <div class="flex items-start gap-3 text-sm text-slate-700">
          <i class="fa fa-info-circle text-primary-600 mt-0.5" aria-hidden="true"></i>
          <div class="flex-1">
            <p class="font-medium mb-1">安装说明</p>
            <ul class="text-xs text-slate-600 space-y-1">
              <li>• 支持 Android 5.0 及以上版本</li>
              <li>• 首次安装需允许"未知来源"权限</li>
              <li>• 文件大小约 {{ appSize }}</li>
            </ul>
          </div>
        </div>
      </section>

      <!-- 按钮区域 -->
      <footer class="flex gap-3">
        <Button
          type="primary"
          size="lg"
          icon="download"
          block
          class="flex-1"
          @click="handleDownload"
        >
          立即下载
        </Button>
        <Button
          type="default"
          size="lg"
          icon="arrow-left"
          class="w-auto px-6"
          @click="handleBack"
        >
          返回
        </Button>
      </footer>
    </Card>
  </main>
</template>

<script setup>
/**
 * 巡检APP下载页面
 * 功能：展示二维码和提供APK下载
 * 依赖组件：Card, Button
 * 遵循原则：KISS, YAGNI, SOLID
 * Source: 前端页面规范.md, 前端组件规范.md
 */
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import Card from '@/components/basic/Card.vue'
import Button from '@/components/basic/Button.vue'

const router = useRouter()

// 配置常量
const APP_CONFIG = {
  qrcodeUrl: '/icons/app-qrcode.png',
  apkUrl: '/app/jzsk-inspection.apk',
  fileName: '荆竹水库巡检APP.apk',
  size: '15MB'
}

// 响应式数据
const qrcodeUrl = ref(APP_CONFIG.qrcodeUrl)
const appSize = ref(APP_CONFIG.size)

/**
 * 处理图片加载失败
 * 显示占位符
 */
const handleImageError = (event) => {
  event.target.src = 'data:image/svg+xml,%3Csvg xmlns="http://www.w3.org/2000/svg" width="200" height="200"%3E%3Crect width="200" height="200" fill="%23f1f5f9"/%3E%3Ctext x="50%25" y="50%25" dominant-baseline="middle" text-anchor="middle" font-family="sans-serif" font-size="14" fill="%2394a3b8"%3E二维码加载中...%3C/text%3E%3C/svg%3E'
}

/**
 * 处理下载
 * 创建隐藏链接触发下载
 */
const handleDownload = () => {
  const link = document.createElement('a')
  link.href = APP_CONFIG.apkUrl
  link.download = APP_CONFIG.fileName
  link.style.display = 'none'
  document.body.appendChild(link)
  link.click()
  document.body.removeChild(link)
}

/**
 * 返回上一页
 */
const handleBack = () => {
  router.back()
}
</script>
