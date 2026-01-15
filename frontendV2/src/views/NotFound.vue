<template>
  <div class="min-h-screen flex items-center justify-center bg-gray-50 px-4">
    <div class="text-center">
      <!-- 404 图标 -->
      <div class="text-9xl font-bold text-gray-200">404</div>
      
      <!-- 提示信息 -->
      <h1 class="mt-4 text-2xl font-bold text-gray-900">页面未找到</h1>
      <p class="mt-2 text-gray-500">抱歉，您访问的页面不存在</p>
      
      <!-- 操作按钮 -->
      <div class="mt-8 flex items-center justify-center gap-4">
        <Button type="default" icon="arrow-left" @click="goBack">
          返回上页
        </Button>
        <Button type="primary" icon="home" @click="goHome">
          返回首页
        </Button>
      </div>
    </div>
  </div>
</template>

<script setup>
/**
 * 404 页面
 * 功能：页面未找到提示
 * 依赖组件：Button
 * Source: 前端页面规范.md
 */
import { useRouter } from 'vue-router'
import Button from '@/components/basic/Button.vue'

const router = useRouter()

/**
 * 返回上一页
 */
const goBack = () => {
  router.back()
}

/**
 * 返回首页
 */
const goHome = () => {
  router.push('/home/onemap')
}
</script>
