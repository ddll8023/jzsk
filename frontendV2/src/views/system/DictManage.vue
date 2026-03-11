<template>
  <div class="min-h-full bg-slate-50 flex flex-col">
    <!-- 面包屑 -->
    <nav class="px-6 py-3 text-sm text-slate-500">
      <span>首页</span>
      <i class="fa fa-angle-right mx-2"></i>
      <span>系统管理</span>
      <i class="fa fa-angle-right mx-2"></i>
      <span class="text-slate-900 font-medium">字典管理</span>
    </nav>

    <!-- 工具栏 -->
    <div class="px-6">
      <div class="flex items-center justify-between p-4 bg-white rounded-lg shadow-sm border border-slate-200">
        <div class="flex items-center gap-4">
          <Button type="primary" @click="showAddDialog">
            <i class="fa fa-plus mr-2"></i>新增字典
          </Button>
          <!-- 搜索框 -->
          <div class="flex items-center gap-2">
            <input 
              v-model="searchName"
              type="text"
              placeholder="请输入字典名称"
              class="px-3 py-2 border border-slate-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-all w-48"
              @keyup.enter="handleSearch"
            />
            <Button @click="handleSearch">
              <i class="fa fa-search mr-1"></i>搜索
            </Button>
          </div>
        </div>
        <Button @click="handleSearch">
          <i class="fa fa-refresh mr-2"></i>刷新
        </Button>
      </div>
    </div>

    <!-- 表格区域 -->
    <div class="flex-1 overflow-auto px-6 py-4 custom-scrollbar">
      <div class="bg-white rounded-lg shadow-sm border border-slate-200 overflow-hidden">
        <table class="w-full border-collapse">
          <thead class="bg-slate-50/80 sticky top-0">
            <tr>
              <th class="px-4 py-3 text-left text-xs text-slate-500 uppercase tracking-wider border-b border-slate-200">字典名称</th>
              <th class="px-4 py-3 text-center text-xs text-slate-500 uppercase tracking-wider border-b border-slate-200 w-48">描述</th>
              <th class="px-4 py-3 text-center text-xs text-slate-500 uppercase tracking-wider border-b border-slate-200 w-24">详情数</th>
              <th class="px-4 py-3 text-center text-xs text-slate-500 uppercase tracking-wider border-b border-slate-200 w-64">操作</th>
            </tr>
          </thead>
          <tbody>
            <!-- 加载状态 -->
            <tr v-if="loading">
              <td colspan="4" class="px-4 py-8 text-center">
                <i class="fa fa-spinner fa-spin text-primary-600 text-xl" aria-hidden="true"></i>
                <p class="mt-2 text-slate-500">加载中...</p>
              </td>
            </tr>
            
            <!-- 数据行 -->
            <tr 
              v-else
              v-for="row in displayDictList" 
              :key="row.rowKey" 
              class="hover:bg-slate-50 transition-colors duration-150"
            >
              <!-- 字典/详情名称 -->
              <td class="px-4 py-3 border-b border-slate-100">
                <div class="flex items-center" :style="{ paddingLeft: row.level * 20 + 'px' }">
                  <!-- 展开/折叠按钮（仅字典行且有详情时显示） -->
                  <button
                    v-if="row.isDict && row.hasChildren"
                    class="w-6 h-6 flex items-center justify-center text-slate-400 hover:text-blue-600 transition-colors mr-2"
                    @click="toggleExpand(row.id)"
                  >
                    <i :class="row.isExpanded ? 'fa fa-chevron-down' : 'fa fa-chevron-right'" class="text-xs"></i>
                  </button>
                  <span v-else class="w-6 mr-2"></span>
                  <!-- 详情行图标 -->
                  <i v-if="!row.isDict" class="fa fa-tag text-xs text-slate-400 mr-2"></i>
                  <span :class="row.isDict ? 'text-slate-800 font-medium' : 'text-slate-600'">
                    {{ row.isDict ? row.name : row.label }}
                  </span>
                </div>
              </td>
              <!-- 描述/值 -->
              <td class="px-4 py-3 text-center text-sm border-b border-slate-100">
                <span v-if="row.isDict" class="text-slate-500">{{ row.description || '-' }}</span>
                <span v-else class="px-2 py-0.5 bg-slate-100 rounded text-xs font-mono text-slate-600">{{ row.value }}</span>
              </td>
              <!-- 详情数/排序 -->
              <td class="px-4 py-3 text-center text-slate-600 border-b border-slate-100">
                <span v-if="row.isDict && row.detailCount" class="px-2 py-0.5 bg-blue-50 text-blue-600 text-xs rounded-full">
                  {{ row.detailCount }} 项
                </span>
                <span v-else-if="!row.isDict">{{ row.dictSort }}</span>
                <span v-else>-</span>
              </td>
              <!-- 操作 -->
              <td class="px-4 py-3 text-center border-b border-slate-100">
                <div class="flex items-center justify-center gap-2">
                  <!-- 字典行操作 -->
                  <template v-if="row.isDict">
                    <Button type="primary" size="sm" @click="showAddDetailDialog(row.id)">新增</Button>
                    <Button size="sm" @click="showEditDialog(row)">编辑</Button>
                    <Button type="danger" size="sm" @click="handleDelete(row.id)">删除</Button>
                  </template>
                  <!-- 详情行操作 -->
                  <template v-else>
                    <Button size="sm" @click="showEditDetailDialog(row)">编辑</Button>
                    <Button type="danger" size="sm" @click="handleDeleteDetail(row.id)">删除</Button>
                  </template>
                </div>
              </td>
            </tr>
            
            <!-- 空数据提示 -->
            <tr v-if="!loading && displayDictList.length === 0">
              <td colspan="4" class="px-4 py-12 text-center">
                <i class="fa fa-inbox text-4xl text-slate-300 mb-3" aria-hidden="true"></i>
                <p class="text-slate-400">暂无数据</p>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>

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
 * 字典管理页面 - 表格式布局
 * 设计风格：Dimensional Layering + Minimalism（与 MenuManage 统一）
 * 色彩方案：SaaS标准 (Primary: #2563EB, Background: #F8FAFC)
 * 遵循原则：KISS - 简洁实现，SOLID - 职责分离
 */
import { ref, computed, onMounted } from 'vue'
import { getDictList, saveDict, updateDict, deleteDict, saveDictDetail, updateDictDetail, deleteDictDetail } from '@/api/dict'
import { useDictStore } from '@/stores/dict'
// 基础组件
import Button from '@/components/basic/Button.vue'
import Modal from '@/components/basic/Modal.vue'
import Input from '@/components/basic/Input.vue'

// ==================== 列表状态 ====================
const dictList = ref([])
const loading = ref(false)
const expandedIds = ref([])
const searchName = ref('')
// 分页参数（后端必传，设置大值一次性获取全部）
const pageSize = ref(9999)

// ==================== 弹窗状态 ====================
const dictDialogVisible = ref(false)
const detailDialogVisible = ref(false)
const isEditDict = ref(false)
const isEditDetail = ref(false)
const dictForm = ref({ name: '', description: '' })
const detailForm = ref({ dictId: null, label: '', value: '', dictSort: 0 })

// ==================== Store ====================
const dictStore = useDictStore()

// ==================== 计算属性 ====================

/**
 * 扁平化显示的字典列表（响应式计算属性）
 * 将字典和详情扁平化为单一列表，便于表格渲染
 */
const displayDictList = computed(() => {
  const result = []
  
  dictList.value.forEach(dict => {
    const hasChildren = dict.dictDetails && dict.dictDetails.length > 0
    const isExpanded = expandedIds.value.includes(dict.id)
    
    // 添加字典行
    result.push({
      rowKey: `dict-${dict.id}`,
      id: dict.id,
      name: dict.name,
      description: dict.description,
      isDict: true,
      level: 0,
      hasChildren,
      isExpanded,
      detailCount: dict.dictDetails?.length || 0
    })
    
    // 如果展开，添加详情行
    if (hasChildren && isExpanded) {
      dict.dictDetails.forEach(detail => {
        result.push({
          rowKey: `detail-${detail.id}`,
          id: detail.id,
          label: detail.label,
          value: detail.value,
          dictSort: detail.dictSort,
          dictId: dict.id,
          isDict: false,
          level: 1
        })
      })
    }
  })
  
  return result
})

// ==================== 方法 ====================

/**
 * 切换展开/折叠
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
 * 查询字典列表（一次性加载全部数据）
 * 后端要求currentPage必传，设置pageSize=9999获取全部
 */
const handleSearch = async () => {
  loading.value = true
  try {
    const res = await getDictList({
      blurry: searchName.value,
      currentPage: 1,
      pageSize: pageSize.value
    })
    if (res.data?.code === 200) {
      // 兼容 content 和 records 两种字段
      dictList.value = res.data.data?.records || res.data.data?.content || []
    }
  } catch (error) {
    console.error('查询字典列表失败:', error)
  } finally {
    loading.value = false
  }
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
      handleSearch() // 重新加载列表
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
      handleSearch() // 重新加载列表
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
      handleSearch() // 重新加载列表
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
      handleSearch() // 重新加载列表
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
/* 滚动条样式 */
.custom-scrollbar {
  scrollbar-width: thin;
  scrollbar-color: rgba(0, 0, 0, 0.15) transparent;
}

.custom-scrollbar::-webkit-scrollbar {
  width: 6px;
  height: 6px;
}

.custom-scrollbar::-webkit-scrollbar-track {
  background: transparent;
}

.custom-scrollbar::-webkit-scrollbar-thumb {
  background-color: rgba(0, 0, 0, 0.15);
  border-radius: 3px;
}

.custom-scrollbar::-webkit-scrollbar-thumb:hover {
  background-color: rgba(0, 0, 0, 0.25);
}
</style>