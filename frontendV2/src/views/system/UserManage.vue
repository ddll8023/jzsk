<template>
  <div class="min-h-full bg-slate-50 flex flex-col">
    <!-- 面包屑 -->
    <nav class="px-6 py-3 text-sm text-slate-500">
      <span>首页</span>
      <i class="fa fa-angle-right mx-2" aria-hidden="true"></i>
      <span>系统管理</span>
      <i class="fa fa-angle-right mx-2" aria-hidden="true"></i>
      <span class="text-slate-900 font-medium">用户管理</span>
    </nav>

    <!-- 工具栏 -->
    <div class="px-6">
      <div class="flex items-center justify-between p-4 bg-white rounded-lg shadow-sm border border-slate-200">
        <div class="flex items-center gap-4">
          <Button type="primary" @click="showAddDialog">
            <i class="fa fa-plus mr-2" aria-hidden="true"></i>新增用户
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
        <Button @click="loadUserList">
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
                  :class="column.key === 'index' || column.key === 'role' || column.key === 'phoneNumber' || column.key === 'email' || column.key === 'actions' ? 'text-center' : 'text-left'"
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
                v-for="(row, index) in userList" 
                :key="row.id" 
                class="hover:bg-slate-50 transition-colors duration-150"
              >
                <td class="px-4 py-3 text-center text-sm text-slate-600 border-b border-slate-100">
                  {{ tableIndex(index) }}
                </td>
                <td class="px-4 py-3 text-sm text-slate-800 border-b border-slate-100">
                  {{ row.username }}
                </td>
                <td class="px-4 py-3 text-sm text-slate-800 border-b border-slate-100">
                  {{ row.name }}
                </td>
                <td class="px-4 py-3 text-center text-sm border-b border-slate-100">
                  <span class="px-2 py-0.5 bg-blue-50 text-blue-600 text-xs rounded">
                    {{ row.roles && row.roles[0] ? row.roles[0].name : '-' }}
                  </span>
                </td>
                <td class="px-4 py-3 text-center text-sm text-slate-600 border-b border-slate-100">
                  {{ row.phoneNumber || '-' }}
                </td>
                <td class="px-4 py-3 text-center text-sm text-slate-600 border-b border-slate-100">
                  {{ row.email || '-' }}
                </td>
                <td class="px-4 py-3 text-center border-b border-slate-100">
                  <div class="flex items-center justify-center gap-2">
                    <Button size="sm" @click="showEditDialog(row.id)">编辑</Button>
                    <Button type="danger" size="sm" @click="handleDelete(row.id)">删除</Button>
                    <Button type="primary" size="sm" @click="showAllocateDialog(row.id)">分配角色</Button>
                    <Button type="warning" size="sm" @click="handleResetPassword(row.id)">初始化密码</Button>
                  </div>
                </td>
              </tr>
              
              <!-- 空数据提示 -->
              <tr v-if="!loading && userList.length === 0">
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

    <!-- 新增/编辑用户弹窗 -->
    <Modal v-model="userDialogVisible" :title="isEdit ? '编辑用户' : '新增用户'" width="lg">
      <div class="space-y-4 max-h-[60vh] overflow-y-auto pr-2">
        <!-- 基本信息 -->
        <div class="pb-2 border-b border-slate-200">
          <h4 class="text-sm font-medium text-slate-700 mb-3">基本信息</h4>
          <div class="grid grid-cols-2 gap-4">
            <Input v-model="userForm.username" label="用户名" required placeholder="请输入用户名" />
            <Input v-model="userForm.name" label="姓名" required placeholder="请输入姓名" />
            <Select v-model="userForm.gender" label="性别" :options="genderOptions" placeholder="请选择性别" />
            <Input v-model="userForm.phoneNumber" label="手机号" placeholder="请输入手机号" />
            <Input v-model="userForm.email" label="邮箱" placeholder="请输入邮箱" />
            <Input v-model="userForm.technicalTitle" label="技术职称" placeholder="请输入技术职称" />
          </div>
        </div>

        <!-- 详细信息 -->
        <div class="pb-2 border-b border-slate-200">
          <h4 class="text-sm font-medium text-slate-700 mb-3">详细信息</h4>
          <div class="grid grid-cols-2 gap-4">
            <Select 
              v-model="userForm.politicalAppearance" 
              label="政治面貌" 
              :options="politicalOptions" 
              placeholder="请选择政治面貌" 
            />
            <Input v-model="userForm.idNumber" label="身份证号" placeholder="请输入身份证号" />
            <div class="col-span-2">
              <Input v-model="userForm.address" label="家庭住址" placeholder="请输入家庭住址" />
            </div>
          </div>
        </div>

        <!-- 时间信息 -->
        <div class="pb-2 border-b border-slate-200">
          <h4 class="text-sm font-medium text-slate-700 mb-3">时间信息</h4>
          <div class="grid grid-cols-2 gap-4">
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">出生年月</label>
              <input 
                v-model="userForm.birthday" 
                type="month"
                class="w-full px-4 py-2 text-sm border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-primary-500 focus:border-primary-500 bg-white transition-all duration-200"
              />
            </div>
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-1">开始工作时间</label>
              <input 
                v-model="userForm.workingTime" 
                type="date"
                class="w-full px-4 py-2 text-sm border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-primary-500 focus:border-primary-500 bg-white transition-all duration-200"
              />
            </div>
          </div>
        </div>

        <!-- 教育信息 -->
        <div class="pb-2 border-b border-slate-200">
          <h4 class="text-sm font-medium text-slate-700 mb-3">教育信息</h4>
          <div class="grid grid-cols-2 gap-4">
            <Select 
              v-model="userForm.academicQualifications" 
              label="学历" 
              :options="educationOptions" 
              placeholder="请选择学历" 
            />
            <Input v-model="userForm.graduationInstitution" label="毕业院校" placeholder="请输入毕业院校" />
            <Input v-model="userForm.major" label="专业" placeholder="请输入专业" />
          </div>
        </div>

        <!-- 备注 -->
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">备注</label>
          <textarea 
            v-model="userForm.note"
            rows="3"
            class="w-full px-4 py-2 text-sm border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-primary-500 focus:border-primary-500 bg-white transition-all duration-200"
            placeholder="请输入备注"
          ></textarea>
        </div>
      </div>
      <template #footer>
        <Button type="primary" @click="submitUser">确 定</Button>
        <Button @click="userDialogVisible = false">取 消</Button>
      </template>
    </Modal>

    <!-- 分配角色弹窗 -->
    <Modal v-model="allocateDialogVisible" title="分配角色" width="md">
      <div class="space-y-4">
        <Input v-model="allocateForm.username" label="用户名" disabled />
        <Select 
          v-model="allocateForm.roleId" 
          label="角色" 
          required
          :options="roleOptions" 
          placeholder="请选择角色" 
        />
      </div>
      <template #footer>
        <Button type="primary" @click="submitAllocate">确 定</Button>
        <Button @click="allocateDialogVisible = false">取 消</Button>
      </template>
    </Modal>
  </div>
</template>

<script setup>
/**
 * 用户管理页面
 * 设计风格：Dimensional Layering + Minimalism（与 DictManage/MenuManage 统一）
 * 色彩方案：SaaS标准 (Primary: #2563EB, Background: #F8FAFC)
 * 遵循原则：KISS - 简洁实现，YAGNI - 只实现必需功能，SOLID - 职责分离
 * Source: frontend/src/components/menu/SystemServe/UserManage.vue
 */
import { ref, computed, onMounted } from 'vue'
import { 
  getUserList, 
  searchUser, 
  getUserInfo, 
  saveUser, 
  updateUser, 
  deleteUser, 
  allocateRole, 
  resetPassword,
  getRoleList 
} from '@/api/user'
// 基础组件
import Button from '@/components/basic/Button.vue'
import Modal from '@/components/basic/Modal.vue'
import Input from '@/components/basic/Input.vue'
import Select from '@/components/basic/Select.vue'
import Pagination from '@/components/basic/Pagination.vue'

// ==================== 列表状态 ====================
const userList = ref([])
const roleList = ref([])
const loading = ref(false)
const searchName = ref('')
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

// ==================== 弹窗状态 ====================
const userDialogVisible = ref(false)
const allocateDialogVisible = ref(false)
const isEdit = ref(false)

// ==================== 表单数据 ====================
const userForm = ref({
  username: '',
  name: '',
  gender: '',
  phoneNumber: '',
  email: '',
  technicalTitle: '',
  politicalAppearance: '',
  idNumber: '',
  address: '',
  birthday: '',
  workingTime: '',
  academicQualifications: '',
  graduationInstitution: '',
  major: '',
  note: ''
})

const allocateForm = ref({
  id: null,
  username: '',
  roleId: ''
})

// ==================== 表格列配置 ====================
const columns = [
  { key: 'index', title: '序号', width: '80px' },
  { key: 'username', title: '用户名', width: '150px' },
  { key: 'name', title: '姓名', width: '120px' },
  { key: 'role', title: '角色', width: '150px' },
  { key: 'phoneNumber', title: '手机号', width: '150px' },
  { key: 'email', title: '邮箱', width: '200px' },
  { key: 'actions', title: '操作', width: '400px' }
]

// ==================== 下拉选项 ====================
const genderOptions = [
  { label: '男', value: '男' },
  { label: '女', value: '女' }
]

const politicalOptions = [
  { label: '中共党员', value: '中共党员' },
  { label: '中共预备党员', value: '中共预备党员' },
  { label: '共青团员', value: '共青团员' },
  { label: '群众', value: '群众' },
  { label: '民革党员', value: '民革党员' },
  { label: '民盟盟员', value: '民盟盟员' },
  { label: '民建会员', value: '民建会员' },
  { label: '民进会员', value: '民进会员' },
  { label: '农工党党员', value: '农工党党员' },
  { label: '致公党党员', value: '致公党党员' },
  { label: '九三学社社员', value: '九三学社社员' },
  { label: '台盟盟员', value: '台盟盟员' },
  { label: '无党派人士', value: '无党派人士' }
]

const educationOptions = [
  { label: '博士研究生', value: '博士研究生' },
  { label: '硕士研究生', value: '硕士研究生' },
  { label: '本科', value: '本科' },
  { label: '专科', value: '专科' },
  { label: '中专/高中', value: '中专/高中' },
  { label: '初中', value: '初中' },
  { label: '小学', value: '小学' }
]

// 角色选项（动态加载）
const roleOptions = computed(() => {
  return roleList.value.map(role => ({
    label: role.name,
    value: role.id
  }))
})

// ==================== 计算属性 ====================
/**
 * 计算表格序号（支持分页连续）
 */
const tableIndex = (index) => {
  return (currentPage.value - 1) * pageSize.value + index + 1
}

// ==================== 方法 ====================
/**
 * 加载用户列表
 */
const loadUserList = async () => {
  loading.value = true
  try {
    const res = await getUserList({
      currentPage: currentPage.value,
      pageSize: pageSize.value
    })
    if (res.data?.code === 200) {
      userList.value = res.data.data?.records || []
      total.value = res.data.data?.total || 0
    }
  } catch (error) {
    console.error('加载用户列表失败:', error)
    alert('加载用户列表失败')
  } finally {
    loading.value = false
  }
}

/**
 * 加载角色列表
 */
const loadRoleList = async () => {
  try {
    const res = await getRoleList({
      currentPage: 1,
      pageSize: 9999
    })
    if (res.data?.code === 200) {
      roleList.value = res.data.data?.records || []
    }
  } catch (error) {
    console.error('加载角色列表失败:', error)
  }
}

/**
 * 搜索用户
 */
const handleSearch = async () => {
  if (!searchName.value.trim()) {
    loadUserList()
    return
  }

  // 搜索时重置到第一页
  currentPage.value = 1

  loading.value = true
  try {
    const res = await searchUser({
      name: searchName.value,
      currentPage: currentPage.value,
      pageSize: pageSize.value
    })
    if (res.data?.code === 200) {
      userList.value = res.data.data?.records || []
      total.value = res.data.data?.total || 0
    }
  } catch (error) {
    console.error('搜索用户失败:', error)
    alert('搜索失败')
  } finally {
    loading.value = false
  }
}

/**
 * 分页变化
 */
const handlePageChange = (page) => {
  currentPage.value = page
  if (searchName.value.trim()) {
    handleSearch()
  } else {
    loadUserList()
  }
}

/**
 * 显示新增弹窗
 */
const showAddDialog = () => {
  isEdit.value = false
  userForm.value = {
    username: '',
    name: '',
    gender: '',
    phoneNumber: '',
    email: '',
    technicalTitle: '',
    politicalAppearance: '',
    idNumber: '',
    address: '',
    birthday: '',
    workingTime: '',
    academicQualifications: '',
    graduationInstitution: '',
    major: '',
    note: ''
  }
  userDialogVisible.value = true
}

/**
 * 显示编辑弹窗
 */
const showEditDialog = async (id) => {
  try {
    const res = await getUserInfo(id)
    if (res.data?.code === 200) {
      isEdit.value = true
      userForm.value = { ...res.data.data }
      userDialogVisible.value = true
    }
  } catch (error) {
    console.error('获取用户信息失败:', error)
    alert('获取用户信息失败')
  }
}

/**
 * 提交用户表单（新增/编辑）
 */
const submitUser = async () => {
  // 基础验证
  if (!userForm.value.username?.trim()) {
    alert('请输入用户名')
    return
  }
  if (!userForm.value.name?.trim()) {
    alert('请输入姓名')
    return
  }
  
  // 邮箱格式验证
  if (userForm.value.email && !/^[\w.-]+@[a-zA-Z\d.-]+\.[a-zA-Z]{2,}$/.test(userForm.value.email)) {
    alert('请输入正确的邮箱格式')
    return
  }
  
  // 手机号格式验证
  if (userForm.value.phoneNumber && !/^1(3[0-9]|5[0-3,5-9]|7[1-3,5-8]|8[0-9])\d{8}$/.test(userForm.value.phoneNumber)) {
    alert('请输入正确的手机号码')
    return
  }
  
  // 身份证格式验证
  if (userForm.value.idNumber && !/(^[1-9]\d{5}(18|19|([23]\d))\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\d{3}[0-9Xx]$)|(^[1-9]\d{5}\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\d{2}[0-9Xx]$)/.test(userForm.value.idNumber)) {
    alert('请输入正确的身份证号')
    return
  }
  
  try {
    const fn = isEdit.value ? updateUser : saveUser
    const res = await fn(userForm.value)
    if (res.data?.code === 200) {
      alert(isEdit.value ? '更新成功' : '添加成功')
      userDialogVisible.value = false
      loadUserList()
    } else {
      alert(res.data?.message || '操作失败')
    }
  } catch (error) {
    console.error('保存用户失败:', error)
    alert(error.response?.data?.message || '操作失败')
  }
}

/**
 * 删除用户
 */
const handleDelete = async (id) => {
  if (!confirm('此操作将永久删除该用户, 是否继续?')) return
  
  try {
    const res = await deleteUser(id)
    if (res.data?.code === 200) {
      alert('删除成功')
      loadUserList()
    } else {
      alert(res.data?.message || '删除失败')
    }
  } catch (error) {
    console.error('删除用户失败:', error)
    alert('删除失败')
  }
}

/**
 * 显示分配角色弹窗
 */
const showAllocateDialog = async (id) => {
  try {
    const res = await getUserInfo(id)
    if (res.data?.code === 200) {
      const user = res.data.data
      allocateForm.value = {
        id: user.id,
        username: user.username,
        roleId: user.roles && user.roles.length > 0 ? user.roles[0].id : ''
      }
      allocateDialogVisible.value = true
    }
  } catch (error) {
    console.error('获取用户信息失败:', error)
    alert('获取用户信息失败')
  }
}

/**
 * 提交角色分配
 */
const submitAllocate = async () => {
  if (!allocateForm.value.roleId) {
    alert('请选择角色')
    return
  }
  
  try {
    const res = await allocateRole(allocateForm.value.id, [allocateForm.value.roleId])
    if (res.data?.code === 200) {
      alert('分配角色成功')
      allocateDialogVisible.value = false
      loadUserList()
    } else {
      alert(res.data?.message || '分配角色失败')
    }
  } catch (error) {
    console.error('分配角色失败:', error)
    alert('分配角色失败')
  }
}

/**
 * 初始化密码
 */
const handleResetPassword = async (id) => {
  if (!confirm('此操作将初始化该用户的密码为123456, 是否继续?')) return
  
  try {
    const res = await resetPassword(id)
    if (res.data?.code === 200) {
      alert('初始化密码成功')
    } else {
      alert(res.data?.message || '初始化密码失败')
    }
  } catch (error) {
    console.error('初始化密码失败:', error)
    alert('初始化密码失败')
  }
}

// ==================== 生命周期 ====================
onMounted(() => {
  loadUserList()
  loadRoleList()
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


