<template>
  <!-- 根容器：overflow-hidden 阻止外层滚动，滚动仅限于子区域 -->
    <div class="flex h-screen bg-gray-100 overflow-hidden">
    <!-- 侧边栏 -->
    <aside :class="sidebarClasses">
      <!-- 菜单组件 (包含Logo) -->
      <SidebarMenu 
        :collapsed="collapsed" 
        class="flex-1"
      />
      
      <!-- 折叠按钮 -->
      <div class="px-3 py-3 border-t border-white/10 bg-[#1a1c20]">
        <button
          @click="collapsed = !collapsed"
          class="w-full flex items-center justify-center py-2 text-slate-400 hover:text-white hover:bg-white/5 rounded-lg transition-all duration-300"
        >
          <i :class="`fa fa-angle-double-${collapsed ? 'right' : 'left'}`" aria-hidden="true"></i>
        </button>
      </div>
    </aside>
    
    <!-- 主内容区 -->
    <div class="flex-1 flex flex-col overflow-hidden">
      <!-- 顶部导航 -->
      <header class="flex items-center justify-between h-16 px-6 bg-white border-b border-gray-200">
        <!-- 面包屑 -->
        <div class="flex items-center text-sm text-gray-500">
          <i class="fa fa-home mr-2" aria-hidden="true"></i>
          <span>首页</span>
          <span v-if="currentTitle" class="mx-2">/</span>
          <span v-if="currentTitle" class="text-gray-900">{{ currentTitle }}</span>
        </div>
        
        <!-- 用户信息下拉菜单 -->
        <div class="relative group">
          <!-- 触发器 -->
          <Button type="text" size="sm" class="gap-2 px-2 py-1 text-gray-600 hover:text-gray-900">
            <span class="text-sm font-medium">{{ userInfo?.username || '用户' }}</span>
            <i class="fa fa-angle-down text-xs transition-transform duration-200 group-hover:rotate-180" aria-hidden="true"></i>
          </Button>

          <!-- 下拉菜单内容 -->
          <div class="absolute right-0 top-full mt-1 w-32 bg-white rounded-md shadow-lg border border-gray-100 py-1 opacity-0 invisible translate-y-2 group-hover:opacity-100 group-hover:visible group-hover:translate-y-0 transition-all duration-200 z-50">
            <button 
              @click="openUserInfoModal"
              class="w-full text-left px-4 py-2 text-sm text-gray-700 hover:bg-gray-50 hover:text-primary-600 transition-colors"
            >
              <i class="fa fa-user-circle-o mr-2" aria-hidden="true"></i>
              个人信息
            </button>
            <div class="h-[1px] bg-gray-100 my-1"></div>
            <button
              @click="handleLogout"
              class="w-full text-left px-4 py-2 text-sm text-red-600 hover:bg-red-50 transition-colors"
            >
              <i class="fa fa-sign-out mr-2" aria-hidden="true"></i>
              退出登录
            </button>
          </div>
        </div>

        <!-- 个人信息弹窗 (内联) -->
        <Modal 
          v-model="showUserInfo" 
          title="个人信息"
        >
          <div v-if="userInfo" class="grid grid-cols-2 gap-x-8 gap-y-6 p-2">
            <!-- 普通字段 -->
            <div 
              v-for="field in userInfoFields" 
              :key="field.key"
              :class="['flex flex-col gap-1', field.fullWidth && 'col-span-2']"
            >
              <span class="text-xs text-gray-500">{{ field.label }}</span>
              <span class="text-sm font-medium text-gray-900 break-words">{{ userInfo[field.key] || '-' }}</span>
            </div>
          </div>
          <div v-else class="text-center py-8 text-gray-500">
            暂无用户信息
          </div>
          <!-- 底部按钮 -->
          <template #footer>
            <div class="flex justify-end">
              <Button @click="showUserInfo = false">关闭</Button>
            </div>
          </template>
        </Modal>
      </header>
      
      <!-- 内容区 -->
      <main ref="mainContent" class="flex-1 overflow-auto p-6">
        <router-view />
      </main>
    </div>
  </div>
</template>

<script setup>
/**
 * 主布局页面
 * 功能：侧边栏、顶部导航、内容区布局
 * 遵循原则：KISS、YAGNI、SOLID
 * Source: frontend/src/components/Home.vue
 */

// 1. Vue 官方 API
import { ref, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'

// 2. Pinia Store
import { useAuthStore } from '@/stores/auth'

// 3. 业务组件
import SidebarMenu from '@/components/business/system/SidebarMenu.vue'
import Modal from '@/components/basic/Modal.vue'
import Button from '@/components/basic/Button.vue'

// 路由实例
const route = useRoute()
const router = useRouter()

// Store实例
const authStore = useAuthStore()

// 侧边栏折叠状态
const collapsed = ref(false)

// 个人信息弹窗状态
const showUserInfo = ref(false)

/**
 * 打开个人信息弹窗
 */
const openUserInfoModal = () => {
  showUserInfo.value = true
}

// 用户信息
const userInfo = computed(() => authStore.userInfo)

// 当前页面标题
const currentTitle = computed(() => route.meta?.title || '')

// 侧边栏样式 - 添加 sticky 定位和高度约束，确保侧边栏固定不随页面滚动
const sidebarClasses = computed(() => [
  'flex flex-col bg-[#2e323a] transition-all duration-300 sticky top-0 h-screen shrink-0',
  collapsed.value ? 'w-16' : 'w-64'
])

// 用户信息字段配置 - 数据驱动渲染，避免重复模板
const userInfoFields = [
  { key: 'username', label: '用户名' },
  { key: 'major', label: '专业' },
  { key: 'name', label: '姓名' },
  { key: 'email', label: '电子邮件' },
  { key: 'idNumber', label: '身份证号' },
  { key: 'gender', label: '性别' },
  { key: 'workingTime', label: '开始工作时间' },
  { key: 'technicalTitle', label: '技术职称' },
  { key: 'birthday', label: '出生年月' },
  { key: 'academicQualifications', label: '学历' },
  { key: 'politicalAppearance', label: '政治面貌' },
  { key: 'graduationInstitution', label: '毕业院校' },
  { key: 'phoneNumber', label: '手机号码', fullWidth: true },
  { key: 'address', label: '家庭住址', fullWidth: true },
  { key: 'note', label: '备注', fullWidth: true }
]

/**
 * 退出登录
 */
const handleLogout = () => {
  authStore.logout()
  router.push('/login')
}

/**
 * 初始化加载用户信息
 * 优化：优先使用缓存，仅在缓存缺失时请求 API
 */
onMounted(async () => {
  // 如果 store 中已有用户信息（从 sessionStorage 恢复），则无需请求
  if (!authStore.userInfo && authStore.token) {
    // 仅在有 token 但无用户信息时请求（降级处理）
    await authStore.fetchUserInfo()
  }
})
</script>
