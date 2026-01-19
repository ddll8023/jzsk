<template>
  <div class="min-h-full bg-slate-50 flex flex-col">
    <!-- 面包屑 -->
    <nav class="px-6 py-3 text-sm text-slate-500">
      <span>首页</span>
      <i class="fa fa-angle-right mx-2" aria-hidden="true"></i>
      <span>系统管理</span>
      <i class="fa fa-angle-right mx-2" aria-hidden="true"></i>
      <span class="text-slate-900 font-medium">部门管理</span>
    </nav>

    <!-- 工具栏 -->
    <div class="px-6">
      <div class="flex items-center justify-between p-4 bg-white rounded-lg shadow-sm border border-slate-200">
        <div class="flex items-center gap-4">
          <Button type="primary" @click="showAddDialog">
            <i class="fa fa-plus mr-2" aria-hidden="true"></i>新增部门
          </Button>
          <!-- 搜索框 -->
          <div class="flex items-center gap-2">
            <input 
              v-model="searchName"
              type="text"
              placeholder="请输入部门名称搜索"
              class="px-3 py-2 border border-slate-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-all w-48"
              @keyup.enter="handleSearch"
            />
            <Button @click="handleSearch">
              <i class="fa fa-search mr-1" aria-hidden="true"></i>搜索
            </Button>
          </div>
        </div>
        <Button @click="loadDeptList">
          <i class="fa fa-refresh mr-2" aria-hidden="true"></i>刷新
        </Button>
      </div>
    </div>

    <!-- 表格区域（固定高度，内含分页） -->
    <div class="flex-1 px-6 py-4 flex flex-col min-h-0">
      <div class="flex-1 bg-white rounded-lg shadow-sm border border-slate-200 overflow-hidden flex flex-col">
        <!-- 表格容器（可滚动） -->
        <div class="flex-1 overflow-auto custom-scrollbar">
          <table class="w-full border-collapse">
            <thead class="bg-slate-50/80 sticky top-0">
              <tr>
                <th 
                  v-for="column in columns" 
                  :key="column.key"
                  :style="{ width: column.width }"
                  class="px-4 py-3 text-xs text-slate-500 uppercase tracking-wider border-b border-slate-200"
                  :class="column.align === 'center' ? 'text-center' : 'text-left'"
                >
                  {{ column.title }}
                </th>
              </tr>
            </thead>
            <tbody>
              <!-- 加载状态 -->
              <tr v-if="loading">
                <td :colspan="columns.length" class="px-4 py-8 text-center">
                  <i class="fa fa-spinner fa-spin text-primary-600 text-xl" aria-hidden="true"></i>
                  <p class="mt-2 text-slate-500">加载中...</p>
                </td>
              </tr>
              
              <!-- 数据行 -->
              <tr 
                v-else
                v-for="(row, index) in deptList" 
                :key="row.id" 
                class="hover:bg-slate-50 transition-colors duration-150"
              >
                <td class="px-4 py-3 text-center text-sm text-slate-600 border-b border-slate-100">
                  {{ tableIndex(index) }}
                </td>
                <td class="px-4 py-3 text-sm text-slate-800 border-b border-slate-100">
                  {{ row.departmentName }}
                </td>
                <td class="px-4 py-3 text-sm text-slate-600 border-b border-slate-100">
                  {{ row.departmentResponsibility || '-' }}
                </td>
                <td class="px-4 py-3 text-center text-sm border-b border-slate-100">
                  <span v-if="row.level" class="px-2 py-0.5 bg-blue-50 text-blue-600 text-xs rounded">
                    {{ row.level }}
                  </span>
                  <span v-else>-</span>
                </td>
                <td class="px-4 py-3 text-sm text-slate-600 border-b border-slate-100">
                  {{ row.company || '-' }}
                </td>
                <td class="px-4 py-3 text-center border-b border-slate-100">
                  <div class="flex items-center justify-center gap-2">
                    <Button size="sm" @click="showEditDialog(row.id)">编辑</Button>
                    <Button type="danger" size="sm" @click="handleDelete(row.id)">删除</Button>
                  </div>
                </td>
              </tr>
              
              <!-- 空数据提示 -->
              <tr v-if="!loading && deptList.length === 0">
                <td :colspan="columns.length" class="px-4 py-12 text-center">
                  <i class="fa fa-inbox text-4xl text-slate-300 mb-3" aria-hidden="true"></i>
                  <p class="text-slate-400">暂无数据</p>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
        
        <!-- 分页（固定在表格底部） -->
        <div class="flex-shrink-0 flex items-center justify-center px-4 py-3 border-t border-slate-200 bg-slate-50/30">
          <Pagination
            :total="total"
            :current-page="currentPage"
            :page-size="pageSize"
            @change="handlePageChange"
          />
        </div>
      </div>
    </div>

    <!-- 新增/编辑部门弹窗 -->
    <Modal v-model="dialogVisible" :title="isEdit ? '编辑部门' : '新增部门'" width="lg">
      <div class="space-y-4">
        <div class="grid grid-cols-2 gap-4">
          <Input v-model="form.departmentName" label="部门名称" required placeholder="请输入部门名称" />
          <Input v-model="form.level" label="部门级别" placeholder="请输入部门级别" />
        </div>
        <Input v-model="form.departmentResponsibility" label="部门职责" required placeholder="请输入部门职责" />
        <Input v-model="form.company" label="所属公司" placeholder="请输入所属公司" />
      </div>
      <template #footer>
        <Button type="primary" @click="submitForm">确 定</Button>
        <Button @click="dialogVisible = false">取 消</Button>
      </template>
    </Modal>
  </div>
</template>

<script setup>
/**
 * 部门管理页面
 * 设计风格：Dimensional Layering + Minimalism（与 UserManage/PersonManage 统一）
 * 色彩方案：SaaS标准 (Primary: #2563EB, Background: #F8FAFC)
 * 遵循原则：KISS - 简洁实现，YAGNI - 只实现必需功能，SOLID - 职责分离
 * Source: frontend/src/components/menu/SystemServe/DepartmentManage.vue
 */
import { ref, onMounted } from 'vue'
import { 
  getDeptList, 
  getDeptInfo, 
  saveDept, 
  updateDept, 
  deleteDept 
} from '@/api/dept'
// 基础组件
import Button from '@/components/basic/Button.vue'
import Modal from '@/components/basic/Modal.vue'
import Input from '@/components/basic/Input.vue'
import Pagination from '@/components/basic/Pagination.vue'

// ==================== 列表状态 ====================
const deptList = ref([])
const loading = ref(false)
const searchName = ref('')
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

// ==================== 弹窗状态 ====================
const dialogVisible = ref(false)
const isEdit = ref(false)

// ==================== 表单数据 ====================
const form = ref({
  departmentName: '',
  departmentResponsibility: '',
  level: '',
  company: ''
})

// ==================== 表格列配置 ====================
const columns = [
  { key: 'index', title: '序号', width: '80px', align: 'center' },
  { key: 'departmentName', title: '部门名称', width: '180px', align: 'left' },
  { key: 'departmentResponsibility', title: '部门职责', width: '250px', align: 'left' },
  { key: 'level', title: '部门级别', width: '120px', align: 'center' },
  { key: 'company', title: '所属公司', width: '180px', align: 'left' },
  { key: 'actions', title: '操作', width: '160px', align: 'center' }
]

// ==================== 计算属性 ====================
/**
 * 计算表格序号（支持分页连续）
 */
const tableIndex = (index) => {
  return (currentPage.value - 1) * pageSize.value + index + 1
}

// ==================== 方法 ====================
/**
 * 加载部门列表
 */
const loadDeptList = async () => {
  loading.value = true
  try {
    const res = await getDeptList({
      currentPage: currentPage.value,
      pageSize: pageSize.value,
      departmentName: searchName.value || undefined
    })
    if (res.data?.code === 200) {
      deptList.value = res.data.data?.records || []
      total.value = res.data.data?.total || 0
    }
  } catch (error) {
    console.error('加载部门列表失败:', error)
    alert('加载部门列表失败')
  } finally {
    loading.value = false
  }
}

/**
 * 搜索部门
 */
const handleSearch = () => {
  currentPage.value = 1
  loadDeptList()
}

/**
 * 分页变化
 */
const handlePageChange = (page) => {
  currentPage.value = page
  loadDeptList()
}

/**
 * 显示新增弹窗
 */
const showAddDialog = () => {
  isEdit.value = false
  form.value = {
    departmentName: '',
    departmentResponsibility: '',
    level: '',
    company: ''
  }
  dialogVisible.value = true
}

/**
 * 显示编辑弹窗
 */
const showEditDialog = async (id) => {
  try {
    const res = await getDeptInfo(id)
    if (res.data?.code === 200) {
      isEdit.value = true
      form.value = { ...res.data.data }
      dialogVisible.value = true
    }
  } catch (error) {
    console.error('获取部门信息失败:', error)
    alert('获取部门信息失败')
  }
}

/**
 * 提交表单（新增/编辑）
 */
const submitForm = async () => {
  // 基础验证
  if (!form.value.departmentName?.trim()) {
    alert('请输入部门名称')
    return
  }
  if (!form.value.departmentResponsibility?.trim()) {
    alert('请输入部门职责')
    return
  }
  
  try {
    const fn = isEdit.value ? updateDept : saveDept
    const res = await fn(form.value)
    if (res.data?.code === 200) {
      alert(isEdit.value ? '更新成功' : '添加成功')
      dialogVisible.value = false
      loadDeptList()
    } else {
      alert(res.data?.message || '操作失败')
    }
  } catch (error) {
    console.error('保存部门失败:', error)
    alert(error.response?.data?.message || '操作失败')
  }
}

/**
 * 删除部门
 */
const handleDelete = async (id) => {
  if (!confirm('此操作将永久删除该部门信息, 是否继续?')) return
  
  try {
    const res = await deleteDept(id)
    if (res.data?.code === 200) {
      alert('删除成功')
      loadDeptList()
    } else {
      alert(res.data?.message || '删除失败')
    }
  } catch (error) {
    console.error('删除部门失败:', error)
    alert('删除失败')
  }
}

// ==================== 生命周期 ====================
onMounted(() => {
  loadDeptList()
})
</script>

<style scoped>
/* 滚动条样式 - 与 UserManage/PersonManage 统一 */
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
