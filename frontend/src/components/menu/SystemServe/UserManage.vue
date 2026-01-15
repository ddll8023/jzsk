<template>
  <div id="div1">
    <div id="bread">
      <el-breadcrumb separator-class="el-icon-arrow-right" style="padding-top:5px; padding-left:10px">
        <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
        <el-breadcrumb-item>系统管理</el-breadcrumb-item>
        <el-breadcrumb-item :to="{ path: '/home/usermanage' }">用户管理</el-breadcrumb-item>
      </el-breadcrumb>
    </div>

    <div id="div-body">
      <div id="div-body-main">
        <div id="div-body-main-header">
          <div style="align-self:center;margin-left:10px">
            <el-input placeholder="请输入姓名搜索" v-model="input" class="input-with-select" clearable @clear="getUserList">
              <el-button slot="append" icon="el-icon-search" @click="searchUser"></el-button>
            </el-input>
          </div>

          <el-button type="primary" round icon="el-icon-plus"
            style="margin-left:auto; align-self:center;margin-right:10px"
            @click="addDialogVisible = true">新增</el-button>
        </div>

        <div id="div-body-main-body">
          <el-table :data="UserList" border stripe height="100%" style="width: 100%"
            :header-cell-style="{ background: '#cfe2f3', color: '#606266' }">
            <el-table-column type="index" label="序号" width="50px" align="center" :index="table_index"></el-table-column>
            <el-table-column prop="username" label="用户名" width="150" align="center">
            </el-table-column>
            <el-table-column prop="roles[0].name" label="角色类型" width="150" align="center">
            </el-table-column>
            <el-table-column prop="name" label="姓名" width="80" align="center"></el-table-column>
            <el-table-column prop="technicalTitle" label="技术职称" width="100" align="center">
            </el-table-column>
            <el-table-column prop="gender" label="性别" width="50" align="center">
            </el-table-column>
            <el-table-column prop="politicalAppearance" label="政治面貌" width="100" align="center">
            </el-table-column>

            <el-table-column prop="idNumber" label="身份证号" width="200" align="center">
            </el-table-column>
            <el-table-column prop="phoneNumber" label="手机号码" width="120" align="center">
            </el-table-column>
            <el-table-column prop="email" label="电子邮件" width="200" align="center">
            </el-table-column>
            <el-table-column prop="address" label="家庭住址" width="200" align="center">
            </el-table-column>

            <el-table-column prop="workingTime" label="开始工作时间" width="150" align="center">
            </el-table-column>

            <el-table-column prop="birthday" label="出生年月" width="120" align="center">
            </el-table-column>
            <el-table-column prop="academicQualifications" label="学历" width="100" align="center">
            </el-table-column>

            <el-table-column prop="graduationInstitution" label="毕业院校" width="120" align="center">
            </el-table-column>
            <el-table-column prop="major" label="专业" width="100" align="center">
            </el-table-column>
            <el-table-column prop="note" label="备注" width="120" align="center">
            </el-table-column>

            <el-table-column fixed="right" label="操作" width="400" align="center">
              <template slot-scope="scope">
                <el-button size="mini" @click="showEditDialog(scope.row.id)">编辑</el-button>
                <el-button size="mini" type="danger" @click="removeById(scope.row.id)">删除</el-button>
                <el-button size="mini" style="background-color: #409EFF; color: white;" @click="initpassword(scope.row.id)">初始化密码</el-button>
                <el-button size="mini" type="success" @click="showAllocateRoleDialog(scope.row.id)">分配角色</el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>

        <div id="div-body-main-footer">
          <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
            :current-page="queryinfo.currentPage" :page-sizes="[10, 20, 30, 40]" :page-size="queryinfo.pageSize"
            layout="total, sizes, prev, pager, next, jumper" :total="total">
          </el-pagination>
        </div>
      </div>
    </div>

    <!-- 分配角色对话框 -->
    <el-dialog title="分配角色" :visible.sync="allocateDialogVisible" width="40%" @close="allocateDialogClosed" center
      :close-on-press-escape="false">
      <el-form :model="allocateForm" ref="allocateFormRef" label-width="80px" size="small">
        <el-row :gutter="20" class="centered-row">
          <el-col :span="12" class="centered-col">
            <el-form-item label="用户名:" prop="username">
              <el-input disabled v-model="allocateForm.username"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12" class="centered-col">
            <el-form-item label="角色类型:" prop="roles">
              <el-select v-model="allocateForm.roles" clearable placeholder="请选择" style="align-self:center;">
                <el-option v-for="item in CharacterList" :key="item.id" :label="item.name" :value="item.id">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>

      <!-- 底部按钮 -->
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="allocateUser">确 定</el-button>
        <el-button @click="allocateDialogVisible = false">取 消</el-button>
      </span>

    </el-dialog>

    <!-- 新增对话框 -->
    <el-dialog title="新增用户" :visible.sync="addDialogVisible" width="40%" @close="addDialogClosed" center
      :close-on-press-escape="false">
      <el-form :model="addForm" :rules="addFormRules" ref="addFormRef" label-width="150px" size="small">
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="用户名:" prop="username">
              <el-input v-model="addForm.username"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="姓名:" prop="name">
              <el-input v-model="addForm.name"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="技术职称:" prop="technicalTitle">
              <el-input v-model="addForm.technicalTitle"></el-input>
            </el-form-item>
          </el-col>

        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="性别:" prop="gender">
              <el-select v-model="addForm.gender" clearable placeholder="请选择" style="align-self:center;">
                <el-option label="男" value="男"></el-option>
                <el-option label="女" value="女"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="政治面貌:" prop="politicalAppearance">
              <el-select v-model="addForm.politicalAppearance" clearable placeholder="请选择" style="align-self:center;">
                <el-option label="中共党员" value="中共党员"></el-option>
                <el-option label="中共预备党员" value="中共预备党员"></el-option>
                <el-option label="共青团员" value="共青团员"></el-option>
                <el-option label="群众" value="群众"></el-option>
                <el-option label="民革党员" value="民革党员"></el-option>
                <el-option label="民盟盟员" value="民盟盟员"></el-option>
                <el-option label="民建会员" value="民建会员"></el-option>
                <el-option label="民进会员" value="民进会员"></el-option>
                <el-option label="农工党党员" value="农工党党员"></el-option>
                <el-option label="致公党党员" value="致公党党员"></el-option>
                <el-option label="九三学社社员" value="九三学社社员"></el-option>
                <el-option label="台盟盟员" value="台盟盟员"></el-option>
                <el-option label="无党派人士" value="无党派人士"></el-option>
              </el-select>
            </el-form-item>
          </el-col>

        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="身份证号:" prop="idNumber">
              <el-input v-model="addForm.idNumber"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="手机号码:" prop="phoneNumber">
              <el-input v-model="addForm.phoneNumber"></el-input>
            </el-form-item>
          </el-col>

        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="电子邮件:" prop="email">
              <el-input v-model="addForm.email"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="家庭住址:" prop="address">
              <el-input v-model="addForm.address"></el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="开始工作时间:" prop="workingTime">
              <el-date-picker v-model="addForm.workingTime" type="date" placeholder="选择日期" value-format="yyyy-MM-dd"
                style="width:100%">
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="出生年月:" prop="birthday">
              <el-date-picker v-model="addForm.birthday" type="month" placeholder="选择日期" value-format="yyyy-MM"
                style="width:100%">
              </el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="学历:" prop="academicQualifications">
              <el-select v-model="addForm.academicQualifications" clearable placeholder="请选择"
                style="align-self:center;">
                <el-option label="博士研究生" value="博士研究生"></el-option>
                <el-option label="硕士研究生" value="硕士研究生"></el-option>
                <el-option label="本科" value="本科"></el-option>
                <el-option label="专科" value="专科"></el-option>
                <el-option label="中专/高中" value="中专/高中"></el-option>
                <el-option label="初中" value="初中"></el-option>
                <el-option label="小学" value="小学"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="毕业院校:" prop="graduationInstitution">
              <el-input v-model="addForm.graduationInstitution"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="专业:" prop="major">
              <el-input v-model="addForm.major"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="备注:" prop="note">
              <el-input type="textarea" v-model="addForm.note"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <!-- 底部按钮 -->
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="addUser">确 定</el-button>
        <el-button @click="addDialogVisible = false">取 消</el-button>
      </span>
    </el-dialog>

    <!-- 编辑对话框 -->
    <el-dialog title="编辑用户信息" :visible.sync="editDialogVisible" width="40%" @close="editDialogClosed" center
      :close-on-press-escape="false">
      <el-form :model="editForm" :rules="editFormRules" ref="editFormRef" label-width="150px" class="demo-ruleForm"
        size="small ">
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="用户名:" prop="username">
              <el-input v-model="editForm.username"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="姓名:" prop="name">
              <el-input v-model="editForm.name"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="技术职称:" prop="technicalTitle">
              <el-input v-model="editForm.technicalTitle"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="性别:" prop="gender">
              <el-select v-model="editForm.gender" clearable placeholder="请选择" style="align-self:center;">
                <el-option label="男" value="男"></el-option>
                <el-option label="女" value="女"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="政治面貌:" prop="politicalAppearance">
              <el-select v-model="editForm.politicalAppearance" clearable placeholder="请选择" style="align-self:center;">
                <el-option label="中共党员" value="中共党员"></el-option>
                <el-option label="中共预备党员" value="中共预备党员"></el-option>
                <el-option label="共青团员" value="共青团员"></el-option>
                <el-option label="群众" value="群众"></el-option>
                <el-option label="民革党员" value="民革党员"></el-option>
                <el-option label="民盟盟员" value="民盟盟员"></el-option>
                <el-option label="民建会员" value="民建会员"></el-option>
                <el-option label="民进会员" value="民进会员"></el-option>
                <el-option label="农工党党员" value="农工党党员"></el-option>
                <el-option label="致公党党员" value="致公党党员"></el-option>
                <el-option label="九三学社社员" value="九三学社社员"></el-option>
                <el-option label="台盟盟员" value="台盟盟员"></el-option>
                <el-option label="无党派人士" value="无党派人士"></el-option>
              </el-select>
            </el-form-item>
          </el-col>

        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="身份证号:" prop="idNumber">
              <el-input v-model="editForm.idNumber"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="手机号码:" prop="phoneNumber">
              <el-input v-model="editForm.phoneNumber"></el-input>
            </el-form-item>
          </el-col>

        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="电子邮件:" prop="email">
              <el-input v-model="editForm.email"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="家庭住址:" prop="address">
              <el-input v-model="editForm.address"></el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="开始工作时间:" prop="workingTime">
              <el-date-picker v-model="editForm.workingTime" type="date" placeholder="选择日期" value-format="yyyy-MM-dd"
                style="width:100%">
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="出生年月:" prop="birthday">
              <el-date-picker v-model="editForm.birthday" type="month" placeholder="选择日期" value-format="yyyy-MM"
                style="width:100%">
              </el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="学历:" prop="academicQualifications">
              <el-select v-model="editForm.academicQualifications" clearable placeholder="请选择"
                style="align-self:center;">
                <el-option label="博士研究生" value="博士研究生"></el-option>
                <el-option label="硕士研究生" value="硕士研究生"></el-option>
                <el-option label="本科" value="本科"></el-option>
                <el-option label="专科" value="专科"></el-option>
                <el-option label="中专/高中" value="中专/高中"></el-option>
                <el-option label="初中" value="初中"></el-option>
                <el-option label="小学" value="小学"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="毕业院校:" prop="graduationInstitution">
              <el-input v-model="editForm.graduationInstitution"></el-input>
            </el-form-item>
          </el-col>


        </el-row>
        <el-row :gutter="20">

          <el-col :span="12">
            <el-form-item label="专业:" prop="major">
              <el-input v-model="editForm.major"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="备注:" prop="note">
              <el-input type="textarea" v-model="editForm.note"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <!-- 底部按钮 -->
      <span slot="footer">
        <el-button type="primary" @click="editUser">确 定</el-button>
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
      input: '',
      //获取表格的参数对象
      queryinfo: {
        //当前页数
        currentPage: 1,
        //当前每页显示条数
        pageSize: 10
      },
      //数据列表
      UserList: [],
      //角色类型列表
      CharacterList: [],
      total: 0,
      //控制分配角色对话框
      allocateDialogVisible: false,
      //控制新增对话框
      addDialogVisible: false,
      //控制编辑对话框
      editDialogVisible: false,
      //分配角色表单数据
      allocateForm: {
        id: '',
        username: '',
        roles: ''
      },
      //添加表单数据
      addForm: {
        username: '',
        major: '',
        name: '',
        email: '',
        idNumber: '',
        address: '',
        gender: '',
        workingTime: '',
        technicalTitle: '',
        birthday: '',
        academicQualifications: '',
        politicalAppearance: '',
        graduationInstitution: '',
        note: '',
        phoneNumber: ''
      },
      //添加表单的验证规则对象
      addFormRules: {
        username: [
          { required: true, message: '请输入用户名', trigger: 'blur' }
        ],
        major: [
          // {required:true,message:'请输入专业',trigger:'blur'}
        ],
        name: [
          { required: true, message: '请输入姓名', trigger: 'blur' }
        ],
        email: [
          // {required:true,message:'请输入电子邮件',trigger:'blur'},
          { pattern: /^[\w.-]+@[a-zA-Z\d.-]+\.[a-zA-Z]{2,}$/, message: "请输入正确的邮箱格式", trigger: 'blur' }
        ],
        idNumber: [
          // {required:true,message:'请输入身份证号',trigger:'blur'},
          { pattern: /(^[1-9]\d{5}(18|19|([23]\d))\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\d{3}[0-9Xx]$)|(^[1-9]\d{5}\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\d{2}[0-9Xx]$)/, message: '请输入正确的身份证号', trigger: 'blur' }
        ],
        address: [
          // {required:true,message:'请输入家庭住址',trigger:'blur'}
        ],
        gender: [
          // {required:true,message:'请输入性别',trigger:'blur'}
        ],
        workingTime: [
          // {required:true,message:'请输入开始工作时间',trigger:'blur'}
        ],
        technicalTitle: [
          // {required:true,message:'请输入技术职称',trigger:'blur'}
        ],
        birthday: [
          // {required:true,message:'请输入出生年月',trigger:'blur'}
        ],
        academicQualifications: [
          // {required:true,message:'请输入学历',trigger:'blur'}
        ],
        politicalAppearance: [
          // {required:true,message:'请输入政治面貌',trigger:'blur'}
        ],
        graduationInstitution: [
          // {required:true,message:'请输入毕业院校',trigger:'blur'}
        ],
        note: [
          // {required:true,message:'请输入备注',trigger:'blur'}
        ],
        phoneNumber: [
          // {required:true,message:'请输入手机号码',trigger:'blur'},
          { pattern: /^1(3[0-9]|5[0-3,5-9]|7[1-3,5-8]|8[0-9])\d{8}$/, message: '请输入正确的手机号码', trigger: 'blur' }
        ],
      },
      //查询到的信息对象
      editForm: {},
      //编辑表单的验证规则对象
      editFormRules: {
        username: [
          { required: true, message: '请输入用户名', trigger: 'blur' }
        ],
        major: [
          // {required:true,message:'请输入专业',trigger:'blur'}
        ],
        name: [
          { required: true, message: '请输入姓名', trigger: 'blur' }
        ],
        email: [
          // {required:true,message:'请输入电子邮件',trigger:'blur'},
          { pattern: /^[\w.-]+@[a-zA-Z\d.-]+\.[a-zA-Z]{2,}$/, message: "请输入正确的邮箱格式", trigger: 'blur' }
        ],
        idNumber: [
          // {required:true,message:'请输入身份证号',trigger:'blur'},
          { pattern: /(^[1-9]\d{5}(18|19|([23]\d))\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\d{3}[0-9Xx]$)|(^[1-9]\d{5}\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\d{2}[0-9Xx]$)/, message: '请输入正确的身份证号', trigger: 'blur' }
        ],
        address: [
          // {required:true,message:'请输入家庭住址',trigger:'blur'}
        ],
        gender: [
          // {required:true,message:'请输入性别',trigger:'blur'}
        ],
        workingTime: [
          // {required:true,message:'请输入开始工作时间',trigger:'blur'}
        ],
        technicalTitle: [
          // {required:true,message:'请输入技术职称',trigger:'blur'}
        ],
        birthday: [
          // {required:true,message:'请输入出生年月',trigger:'blur'}
        ],
        academicQualifications: [
          // {required:true,message:'请输入学历',trigger:'blur'}
        ],
        politicalAppearance: [
          // {required:true,message:'请输入政治面貌',trigger:'blur'}
        ],
        graduationInstitution: [
          // {required:true,message:'请输入毕业院校',trigger:'blur'}
        ],
        note: [
          // {required:true,message:'请输入备注',trigger:'blur'}
        ],
        phoneNumber: [
          // {required:true,message:'请输入手机号码',trigger:'blur'},
          { pattern: /^1(3[0-9]|5[0-3,5-9]|7[1-3,5-8]|8[0-9])\d{8}$/, message: '请输入正确的手机号码', trigger: 'blur' }
        ],
      }
    }
  },
  methods: {
    //获取数据列表
    async getUserList() {
      const { data: res } = await this.$http.get('/user/list', {
        params: this.queryinfo
      })
      if (res.code !== 200) {
        return this.$message.error('获取数据列表失败')
      }
      this.UserList = res.data.records
      this.total = res.data.total
    },
    //获取角色类型列表
    async getCharacterList() {
      const { data: res } = await this.$http.get('/role/list', {
        params: this.queryinfo
      })
      if (res.code !== 200) {
        return this.$message.error('获取数据列表失败')
      }
      this.CharacterList = res.data.records
    },
    //获取编辑数据对话框
    async showEditDialog(id) {
      const { data: res } = await this.$http.get('/user/info/' + id)
      if (res.code !== 200) {
        return this.$message.error('查询数据信息失败')
      }
      this.editForm = res.data
      this.editDialogVisible = true
    },
    //获取分配角色对话框
    async showAllocateRoleDialog(id) {
      const { data: res } = await this.$http.get('/user/info/' + id)
      if (res.code !== 200) {
        return this.$message.error('查询数据信息失败')
      }
      this.allocateForm.id = res.data.id
      this.allocateForm.username = res.data.username
      this.allocateForm.roles = res.data.roles && res.data.roles.length > 0 ? res.data.roles[0].id : '';
      console.log(res)
      this.allocateDialogVisible = true
    },
    //监听pageSize
    handleSizeChange(newSize) {
      this.queryinfo.pageSize = newSize
      this.getUserList()
    },
    //监听currentPage
    handleCurrentChange(newPage) {
      this.queryinfo.currentPage = newPage
      this.getUserList()
    },
    //序号连续
    table_index(index) {
      return (this.queryinfo.currentPage - 1) * this.queryinfo.pageSize + index + 1
    },
    //监听分配角色对话框关闭重置事件
    allocateDialogClosed() {
      this.$refs.allocateFormRef.resetFields()
    },
    //监听新增对话框关闭重置事件
    addDialogClosed() {
      this.$refs.addFormRef.resetFields()
    },
    //监听编辑对话框关闭重置事件
    editDialogClosed() {
      this.$refs.editFormRef.resetFields()
    },
    // 初始化密码
    async initpassword(id) {
      const confirmResult = await this.$confirm(
        '此操作将初始化该用户的密码为123456, 是否继续?',
        '提示',
        {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }
      ).catch(err => err)
      if (confirmResult !== 'confirm') return
      try {
        const { data: res } = await this.$http.post(`/user/repass?id=${id}`);
        if (res.code !== 200) {
          this.$message.error('初始化密码失败');
        } else {
          this.$message.success('初始化密码成功');
        }
      } catch (error) {
        console.error(error);
        this.$message.error('请求失败');
      }
    },
    //分配角色
    allocateUser() {
      this.$refs.allocateFormRef.validate(async valid => {
        if (!valid) return
        //发起网络请求
        const { data: res } = await this.$http.post('/user/role/' + this.allocateForm.id, [this.allocateForm.roles])
        if (res.code !== 200) {
          this.$message.error('分配角色失败')
        }
        this.$message.success('分配角色成功')
        this.allocateDialogVisible = false
        this.getUserList()
      })
    },
    //新增角色
    addUser() {
      this.$refs.addFormRef.validate(async valid => {
        if (!valid) return
        //发起网络请求
        try {
          const { data: res } = await this.$http.post('/user/save', this.addForm)
          if (res.code !== 200) {
            this.$message.error('添加失败')
          }
          this.$message.success('添加成功')
          this.addDialogVisible = false
          this.getUserList()
        } catch (error) {
          if (error.response) {
            this.$message.error(error.response.data.message)
          }
        }

      })
    },
    //编辑角色信息表单
    editUser() {
      this.$refs.editFormRef.validate(async valid => {
        if (!valid) return
        const { data: res } = await this.$http.post('/user/update', {
          id: this.editForm.id,
          username: this.editForm.username,
          roles: this.editForm.roles,
          major: this.editForm.major,
          name: this.editForm.name,
          email: this.editForm.email,
          idNumber: this.editForm.idNumber,
          address: this.editForm.address,
          gender: this.editForm.gender,
          workingTime: this.editForm.workingTime,
          technicalTitle: this.editForm.technicalTitle,
          birthday: this.editForm.birthday,
          academicQualifications: this.editForm.academicQualifications,
          politicalAppearance: this.editForm.politicalAppearance,
          graduationInstitution: this.editForm.graduationInstitution,
          note: this.editForm.note,
          phoneNumber: this.editForm.phoneNumber
        })
        if (res.code !== 200) {
          return this.$message.error('更新数据失败')
        }
        this.$message.success('更新数据成功')
        this.editDialogVisible = false
        this.getUserList()
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
      const { data: res } = await this.$http.post('/user/delete/' + id)
      if (res.code !== 200) {
        return this.$message.error('删除失败')
      }
      this.$message.success('删除成功')
      this.getUserList()
    },
    //搜索
    async searchUser() {
      const { data: res } = await this.$http.get('/user/search-list', {
        params: {
          name: this.input,
          currentPage: this.queryinfo.currentPage,
          pageSize: this.queryinfo.pageSize
        }
      })
      if (res.code !== 200) {
        return this.$message.error('搜索数据错误')
      }
      this.UserList = res.data.records
      this.total = res.data.total
    }
  },
  created() {
    this.getUserList()
    this.getCharacterList()
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
  height: 25px;
  width: 100%;
}

#div-body {
  height: calc(100% - 25px);
  width: 100%;
  display: flex;
}

#div-body-aside {
  height: 100%;
  width: 11.5%;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.12), 0 0 6px rgba(0, 0, 0, 0.04);
  background-color: rgb(249, 249, 249);
}

#div-body-main {
  height: 100%;
  width: 100%;
}

#div-body-main-header {
  height: 60px;
  width: 100%;
  margin-top: 0px;
  display: flex;
  background-color: rgb(253, 242, 228);
}

#div-body-main-body {
  height: calc(100% - 95px);
  width: 100%;
}

#div-body-main-footer {
  height: 35px;
  width: 100%;
  float: left;
  text-align: center;
  background-color: rgb(245, 237, 230);
}

.centered-row {
  display: flex;
  justify-content: space-between;
  width: 100%;
}

/* 使 .centered-col 内的内容水平居中 */
.centered-col {
  display: flex;
  flex-direction: column;
  /* 根据需要可以改变为行方向 */
  align-items: center;
  /* 垂直居中 */
  justify-content: center;
  /* 水平居中 */
  text-align: left;
}
</style>
