<template>
  <div class="min-h-full bg-slate-50 flex flex-col">
    <!-- 面包屑 -->
    <nav class="px-6 py-3 text-sm text-slate-500">
      <span>首页</span>
      <i class="fa fa-angle-right mx-2" aria-hidden="true"></i>
      <span>系统管理</span>
      <i class="fa fa-angle-right mx-2" aria-hidden="true"></i>
      <span class="text-slate-900 font-medium">机构信息</span>
    </nav>

    <!-- 工具栏 -->
    <div class="px-6">
      <div class="flex items-center justify-between p-4 bg-white rounded-lg shadow-sm border border-slate-200">
        <div class="flex items-center gap-4">
          <Button type="primary" @click="showAddDialog">
            <i class="fa fa-plus mr-2" aria-hidden="true"></i>新增机构
          </Button>
          <!-- 搜索框 -->
          <div class="flex items-center gap-2">
            <input 
              v-model="searchName"
              type="text"
              placeholder="请输入机构名称搜索"
              class="px-3 py-2 border border-slate-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-all w-64"
              @keyup.enter="handleSearch"
            />
            <Button @click="handleSearch">
              <i class="fa fa-search mr-1" aria-hidden="true"></i>搜索
            </Button>
          </div>
        </div>
        <Button @click="loadOrgList">
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
                v-for="(row, index) in orgList" 
                :key="row.id" 
                class="hover:bg-slate-50 transition-colors duration-150"
              >
                <td class="px-4 py-3 text-center text-sm text-slate-600 border-b border-slate-100">
                  {{ tableIndex(index) }}
                </td>
                <td class="px-4 py-3 text-sm text-slate-800 border-b border-slate-100">
                  {{ row.organizationName }}
                </td>
                <td class="px-4 py-3 text-sm text-slate-600 border-b border-slate-100">
                  {{ row.organizationCode || '-' }}
                </td>
                <td class="px-4 py-3 text-sm text-slate-600 border-b border-slate-100">
                  {{ row.legalRepresentative || '-' }}
                </td>
                <td class="px-4 py-3 text-center text-sm text-slate-600 border-b border-slate-100">
                  {{ row.officeTelephone || '-' }}
                </td>
                <td class="px-4 py-3 text-sm text-slate-600 border-b border-slate-100">
                  {{ row.address || '-' }}
                </td>
                <td class="px-4 py-3 text-center border-b border-slate-100">
                  <div class="flex items-center justify-center gap-2">
                    <Button size="sm" @click="showEditDialog(row.id)">编辑</Button>
                    <Button type="danger" size="sm" @click="handleDelete(row.id)">删除</Button>
                  </div>
                </td>
              </tr>
              
              <!-- 空数据提示 -->
              <tr v-if="!loading && orgList.length === 0">
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

    <!-- 新增/编辑机构弹窗 -->
    <Modal v-model="dialogVisible" :title="isEdit ? '编辑机构' : '新增机构'" width="xl">
      <div class="space-y-4 max-h-[60vh] overflow-y-auto pr-2 custom-scrollbar">
        <!-- 基本信息 -->
        <div class="pb-3 border-b border-slate-200">
          <h4 class="text-sm font-medium text-slate-700 mb-3">基本信息</h4>
          <div class="grid grid-cols-2 gap-4">
            <Input v-model="form.organizationName" label="机构名称" required placeholder="请输入机构名称" />
            <Input v-model="form.organizationCode" label="机构代码" placeholder="请输入机构代码" />
            <Input v-model="form.administrativeName" label="行政区划名称" placeholder="请输入行政区划名称" />
            <Input v-model="form.organizationAbbr" label="机构简称" placeholder="请输入机构简称" />
            <Input v-model="form.institutionalType" label="机构类型" placeholder="请输入机构类型" />
            <Input v-model="form.agencySpecifications" label="机构规格" placeholder="请输入机构规格" />
          </div>
        </div>

        <!-- 联系方式 -->
        <div class="pb-3 border-b border-slate-200">
          <h4 class="text-sm font-medium text-slate-700 mb-3">联系方式</h4>
          <div class="grid grid-cols-2 gap-4">
            <Input v-model="form.officeTelephone" label="办公室电话" placeholder="请输入办公室电话" />
            <Input v-model="form.fax" label="传真" placeholder="请输入传真" />
            <Input v-model="form.email" label="邮箱" placeholder="请输入邮箱" />
            <Input v-model="form.website" label="网站" placeholder="请输入网站" />
            <Input v-model="form.postalCode" label="邮政编码" placeholder="请输入邮政编码" />
            <div class="col-span-2">
              <Input v-model="form.address" label="地址" placeholder="请输入地址" />
            </div>
          </div>
        </div>

        <!-- 其他信息 -->
        <div class="pb-3">
          <h4 class="text-sm font-medium text-slate-700 mb-3">其他信息</h4>
          <div class="grid grid-cols-2 gap-4">
            <Input v-model="form.legalRepresentative" label="法人代表" placeholder="请输入法人代表" />
            <Input v-model="form.subordinateRelations" label="隶属关系" placeholder="请输入隶属关系" />
            <Input v-model="form.mainFunction" label="主要职能" placeholder="请输入主要职能" />
            <Input v-model="form.approveContent" label="主要审批内容" placeholder="请输入主要审批内容" />
            <Input v-model="form.staffSize" type="number" label="编制人数" placeholder="请输入编制人数" />
            <Select 
              v-model="form.whetherReform" 
              label="是否施行水务改革" 
              :options="reformOptions" 
              placeholder="请选择" 
            />
          </div>
        </div>
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
 * 机构管理页面
 * 设计风格：Dimensional Layering + Minimalism（与 UserManage/PersonManage/DeptManage 统一）
 * 色彩方案：SaaS标准 (Primary: #2563EB, Background: #F8FAFC)
 * 遵循原则：KISS - 简洁实现，YAGNI - 只实现必需功能，SOLID - 职责分离
 * Source: frontend/src/components/menu/ManageInformation/ManageOrganization.vue
 */
import { ref, onMounted } from 'vue'
import { 
  getOrgList, 
  getOrgInfo, 
  saveOrg, 
  updateOrg, 
  deleteOrg 
} from '@/api/organization'
// 基础组件
import Button from '@/components/basic/Button.vue'
import Modal from '@/components/basic/Modal.vue'
import Input from '@/components/basic/Input.vue'
import Select from '@/components/basic/Select.vue'
import Pagination from '@/components/basic/Pagination.vue'

// ==================== 列表状态 ====================
const orgList = ref([])
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
  organizationName: '',
  organizationCode: '',
  administrativeName: '',
  organizationAbbr: '',
  legalRepresentative: '',
  agencySpecifications: '',
  subordinateRelations: '',
  institutionalType: '',
  mainFunction: '',
  approveContent: '',
  website: '',
  email: '',
  address: '',
  postalCode: '',
  officeTelephone: '',
  fax: '',
  staffSize: '',
  whetherReform: ''
})

// ==================== 表格列配置 ====================
const columns = [
  { key: 'index', title: '序号', width: '80px', align: 'center' },
  { key: 'organizationName', title: '机构名称', width: '200px', align: 'left' },
  { key: 'organizationCode', title: '机构代码', width: '150px', align: 'left' },
  { key: 'legalRepresentative', title: '法人代表', width: '120px', align: 'left' },
  { key: 'officeTelephone', title: '办公电话', width: '140px', align: 'center' },
  { key: 'address', title: '地址', width: '250px', align: 'left' },
  { key: 'actions', title: '操作', width: '160px', align: 'center' }
]

// ==================== 下拉选项 ====================
const reformOptions = [
  { label: '是', value: '是' },
  { label: '否', value: '否' }
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
 * 加载机构列表
 */
const loadOrgList = async () => {
  loading.value = true
  try {
    const res = await getOrgList({
      currentPage: currentPage.value,
      pageSize: pageSize.value,
      name: searchName.value || ''
    })
    if (res.data?.code === 200) {
      orgList.value = res.data.data?.records || []
      total.value = res.data.data?.total || 0
    }
  } catch (error) {
    console.error('加载机构列表失败:', error)
    alert('加载机构列表失败')
  } finally {
    loading.value = false
  }
}

/**
 * 搜索机构
 */
const handleSearch = () => {
  currentPage.value = 1
  loadOrgList()
}

/**
 * 分页变化
 */
const handlePageChange = (page) => {
  currentPage.value = page
  loadOrgList()
}

/**
 * 显示新增弹窗
 */
const showAddDialog = () => {
  isEdit.value = false
  form.value = {
    organizationName: '',
    organizationCode: '',
    administrativeName: '',
    organizationAbbr: '',
    legalRepresentative: '',
    agencySpecifications: '',
    subordinateRelations: '',
    institutionalType: '',
    mainFunction: '',
    approveContent: '',
    website: '',
    email: '',
    address: '',
    postalCode: '',
    officeTelephone: '',
    fax: '',
    staffSize: '',
    whetherReform: ''
  }
  dialogVisible.value = true
}

/**
 * 显示编辑弹窗
 */
const showEditDialog = async (id) => {
  try {
    const res = await getOrgInfo(id)
    if (res.data?.code === 200) {
      isEdit.value = true
      form.value = { ...res.data.data }
      dialogVisible.value = true
    }
  } catch (error) {
    console.error('获取机构信息失败:', error)
    alert('获取机构信息失败')
  }
}

/**
 * 提交表单（新增/编辑）
 */
const submitForm = async () => {
  // 基础验证
  if (!form.value.organizationName?.trim()) {
    alert('请输入机构名称')
    return
  }
  
  // 邮箱格式验证
  if (form.value.email && !/^[\w.-]+@[a-zA-Z\d.-]+\.[a-zA-Z]{2,}$/.test(form.value.email)) {
    alert('请输入正确的邮箱格式')
    return
  }
  
  // 网站格式验证
  if (form.value.website && !/^(https?|ftp):\/\/[^\s\/$.?#].[^\s]*$/.test(form.value.website)) {
    alert('请输入正确的网站格式')
    return
  }
  
  // 邮政编码验证
  if (form.value.postalCode && !/^[1-9]\d{5}$/.test(form.value.postalCode)) {
    alert('请输入正确的邮政编码（6位数字）')
    return
  }
  
  // 编制人数验证
  if (form.value.staffSize && !/^[1-9]\d*$/.test(form.value.staffSize)) {
    alert('请输入正确的编制人数（正整数）')
    return
  }
  
  try {
    const fn = isEdit.value ? updateOrg : saveOrg
    const res = await fn(form.value)
    if (res.data?.code === 200) {
      alert(isEdit.value ? '更新成功' : '添加成功')
      dialogVisible.value = false
      loadOrgList()
    } else {
      alert(res.data?.message || '操作失败')
    }
  } catch (error) {
    console.error('保存机构失败:', error)
    alert(error.response?.data?.message || '操作失败')
  }
}

/**
 * 删除机构
 */
const handleDelete = async (id) => {
  if (!confirm('此操作将永久删除该机构信息, 是否继续?')) return
  
  try {
    const res = await deleteOrg(id)
    if (res.data?.code === 200) {
      alert('删除成功')
      loadOrgList()
    } else {
      alert(res.data?.message || '删除失败')
    }
  } catch (error) {
    console.error('删除机构失败:', error)
    alert('删除失败')
  }
}

// ==================== 生命周期 ====================
onMounted(() => {
  loadOrgList()
})
</script>

<style scoped>
/* 滚动条样式 - 与 UserManage/PersonManage/DeptManage 统一 */
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
