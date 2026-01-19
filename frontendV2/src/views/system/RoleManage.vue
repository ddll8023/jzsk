<template>
  <div class="min-h-full bg-slate-50 flex flex-col">
    <!-- 面包屑 -->
    <nav class="px-6 py-3 text-sm text-slate-500">
      <span>首页</span>
      <i class="fa fa-angle-right mx-2" aria-hidden="true"></i>
      <span>系统管理</span>
      <i class="fa fa-angle-right mx-2" aria-hidden="true"></i>
      <span class="text-slate-900 font-medium">角色管理</span>
    </nav>

    <!-- 工具栏 -->
    <div class="px-6">
      <div class="flex items-center justify-between p-4 bg-white rounded-lg shadow-sm border border-slate-200">
        <div class="flex items-center gap-4">
          <Button type="primary" @click="showAddDialog">
            <i class="fa fa-plus mr-2" aria-hidden="true"></i>新增角色
          </Button>
          <!-- 搜索框 -->
          <div class="flex items-center gap-2">
            <input 
              v-model="searchName"
              type="text"
              placeholder="请输入角色名称搜索"
              class="px-3 py-2 border border-slate-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-all w-48"
              @keyup.enter="handleSearch"
            />
            <Button @click="handleSearch">
              <i class="fa fa-search mr-1" aria-hidden="true"></i>搜索
            </Button>
          </div>
        </div>
        <Button @click="loadRoleList">
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
                v-for="(row, index) in roleList" 
                :key="row.id" 
                class="hover:bg-slate-50 transition-colors duration-150"
              >
                <td class="px-4 py-3 text-center text-sm text-slate-600 border-b border-slate-100">
                  {{ tableIndex(index) }}
                </td>
                <td class="px-4 py-3 text-sm text-slate-800 border-b border-slate-100">
                  {{ row.name }}
                </td>
                <td class="px-4 py-3 text-sm text-slate-600 border-b border-slate-100">
                  {{ row.code || '-' }}
                </td>
                <td class="px-4 py-3 text-center text-sm border-b border-slate-100">
                  <span 
                    :class="row.status === 1 ? 'bg-green-50 text-green-600' : 'bg-gray-50 text-gray-600'"
                    class="px-2 py-0.5 text-xs rounded"
                  >
                    {{ row.status === 1 ? '启用' : '禁用' }}
                  </span>
                </td>
                <td class="px-4 py-3 text-center text-sm text-slate-600 border-b border-slate-100">
                  {{ formatDate(row.createTime) }}
                </td>
                <td class="px-4 py-3 text-center border-b border-slate-100">
                  <div class="flex items-center justify-center gap-2">
                    <Button size="sm" @click="showEditDialog(row.id)">编辑</Button>
                    <Button type="danger" size="sm" @click="handleDelete(row.id)">删除</Button>
                    <Button type="primary" size="sm" @click="showMenuDialog(row.id)">权限配置</Button>
                  </div>
                </td>
              </tr>
              
              <!-- 空数据提示 -->
              <tr v-if="!loading && roleList.length === 0">
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

    <!-- 新增/编辑角色弹窗 -->
    <Modal v-model="roleDialogVisible" :title="isEdit ? '编辑角色' : '新增角色'" width="lg">
      <div class="space-y-4">
        <div class="grid grid-cols-2 gap-4">
          <Input v-model="roleForm.name" label="角色名称" required placeholder="请输入角色名称" />
          <Input v-model="roleForm.code" label="角色编码" required placeholder="请输入角色编码" />
        </div>
        <div class="grid grid-cols-2 gap-4">
          <Select 
            v-model="roleForm.status" 
            label="状态" 
            :options="statusOptions" 
            placeholder="请选择状态" 
          />
          <Input v-model="roleForm.sort" type="number" label="排序号" placeholder="数字越小越靠前" />
        </div>
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-1">角色描述</label>
          <textarea 
            v-model="roleForm.description"
            rows="3"
            class="w-full px-4 py-2 text-sm border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-primary-500 focus:border-primary-500 bg-white transition-all duration-200"
            placeholder="请输入角色描述"
          ></textarea>
        </div>
      </div>
      <template #footer>
        <Button type="primary" @click="submitRole">确 定</Button>
        <Button @click="roleDialogVisible = false">取 消</Button>
      </template>
    </Modal>

    <!-- 权限配置弹窗 -->
    <Modal v-model="menuDialogVisible" title="权限配置" width="md">
      <div class="space-y-4">
        <Input v-model="menuForm.roleName" label="角色名称" disabled />
        <div>
          <label class="block text-sm font-medium text-gray-700 mb-2">菜单权限</label>
          <div class="border border-slate-200 rounded-lg p-4 max-h-96 overflow-y-auto custom-scrollbar bg-slate-50">
            <MenuTree 
              :menus="menuList" 
              v-model="menuForm.menuIds"
            />
          </div>
        </div>
      </div>
      <template #footer>
        <Button type="primary" @click="submitMenu">确 定</Button>
        <Button @click="menuDialogVisible = false">取 消</Button>
      </template>
    </Modal>
  </div>
</template>

<script setup>
/**
 * 角色管理页面
 * 设计风格：Dimensional Layering + Minimalism（与 UserManage/DeptManage/OrgManage 统一）
 * 色彩方案：SaaS标准 (Primary: #2563EB, Background: #F8FAFC)
 * 遵循原则：KISS - 简洁实现，YAGNI - 只实现必需功能，SOLID - 职责分离
 */
import { ref, onMounted } from 'vue'
import { 
  getRoleList, 
  getRoleInfo, 
  saveRole, 
  updateRole, 
  deleteRole,
  allocateMenu,
  getRoleMenus
} from '@/api/role'
import { getMenuList } from '@/api/menu'
// 基础组件
import Button from '@/components/basic/Button.vue'
import Modal from '@/components/basic/Modal.vue'
import Input from '@/components/basic/Input.vue'
import Select from '@/components/basic/Select.vue'
import Pagination from '@/components/basic/Pagination.vue'
// 菜单树组件
import MenuTree from '@/components/business/system/MenuTree.vue'

// ==================== 列表状态 ====================
const roleList = ref([])
const menuList = ref([])
const loading = ref(false)
const searchName = ref('')
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

// ==================== 弹窗状态 ====================
const roleDialogVisible = ref(false)
const menuDialogVisible = ref(false)
const isEdit = ref(false)

// ==================== 表单数据 ====================
const roleForm = ref({
  name: '',
  code: '',
  description: '',
  status: 1,
  sort: 0
})

const menuForm = ref({
  roleId: null,
  roleName: '',
  menuIds: []
})

// ==================== 表格列配置 ====================
const columns = [
  { key: 'index', title: '序号', width: '80px', align: 'center' },
  { key: 'name', title: '角色名称', width: '180px', align: 'left' },
  { key: 'code', title: '角色编码', width: '150px', align: 'left' },
  { key: 'status', title: '状态', width: '120px', align: 'center' },
  { key: 'createTime', title: '创建时间', width: '180px', align: 'center' },
  { key: 'actions', title: '操作', width: '280px', align: 'center' }
]

// ==================== 下拉选项 ====================
const statusOptions = [
  { label: '启用', value: 1 },
  { label: '禁用', value: 0 }
]

// ==================== 计算属性 ====================
/**
 * 计算表格序号（支持分页连续）
 */
const tableIndex = (index) => {
  return (currentPage.value - 1) * pageSize.value + index + 1
}

/**
 * 格式化日期时间
 */
const formatDate = (dateStr) => {
  if (!dateStr) return '-'
  const date = new Date(dateStr)
  return date.toLocaleString('zh-CN', { 
    year: 'numeric', 
    month: '2-digit', 
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit'
  })
}

// ==================== 方法 ====================
/**
 * 加载角色列表
 */
const loadRoleList = async () => {
  loading.value = true
  try {
    const res = await getRoleList({
      currentPage: currentPage.value,
      pageSize: pageSize.value,
      name: searchName.value || undefined
    })
    if (res.data?.code === 200) {
      roleList.value = res.data.data?.records || []
      total.value = res.data.data?.total || 0
    }
  } catch (error) {
    console.error('加载角色列表失败:', error)
    alert('加载角色列表失败')
  } finally {
    loading.value = false
  }
}

/**
 * 加载菜单列表
 */
const loadMenuList = async () => {
  try {
    const res = await getMenuList({
      currentPage: 1,
      pageSize: 9999
    })
    if (res.data?.code === 200) {
      menuList.value = res.data.data?.records || []
    }
  } catch (error) {
    console.error('加载菜单列表失败:', error)
  }
}

/**
 * 搜索角色
 */
const handleSearch = () => {
  currentPage.value = 1
  loadRoleList()
}

/**
 * 分页变化
 */
const handlePageChange = (page) => {
  currentPage.value = page
  loadRoleList()
}

/**
 * 显示新增弹窗
 */
const showAddDialog = () => {
  isEdit.value = false
  roleForm.value = {
    name: '',
    code: '',
    description: '',
    status: 1,
    sort: 0
  }
  roleDialogVisible.value = true
}

/**
 * 显示编辑弹窗
 */
const showEditDialog = async (id) => {
  try {
    const res = await getRoleInfo(id)
    if (res.data?.code === 200) {
      isEdit.value = true
      roleForm.value = { ...res.data.data }
      roleDialogVisible.value = true
    }
  } catch (error) {
    console.error('获取角色信息失败:', error)
    alert('获取角色信息失败')
  }
}

/**
 * 提交角色表单（新增/编辑）
 */
const submitRole = async () => {
  // 基础验证
  if (!roleForm.value.name?.trim()) {
    alert('请输入角色名称')
    return
  }
  if (!roleForm.value.code?.trim()) {
    alert('请输入角色编码')
    return
  }
  
  try {
    const fn = isEdit.value ? updateRole : saveRole
    const res = await fn(roleForm.value)
    if (res.data?.code === 200) {
      alert(isEdit.value ? '更新成功' : '添加成功')
      roleDialogVisible.value = false
      loadRoleList()
    } else {
      alert(res.data?.message || '操作失败')
    }
  } catch (error) {
    console.error('保存角色失败:', error)
    alert(error.response?.data?.message || '操作失败')
  }
}

/**
 * 删除角色
 */
const handleDelete = async (id) => {
  if (!confirm('此操作将永久删除该角色, 是否继续?')) return
  
  try {
    const res = await deleteRole(id)
    if (res.data?.code === 200) {
      alert('删除成功')
      loadRoleList()
    } else {
      alert(res.data?.message || '删除失败')
    }
  } catch (error) {
    console.error('删除角色失败:', error)
    alert('删除失败')
  }
}

/**
 * 显示权限配置弹窗
 */
const showMenuDialog = async (id) => {
  try {
    // 获取角色信息
    const roleRes = await getRoleInfo(id)
    if (roleRes.data?.code !== 200) {
      alert('获取角色信息失败')
      return
    }
    
    // 获取角色已分配的菜单
    const menuRes = await getRoleMenus(id)
    
    menuForm.value = {
      roleId: id,
      roleName: roleRes.data.data.name,
      menuIds: menuRes.data?.code === 200 ? (menuRes.data.data || []) : []
    }
    
    menuDialogVisible.value = true
  } catch (error) {
    console.error('获取权限信息失败:', error)
    alert('获取权限信息失败')
  }
}

/**
 * 提交权限配置
 */
const submitMenu = async () => {
  try {
    const res = await allocateMenu(menuForm.value.roleId, menuForm.value.menuIds)
    if (res.data?.code === 200) {
      alert('权限配置成功')
      menuDialogVisible.value = false
    } else {
      alert(res.data?.message || '权限配置失败')
    }
  } catch (error) {
    console.error('权限配置失败:', error)
    alert('权限配置失败')
  }
}

// ==================== 生命周期 ====================
onMounted(() => {
  loadRoleList()
  loadMenuList()
})
</script>

<style scoped>
/* 滚动条样式 - 与 UserManage/DeptManage/OrgManage 统一 */
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
