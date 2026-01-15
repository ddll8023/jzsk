<template>
  <div class="flex items-center gap-3 p-2 bg-white rounded-2xl shadow-sm border border-gray-100 transition-all hover:shadow-md">
    <!-- 搜索输入容器 -->
    <div class="relative w-64">
      <input
        class="input-base"
        :value="modelValue"
        type="text"
        placeholder=" "
        @input="$emit('update:modelValue', $event.target.value)"
        @keyup.enter="$emit('search')"
      />
      <label class="float-label">{{ label }}</label>
    </div>

    <!-- 独立的搜索按钮 -->
    <Button
      type="primary"
      icon="search"
      class="shadow-sm shadow-blue-500/20 !h-[42px]"
      @click="$emit('search')"
    >
      搜索
    </Button>

    <!-- 右侧操作区 -->
    <div class="ml-auto flex items-center gap-3">
      <slot name="extra"></slot>
      <Button
        v-if="showAddBtn"
        type="primary"
        icon="plus"
        class="shadow-md shadow-blue-500/20"
        @click="$emit('add')"
      >
        {{ addBtnText }}
      </Button>
    </div>
  </div>
</template>

<script setup>
/**
 * SearchBar 搜索栏组件
 * 功能：带浮动标签效果的现代化搜索栏
 * 遵循原则：KISS, YAGNI, SOLID
 * Source: 样式参考 Google Material Design Floating Label
 */
import Button from '@/components/basic/Button.vue'

defineProps({
  modelValue: {
    type: String,
    default: ''
  },
  label: {
    type: String,
    default: '关键词'
  },
  // Placeholder 默认为空格以触发 CSS :placeholder-shown 逻辑，不可随意更改以免破坏样式
  placeholder: {
    type: String,
    default: ' '
  },
  showAddBtn: {
    type: Boolean,
    default: true
  },
  addBtnText: {
    type: String,
    default: '新增'
  }
})

defineEmits(['update:modelValue', 'search', 'add'])
</script>

<style scoped>
/**
 * 浮动标签样式组件
 * 采用 Scoped CSS 实现复杂的交互动画，避免污染全局
 * ID 选择器已替换为类选择器，符合复用规范
 */

/* 标签初始状态 */
.float-label {
  transition: all 0.2s ease-in-out;
  color: #6b7280; /* text-gray-500 */
  position: absolute;
  top: 10px; /* 垂直居中调整 */
  left: 14px;
  transform: scale(1);
  opacity: 1;
  pointer-events: none; /* 确保点击穿透到 input */
  background-color: transparent;
  transform-origin: top left;
  z-index: 10;
}

/* 输入框基础样式 */
.input-base {
  transition: all 0.2s ease;
  font-size: 0.95rem;
  outline: none;
  border: 1px solid #e5e7eb; /* border-gray-200 */
  padding: 10px 14px;
  width: 100%;
  border-radius: 10px;
  background-color: #f3f4f6; /* bg-gray-100 */
  color: #1f2937; /* text-gray-800 */
  height: 42px; /* 固定高度以匹配按钮 */
}

/* 输入框聚焦状态 */
.input-base:focus {
  border-color: #3b82f6; /* blue-500 */
  box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.15); /* blue-500/15 */
  background-color: white;
}

/* 
 * 标签浮动逻辑 
 * 触发条件：输入框获得焦点 OR 输入框内容不为空(placeholder 不显示)
 */
.input-base:focus ~ .float-label,
.input-base:not(:placeholder-shown) ~ .float-label {
  top: -8px !important;
  left: 10px !important;
  transform: scale(0.85);
  background: white;
  color: #2563eb; /* blue-600 */
  padding: 0 6px;
  opacity: 1;
  font-weight: 500;
}
</style>
