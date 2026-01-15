<template>
  <div class="flex items-center justify-center gap-4 px-4 py-3 bg-white rounded-xl border border-gray-100 shadow-sm" :class="bgClass">
    <!-- 总条数 -->
    <span class="text-sm text-gray-500">共 <span class="font-medium text-gray-700">{{ total }}</span> 条</span>
    
    <!-- 每页条数选择 - 自定义下拉组件 -->
    <div v-if="showSizeChanger" class="relative" ref="sizeDropdownRef">
      <!-- 触发器按钮 -->
      <button
        type="button"
        class="flex items-center gap-2 px-3 py-1.5 bg-gray-50 border border-gray-200 rounded-lg text-sm text-gray-600 cursor-pointer transition-all duration-200 hover:border-primary-400 focus:outline-none focus:ring-2 focus:ring-primary-500/20 focus:border-primary-500"
        @click="toggleSizeDropdown"
      >
        <span>{{ pageSize }}条/页</span>
        <i class="fa fa-angle-down text-gray-400 transition-transform duration-200" :class="{ 'rotate-180': sizeDropdownOpen }" aria-hidden="true"></i>
      </button>
      <!-- 下拉面板 - 向上弹出 -->
      <div
        v-show="sizeDropdownOpen"
        class="absolute bottom-full left-0 mb-1 w-full bg-white border border-gray-200 rounded-lg shadow-lg z-10 py-1 overflow-hidden"
      >
        <div
          v-for="size in pageSizes"
          :key="size"
          class="px-3 py-2 text-sm cursor-pointer transition-colors duration-150"
          :class="size === pageSize ? 'bg-primary-50 text-primary-600 font-medium' : 'text-gray-600 hover:bg-gray-50'"
          @click="selectSize(size)"
        >
          {{ size }}条/页
        </div>
      </div>
    </div>
    
    <!-- 分页按钮 -->
    <div class="flex items-center gap-2">
      <button 
        class="w-8 h-8 flex items-center justify-center rounded-lg border border-gray-200 bg-white text-gray-500 transition-all duration-200 hover:bg-primary-50 hover:border-primary-300 hover:text-primary-600 disabled:opacity-40 disabled:cursor-not-allowed disabled:hover:bg-white disabled:hover:border-gray-200 disabled:hover:text-gray-500"
        :disabled="currentPage <= 1"
        @click="changePage(currentPage - 1)"
      >
        <i class="fa fa-angle-left" aria-hidden="true"></i>
      </button>
      
      <!-- 页码显示 -->
      <div class="flex items-center gap-1 px-3 py-1.5 bg-gray-50 rounded-lg">
        <span class="text-sm font-semibold text-primary-600">{{ currentPage }}</span>
        <span class="text-sm text-gray-400">/</span>
        <span class="text-sm text-gray-500">{{ totalPages }}</span>
      </div>
      
      <button 
        class="w-8 h-8 flex items-center justify-center rounded-lg border border-gray-200 bg-white text-gray-500 transition-all duration-200 hover:bg-primary-50 hover:border-primary-300 hover:text-primary-600 disabled:opacity-40 disabled:cursor-not-allowed disabled:hover:bg-white disabled:hover:border-gray-200 disabled:hover:text-gray-500"
        :disabled="currentPage >= totalPages"
        @click="changePage(currentPage + 1)"
      >
        <i class="fa fa-angle-right" aria-hidden="true"></i>
      </button>
    </div>
  </div>
</template>

<script setup>
/**
 * Pagination 分页组件
 * 功能：通用分页控制
 * 遵循原则：KISS - 简洁实现，SOLID - 单一职责
 */
import { computed, ref, onMounted, onUnmounted } from 'vue'

const props = defineProps({
  total: {
    type: Number,
    default: 0
  },
  currentPage: {
    type: Number,
    default: 1
  },
  pageSize: {
    type: Number,
    default: 20
  },
  pageSizes: {
    type: Array,
    default: () => [10, 20, 50]
  },
  showSizeChanger: {
    type: Boolean,
    default: true
  },
  background: {
    type: String,
    default: '' // 可选: 'gray' 或空（默认白色）
  }
})

const emit = defineEmits(['update:currentPage', 'update:pageSize', 'change'])

// 计算总页数
const totalPages = computed(() => Math.ceil(props.total / props.pageSize) || 1)

// 背景样式 - 移除 orange，保留 gray 作为备选
const bgClass = computed(() => {
  return props.background === 'gray' ? 'bg-gray-50' : ''
})

// ==================== 每页条数下拉逻辑 ====================
const sizeDropdownRef = ref(null)
const sizeDropdownOpen = ref(false)

/**
 * 切换下拉框显示状态
 */
const toggleSizeDropdown = () => {
  sizeDropdownOpen.value = !sizeDropdownOpen.value
}

/**
 * 选择每页条数
 */
const selectSize = (size) => {
  sizeDropdownOpen.value = false
  if (size !== props.pageSize) {
    emit('update:pageSize', size)
    emit('update:currentPage', 1)
    emit('change', { page: 1, pageSize: size })
  }
}

/**
 * 点击外部关闭下拉框
 */
const handleClickOutside = (e) => {
  if (sizeDropdownRef.value && !sizeDropdownRef.value.contains(e.target)) {
    sizeDropdownOpen.value = false
  }
}

onMounted(() => {
  document.addEventListener('click', handleClickOutside)
})

onUnmounted(() => {
  document.removeEventListener('click', handleClickOutside)
})

/**
 * 切换页码
 */
const changePage = (page) => {
  if (page >= 1 && page <= totalPages.value) {
    emit('update:currentPage', page)
    emit('change', { page, pageSize: props.pageSize })
  }
}

/**
 * 切换每页条数
 */
const handleSizeChange = (e) => {
  const newSize = Number(e.target.value)
  emit('update:pageSize', newSize)
  emit('update:currentPage', 1) // 重置到第一页
  emit('change', { page: 1, pageSize: newSize })
}
</script>
