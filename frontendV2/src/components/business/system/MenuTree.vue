<template>
  <div class="menu-tree">
    <div 
      v-for="menu in menus" 
      :key="menu.id"
      class="menu-item"
    >
      <!-- 菜单项 -->
      <div class="flex items-center py-2 hover:bg-slate-100 rounded transition-colors">
        <!-- 展开/折叠图标 -->
        <button 
          v-if="menu.children && menu.children.length > 0"
          @click="toggleExpand(menu.id)"
          class="w-5 h-5 flex items-center justify-center text-slate-400 hover:text-slate-600 mr-1"
        >
          <i 
            :class="expandedIds.includes(menu.id) ? 'fa fa-angle-down' : 'fa fa-angle-right'"
            aria-hidden="true"
          ></i>
        </button>
        <span v-else class="w-5 mr-1"></span>
        
        <!-- 复选框 -->
        <label class="flex items-center cursor-pointer flex-1">
          <input 
            type="checkbox"
            :checked="isChecked(menu.id)"
            @change="handleCheck(menu.id, $event.target.checked)"
            class="w-4 h-4 text-blue-600 border-gray-300 rounded focus:ring-blue-500 focus:ring-2 cursor-pointer"
          />
          <span class="ml-2 text-sm text-slate-700">{{ menu.name }}</span>
        </label>
      </div>
      
      <!-- 子菜单（递归） -->
      <div 
        v-if="menu.children && menu.children.length > 0 && expandedIds.includes(menu.id)"
        class="ml-6 border-l border-slate-200 pl-2"
      >
        <MenuTree 
          :menus="menu.children" 
          :model-value="modelValue"
          @update:model-value="$emit('update:modelValue', $event)"
        />
      </div>
    </div>
  </div>
</template>

<script setup>
/**
 * 菜单树组件
 * 功能：递归渲染菜单树，支持复选框选择
 * 遵循原则：KISS - 简洁实现，SOLID - 单一职责
 */
import { ref, watch } from 'vue'

const props = defineProps({
  menus: {
    type: Array,
    default: () => []
  },
  modelValue: {
    type: Array,
    default: () => []
  }
})

const emit = defineEmits(['update:modelValue'])

// 展开的菜单ID列表
const expandedIds = ref([])

/**
 * 切换展开/折叠状态
 */
const toggleExpand = (id) => {
  const index = expandedIds.value.indexOf(id)
  if (index > -1) {
    expandedIds.value.splice(index, 1)
  } else {
    expandedIds.value.push(id)
  }
}

/**
 * 判断菜单是否被选中
 */
const isChecked = (id) => {
  return props.modelValue.includes(id)
}

/**
 * 处理复选框变化
 */
const handleCheck = (id, checked) => {
  const newValue = [...props.modelValue]
  
  if (checked) {
    // 选中：添加当前菜单ID
    if (!newValue.includes(id)) {
      newValue.push(id)
    }
    // 递归选中所有子菜单
    const menu = findMenu(props.menus, id)
    if (menu && menu.children) {
      addChildrenIds(menu.children, newValue)
    }
  } else {
    // 取消选中：移除当前菜单ID
    const index = newValue.indexOf(id)
    if (index > -1) {
      newValue.splice(index, 1)
    }
    // 递归取消选中所有子菜单
    const menu = findMenu(props.menus, id)
    if (menu && menu.children) {
      removeChildrenIds(menu.children, newValue)
    }
  }
  
  emit('update:modelValue', newValue)
}

/**
 * 查找菜单
 */
const findMenu = (menus, id) => {
  for (const menu of menus) {
    if (menu.id === id) return menu
    if (menu.children) {
      const found = findMenu(menu.children, id)
      if (found) return found
    }
  }
  return null
}

/**
 * 递归添加子菜单ID
 */
const addChildrenIds = (children, arr) => {
  children.forEach(child => {
    if (!arr.includes(child.id)) {
      arr.push(child.id)
    }
    if (child.children) {
      addChildrenIds(child.children, arr)
    }
  })
}

/**
 * 递归移除子菜单ID
 */
const removeChildrenIds = (children, arr) => {
  children.forEach(child => {
    const index = arr.indexOf(child.id)
    if (index > -1) {
      arr.splice(index, 1)
    }
    if (child.children) {
      removeChildrenIds(child.children, arr)
    }
  })
}

// 初始化时展开所有一级菜单
watch(() => props.menus, (newMenus) => {
  if (newMenus && newMenus.length > 0) {
    expandedIds.value = newMenus.map(m => m.id)
  }
}, { immediate: true })
</script>

<style scoped>
.menu-tree {
  user-select: none;
}

.menu-item {
  margin-bottom: 4px;
}
</style>
