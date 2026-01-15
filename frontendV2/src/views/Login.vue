<template>
  <!-- 登录页面：左右分栏布局 -->
  <main class="login-page min-h-screen flex">
    <!-- 左侧：背景展示区 -->
    <section class="hidden lg:flex flex-1 relative bg-cover bg-center login-bg-left">
      <!-- 底部白色渐变遮罩 -->
      <div class="absolute inset-0 bg-gradient-to-t from-white via-transparent to-transparent"></div>
      <!-- 标题内容（垂直居中） -->
      <div class="relative z-10 flex flex-col items-center justify-center w-full px-12">
        <h1 class="text-5xl font-bold text-white tracking-wider mb-4">
          智慧荆竹水库管理平台
        </h1>
        <p class="text-xl text-white/70">
          数字化 · 智能化 · 精细化
        </p>
      </div>
    </section>

    <!-- 右侧：登录框区域 -->
    <section class="w-full lg:w-[420px] flex items-center justify-center px-8 py-12 bg-[#E6F0F5]">
      <div class="w-full max-w-sm">
        <!-- 移动端标题（仅在小屏显示） -->
        <h1 class="lg:hidden text-[#1E82BE] text-center text-2xl font-semibold mb-10">
          智慧荆竹水库管理平台
        </h1>

        <!-- 登录标题 -->
        <h2 class="text-[#1E82BE] text-center text-2xl font-medium mb-10">用户登录</h2>
        
        <!-- 登录表单：简洁扁平风格 -->
        <!-- 登录表单：使用基础组件构建 -->
        <form @submit.prevent="handleLogin" class="space-y-8" aria-label="用户登录表单">
          <!-- 用户名 -->
          <Input
            v-model="form.username"
            variant="underline"
            prefix-icon="user"
            placeholder="请输入用户名"
            size="lg"
            :error="errors.username"
            :aria-invalid="!!errors.username"
            :aria-describedby="errors.username ? ERROR_ID.username : undefined"
            input-class="text-slate-800 placeholder-slate-400"
          />
          
          <!-- 密码 -->
          <Input
            v-model="form.password"
            type="password"
            variant="underline"
            prefix-icon="lock"
            placeholder="请输入密码"
            size="lg"
            :error="errors.password"
            :aria-invalid="!!errors.password"
            :aria-describedby="errors.password ? ERROR_ID.password : undefined"
            input-class="text-slate-800 placeholder-slate-400"
          />
          
          <!-- 错误提示 -->
          <div 
            v-if="errors.submit" 
            :id="ERROR_ID.submit"
            role="alert"
            aria-live="polite"
            class="p-3 bg-red-100 border border-red-300 rounded-lg"
          >
            <p class="text-sm text-red-600">{{ errors.submit }}</p>
          </div>
          
          <!-- 按钮区域 -->
          <div class="flex gap-4 pt-4">
            <Button
              type="primary"
              size="lg"
              :loading="loading"
              :aria-busy="loading"
              block
              class="flex-1 bg-[#1E82BE] hover:bg-[#1a6fa3] border-transparent"
              @click="handleLogin"
            >
              登 录
            </Button>
            <Button
              type="default"
              size="lg"
              block
              class="flex-1 bg-[#82C8F0] hover:bg-[#6bb8e6] text-slate-800 border-transparent"
              @click="resetForm"
            >
              重 置
            </Button>
          </div>
        </form>
      </div>
    </section>
  </main>
</template>

<script setup>
/**
 * 登录页面
 * 功能：用户登录认证
 * 布局：左侧背景展示区 + 右侧登录框
 * 依赖组件：Button, Input
 * Source: 前端页面规范.md
 */
// 1. Vue 官方 API
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'

// 2. Pinia Store
import { useAuthStore } from '@/stores/auth'

// 3. API 接口
import { login as loginApi } from '@/api/auth'

// 4. 基础组件
import Button from '@/components/basic/Button.vue'
import Input from '@/components/basic/Input.vue'

// 错误提示 ID（用于可访问性关联）
const ERROR_ID = {
  username: 'error-username',
  password: 'error-password',
  submit: 'error-submit'
}

const router = useRouter()
const authStore = useAuthStore()

// 表单数据
const form = reactive({
  username: '',
  password: ''
})

// 错误信息
const errors = reactive({
  username: '',
  password: '',
  submit: ''
})

// 加载状态
const loading = ref(false)

/**
 * 表单验证
 */
const validateForm = () => {
  let valid = true
  errors.username = ''
  errors.password = ''
  errors.submit = ''
  
  if (!form.username.trim()) {
    errors.username = '请输入用户名'
    valid = false
  }
  
  if (!form.password.trim()) {
    errors.password = '请输入密码'
    valid = false
  }
  
  return valid
}

/**
 * 重置表单
 */
const resetForm = () => {
  form.username = ''
  form.password = ''
  errors.username = ''
  errors.password = ''
  errors.submit = ''
}

/**
 * 处理登录
 */
const handleLogin = async () => {
  if (!validateForm()) return
  
  loading.value = true
  errors.submit = ''
  
  try {
    // 调用登录API
    const res = await loginApi({
      username: form.username,
      password: form.password
    })
    
    if (res.data && res.data.code === 200) {
      // 保存 token
      const token = res.data.data?.token || res.data.token || res.headers?.authorization
      if (token) {
        authStore.setToken(token)
      }
      
      // 保存用户信息
      if (res.data.data?.user) {
        authStore.setUserInfo(res.data.data.user)
      }
      
      // 跳转首页
      router.push('/home/onemap')
    } else {
      errors.submit = res.data?.message || '登录失败，请检查用户名和密码'
    }
  } catch (error) {
    console.error('登录失败:', error)
    errors.submit = error.response?.data?.message || error.message || '登录失败，请稍后重试'
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
/**
 * 登录页面样式
 * 左侧背景图使用 scoped CSS（Tailwind 不支持动态背景图 URL）
 */
.login-bg-left {
  background-image: url('@/assets/img/登录左侧图片.png');
}
</style>
