<template>
  <div class="h-full flex flex-col">
    <!-- 面包屑 -->
    <nav class="px-4 py-2 text-sm text-gray-500">
      <span>首页</span>
      <i class="fa fa-angle-right mx-2"></i>
      <span>系统管理</span>
      <i class="fa fa-angle-right mx-2"></i>
      <span class="text-gray-900">字典管理</span>
    </nav>

    <!-- 搜索栏 -->
    <SearchBar
      v-model="searchName"
      placeholder="请输入数据项名称"
      @search="handleSearch"
      @add="showAddDialog"
    />

    <!-- 表格区域 - 传统表格轻量优化 -->
    <div class="flex-1 overflow-auto px-4 custom-scrollbar mt-4">
      <table class="w-full border-collapse">
        <!-- 表头：柔和灰色背景 -->
        <thead class="bg-slate-100 sticky top-0">
          <tr>
            <th class="border border-gray-200 px-4 py-2.5 text-center text-sm font-semibold text-gray-700 w-20">序号</th>
            <th class="border border-gray-200 px-4 py-2.5 text-center text-sm font-semibold text-gray-700">数据项名称</th>
            <th class="border border-gray-200 px-4 py-2.5 text-center text-sm font-semibold text-gray-700">描述</th>
            <th class="border border-gray-200 px-4 py-2.5 text-center text-sm font-semibold text-gray-700">标签</th>
            <th class="border border-gray-200 px-4 py-2.5 text-center text-sm font-semibold text-gray-700">值</th>
            <th class="border border-gray-200 px-4 py-2.5 text-center text-sm font-semibold text-gray-700 w-20">排序</th>
            <th class="border border-gray-200 px-4 py-2.5 text-center text-sm font-semibold text-gray-700 w-64">操作</th>
          </tr>
        </thead>
        <tbody>
          <template v-for="(item, index) in dictList" :key="`parent-${item.id}`">
            <!-- 字典主行：淡蓝悬浮高亮 -->
            <tr class="bg-white hover:bg-blue-50 transition-colors">
              <td class="border border-gray-200 px-4 py-2.5 text-center text-gray-600">{{ getRowIndex(index) }}</td>
              <td class="border border-gray-200 px-4 py-2.5">
                <div class="flex items-center gap-2">
                  <button
                    v-if="item.dictDetails?.length"
                    class="w-5 h-5 flex items-center justify-center text-gray-400 hover:text-blue-600 transition-colors"
                    @click="toggleExpand(item.id)"
                  >
                    <i :class="expandedRows.has(item.id) ? 'far fa-minus-square' : 'far fa-plus-square'"></i>
                  </button>
                  <span v-else class="w-5"></span>
                  <span class="font-medium text-gray-800">{{ item.name }}</span>
                </div>
              </td>
              <td class="border border-gray-200 px-4 py-2.5 text-gray-600">{{ item.description }}</td>
              <td class="border border-gray-200 px-4 py-2.5 text-gray-400">-</td>
              <td class="border border-gray-200 px-4 py-2.5 text-gray-400">-</td>
              <td class="border border-gray-200 px-4 py-2.5 text-center text-gray-400">-</td>
              <td class="border border-gray-200 px-4 py-2.5 text-center">
                <div class="flex items-center justify-center gap-2">
                  <Button size="sm" @click="showEditDialog(item)">编辑</Button>
                  <Button type="primary" size="sm" @click="showAddDetailDialog(item.id)">新增</Button>
                  <Button type="danger" size="sm" @click="handleDelete(item.id)">删除</Button>
                </div>
              </td>
            </tr>
            <!-- 字典详情子行：左侧蓝色指示线 -->
            <template v-if="expandedRows.has(item.id)">
              <tr v-for="detail in item.dictDetails" :key="`child-${detail.id}`" class="bg-gray-50 hover:bg-gray-100 transition-colors">
                <td class="border border-gray-200 px-4 py-2 text-center"></td>
                <td class="border border-gray-200 px-4 py-2 border-l-2 border-l-blue-400 pl-10 text-gray-500">
                  <i class="fa fa-level-up fa-rotate-90 text-xs text-gray-300 mr-2"></i>
                </td>
                <td class="border border-gray-200 px-4 py-2"></td>
                <td class="border border-gray-200 px-4 py-2 text-gray-700">{{ detail.label }}</td>
                <td class="border border-gray-200 px-4 py-2 text-gray-700">{{ detail.value }}</td>
                <td class="border border-gray-200 px-4 py-2 text-center text-gray-600">{{ detail.dictSort }}</td>
                <td class="border border-gray-200 px-4 py-2 text-center">
                  <div class="flex items-center justify-center gap-2">
                    <Button size="sm" @click="showEditDetailDialog(detail)">编辑</Button>
                    <Button type="danger" size="sm" @click="handleDeleteDetail(detail.id)">删除</Button>
                  </div>
                </td>
              </tr>
            </template>
          </template>
          <!-- 空数据提示 -->
          <tr v-if="!loading && dictList.length === 0">
            <td colspan="7" class="border border-gray-200 px-4 py-8 text-center text-gray-400">暂无数据</td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- 分页 -->
    <Pagination
      v-model:currentPage="currentPage"
      v-model:pageSize="pageSize"
      :total="total"
      @change="handlePageChange"
    />

    <!-- 新增/编辑字典弹窗 -->
    <Modal v-model="dictDialogVisible" :title="isEditDict ? '编辑字典' : '新增字典'">
      <div class="space-y-4">
        <Input 
          v-model="dictForm.name" 
          label="字典名称" 
          required 
          placeholder="请输入字典名称"
        />
        <Input 
          v-model="dictForm.description" 
          label="描述" 
          placeholder="请输入描述"
        />
      </div>
      <template #footer>
        <Button type="primary" @click="submitDict">确 定</Button>
        <Button @click="dictDialogVisible = false">取 消</Button>
      </template>
    </Modal>

    <!-- 新增/编辑字典详情弹窗 -->
    <Modal v-model="detailDialogVisible" :title="isEditDetail ? '编辑字典详情' : '新增字典详情'">
      <div class="space-y-4">
        <Input 
          v-model="detailForm.label" 
          label="字典标签" 
          required 
          placeholder="请输入字典标签"
        />
        <Input 
          v-model="detailForm.value" 
          label="字典值" 
          required 
          placeholder="请输入字典值"
        />
        <Input 
          v-model="detailForm.dictSort" 
          type="number" 
          label="排序" 
          required 
          placeholder="请输入排序"
        />
      </div>
      <template #footer>
        <Button type="primary" @click="submitDetail">确 定</Button>
        <Button @click="detailDialogVisible = false">取 消</Button>
      </template>
    </Modal>
  </div>
</template>

<script setup>
/**
 * 字典管理页面
 * 功能：字典及字典详情的CRUD管理
 * 遵循原则：KISS - 简洁实现，SOLID - 职责分离
 */
import { ref, onMounted } from 'vue'
import { getDictList, saveDict, updateDict, deleteDict, saveDictDetail, updateDictDetail, deleteDictDetail } from '@/api/dict'
import { useDictStore } from '@/stores/dict'
// 基础组件
import SearchBar from '@/components/basic/SearchBar.vue'
import Button from '@/components/basic/Button.vue'
import Modal from '@/components/basic/Modal.vue'
import Input from '@/components/basic/Input.vue'
import Pagination from '@/components/basic/Pagination.vue'

// ==================== 列表状态 ====================
const dictList = ref([])
const loading = ref(false)
const total = ref(0)
const currentPage = ref(1)
const pageSize = ref(20)
const searchName = ref('')
const expandedRows = ref(new Set())

// ==================== 弹窗状态 ====================
const dictDialogVisible = ref(false)
const detailDialogVisible = ref(false)
const isEditDict = ref(false)
const isEditDetail = ref(false)
const dictForm = ref({ name: '', description: '' })
const detailForm = ref({ dictId: null, label: '', value: '', dictSort: 0 })

// ==================== Store ====================
const dictStore = useDictStore()

// ==================== 方法 ====================

/**
 * 获取行序号
 */
const getRowIndex = (index) => {
  return (currentPage.value - 1) * pageSize.value + index + 1
}

/**
 * 切换展开/折叠
 */
const toggleExpand = (id) => {
  if (expandedRows.value.has(id)) {
    expandedRows.value.delete(id)
  } else {
    expandedRows.value.add(id)
  }
}

/**
 * 查询字典列表
 */
const handleSearch = async () => {
  loading.value = true
  try {
    const res = await getDictList({
      blurry: searchName.value,
      currentPage: currentPage.value,
      pageSize: pageSize.value
    })
    if (res.data?.code === 200) {
      dictList.value = res.data.data?.content || []
      total.value = res.data.data?.totalElements || 0
    }
  } catch (error) {
    console.error('查询字典列表失败:', error)
  } finally {
    loading.value = false
  }
}

/**
 * 分页变化处理
 */
const handlePageChange = ({ page, pageSize: newSize }) => {
  currentPage.value = page
  pageSize.value = newSize
  handleSearch()
}

/**
 * 显示新增字典弹窗
 */
const showAddDialog = () => {
  isEditDict.value = false
  dictForm.value = { name: '', description: '' }
  dictDialogVisible.value = true
}

/**
 * 显示编辑字典弹窗
 * 直接使用列表数据，避免API请求延迟
 */
const showEditDialog = (item) => {
  isEditDict.value = true
  dictForm.value = { id: item.id, name: item.name, description: item.description }
  dictDialogVisible.value = true
}

/**
 * 提交字典表单
 */
const submitDict = async () => {
  if (!dictForm.value.name?.trim()) {
    alert('请输入字典名称')
    return
  }
  try {
    const fn = isEditDict.value ? updateDict : saveDict
    const res = await fn(dictForm.value)
    if (res.data?.code === 200) {
      dictDialogVisible.value = false
      handleSearch()
      // 清除相关缓存
      dictStore.clearCache(dictForm.value.name)
    } else {
      alert(res.data?.message || '操作失败')
    }
  } catch (error) {
    console.error('保存字典失败:', error)
  }
}

/**
 * 删除字典
 */
const handleDelete = async (id) => {
  if (!confirm('此操作将永久删除该数据, 是否继续?')) return
  try {
    const res = await deleteDict(id)
    if (res.data?.code === 200) {
      handleSearch()
    } else {
      alert(res.data?.message || '删除失败')
    }
  } catch (error) {
    console.error('删除字典失败:', error)
  }
}

/**
 * 显示新增字典详情弹窗
 */
const showAddDetailDialog = (dictId) => {
  isEditDetail.value = false
  detailForm.value = { dictId, label: '', value: '', dictSort: 0 }
  detailDialogVisible.value = true
}

/**
 * 显示编辑字典详情弹窗
 * 直接使用列表数据，避免API请求延迟
 */
const showEditDetailDialog = (item) => {
  isEditDetail.value = true
  detailForm.value = { id: item.id, label: item.label, value: item.value, dictSort: item.dictSort }
  detailDialogVisible.value = true
}

/**
 * 提交字典详情表单
 */
const submitDetail = async () => {
  if (!detailForm.value.label?.trim()) {
    alert('请输入字典标签')
    return
  }
  if (!detailForm.value.value?.trim()) {
    alert('请输入字典值')
    return
  }
  try {
    const fn = isEditDetail.value ? updateDictDetail : saveDictDetail
    const res = await fn(detailForm.value)
    if (res.data?.code === 200) {
      detailDialogVisible.value = false
      handleSearch()
    } else {
      alert(res.data?.message || '操作失败')
    }
  } catch (error) {
    console.error('保存字典详情失败:', error)
  }
}

/**
 * 删除字典详情
 */
const handleDeleteDetail = async (id) => {
  if (!confirm('此操作将永久删除该数据, 是否继续?')) return
  try {
    const res = await deleteDictDetail(id)
    if (res.data?.code === 200) {
      handleSearch()
    } else {
      alert(res.data?.message || '删除失败')
    }
  } catch (error) {
    console.error('删除字典详情失败:', error)
  }
}

// ==================== 生命周期 ====================
onMounted(() => {
  handleSearch()
})
</script>


<style scoped>
/* 滚动条样式 - 统一风格 */
.custom-scrollbar {
  scrollbar-width: thin;
  scrollbar-color: rgba(0, 0, 0, 0.2) transparent;
}

.custom-scrollbar::-webkit-scrollbar {
  width: 4px;
  height: 4px;
}

.custom-scrollbar::-webkit-scrollbar-track {
  background: transparent;
}

.custom-scrollbar::-webkit-scrollbar-thumb {
  background-color: rgba(0, 0, 0, 0.2);
  border-radius: 4px;
}

.custom-scrollbar::-webkit-scrollbar-thumb:hover {
  background-color: rgba(0, 0, 0, 0.35);
}
</style>
