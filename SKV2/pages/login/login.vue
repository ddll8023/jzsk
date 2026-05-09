<template>
  <view class="flex flex-col min-h-screen bg-white">
    <!-- 顶部装饰 -->
    <view class="bg-primary pt-16 pb-24 px-8 rounded-b-3xl">
      <view class="flex items-center justify-center mb-4">
        <image
          src="/static/img/sk.jpg"
          mode="aspectFill"
          class="w-20 h-20 rounded-full"
        />
      </view>
      <text class="text-white text-2xl font-bold text-center block">
        武穴市荆竹水库
      </text>
      <text class="text-white/80 text-sm text-center block mt-2">
        智慧水利管理系统
      </text>
    </view>

    <!-- 登录表单 -->
    <view class="px-8 -mt-12">
      <view class="bg-white rounded-2xl shadow-lg p-8">
        <text class="text-xl font-bold text-gray-900 block mb-6 text-center">
          账号登录
        </text>

        <!-- 用户名 -->
        <view class="mb-5">
          <view
            class="flex items-center border rounded-xl px-4 py-3"
            :class="errors.username ? 'border-error' : 'border-gray-200'"
          >
            <text class="text-gray-400 mr-3 text-lg">
              &#xe60a;
            </text>
            <input
              v-model="form.username"
              type="text"
              placeholder="请输入用户名"
              class="flex-1 text-base"
              @blur="validateField('username')"
            />
          </view>
          <text v-if="errors.username" class="text-error text-xs mt-1 block">
            {{ errors.username }}
          </text>
        </view>

        <!-- 密码 -->
        <view class="mb-8">
          <view
            class="flex items-center border rounded-xl px-4 py-3"
            :class="errors.password ? 'border-error' : 'border-gray-200'"
          >
            <text class="text-gray-400 mr-3 text-lg">
              &#xe634;
            </text>
            <input
              v-model="form.password"
              :password="!showPassword"
              type="text"
              placeholder="请输入密码"
              class="flex-1 text-base"
              @blur="validateField('password')"
            />
            <text class="text-gray-400 text-sm" @click="showPassword = !showPassword">
              {{ showPassword ? '隐藏' : '显示' }}
            </text>
          </view>
          <text v-if="errors.password" class="text-error text-xs mt-1 block">
            {{ errors.password }}
          </text>
        </view>

        <!-- 登录按钮 -->
        <view
          class="rounded-xl py-3.5 text-center"
          :class="loading ? 'bg-primary/60' : 'bg-primary'"
          @click="handleLogin"
        >
          <text v-if="loading" class="text-white text-base">
            登录中...
          </text>
          <text v-else class="text-white text-base font-medium">
            登 录
          </text>
        </view>
      </view>
    </view>

    <!-- 底部 -->
    <view class="flex-1 flex items-end justify-center pb-10">
      <text class="text-gray-400 text-xs">
        智慧水利移动端 v1.0.0
      </text>
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
