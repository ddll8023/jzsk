<template>
  <view class="min-h-screen bg-gray-50">
    <!-- 加载状态 -->
    <SkLoading v-if="loading" />

    <template v-else-if="userInfo">
      <!-- 顶部用户概要 -->
      <view class="bg-primary px-5 pb-8 pt-2">
        <view class="flex items-center">
          <!-- 默认头像 -->
          <view
            class="w-16 h-16 rounded-full bg-white/20 flex items-center justify-center"
          >
            <view class="w-8 h-8 rounded-full bg-white/40"></view>
            <view
              class="absolute w-10 h-5 rounded-full bg-white/40"
              style="bottom: 8px"
            ></view>
          </view>

          <!-- 用户基本信息 -->
          <view class="ml-4 flex-1 min-w-0">
            <text class="text-white text-xl font-bold block truncate">{{
              displayName
            }}</text>
            <text class="text-white/70 text-sm block mt-1">{{
              userInfo.department || '未设置部门'
            }}</text>
            <text class="text-white/60 text-xs block mt-0.5">{{
              roleNamesText
            }}</text>
          </view>
        </view>
      </view>

      <!-- 基础信息卡片 -->
      <SkCard title="基础信息" class="mx-4 -mt-4" border>
        <view class="space-y-0">
          <view
            class="flex items-center justify-between py-3 border-b border-gray-50"
          >
            <text class="text-sm text-gray-500">姓名</text>
            <text class="text-sm text-gray-900">{{
              userInfo.name || '--'
            }}</text>
          </view>
          <view
            class="flex items-center justify-between py-3 border-b border-gray-50"
          >
            <text class="text-sm text-gray-500">部门</text>
            <text class="text-sm text-gray-900">{{
              userInfo.department || '--'
            }}</text>
          </view>
          <view class="flex items-center justify-between py-3">
            <text class="text-sm text-gray-500">角色权限</text>
            <text class="text-sm text-gray-900">{{ roleNamesText }}</text>
          </view>
        </view>
      </SkCard>

      <!-- 详细信息卡片 -->
      <SkCard title="详细信息" class="mx-4" border>
        <view class="grid grid-cols-2 gap-3">
          <view class="p-3 rounded-lg bg-gray-50">
            <text class="block text-xs text-gray-500 mb-1">职位</text>
            <text class="block text-sm font-medium text-gray-900">{{
              userInfo.position || '--'
            }}</text>
          </view>
          <view class="p-3 rounded-lg bg-gray-50">
            <text class="block text-xs text-gray-500 mb-1">技术职称</text>
            <text class="block text-sm font-medium text-gray-900">{{
              userInfo.technicalTitle || '--'
            }}</text>
          </view>
          <view class="p-3 rounded-lg bg-gray-50">
            <text class="block text-xs text-gray-500 mb-1">联系电话</text>
            <text class="block text-sm font-medium text-gray-900">{{
              userInfo.phoneNumber || '--'
            }}</text>
          </view>
          <view class="p-3 rounded-lg bg-gray-50">
            <text class="block text-xs text-gray-500 mb-1">电子邮箱</text>
            <text class="block text-sm font-medium text-gray-900">{{
              userInfo.email || '--'
            }}</text>
          </view>
          <view class="p-3 rounded-lg bg-gray-50">
            <text class="block text-xs text-gray-500 mb-1">性别</text>
            <text class="block text-sm font-medium text-gray-900">{{
              userInfo.gender || '--'
            }}</text>
          </view>
          <view class="p-3 rounded-lg bg-gray-50">
            <text class="block text-xs text-gray-500 mb-1">出生日期</text>
            <text class="block text-sm font-medium text-gray-900">{{
              userInfo.birthday || '--'
            }}</text>
          </view>
          <view class="p-3 rounded-lg bg-gray-50 col-span-2">
            <text class="block text-xs text-gray-500 mb-1">入职时间</text>
            <text class="block text-sm font-medium text-gray-900">{{
              formatWorkingTime(userInfo.workingTime)
            }}</text>
          </view>
        </view>
      </SkCard>

      <!-- 退出登录 -->
      <view class="mx-4 mt-6 mb-8">
        <view
          class="bg-white rounded-xl py-4 text-center active:bg-gray-50"
          @click="handleLogout"
        >
          <text class="text-base text-error">退出登录</text>
        </view>
      </view>
    </template>
  </view>
</template>

<script setup>
/**
 * 我的页面
 * 功能：当前用户信息展示、退出登录
 */
import { ref, computed } from 'vue'
import { onShow } from '@dcloudio/uni-app'
import { useUserStore } from '@/stores/modules/user.js'
import { formatDate } from '@/utils/format.js'
import SkCard from '@/components/common/SkCard.vue'
import SkLoading from '@/components/common/SkLoading.vue'

const userStore = useUserStore()

const loading = ref(false)

const userInfo = computed(() => userStore.userInfo)

const displayName = computed(
  () => userInfo.value?.displayName || userInfo.value?.name || '--',
)

const roleNamesText = computed(() => {
  const roles = userInfo.value?.roles
  if (!roles || roles.length === 0) return '--'
  return roles.map((r) => r.name).join('、')
})

const formatWorkingTime = (timeStr) => {
  if (!timeStr) return '--'
  return formatDate(timeStr, 'YYYY-MM-DD')
}

onShow(async () => {
  if (!userStore.isLoggedIn) return
  loading.value = true
  try {
    await userStore.fetchCurrentUser()
  } catch (error) {
    console.error('加载用户信息失败:', error)
  } finally {
    loading.value = false
  }
})

const handleLogout = () => {
  uni.showModal({
    title: '提示',
    content: '确定退出登录？',
    success: (res) => {
      if (res.confirm) {
        userStore.logout()
      }
    },
  })
}
</script>
