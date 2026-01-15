<template>
  <div class="h-full flex flex-col">
    <!-- 面包屑 -->
    <nav class="px-4 py-2 text-sm text-gray-500">
      <span>首页</span>
      <i class="fa fa-angle-right mx-2"></i>
      <span>系统管理</span>
      <i class="fa fa-angle-right mx-2"></i>
      <span class="text-gray-900">菜单管理</span>
    </nav>

    <!-- 工具栏 -->
    <div class="flex items-center justify-between px-4 py-3 bg-white border-b">
      <button 
        class="px-4 py-2 bg-blue-600 text-white rounded hover:bg-blue-700 flex items-center gap-2"
        @click="showAddDialog(null)"
      >
        <i class="fa fa-plus"></i>
        新增菜单
      </button>
      <button 
        class="px-4 py-2 border border-gray-300 rounded hover:bg-gray-100 flex items-center gap-2"
        @click="loadMenuList"
      >
        <i class="fa fa-refresh"></i>
        刷新
      </button>
    </div>

    <!-- 表格区域 -->
    <div class="flex-1 overflow-auto px-4 py-2">
      <table class="w-full border-collapse">
        <thead class="bg-blue-100 sticky top-0">
          <tr>
            <th class="border border-gray-300 px-4 py-2 text-left">菜单名称</th>
            <th class="border border-gray-300 px-4 py-2 text-center w-32">权限代码</th>
            <th class="border border-gray-300 px-4 py-2 text-center w-48">路由路径</th>
            <th class="border border-gray-300 px-4 py-2 text-center w-20">排序</th>
            <th class="border border-gray-300 px-4 py-2 text-center w-20">状态</th>
            <th class="border border-gray-300 px-4 py-2 text-center w-64">操作</th>
          </tr>
        </thead>
        <tbody>
          <!-- 使用计算属性渲染扁平化的菜单列表 -->
          <tr v-for="row in displayMenuList" :key="row.id" class="hover:bg-gray-50">
            <!-- 菜单名称 -->
            <td class="border border-gray-300 px-4 py-2">
              <div class="flex items-center" :style="{ paddingLeft: row.level * 20 + 'px' }">
                <!-- 展开/折叠按钮 -->
                <button
                  v-if="row.hasChildren"
                  class="w-6 h-6 flex items-center justify-center text-gray-500 hover:text-gray-700 mr-2"
                  @click="toggleExpand(row.id)"
                >
                  <!-- 使用 Font Awesome 5/6 兼容的图标类名 -->
                  <i :class="row.isExpanded ? 'far fa-minus-square' : 'far fa-plus-square'"></i>
                </button>
                <span v-else class="w-6 mr-2"></span>
                <span>{{ row.name }}</span>
              </div>
            </td>
            <!-- 权限代码 -->
            <td class="border border-gray-300 px-4 py-2 text-center text-sm text-gray-600">{{ row.code }}</td>
            <!-- 路由路径 -->
            <td class="border border-gray-300 px-4 py-2 text-center text-sm text-gray-600">{{ row.path || '-' }}</td>
            <!-- 排序 -->
            <td class="border border-gray-300 px-4 py-2 text-center">{{ row.ordernum }}</td>
            <!-- 状态：Switch 开关，点击可切换启用/禁用 -->
            <td class="border border-gray-300 px-4 py-2 text-center">
              <button 
                @click="toggleStatus(row)"
                :class="isEnabled(row.status) ? 'bg-blue-600' : 'bg-gray-300'"
                class="relative w-12 h-6 rounded-full transition-colors duration-200 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2"
                :title="isEnabled(row.status) ? '点击禁用' : '点击启用'"
              >
                <span 
                  :class="isEnabled(row.status) ? 'translate-x-6' : 'translate-x-1'"
                  class="absolute top-1 left-0 w-4 h-4 bg-white rounded-full shadow transition-transform duration-200"
                ></span>
              </button>
            </td>
            <!-- 操作 -->
            <td class="border border-gray-300 px-4 py-2 text-center">
              <div class="flex items-center justify-center gap-2">
                <button 
                  class="px-3 py-1 text-sm bg-blue-600 text-white rounded hover:bg-blue-700"
                  @click="showAddDialog(row)"
                >新增</button>
                <button 
                  class="px-3 py-1 text-sm border border-gray-300 rounded hover:bg-gray-100"
                  @click="showEditDialog(row)"
                >编辑</button>
                <button 
                  class="px-3 py-1 text-sm bg-red-600 text-white rounded hover:bg-red-700"
                  @click="handleDelete(row)"
                >删除</button>
              </div>
            </td>
          </tr>
          <!-- 空数据提示 -->
          <tr v-if="!loading && displayMenuList.length === 0">
            <td colspan="6" class="border border-gray-300 px-4 py-8 text-center text-gray-500">暂无数据</td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- 新增/编辑弹窗 -->
    <div v-if="dialogVisible" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
      <div class="bg-white rounded-lg w-[550px] shadow-xl">
        <div class="flex items-center justify-between px-4 py-3 border-b">
          <h3 class="text-lg font-medium">{{ isEdit ? '编辑菜单' : '新增菜单' }}</h3>
          <button class="text-gray-400 hover:text-gray-600" @click="dialogVisible = false">
            <i class="fa fa-times"></i>
          </button>
        </div>
        <div class="p-4 space-y-4">
          <!-- 父级菜单 -->
          <div class="flex items-center">
            <label class="w-24 text-right pr-2">父级菜单:</label>
            <select 
              v-model="form.subsystemid" 
              class="flex-1 px-3 py-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
            >
              <option :value="0">顶级菜单</option>
              <option v-for="item in flatMenuList" :key="item.id" :value="item.id">
                {{ item.displayName }}
              </option>
            </select>
          </div>
          <!-- 菜单名称 -->
          <div class="flex items-center">
            <label class="w-24 text-right pr-2"><span class="text-red-500">*</span>菜单名称:</label>
            <input 
              v-model="form.name" 
              type="text" 
              placeholder="请输入菜单名称"
              class="flex-1 px-3 py-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500" 
            />
          </div>
          <!-- 权限代码 -->
          <div class="flex items-center">
            <label class="w-24 text-right pr-2"><span class="text-red-500">*</span>权限代码:</label>
            <input 
              v-model="form.code" 
              type="text" 
              placeholder="如: sys_menu"
              class="flex-1 px-3 py-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500" 
            />
          </div>
          <!-- 路由路径 -->
          <div class="flex items-center">
            <label class="w-24 text-right pr-2">路由路径:</label>
            <input 
              v-model="form.path" 
              type="text" 
              placeholder="如: /home/menumanage"
              class="flex-1 px-3 py-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500" 
            />
          </div>
          <!-- 排序 -->
          <div class="flex items-center">
            <label class="w-24 text-right pr-2"><span class="text-red-500">*</span>排序:</label>
            <input 
              v-model.number="form.ordernum" 
              type="number" 
              min="0"
              class="flex-1 px-3 py-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500" 
            />
          </div>
          <!-- 状态 -->
          <div class="flex items-center">
            <label class="w-24 text-right pr-2"><span class="text-red-500">*</span>状态:</label>
            <select 
              v-model="form.status" 
              class="flex-1 px-3 py-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
            >
              <option value="启用">启用</option>
              <option value="禁用">禁用</option>
            </select>
          </div>
          <!-- 备注 -->
          <div class="flex items-center">
            <label class="w-24 text-right pr-2">备注:</label>
            <input 
              v-model="form.note" 
              type="text" 
              placeholder="请输入备注"
              class="flex-1 px-3 py-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500" 
            />
          </div>
        </div>
        <div class="flex justify-center gap-4 px-4 py-3 border-t">
          <button class="px-6 py-2 bg-blue-600 text-white rounded hover:bg-blue-700" @click="submitForm">确 定</button>
          <button class="px-6 py-2 border border-gray-300 rounded hover:bg-gray-100" @click="dialogVisible = false">取 消</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
/**
 * 菜单管理页面
 * 功能：菜单的增删改查，树形结构展示
 * 遵循原则：KISS - 简洁实现，SOLID - 职责分离
 * Source: backend/szy/src/main/java/com/szy/controller/ResourceController.java
 */
import { ref, computed, onMounted } from 'vue'
import { getMenuList, saveMenu, updateMenu, deleteMenu } from '@/api/menu'

// ==================== 列表状态 ====================
const menuList = ref([])
const loading = ref(false)
// 使用数组存储展开的ID，确保响应式
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
 * 根据展开状态动态生成要显示的行
 */
const displayMenuList = computed(() => {
  const result = []
  
  const flatten = (list, level = 0) => {
    list.forEach(menu => {
      const hasChildren = menu.children && menu.children.length > 0
      const isExpanded = expandedIds.value.includes(menu.id)
      
      // 添加当前菜单行
      result.push({
        ...menu,
        level,
        hasChildren,
        isExpanded,
        // 保留原始children用于删除检查
        children: menu.children
      })
      
      // 如果展开且有子菜单，递归添加子菜单
      if (hasChildren && isExpanded) {
        flatten(menu.children, level + 1)
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
      console.log('菜单数据加载成功，共', menuList.value.length, '个顶级菜单')
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
    // 已展开，移除
    expandedIds.value.splice(index, 1)
  } else {
    // 未展开，添加
    expandedIds.value.push(id)
  }
  console.log('展开状态:', expandedIds.value)
}

/**
 * 显示新增弹窗
 * @param {Object|null} parent - 父级菜单，null表示顶级
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
  // 表单验证
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
 * 遵循 KISS 原则：复用现有 updateMenu 接口
 * @param {Object} row - 菜单行数据
 */
const toggleStatus = async (row) => {
  const newStatus = isEnabled(row.status) ? '禁用' : '启用'
  try {
    const res = await updateMenu({
      id: row.id,
      status: newStatus
    })
    if (res.data?.code === 200) {
      // 直接更新本地状态，避免重新加载整个列表
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
  // 检查是否有子菜单
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
