<template>
  <div class="min-h-full bg-slate-50 flex flex-col">
    <!-- 面包屑 -->
    <nav class="px-6 py-3 text-sm text-slate-500">
      <span>首页</span>
      <i class="fa fa-angle-right mx-2" aria-hidden="true"></i>
      <span>管理信息服务</span>
      <i class="fa fa-angle-right mx-2" aria-hidden="true"></i>
      <span class="text-slate-900 font-medium">人员管理</span>
    </nav>

    <!-- 工具栏 -->
    <div class="px-6">
      <div class="flex items-center justify-between p-4 bg-white rounded-lg shadow-sm border border-slate-200">
        <div class="flex items-center gap-4">
          <Button type="primary" @click="showAddDialog">
            <i class="fa fa-plus mr-2" aria-hidden="true"></i>新增人员
          </Button>
          <!-- 搜索框 -->
          <div class="flex items-center gap-2">
            <input 
              v-model="searchName"
              type="text"
              placeholder="请输入姓名搜索"
              class="px-3 py-2 border border-slate-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-all w-48"
              @keyup.enter="handleSearch"
            />
            <Button @click="handleSearch">
              <i class="fa fa-search mr-1" aria-hidden="true"></i>搜索
            </Button>
          </div>
        </div>
        <Button @click="loadPersonList">
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
                v-for="(row, index) in personList" 
                :key="row.id" 
                class="hover:bg-slate-50 transition-colors duration-150"
              >
                <td class="px-4 py-3 text-center text-sm text-slate-600 border-b border-slate-100">
                  {{ tableIndex(index) }}
                </td>
                <td class="px-4 py-3 text-sm text-slate-800 border-b border-slate-100">
                  {{ row.name }}
                </td>
                <td class="px-4 py-3 text-center text-sm text-slate-600 border-b border-slate-100">
                  {{ row.age || '-' }}
                </td>
                <td class="px-4 py-3 text-center text-sm border-b border-slate-100">
                  <span v-if="row.gender" class="px-2 py-0.5 bg-blue-50 text-blue-600 text-xs rounded">
                    {{ row.gender }}
                  </span>
                  <span v-else>-</span>
                </td>
                <td class="px-4 py-3 text-center text-sm text-slate-600 border-b border-slate-100">
                  {{ row.phone || '-' }}
                </td>
                <td class="px-4 py-3 text-sm text-slate-600 border-b border-slate-100">
                  {{ row.organization || '-' }}
                </td>
                <td class="px-4 py-3 text-center text-sm text-slate-600 border-b border-slate-100">
                  {{ row.position || '-' }}
                </td>
                <td class="px-4 py-3 text-sm text-slate-600 border-b border-slate-100">
                  {{ row.duty || '-' }}
                </td>
                <td class="px-4 py-3 text-center border-b border-slate-100">
                  <div class="flex items-center justify-center gap-2">
                    <Button size="sm" @click="showEditDialog(row.id)">编辑</Button>
                    <Button type="danger" size="sm" @click="handleDelete(row.id)">删除</Button>
                  </div>
                </td>
              </tr>
              
              <!-- 空数据提示 -->
              <tr v-if="!loading && personList.length === 0">
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

    <!-- 新增/编辑人员弹窗 -->
    <Modal v-model="dialogVisible" :title="isEdit ? '编辑人员' : '新增人员'" width="lg">
      <div class="space-y-4">
        <div class="grid grid-cols-2 gap-4">
          <Input v-model="form.name" label="姓名" required placeholder="请输入姓名" />
          <Input v-model="form.age" type="number" label="年龄" placeholder="请输入年龄" />
        </div>
        <div class="grid grid-cols-2 gap-4">
          <Select v-model="form.gender" label="性别" :options="genderOptions" placeholder="请选择性别" />
          <Input v-model="form.phone" label="电话" placeholder="请输入电话" />
        </div>
        <div class="grid grid-cols-2 gap-4">
          <Input v-model="form.organization" label="所属机构" placeholder="请输入所属机构" />
          <Input v-model="form.position" label="职位" placeholder="请输入职位" />
        </div>
        <Input v-model="form.duty" label="职责" placeholder="请输入职责" />
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
 * 人员管理页面
 * 设计风格：Dimensional Layering + Minimalism（与 DictManage/MenuManage 统一）
 * 色彩方案：SaaS标准 (Primary: #2563EB, Background: #F8FAFC)
 * 遵循原则：KISS - 简洁实现，YAGNI - 只实现必需功能，SOLID - 职责分离
 * Source: frontend/src/components/menu/ManageInformation/ManagePerson.vue
 */
import { ref, computed, onMounted } from 'vue'
import { 
  getPersonList, 
  getPersonInfo, 
  savePerson, 
  updatePerson, 
  deletePerson 
} from '@/api/person'
// 基础组件
import Button from '@/components/basic/Button.vue'
import Modal from '@/components/basic/Modal.vue'
import Input from '@/components/basic/Input.vue'
import Select from '@/components/basic/Select.vue'
import Pagination from '@/components/basic/Pagination.vue'

// ==================== 列表状态 ====================
const personList = ref([])
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
  name: '',
  age: '',
  gender: '',
  phone: '',
  organization: '',
  position: '',
  duty: ''
})

// ==================== 表格列配置 ====================
const columns = [
  { key: 'index', title: '序号', width: '80px', align: 'center' },
  { key: 'name', title: '姓名', width: '120px', align: 'left' },
  { key: 'age', title: '年龄', width: '80px', align: 'center' },
  { key: 'gender', title: '性别', width: '80px', align: 'center' },
  { key: 'phone', title: '电话', width: '140px', align: 'center' },
  { key: 'organization', title: '所属机构', width: '180px', align: 'left' },
  { key: 'position', title: '职位', width: '120px', align: 'center' },
  { key: 'duty', title: '职责', width: '200px', align: 'left' },
  { key: 'actions', title: '操作', width: '160px', align: 'center' }
]

// ==================== 下拉选项 ====================
const genderOptions = [
  { label: '男', value: '男' },
  { label: '女', value: '女' }
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
 * 加载人员列表
 */
const loadPersonList = async () => {
  loading.value = true
  try {
    const res = await getPersonList({
      currentPage: currentPage.value,
      pageSize: pageSize.value,
      name: searchName.value || undefined
    })
    if (res.data?.code === 200) {
      personList.value = res.data.data?.records || []
      total.value = res.data.data?.total || 0
    }
  } catch (error) {
    console.error('加载人员列表失败:', error)
    alert('加载人员列表失败')
  } finally {
    loading.value = false
  }
}

/**
 * 搜索人员
 */
const handleSearch = () => {
  currentPage.value = 1
  loadPersonList()
}

/**
 * 分页变化
 */
const handlePageChange = (page) => {
  currentPage.value = page
  loadPersonList()
}

/**
 * 显示新增弹窗
 */
const showAddDialog = () => {
  isEdit.value = false
  form.value = {
    name: '',
    age: '',
    gender: '',
    phone: '',
    organization: '',
    position: '',
    duty: ''
  }
  dialogVisible.value = true
}

/**
 * 显示编辑弹窗
 */
const showEditDialog = async (id) => {
  try {
    const res = await getPersonInfo(id)
    if (res.data?.code === 200) {
      isEdit.value = true
      form.value = { ...res.data.data }
      dialogVisible.value = true
    }
  } catch (error) {
    console.error('获取人员信息失败:', error)
    alert('获取人员信息失败')
  }
}

/**
 * 提交表单（新增/编辑）
 */
const submitForm = async () => {
  // 基础验证
  if (!form.value.name?.trim()) {
    alert('请输入姓名')
    return
  }
  
  // 年龄验证
  if (form.value.age && (form.value.age < 1 || form.value.age > 120)) {
    alert('请输入正确的年龄（1-120）')
    return
  }
  
  // 手机号验证
  if (form.value.phone && !/^1[3-9]\d{9}$/.test(form.value.phone)) {
    alert('请输入正确的手机号码')
    return
  }
  
  try {
    const fn = isEdit.value ? updatePerson : savePerson
    const res = await fn(form.value)
    if (res.data?.code === 200) {
      alert(isEdit.value ? '更新成功' : '添加成功')
      dialogVisible.value = false
      loadPersonList()
    } else {
      alert(res.data?.message || '操作失败')
    }
  } catch (error) {
    console.error('保存人员失败:', error)
    alert(error.response?.data?.message || '操作失败')
  }
}

/**
 * 删除人员
 */
const handleDelete = async (id) => {
  if (!confirm('此操作将永久删除该人员信息, 是否继续?')) return
  
  try {
    const res = await deletePerson(id)
    if (res.data?.code === 200) {
      alert('删除成功')
      loadPersonList()
    } else {
      alert(res.data?.message || '删除失败')
    }
  } catch (error) {
    console.error('删除人员失败:', error)
    alert('删除失败')
  }
}

// ==================== 生命周期 ====================
onMounted(() => {
  loadPersonList()
})
</script>

<style scoped>
/* 滚动条样式 - 与 DictManage/MenuManage 统一 */
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
