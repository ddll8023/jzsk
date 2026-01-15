<template>
  <div class="min-h-full bg-slate-50 flex flex-col">
    <!-- 面包屑 -->
    <nav class="px-6 py-3 text-sm text-slate-500">
      <span>首页</span>
      <i class="fa fa-angle-right mx-2"></i>
      <span>系统管理</span>
      <i class="fa fa-angle-right mx-2"></i>
      <span class="text-slate-900 font-medium">菜单管理</span>
    </nav>

    <!-- 工具栏 -->
    <div class="px-6">
      <div class="flex items-center justify-between p-4 bg-white rounded-lg shadow-sm border border-slate-200">
        <Button type="primary" @click="showAddDialog(null)">
          <i class="fa fa-plus mr-2"></i>新增菜单
        </Button>
        <Button @click="loadMenuList">
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
              <th class="px-4 py-3 text-left text-xs text-slate-500 uppercase tracking-wider border-b border-slate-200">菜单名称</th>
              <th class="px-4 py-3 text-center text-xs text-slate-500 uppercase tracking-wider border-b border-slate-200 w-32">权限代码</th>
              <th class="px-4 py-3 text-center text-xs text-slate-500 uppercase tracking-wider border-b border-slate-200 w-48">路由路径</th>
              <th class="px-4 py-3 text-center text-xs text-slate-500 uppercase tracking-wider border-b border-slate-200 w-20">排序</th>
              <th class="px-4 py-3 text-center text-xs text-slate-500 uppercase tracking-wider border-b border-slate-200 w-20">状态</th>
              <th class="px-4 py-3 text-center text-xs text-slate-500 uppercase tracking-wider border-b border-slate-200 w-64">操作</th>
            </tr>
          </thead>
          <tbody>
            <tr 
              v-for="row in displayMenuList" 
              :key="row.id" 
              class="hover:bg-slate-50 transition-colors duration-150"
            >
              <!-- 菜单名称 -->
              <td class="px-4 py-3 border-b border-slate-100">
                <div class="flex items-center" :style="{ paddingLeft: row.level * 20 + 'px' }">
                  <button
                    v-if="row.hasChildren"
                    class="w-6 h-6 flex items-center justify-center text-slate-400 hover:text-blue-600 transition-colors mr-2"
                    @click="toggleExpand(row.id)"
                  >
                    <i :class="row.isExpanded ? 'fa fa-chevron-down' : 'fa fa-chevron-right'" class="text-xs"></i>
                  </button>
                  <span v-else class="w-6 mr-2"></span>
                  <span class="text-slate-800">{{ row.name }}</span>
                </div>
              </td>
              <!-- 权限代码 -->
              <td class="px-4 py-3 text-center text-sm text-slate-600 border-b border-slate-100">
                <span class="px-2 py-0.5 bg-slate-100 rounded text-xs font-mono">{{ row.code }}</span>
              </td>
              <!-- 路由路径 -->
              <td class="px-4 py-3 text-center text-sm text-slate-500 border-b border-slate-100">{{ row.path || '-' }}</td>
              <!-- 排序 -->
              <td class="px-4 py-3 text-center text-slate-600 border-b border-slate-100">{{ row.ordernum }}</td>
              <!-- 状态 -->
              <td class="px-4 py-3 text-center border-b border-slate-100">
                <button 
                  @click="toggleStatus(row)"
                  :class="isEnabled(row.status) ? 'bg-blue-600' : 'bg-slate-300'"
                  class="relative w-11 h-6 rounded-full transition-colors duration-200 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2 cursor-pointer"
                  :title="isEnabled(row.status) ? '点击禁用' : '点击启用'"
                >
                  <span 
                    :class="isEnabled(row.status) ? 'translate-x-5' : 'translate-x-1'"
                    class="absolute top-1 left-0 w-4 h-4 bg-white rounded-full shadow transition-transform duration-200"
                  ></span>
                </button>
              </td>
              <!-- 操作 -->
              <td class="px-4 py-3 text-center border-b border-slate-100">
                <div class="flex items-center justify-center gap-2">
                  <Button type="primary" size="sm" @click="showAddDialog(row)">新增</Button>
                  <Button size="sm" @click="showEditDialog(row)">编辑</Button>
                  <Button type="danger" size="sm" @click="handleDelete(row)">删除</Button>
                </div>
              </td>
            </tr>
            <!-- 空数据提示 -->
            <tr v-if="!loading && displayMenuList.length === 0">
              <td colspan="6" class="px-4 py-12 text-center">
                <i class="fa fa-inbox text-4xl text-slate-300 mb-3"></i>
                <p class="text-slate-400">暂无数据</p>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>

    <!-- 新增/编辑弹窗 -->
    <Modal v-model="dialogVisible" :title="isEdit ? '编辑菜单' : '新增菜单'">
      <div class="space-y-4">
        <!-- 父级菜单 -->
        <div class="flex items-center">
          <label class="w-24 text-right pr-2 text-slate-600">父级菜单:</label>
          <select 
            v-model="form.subsystemid" 
            class="flex-1 px-3 py-2 border border-slate-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-all"
          >
            <option :value="0">顶级菜单</option>
            <option v-for="item in flatMenuList" :key="item.id" :value="item.id">
              {{ item.displayName }}
            </option>
          </select>
        </div>
        <Input v-model="form.name" label="菜单名称" required placeholder="请输入菜单名称" />
        <Input v-model="form.code" label="权限代码" required placeholder="如: sys_menu" />
        <Input v-model="form.path" label="路由路径" placeholder="如: /home/menumanage" />
        <Input v-model="form.ordernum" type="number" label="排序" required />
        <!-- 状态 -->
        <div class="flex items-center">
          <label class="w-24 text-right pr-2 text-slate-600"><span class="text-red-500">*</span>状态:</label>
          <select 
            v-model="form.status" 
            class="flex-1 px-3 py-2 border border-slate-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-all"
          >
            <option value="启用">启用</option>
            <option value="禁用">禁用</option>
          </select>
        </div>
        <Input v-model="form.note" label="备注" placeholder="请输入备注" />
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
 * 菜单管理页面
 * 设计风格：Dimensional Layering + Minimalism（与 DictManage 统一）
 * 色彩方案：SaaS标准 (Primary: #2563EB, Background: #F8FAFC)
 * 遵循原则：KISS - 简洁实现，SOLID - 职责分离
 * Source: backend/szy/src/main/java/com/szy/controller/ResourceController.java
 */
import { ref, computed, onMounted } from 'vue'
import { getMenuList, saveMenu, updateMenu, deleteMenu } from '@/api/menu'
// 基础组件
import Button from '@/components/basic/Button.vue'
import Modal from '@/components/basic/Modal.vue'
import Input from '@/components/basic/Input.vue'

// ==================== 列表状态 ====================
const menuList = ref([])
const loading = ref(false)
const expandedIds = ref([])

// ==================== 弹窗状态 ====================
const dialogVisible = ref(false)
const isEdit = ref(false)
const form = ref({
  name: '',
  code: '',
  subsystemid: 0,
  path: '',
  type: 'menu',
  ordernum: 0,
  status: '启用',
  note: ''
})

// ==================== 计算属性 ====================

/**
 * 判断状态是否启用（兼容多种格式）
 */
const isEnabled = (status) => {
  return status === '1' || status === '启用' || status === 1
}

/**
 * 扁平化显示的菜单列表（响应式计算属性）
 */
const displayMenuList = computed(() => {
  const result = []
  
  const flatten = (list, level = 0) => {
    // 防御性检查：确保 list 是数组
    if (!Array.isArray(list)) return
    
    list.forEach(menu => {
      // 防御性检查：确保 children 存在且为数组
      const children = Array.isArray(menu.children) ? menu.children : []
      const hasChildren = children.length > 0
      const isExpanded = expandedIds.value.includes(menu.id)
      
      result.push({
        ...menu,
        level,
        hasChildren,
        isExpanded,
        children
      })
      
      if (hasChildren && isExpanded) {
        flatten(children, level + 1)
      }
    })
  }
  
  flatten(menuList.value)
  return result
})

/**
 * 扁平化菜单列表（用于父级选择下拉框）
 */
const flatMenuList = computed(() => {
  const result = []
  const flatten = (list, prefix = '') => {
    list.forEach(item => {
      result.push({
        id: item.id,
        name: item.name,
        displayName: prefix + item.name
      })
      if (item.children?.length) {
        flatten(item.children, prefix + '　')
      }
    })
  }
  flatten(menuList.value)
  return result
})

// ==================== 方法 ====================

/**
 * 加载菜单列表
 */
const loadMenuList = async () => {
  loading.value = true
  try {
    const res = await getMenuList()
    if (res.data?.code === 200) {
      menuList.value = res.data.data || []
    }
  } catch (error) {
    console.error('加载菜单列表失败:', error)
  } finally {
    loading.value = false
  }
}

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
 * 显示新增弹窗
 */
const showAddDialog = (parent) => {
  isEdit.value = false
  form.value = {
    name: '',
    code: '',
    subsystemid: parent?.id || 0,
    path: '',
    type: 'menu',
    ordernum: 0,
    status: '启用',
    note: ''
  }
  dialogVisible.value = true
}

/**
 * 显示编辑弹窗
 */
const showEditDialog = (item) => {
  isEdit.value = true
  form.value = {
    id: item.id,
    name: item.name,
    code: item.code,
    subsystemid: item.subsystemid,
    path: item.path || '',
    type: item.type || 'menu',
    ordernum: item.ordernum,
    status: item.status,
    note: item.note || ''
  }
  dialogVisible.value = true
}

/**
 * 提交表单
 */
const submitForm = async () => {
  if (!form.value.name?.trim()) {
    alert('请输入菜单名称')
    return
  }
  if (!form.value.code?.trim()) {
    alert('请输入权限代码')
    return
  }
  
  try {
    const fn = isEdit.value ? updateMenu : saveMenu
    const res = await fn(form.value)
    if (res.data?.code === 200) {
      dialogVisible.value = false
      loadMenuList()
    } else {
      alert(res.data?.message || '操作失败')
    }
  } catch (error) {
    console.error('保存菜单失败:', error)
    alert('保存失败，请重试')
  }
}

/**
 * 切换菜单启用/禁用状态
 */
const toggleStatus = async (row) => {
  const newStatus = isEnabled(row.status) ? '禁用' : '启用'
  try {
    const res = await updateMenu({
      id: row.id,
      status: newStatus
    })
    if (res.data?.code === 200) {
      row.status = newStatus
    } else {
      alert(res.data?.message || '状态更新失败')
    }
  } catch (error) {
    console.error('切换状态失败:', error)
    alert('操作失败，请重试')
  }
}

/**
 * 删除菜单
 */
const handleDelete = async (item) => {
  if (item.children?.length > 0) {
    alert('该菜单下有子菜单，请先删除子菜单')
    return
  }
  
  if (!confirm(`确定要删除菜单"${item.name}"吗？`)) return
  
  try {
    const res = await deleteMenu(item.id)
    if (res.data?.code === 200) {
      loadMenuList()
    } else {
      alert(res.data?.message || '删除失败')
    }
  } catch (error) {
    console.error('删除菜单失败:', error)
    alert('删除失败，请重试')
  }
}

// ==================== 生命周期 ====================
onMounted(() => {
  loadMenuList()
})
</script>

<style scoped>
/* 滚动条样式 - 与 DictManage 统一 */
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
