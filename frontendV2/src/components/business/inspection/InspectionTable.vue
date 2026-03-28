<template>
  <Table
    :columns="columns"
    :data="data"
    :loading="loading"
    :total="total"
    :current-page="currentPage"
    :page-size="pageSize"
    :show-pagination="true"
    @page-change="$emit('page-change', $event)"
    @update:page-size="$emit('update:page-size', $event)"
  >
    <!-- 巡检情况列 -->
    <template #situation="{ row }">
      <span :title="row.situation" class="line-clamp-2">
        {{ row.situation || '-' }}
      </span>
    </template>

    <!-- 图片列 -->
    <template #image="{ row }">
      <div v-if="row.image" class="flex items-center gap-1">
        <img
          v-for="(img, index) in getImageList(row.image).slice(0, 3)"
          :key="index"
          :src="getImageUrl(img)"
          class="w-12 h-12 object-cover rounded cursor-pointer hover:opacity-80 transition-opacity"
          @click="$emit('preview-image', row.image, index)"
        />
        <span v-if="getImageList(row.image).length > 3" class="text-xs text-gray-500">
          +{{ getImageList(row.image).length - 3 }}
        </span>
      </div>
      <span v-else class="text-gray-400">-</span>
    </template>

    <!-- 操作列 -->
    <template #actions="{ row }">
      <div class="flex items-center justify-center gap-2">
        <Button size="sm" type="primary" @click="$emit('view', row)">
          查看
        </Button>
        <Button size="sm" type="default" @click="$emit('edit', row)">
          编辑
        </Button>
        <Button size="sm" type="danger" @click="$emit('delete', row)">
          删除
        </Button>
      </div>
    </template>
  </Table>
</template>

<script setup>
/**
 * 巡检记录表格
 * 功能：数据展示、图片预览、操作按钮
 * 遵循原则：KISS, YAGNI, SOLID
 */
import { computed } from 'vue'
import Table from '@/components/basic/Table.vue'
import Button from '@/components/basic/Button.vue'

const props = defineProps({
  data: { type: Array, default: () => [] },
  loading: { type: Boolean, default: false },
  total: { type: Number, default: 0 },
  currentPage: { type: Number, default: 1 },
  pageSize: { type: Number, default: 10 }
})

defineEmits(['page-change', 'update:page-size', 'view', 'edit', 'delete', 'preview-image'])

// 表格列定义
const columns = computed(() => [
  { key: 'index', title: '序号', width: '80px' },
  { key: 'project', title: '巡检站点', width: '120px' },
  { key: 'type', title: '巡检类型', width: '100px' },
  { key: 'abnormal', title: '异常情况', width: '120px' },
  { key: 'situation', title: '巡检情况', width: '200px' },
  { key: 'solve', title: '处理状态', width: '100px' },
  { key: 'image', title: '图片', width: '180px' },
  { key: 'person', title: '负责人', width: '100px' },
  { key: 'date', title: '日期', width: '180px' },
  { key: 'actions', title: '操作', width: '220px' }
])

/**
 * 获取图片列表
 * @param {String} imageStr - 图片字符串（分号分隔）
 */
const getImageList = (imageStr) => {
  if (!imageStr) return []
  return imageStr.split(';').filter(img => img.trim())
}

/**
 * 获取图片URL
 * @param {String} img - 图片路径
 */
const getImageUrl = (img) => {
  // 如果已经是完整URL，直接返回
  if (img && (img.startsWith('http://') || img.startsWith('https://'))) {
    return img
  }
  // 使用环境变量配置的图片基础路径
  const baseUrl = import.meta.env.VITE_PHOTO_BASE_URL || 'http://localhost:8081/photo/'
  return `${baseUrl}${img}`
}
</script>
