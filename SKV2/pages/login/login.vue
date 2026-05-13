<template>
  <view class="flex flex-col min-h-screen bg-white">
    <!-- 主内容区 -->
    <view class="flex-1 flex flex-col items-center justify-center px-10">
      <!-- 水库图片 -->
      <view class="w-20 h-20 rounded-full overflow-hidden mb-5 border-2 border-primary/20">
        <image src="/static/img/sk.jpg" mode="aspectFill" class="w-full h-full" />
      </view>

      <!-- 系统名称 -->
      <text class="text-xl font-bold text-gray-900 tracking-wide">武穴市荆竹水库</text>
      <text class="text-sm text-gray-400 mt-1.5 tracking-widest">智慧水利管理系统</text>

      <!-- 表单卡片 -->
      <view class="w-full mt-10 border border-gray-100 rounded-2xl p-6">
        <!-- 用户名 -->
        <view class="mb-4">
          <view
            class="flex items-center bg-gray-50 rounded-lg px-4 py-3"
            :class="errors.username ? 'border border-error' : 'border border-transparent'"
          >
            <image src="/static/icons/user.svg" mode="aspectFit" class="w-5 h-5 mr-3 flex-shrink-0" />
            <input
              v-model="form.username"
              type="text"
              placeholder="请输入用户名"
              placeholder-class="text-gray-300"
              class="flex-1 text-sm text-gray-900"
              @blur="validateField('username')"
            />
          </view>
          <text v-if="errors.username" class="text-error text-xs mt-1.5 block px-1">
            {{ errors.username }}
          </text>
        </view>

        <!-- 密码 -->
        <view class="mb-6">
          <view
            class="flex items-center bg-gray-50 rounded-lg px-4 py-3"
            :class="errors.password ? 'border border-error' : 'border border-transparent'"
          >
            <image src="/static/icons/lock.svg" mode="aspectFit" class="w-5 h-5 mr-3 flex-shrink-0" />
            <input
              v-model="form.password"
              :password="!showPassword"
              type="text"
              placeholder="请输入密码"
              placeholder-class="text-gray-300"
              class="flex-1 text-sm text-gray-900"
              @blur="validateField('password')"
            />
            <text
              class="text-xs ml-2 flex-shrink-0"
              :class="showPassword ? 'text-primary' : 'text-gray-400'"
              @click="showPassword = !showPassword"
            >
              {{ showPassword ? '隐藏' : '显示' }}
            </text>
          </view>
          <text v-if="errors.password" class="text-error text-xs mt-1.5 block px-1">
            {{ errors.password }}
          </text>
        </view>

        <!-- 登录按钮 -->
        <view
          class="w-full py-3.5 rounded-lg text-center"
          :class="loading ? 'bg-primary/50' : 'bg-primary active:bg-primary/80'"
          @click="handleLogin"
        >
          <text class="text-white text-sm font-medium tracking-wider">
            {{ loading ? '登录中...' : '登 录' }}
          </text>
        </view>
      </view>
    </view>

    <!-- 底部版本号 -->
    <view class="flex justify-center pb-10">
      <text class="text-xs text-gray-300 tracking-wide">智慧水利移动端 v1.0.0</text>
    </view>
  </view>
</template>

<script setup>
/**
 * 登录页
 * 功能：账号密码登录、表单校验、Token 持久化、登录后跳转
 */
import { ref, reactive } from 'vue'
import { useUserStore } from '@/stores/modules/user.js'

const userStore = useUserStore()

const form = reactive({
  username: '',
  password: '',
})

const errors = reactive({
  username: '',
  password: '',
})

const loading = ref(false)
const showPassword = ref(false)

function validateField(field) {
  if (field === 'username') {
    errors.username = form.username.trim() ? '' : '请输入用户名'
  }
  if (field === 'password') {
    errors.password = form.password.trim() ? '' : '请输入密码'
  }
}

function validateForm() {
  validateField('username')
  validateField('password')
  return !errors.username && !errors.password
}

async function handleLogin() {
  if (!validateForm()) return
  if (loading.value) return

  loading.value = true
  try {
    await userStore.login({
      username: form.username.trim(),
      password: form.password,
    })

    const redirect = userStore.redirectPath || '/pages/tabbar/index/index'
    userStore.clearRedirectPath()

    if (redirect.includes('/tabbar/')) {
      uni.switchTab({ url: redirect })
    } else {
      uni.reLaunch({ url: redirect })
    }
  } catch (err) {
    // request.js 已统一提示
  } finally {
    loading.value = false
  }
}
</script>
