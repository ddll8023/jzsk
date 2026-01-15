<template>
  <nav class="flex flex-col h-full bg-gradient-to-b from-[#2e323a] to-[#1a1c20] select-none text-slate-300">
    <!-- Logo 区域 -->
    <div class="flex items-center h-16 px-4 bg-white/5 border-b border-white/5 mb-4 shrink-0 transition-all duration-300 overflow-hidden">
      <img src="@/assets/img/sea2.jpg" class="w-8 h-8 rounded-lg shadow-lg ring-1 ring-white/10" alt="Logo" />
      <span v-if="!collapsed" class="ml-3 text-lg font-bold tracking-wide text-transparent bg-clip-text bg-gradient-to-r from-white to-slate-400 truncate">智慧荆竹水库</span>
    </div>

    <div class="flex-1 overflow-y-auto custom-scrollbar">
      <template v-for="menu in menuList" :key="menu.id || menu.path">
        <div class="px-2">
          <!-- 包含子菜单的菜单项 -->
          <div v-if="menu.children && menu.children.length > 0">
            <div
              class="group flex items-center justify-between px-3 py-2.5 rounded-xl cursor-pointer transition-all duration-300"
              :class="[
                isChildActive(menu.children) 
                  ? 'text-white bg-slate-700/50 shadow-inner ring-1 ring-white/5' 
                  : 'text-slate-300 hover:bg-white/5 hover:text-white'
              ]"
              @click="toggleExpand(menu.id || menu.path)"
            >
              <div class="flex items-center min-w-0">
                <div class="relative flex items-center justify-center w-6 h-6">
                  <i :class="[menu.icon, 'text-lg transition-transform duration-300 group-hover:scale-110']" aria-hidden="true"></i>
                  <!-- 选中指示点 -->
                  <span v-if="isChildActive(menu.children)" class="absolute -left-2 top-1.5 bottom-1.5 w-1 bg-slate-400 rounded-r shadow-[0_0_8px_rgba(148,163,184,0.4)]"></span>
                </div>
                <span v-if="!collapsed" class="ml-3 text-sm font-medium truncate">{{ menu.name }}</span>
              </div>
              <i 
                v-if="!collapsed" 
                class="fa fa-chevron-right text-xs transition-transform duration-300 text-slate-500"
                :class="{ 'rotate-90': isExpanded(menu) }"
                aria-hidden="true"
              ></i>
            </div>
            
            <!-- 子菜单列表 -->
            <div 
              v-show="!collapsed && isExpanded(menu)"
              class="mt-1 ml-1 pl-3 border-l border-white/10 space-y-1 overflow-hidden transition-all duration-300"
            >
              <router-link
                v-for="child in menu.children"
                :key="child.path"
                :to="formatPath(child.path)"
                class="flex items-center px-3 py-2 rounded-lg text-sm transition-all duration-200"
                :class="[
                  isActive(child.path)
                    ? 'bg-slate-700 text-white shadow-md shadow-black/20 font-medium'
                    : 'text-slate-400 hover:text-slate-200 hover:bg-white/5'
                ]"
              >
                <span>{{ child.name }}</span>
              </router-link>
            </div>
          </div>

          <!-- 无子菜单的独立菜单项 -->
          <router-link
            v-else
            :to="formatPath(menu.path)"
            class="group flex items-center px-3 py-2.5 rounded-xl transition-all duration-300"
            :class="[
              isActive(menu.path)
                ? 'bg-[#374151] text-white shadow-lg shadow-black/20 border-l-2 border-slate-400 relative overflow-hidden group-hover:shadow-xl'
                : 'text-slate-300 hover:bg-white/5 hover:text-white'
            ]"
          >
            <div class="relative flex items-center justify-center w-6 h-6">
              <i :class="[menu.icon, 'text-lg transition-transform duration-300 group-hover:scale-110']" aria-hidden="true"></i>
            </div>
            <span v-if="!collapsed" class="ml-3 text-sm font-medium">{{ menu.name }}</span>
          </router-link>
        </div>
      </template>
    </div>
  </nav>
</template>

<script setup>
/**
 * 侧边栏菜单组件
 * 功能：静态菜单渲染，采用现代玻璃态设计风格
 * 遵循原则：KISS (配置驱动), YAGNI (移除动态菜单逻辑)
 */
import { ref } from 'vue'
import { useRoute } from 'vue-router'
import { staticMenuData } from '@/data/menuData'

defineProps({
  collapsed: { type: Boolean, default: false }
})

const route = useRoute()

// 菜单数据
const menuList = staticMenuData

// 展开状态
const expandedKeys = ref([])

/**
 * 判断是否展开
 */
const isExpanded = (menu) => {
  const key = menu.id || menu.path
  return expandedKeys.value.includes(key)
}

/**
 * 切换展开/折叠
 */
const toggleExpand = (key) => {
  const index = expandedKeys.value.indexOf(key)
  if (index > -1) {
    expandedKeys.value.splice(index, 1)
  } else {
    expandedKeys.value.push(key)
  }
}

/**
 * 判断路由激活状态
 */
const isActive = (path) => {
  if (!path) return false
  return route.path === path || route.path.startsWith(path + '/')
}

/**
 * 判断子菜单组是否有激活项
 */
const isChildActive = (children) => {
  if (!children) return false
  return children.some(child => isActive(child.path) || (child.path && route.path.startsWith(child.path)))
}

/**
 * 格式化路径
 */
const formatPath = (path) => {
  if (!path) return '/home'
  if (path.startsWith('/')) return path
  return '/home/' + path
}
</script>

<style scoped>
/* 滚动条样式 */
.custom-scrollbar {
  scrollbar-width: thin;
  scrollbar-color: rgba(255, 255, 255, 0.2) transparent;
}

.custom-scrollbar::-webkit-scrollbar {
  width: 4px;
  height: 4px;
}

.custom-scrollbar::-webkit-scrollbar-track {
  background: transparent;
}

.custom-scrollbar::-webkit-scrollbar-thumb {
  background-color: rgba(255, 255, 255, 0.2);
  border-radius: 4px;
}

.custom-scrollbar::-webkit-scrollbar-thumb:hover {
  background-color: rgba(255, 255, 255, 0.35);
}
</style>
