<template>
  <view class="flex-1">
    <!-- 加载状态 -->
    <SkLoading v-if="loading && data.length === 0" />

    <!-- 空数据 -->
    <SkEmpty v-else-if="!loading && data.length === 0" text="暂无巡检记录" />

    <!-- 列表 -->
    <view v-else class="space-y-3">
      <view
        v-for="item in data"
        :key="item.id"
        class="p-4 bg-white rounded-lg shadow-sm active:scale-[0.98] transition-transform"
        @click="handleItemClick(item)"
      >
        <!-- 头部：站点 + 状态 -->
        <view class="flex items-center justify-between mb-2">
          <text class="text-base font-semibold text-gray-900">{{ item.project || '-' }}</text>
          <view
            class="px-2 py-0.5 rounded text-xs"
            :class="getStatusClass(item.solve)"
          >
            {{ item.solve || '未处理' }}
          </view>
        </view>

        <!-- 类型 + 异常 -->
        <view class="flex items-center gap-4 mb-2">
          <view class="flex items-center">
            <text class="text-sm text-gray-500 mr-1">类型:</text>
            <text class="text-sm text-gray-700">{{ item.type || '-' }}</text>
          </view>
          <view class="flex items-center">
            <text class="text-sm text-gray-500 mr-1">异常:</text>
            <text class="text-sm" :class="item.abnormal === '异常' ? 'text-red-500' : 'text-gray-700'">
              {{ item.abnormal || '正常' }}
            </text>
          </view>
        </view>

        <!-- 巡检情况 -->
        <view v-if="item.situation" class="mb-2">
          <text class="text-sm text-gray-600 line-clamp-2">{{ item.situation }}</text>
        </view>

        <!-- 图片缩略图 -->
        <InspectionImageGrid :image-str="item.image" />

        <!-- 底部：负责人 + 时间 -->
        <view class="flex items-center justify-between pt-2 border-t border-gray-100">
          <text class="text-xs text-gray-500">{{ item.person || '-' }}</text>
          <text class="text-xs text-gray-400">{{ item.date ? formatDate(item.date, 'YYYY-MM-DD') : '-' }}</text>
        </view>

        <!-- 操作按钮 -->
        <view class="flex items-center justify-end gap-2 mt-3">
          <SkButton
            v-if="item.solve !== '已处理'"
            text="处理"
            type="success"
            size="small"
            plain
            @click.stop="handleSolve(item)"
          />
          <SkButton text="编辑" type="primary" size="small" plain @click.stop="handleEdit(item)" />
          <SkButton text="删除" type="error" size="small" plain @click.stop="handleDelete(item)" />
        </view>
      </view>

      <!-- 加载更多提示 -->
      <view v-if="data.length > 0" class="py-4 text-center">
        <text v-if="loading" class="text-sm text-gray-500">加载中...</text>
        <text v-else-if="noMore" class="text-sm text-gray-400">没有更多了</text>
        <text v-else class="text-sm text-primary" @click="emit('load-more')">加载更多</text>
      </view>
    </view>
  </view>
</template>

<script setup>
/**
 * 巡检记录列表组件
 * 功能：卡片式列表展示、加载更多、标记处理、编辑删除
 */
import { computed } from 'vue'
import { formatDate } from '@/utils/format.js'
import SkButton from '@/components/common/SkButton.vue'
import SkEmpty from '@/components/common/SkEmpty.vue'
import InspectionImageGrid from '@/components/business/inspection/InspectionImageGrid.vue'

const props = defineProps({
  data: { type: Array, default: () => [] },
  loading: { type: Boolean, default: false },
  total: { type: Number, default: 0 },
})

const emit = defineEmits(['load-more', 'item-click', 'edit', 'delete', 'solve'])

const noMore = computed(() => props.data.length >= props.total && props.total > 0)

const getStatusClass = (status) => {
  const map = {
    '已处理': 'bg-green-100 text-green-600',
    '处理中': 'bg-yellow-100 text-yellow-600',
    '未处理': 'bg-gray-100 text-gray-600',
  }
  return map[status] || map['未处理']
}

const handleItemClick = (item) => {
  emit('item-click', item)
}

const handleEdit = (item) => {
  emit('edit', item)
}

const handleDelete = (item) => {
  uni.showModal({
    title: '提示',
    content: '确定删除该巡检记录吗？',
    success: (res) => {
      if (res.confirm) emit('delete', item)
    },
  })
}

const handleSolve = (item) => {
  uni.showModal({
    title: '提示',
    content: '确定标记为已处理吗？',
    success: (res) => {
      if (res.confirm) emit('solve', item)
    },
  })
}
</script>

<style scoped>
.line-clamp-2 {
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}
</style>
