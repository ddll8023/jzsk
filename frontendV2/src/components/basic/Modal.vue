<template>
  <Teleport to="body">
    <Transition name="modal">
      <div v-if="modelValue" class="fixed inset-0 z-50 overflow-y-auto" @click.self="handleOverlayClick">
        <!-- 遮罩层 -->
        <div class="fixed inset-0 bg-black bg-opacity-50 transition-opacity"></div>
        
        <!-- 弹窗容器 -->
        <div class="flex min-h-full items-center justify-center p-4">
          <div :class="modalClasses" @click.stop>
            <!-- 头部 -->
            <div v-if="title || $slots.header" class="flex items-center justify-between px-6 py-4 border-b border-gray-200">
              <slot name="header">
                <h3 class="text-lg font-medium text-gray-900">{{ title }}</h3>
              </slot>
              <button v-if="showClose" @click="handleClose" class="text-gray-400 hover:text-gray-500 transition-colors">
                <i class="fa fa-times" aria-hidden="true"></i>
              </button>
            </div>
            
            <!-- 内容 -->
            <div class="px-6 py-4">
              <slot></slot>
            </div>
            
            <!-- 底部 -->
            <div v-if="$slots.footer" class="flex items-center justify-end gap-3 px-6 py-4 border-t border-gray-200">
              <slot name="footer"></slot>
            </div>
          </div>
        </div>
      </div>
    </Transition>
  </Teleport>
</template>

<script setup>
/**
 * Modal 组件
 * 功能：弹窗容器组件
 * 遵循 KISS 原则：简洁实现
 */
import { computed, watch } from 'vue'

const props = defineProps({
  modelValue: {
    type: Boolean,
    default: false
  },
  title: {
    type: String,
    default: ''
  },
  width: {
    type: String,
    default: 'md'
  },
  showClose: {
    type: Boolean,
    default: true
  },
  closeOnOverlay: {
    type: Boolean,
    default: true
  }
})

const emit = defineEmits(['update:modelValue', 'close'])

// 宽度映射
const widthClasses = {
  sm: 'max-w-sm',
  md: 'max-w-lg',
  lg: 'max-w-2xl',
  xl: 'max-w-4xl',
  full: 'max-w-full mx-4'
}

// 计算弹窗样式
const modalClasses = computed(() => [
  'relative w-full bg-white rounded-lg shadow-xl transform transition-all',
  widthClasses[props.width] || widthClasses.md
])

/**
 * 处理关闭
 */
const handleClose = () => {
  emit('update:modelValue', false)
  emit('close')
}

/**
 * 处理遮罩层点击
 */
const handleOverlayClick = () => {
  if (props.closeOnOverlay) {
    handleClose()
  }
}

// 监听弹窗状态，控制 body 滚动
watch(() => props.modelValue, (val) => {
  if (val) {
    document.body.style.overflow = 'hidden'
  } else {
    document.body.style.overflow = ''
  }
})
</script>

<style scoped>
/* 弹窗过渡动画 */
.modal-enter-active,
.modal-leave-active {
  transition: opacity 0.3s ease;
}

.modal-enter-from,
.modal-leave-to {
  opacity: 0;
}

.modal-enter-active .relative,
.modal-leave-active .relative {
  transition: transform 0.3s ease;
}

.modal-enter-from .relative,
.modal-leave-to .relative {
  transform: scale(0.95);
}
</style>
