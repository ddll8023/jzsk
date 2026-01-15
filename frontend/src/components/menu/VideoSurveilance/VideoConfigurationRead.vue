<template>
  <div id="div1">
    <div id="bread">
      <el-breadcrumb separator-class="el-icon-arrow-right" style="padding-top:5px; padding-left:10px">
        <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
        <el-breadcrumb-item>实时监测</el-breadcrumb-item>
        <el-breadcrumb-item :to="{ path: '/home/videoconfiguratin' }">视频配置</el-breadcrumb-item>
      </el-breadcrumb>
    </div>

    <div id="main-body">
      <div id="main-body-header">
        <span style="align-self:center; margin-left:20px;font-size:14px">摄像头名称：</span>
        <el-select v-model="name" clearable placeholder="请选择" style="align-self:center;" @change="searchInputType"
          @clear="searchInputType">
          <el-option v-for="item in nameList" :key="item.value" :label="item.label" :value="item.value">
          </el-option>
        </el-select>

        <span style="align-self:center; margin-left:20px;font-size:14px">类型：</span>
        <el-select v-model="type" clearable placeholder="请选择" style="align-self:center;" @change="searchInputType"
          @clear="searchInputType">
          <el-option v-for="item in typeList" :key="item.value" :label="item.label" :value="item.value">
          </el-option>
        </el-select>

        <!-- <el-button type="primary" round icon="el-icon-plus"
          style="margin-left:auto; align-self:center;margin-right:10px" @click="addDialogVisible = true">新增</el-button> -->
      </div>
      <div id="main-body-main">
        <el-table :data="VCList" border stripe style="width: 100%"
          :header-cell-style="{ background: '#cfe2f3', color: '#606266' }">
          <el-table-column type="index" label="序号" width="80px" align="center" :index="table_index"></el-table-column>
          <el-table-column prop="town" label="地点" width="120" align="center">
          </el-table-column>
          <!-- <el-table-column prop="village" label="编码" align="center">
          </el-table-column> -->
          <el-table-column prop="name" label="名称" width="120" align="center">
          </el-table-column>
          <el-table-column prop="type" label="类型" align="center">
          </el-table-column>
          <el-table-column prop="ip" label="IP地址" align="center">
          </el-table-column>
          <el-table-column prop="port" label="端口号" align="center">
          </el-table-column>
          <el-table-column prop="userName" label="用户名" align="center">
          </el-table-column>
          <el-table-column prop="password" label="密码" align="center">
          </el-table-column>
          <el-table-column fixed="right" label="操作" width="180" align="center">
            <template slot-scope="scope">
              <el-button size="mini" @click="showEditDialog(scope.row.id)">编辑</el-button>
              <el-button size="mini" type="danger" @click="removeById(scope.row.id)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>

      <div id="main-body-footer">
        <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
          :current-page="queryinfo.currentPage" :page-sizes="[10, 20, 30, 40]" :page-size="queryinfo.pageSize"
          layout="total, sizes, prev, pager, next, jumper" :total="total" style="align-self:center">
        </el-pagination>
      </div>
    </div>

    <!-- 新增对话框 -->
    <el-dialog title="新增摄像头" :visible.sync="addDialogVisible" width="600px" @close="addDialogClosed" center
      :close-on-press-escape="false" :close-on-click-modal="false">
      <el-form :model="addForm" :rules="addFormRules" ref="addFormRef" label-width="80px" size="small">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="地点:" prop="town">
              <!-- <el-input v-model="addForm.town"></el-input> -->
              <el-select v-model="addForm.town" clearable placeholder="请选择" style="align-self:center;"
                @change="addselectTownVillage" @clear="addselectTownVillage">
                <el-option v-for="item in townList" :key="item.value" :label="item.label" :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <!-- <el-col :span="12">
            <el-form-item label="编码:" prop="village">
              <el-input v-model="addForm.village" placeholder="请输入"></el-input>
            </el-form-item>
          </el-col> -->
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="名称:" prop="name">
              <el-input v-model="addForm.name"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="类型:" prop="type">
              <el-select v-model="addForm.type" placeholder="请选择类型">
                <el-option v-for="item in typeList" :key="item.value" :label="item.label" :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="IP地址:" prop="ip">
              <el-input v-model="addForm.ip"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="端口号:" prop="port">
              <el-input v-model="addForm.port"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="用户名:" prop="userName">
              <el-input v-model="addForm.userName"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="密码:" prop="password">
              <el-input v-model="addForm.password"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <!-- 底部按钮 -->
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="addVC">确 定</el-button>
        <el-button @click="addDialogVisible = false">取 消</el-button>
      </span>
    </el-dialog>

    <!-- 编辑对话框 -->
    <el-dialog title="编辑摄像头信息" :visible.sync="editDialogVisible" width="600px" @close="editDialogClosed" center
      :close-on-press-escape="false" :close-on-click-modal="false">
      <el-form :model="editForm" :rules="editFormRules" ref="editFormRef" label-width="80px" size="small ">

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="地点:" prop="town">
              <!-- <el-input v-model="addForm.town"></el-input> -->
              <el-select v-model="editForm.town" clearable placeholder="请选择" style="align-self:center;"
                @change="editselectTownVillage" @clear="editselectTownVillage">
                <el-option v-for="item in townList" :key="item.value" :label="item.label" :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <!-- <el-col :span="12">
            <el-form-item label="编码:" prop="village">
              <el-input v-model="editForm.village" placeholder="请输入"></el-input>
            </el-form-item>
          </el-col> -->

        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="名称:" prop="name">
              <el-input v-model="editForm.name"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="类型:" prop="type">
              <el-select v-model="editForm.type" placeholder="请选择类型">
                <el-option v-for="item in typeList" :key="item.value" :label="item.label" :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="IP地址:" prop="ip">
              <el-input v-model="editForm.ip"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="端口号:" prop="port">
              <el-input v-model="editForm.port"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="用户名:" prop="userName">
              <el-input v-model="editForm.userName"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="密码:" prop="password">
              <el-input v-model="editForm.password"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <!-- 底部按钮 -->
      <span slot="footer">
        <el-button type="primary" @click="editVC">确 定</el-button>
        <el-button @click="editDialogVisible = false">取 消</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: '',
  data() {
    return {
      type: '',
      types: [],
      typeList: [],
      town: '',
      village: '',
      //镇 村类型列表
      townVillageData: [],
      towns: [],
      townList: [],
      villageList: [],
      activeIndex: '',//选择序号
      openeds: ['1'],
      name: '',
      names: [],
      nameList: [],
      //获取表格的参数对象
      queryinfo: {
        //当前页数
        currentPage: 1,
        //当前每页显示条数
        pageSize: 10
      },
      //数据列表
      VCList: [],
      total: 0,
      //控制新增对话框
      addDialogVisible: false,
      //控制编辑对话框
      editDialogVisible: false,
      //添加表单数据
      addForm: {
        town: '',
        village: '',
        name: '',
        type: '',
        ip: '',
        port: '',
        userName: '',
        password: ''
      },
      //添加表单的验证规则对象
      addFormRules: {
        town: [{ required: true, message: '请输入地点', trigger: 'blur' }],
        // village: [
        //   { required: true, message: '请输入编码', trigger: 'blur' },
        //   { min: 8, max: 8, message: '编码必须为8位', trigger: 'blur' }
        // ],
        name: [{ required: true, message: '请输入名称', trigger: 'blur' }],
        type: [{ required: true, message: '请输入类型', trigger: 'blur' }],
        ip: [{ required: false, message: '请输入IP地址', trigger: 'blur' }],
        port: [{ required: false, message: '请输入端口号', trigger: 'blur' }],
        userName: [
          { required: false, message: '请输入用户名', trigger: 'blur' }
        ],
        password: [{ required: false, message: '请输入密码', trigger: 'blur' }]
      },
      //查询到的信息对象
      editForm: {},
      //编辑表单的验证规则对象
      editFormRules: {
        town: [{ required: true, message: '请输入地点', trigger: 'blur' }],
        // village: [
        //   { required: true, message: '请输入编码', trigger: 'blur' },
        //   { min: 8, max: 8, message: '编码必须为8位', trigger: 'blur' }
        // ],
        name: [{ required: true, message: '请输入名称', trigger: 'blur' }],
        type: [{ required: true, message: '请输入类型', trigger: 'blur' }],
        ip: [{ required: false, message: '请输入IP地址', trigger: 'blur' }],
        port: [{ required: false, message: '请输入端口号', trigger: 'blur' }],
        userName: [
          { required: false, message: '请输入用户名', trigger: 'blur' }
        ],
        password: [{ required: false, message: '请输入密码', trigger: 'blur' }]
      }
    }
  },
  methods: {
    //获取数据列表
    async getVCList() {
      const { data: res } = await this.$http.get('/video-configuration/list', {
        params: {
          currentPage: this.queryinfo.currentPage,
          pageSize: this.queryinfo.pageSize,
          town: this.town,
          village: this.village
        }
      })
      if (res.code !== 200) {
        return this.$message.error('获取数据列表失败')
      }
      this.VCList = res.data.records
      this.total = res.data.total
    },
    //获取编辑数据对话框
    async showEditDialog(id) {
      const { data: res } = await this.$http.get(
        '/video-configuration/info/' + id
      )
      if (res.code !== 200) {
        return this.$message.error('查询数据信息失败')
      }
      this.editForm = res.data
      this.editDialogVisible = true
    },
    //监听pageSize
    handleSizeChange(newSize) {
      this.queryinfo.pageSize = newSize
      this.getVCList()
    },
    //监听currentPage
    handleCurrentChange(newPage) {
      this.queryinfo.currentPage = newPage
      this.getVCList()
    },
    //序号连续
    table_index(index) {
      return (
        (this.queryinfo.currentPage - 1) * this.queryinfo.pageSize + index + 1
      )
    },
    //监听新增对话框关闭重置事件
    addDialogClosed() {
      this.$refs.addFormRef.resetFields()
    },
    //监听编辑对话框关闭重置事件
    editDialogClosed() {
      this.$refs.editFormRef.resetFields()
    },
    //新增角色
    addVC() {
      this.$refs.addFormRef.validate(async valid => {
        if (!valid) return
        //发起网络请求
        const { data: res } = await this.$http.post(
          '/video-configuration/save',
          this.addForm
        )
        if (res.code !== 200) {
          this.$message.error('添加失败')
        }
        this.$message.success('添加成功')
        this.addDialogVisible = false
        this.getVCList()
      })
    },
    //编辑角色信息表单
    editVC() {
      this.$refs.editFormRef.validate(async valid => {
        if (!valid) return
        const { data: res } = await this.$http.post(
          '/video-configuration/update',
          {
            id: this.editForm.id,
            name: this.editForm.name,
            type: this.editForm.type,
            ip: this.editForm.ip,
            port: this.editForm.port,
            userName: this.editForm.userName,
            password: this.editForm.password,
            town: this.editForm.town,
            village: this.editForm.village
          }
        )
        if (res.code !== 200) {
          return this.$message.error('更新数据失败')
        }
        this.$message.success('更新数据成功')
        this.editDialogVisible = false
        this.getVCList()
      })
    },
    //删除
    async removeById(id) {
      const confirmResult = await this.$confirm(
        '此操作将永久删除该数据, 是否继续?',
        '提示',
        {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }
      ).catch(err => err)
      //如果确认删除，返回值为字符串confirm,如果取消删除，则返回值为cancel
      if (confirmResult !== 'confirm') return
      const { data: res } = await this.$http.post(
        '/video-configuration/delete/' + id
      )
      if (res.code !== 200) {
        return this.$message.error('删除失败')
      }
      this.$message.success('删除成功')
      this.getVCList()
    },
    //搜索
    async searchInputType() {
      if (this.name == null) {
        this.name = ''
      }
      if (this.type == null) {
        this.type = ''
      }
      const { data: res } = await this.$http.get(
        '/video-configuration/type-name-list',
        {
          params: {
            name: this.name,
            currentPage: this.queryinfo.currentPage,
            pageSize: this.queryinfo.pageSize,
            type: this.type
          }
        }
      )
      if (res.code !== 200) {
        return this.$message.error('搜索数据错误')
      }
      this.VCList = res.data.records
      this.total = res.data.total
    },
    //获取监控设备的所有类型
    async getTypeList() {
      const { data: res } = await this.$http.get('/dict/kinds', {
        params: {
          name: '摄像头类型'
        }
      })
      if (res.code !== 200) {
        return this.$message.error('获取数据列表失败')
      }
      this.types = res.data
      this.typeList = this.types.map(name => ({
        value: name,
        label: name
      }))
    },
    //获取所有乡镇以及乡镇下面的所有村子
    async getTownList() {
      const { data: res } = await this.$http.get('/dict/kinds', {
        params: {
          name: '摄像头地点'
        }
      });
      if (res.code !== 200) {
        return this.$message.error('获取数据列表失败')
      }
      console.log(res.data)
      this.towns = res.data;
      this.townList = this.towns.map(town => ({
        value: town,
        label: town
      }))
    },
    //获取所有乡镇以及乡镇下面的所有村子
    async getNameList() {
      const { data: res } = await this.$http.get('/video-configuration/getNames');
      if (res.code !== 200) {
        return this.$message.error('获取数据列表失败')
      }
      console.log(res.data)
      this.names = res.data;
      this.nameList = this.names.map(name => ({
        value: name,
        label: name
      }))
    },
    //添加弹窗 下拉框联动控制
    addselectTownVillage() {
      //在第一个下拉框不为空时
      if (this.addForm.town !== '') {
        //联动更新第二个下拉框内容
        this.villageList = this.townList.filter((item) => item.value === this.addForm.town)[0].village
        // const findlevel = (item) => item.lable == this.addForm.village
        // if (this.addForm.village !== '' && !this.villageList.some(findlevel)) {
        //   this.addForm.village = ''
        // }
      }
      else {
        this.addForm.village = ''
        this.villageList = []
      }
    },
    //添加弹窗 下拉框联动控制
    editselectTownVillage() {
      //在第一个下拉框不为空时
      if (this.editForm.town !== '') {
        //联动更新第二个下拉框内容
        this.villageList = this.townList.filter((item) => item.value === this.editForm.town)[0].village
        // const findlevel = (item) => item.lable == this.editForm.village
        // if (this.editForm.village !== '' && !this.villageList.some(findlevel)) {
        //   this.editForm.village = ''
        // }
      }
      else {
        this.editForm.village = ''
        this.villageList = []
      }
    }
  },
  created() {
    this.getVCList()
    this.getTypeList()
    this.getTownList()
    this.getNameList()
  },
  mounted() { }
}
</script>

<style lang="less" scoped>
#div1 {
  height: 100%;
  width: 100%;
}

#bread {
  height: 3%;
  width: 100%;
}

// #main {
//   height: 94.5%;
//   width: 99%;
//   display: flex;
//   margin: 10px;
//   border: 1px solid rgb(212, 212, 212);
//   box-shadow: 0 2px 4px rgba(0, 0, 0, 0.12), 0 0 6px rgba(0, 0, 0, 0.04);
//   background-color: rgb(255, 255, 255);
// }

#main-body {
  height: 94.5%;
  width: 100%;
}

#main-body-header {
  height: 7%;
  width: 100%;
  background-color: rgb(253, 242, 228);
  margin-top: 10px;
  display: flex;
}

#main-body-main {
  height: 89%;
  width: 100%;
}

#main-body-footer {
  height: 4%;
  width: 100%;
  background-color: rgb(245, 237, 230);
  text-align: center;
}
</style>
