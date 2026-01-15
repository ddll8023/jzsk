<template>
  <div id="div1">
    <div id="div-header">
      <div style="align-self:center;margin-left:10px">
        <el-input placeholder="请输入组织机构名称搜索" v-model="inputWord" class="input-with-select" clearable
          @clear="getOrganizazitonList">
          <el-button slot="append" icon="el-icon-search" @click="getOrganizazitonList"></el-button>
        </el-input>
      </div>

      <el-button type="primary" round icon="el-icon-plus"
        style="margin-left:auto; align-self: center;margin-right:10px;" @click="addDialogVisible = true">新增</el-button>

    </div>

    <div id="div-main">
      <el-table :data="OrganizationList" border stripe height="100%" style="width: 100%"
        :header-cell-style="{ background: '#cfe2f3', color: '#606266' }">
        <el-table-column fixed="left" prop="organizationName" label="组织机构名称" width="150" align="center">
        </el-table-column>
        <el-table-column prop="organizationCode" label="组织机构代码" width="150" align="center">
        </el-table-column><el-table-column prop="administrativeName" label="行政区划名称" width="120" align="center">
        </el-table-column><el-table-column prop="organizationAbbr" label="机构简称" width="100" align="center">
        </el-table-column><el-table-column prop="legalRepresentative" label="法人代表" width="100" align="center">
        </el-table-column>
        <el-table-column prop="agencySpecifications" label="机构规格" width="100" align="center">
        </el-table-column>
        <el-table-column prop="subordinateRelations" label="隶属关系" width="100" align="center">
        </el-table-column>
        <el-table-column prop="institutionalType" label="机构类型" width="150" align="center">
        </el-table-column>
        <el-table-column prop="mainFunction" label="主要职能" width="120" align="center">
        </el-table-column>
        <el-table-column prop="approveContent" label="主要审批内容" width="180" align="center">
        </el-table-column>
        <el-table-column prop="website" label="网站" width="180" align="center">
        </el-table-column>
        <el-table-column prop="email" label="邮箱" width="180" align="center">
        </el-table-column>
        <el-table-column prop="address" label="地址" width="250" align="center">
        </el-table-column>
        <el-table-column prop="postalCode" label="邮政编码" width="100" align="center">
        </el-table-column>
        <el-table-column prop="officeTelephone" label="办公室电话" width="120" align="center">
        </el-table-column>
        <el-table-column prop="fax" label="传真" width="100" align="center">
        </el-table-column>
        <el-table-column prop="staffSize" label="编制人数" width="80" align="center">
        </el-table-column>
        <el-table-column prop="whetherReform" label="是否施行水务改革" width="100" align="center">
        </el-table-column>
        <!-- 操作列 -->
        <el-table-column fixed="right" label="操作" width="200" align="center">
          <template slot-scope="scope">
            <el-button size="mini" @click="showEditDialog(scope.row.id)">
              编辑</el-button>
            <el-button size="mini" type="danger" @click="removeById(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <div id="div-footer">
      <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
        :current-page="queryinfo.currentPage" :page-sizes="[10, 20, 30, 40]" :page-size="queryinfo.pageSize"
        layout="total, sizes, prev, pager, next, jumper" :total="total">
      </el-pagination>
    </div>

    <!-- 新增对话框 -->
    <el-dialog title="新增组织机构" :visible.sync="addDialogVisible" width="800px" @close="addDialogClosed" center
      :close-on-press-escape="false" :close-on-click-modal="false">
      <el-form :model="addForm" :rules="addFormRules" ref="addFormRef" label-width="150px" size="small">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="组织机构名称:" prop="organizationName">
              <el-input v-model="addForm.organizationName"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="组织机构代码:" prop="organizationCode">
              <el-input v-model="addForm.organizationCode"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="行政区划名称:" prop="administrativeName">
              <el-input v-model="addForm.administrativeName"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="机构简称:" prop="organizationAbbr">
              <el-input v-model="addForm.organizationAbbr"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="法人代表:" prop="legalRepresentative">
              <el-input v-model="addForm.legalRepresentative"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="机构规格:" prop="agencySpecifications">
              <el-input v-model="addForm.agencySpecifications"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="隶属关系:" prop="subordinateRelations">
              <el-input v-model="addForm.subordinateRelations"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="机构类型:" prop="institutionalType">
              <el-input v-model="addForm.institutionalType"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="主要职能:" prop="mainFunction">
              <el-input v-model="addForm.mainFunction"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="主要审批内容:" prop="approveContent">
              <el-input v-model="addForm.approveContent"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="网站:" prop="website">
              <el-input v-model="addForm.website"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="邮箱:" prop="email">
              <el-input v-model="addForm.email"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="地址:" prop="address">
              <el-input v-model="addForm.address" autosize type="textarea"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="邮政编码:" prop="postalCode">
              <el-input v-model="addForm.postalCode"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="办公室电话:" prop="officeTelephone">
              <el-input v-model="addForm.officeTelephone"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="传真:" prop="fax">
              <el-input v-model="addForm.fax"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="编制人数:" prop="staffSize">
              <el-input v-model="addForm.staffSize"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="是否施行水务改革:" prop="whetherReform">
              <el-select v-model="addForm.whetherReform" placeholder="请选择是否施行水务改革">
                <el-option label="是" value="是"></el-option>
                <el-option label="否" value="否"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <!-- 底部按钮 -->
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="addAdministrativeDivision">确 定</el-button>
        <el-button @click="addDialogVisible = false">取 消</el-button>
      </span>
    </el-dialog>

    <!-- 编辑对话框 -->
    <el-dialog title="编辑组织信息" :visible.sync="editDialogVisible" width="800px  " @close="editDialogClosed" center
      :close-on-press-escape="false" :close-on-click-modal="false">
      <el-form :model="editForm" :rules="editFormRules" ref="editFormRef" label-width="150px" class="demo-ruleForm"
        size="small ">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="组织机构名称:" prop="organizationName">
              <el-input v-model="editForm.organizationName"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="组织机构代码:" prop="organizationCode">
              <el-input v-model="editForm.organizationCode"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="行政区划名称:" prop="administrativeName">
              <el-input v-model="editForm.administrativeName"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="机构简称:" prop="organizationAbbr">
              <el-input v-model="editForm.organizationAbbr"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="法人代表:" prop="legalRepresentative">
              <el-input v-model="editForm.legalRepresentative"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="机构规格:" prop="agencySpecifications">
              <el-input v-model="editForm.agencySpecifications"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="隶属关系:" prop="subordinateRelations">
              <el-input v-model="editForm.subordinateRelations"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="机构类型:" prop="institutionalType">
              <el-input v-model="editForm.institutionalType"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="主要职能:" prop="mainFunction">
              <el-input v-model="editForm.mainFunction"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="主要审批内容:" prop="approveContent">
              <el-input v-model="editForm.approveContent"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="网站:" prop="website">
              <el-input v-model="editForm.website"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="邮箱:" prop="email">
              <el-input v-model="editForm.email"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="地址:" prop="address">
              <el-input v-model="editForm.address" autosize type="textarea"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="邮政编码:" prop="postalCode">
              <el-input v-model="editForm.postalCode"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="办公室电话:" prop="officeTelephone">
              <el-input v-model="editForm.officeTelephone"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="传真:" prop="fax">
              <el-input v-model="editForm.fax"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="编制人数:" prop="staffSize">
              <el-input v-model="editForm.staffSize"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="是否施行水务改革:" prop="whetherReform">
              <el-select v-model="editForm.whetherReform" placeholder="请选择是否施行水务改革">
                <el-option label="是" value="是"></el-option>
                <el-option label="否" value="否"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <!-- 底部按钮 -->
      <span slot="footer">
        <el-button type="primary" @click="editAdministrativeDivision">确 定</el-button>
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
      //搜索词
      inputWord: '',
      //获取列表的参数对象
      queryinfo: {
        //当前页数
        currentPage: 1,
        //当前每页显示条数
        pageSize: 10
      },
      //数据列表
      OrganizationList: [],
      total: 0,
      //控制新增对话框
      addDialogVisible: false,
      //控制编辑对话框
      editDialogVisible: false,
      //添加表单数据
      addForm: {
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
      },
      //添加表单的验证规则对象
      addFormRules: {
        organizationName: [
          { required: true, message: '请输入组织机构名称', trigger: 'blur' }
        ],
        organizationCode: [
          { required: false, message: '请输入组织机构代码', trigger: 'blur' }
        ],
        administrativeName: [
          { required: false, message: '请输入行政区划名称', trigger: 'blur'}
        ],
        organizationAbbr: [
          { required: false, message: '请输入机构简称', trigger: 'blur' }
        ],
        legalRepresentative: [
          { required: false, message: '请输入法人代表', trigger: 'blur' }
        ],
        agencySpecifications: [
          { required: false, message: '请输入机构规格', trigger: 'blur' }
        ],
        subordinateRelations: [
          { required: false, message: '请输入隶属关系', trigger: 'blur' }
        ],
        institutionalType: [
          { required: false, message: '请输入机构类型', trigger: 'blur' }
        ],
        mainFunction: [
          { required: false, message: '请输入主要职能', trigger: 'blur' }
        ],
        approveContent: [
          { required: false, message: '请输入主要审判内容', trigger: 'blur' }
        ],
        website: [{ required: false, message: '请输入网站', trigger: 'blur' },
        { pattern: /^(https?|ftp):\/\/[^\s\/$.?#].[^\s]*$/, message: "请输入正确网站格式", trigger: 'blur' }
        ],
        email: [{ required: false, message: '请输入邮箱', trigger: 'blur' },
        { pattern: /^[\w.-]+@[a-zA-Z\d.-]+\.[a-zA-Z]{2,}$/, message: "请输入正确的邮箱格式", trigger: 'blur' }
        ],
        address: [{ required: false, message: '请输入地址', trigger: 'blur' }],
        postalCode: [
          { required: false, message: '请输入邮政编码', trigger: 'blur' },
          { pattern: /^[1-9]\d{5}$/, message: '请输入正确的邮政编码', trigger: 'blur' }
        ],
        officeTelephone: [
          { required: false, message: '请输入办公室电话', trigger: 'blur' }
        ],
        fax: [{ required: false, message: '请输入传真', trigger: 'blur' }],
        staffSize: [
          { required: false, message: '请输入编制人数', trigger: 'blur' },
          { pattern: /^[1-9]\d*$/, message: "请输入正整数", trigger: 'blur' }
        ],
        whetherReform: [
          { required: false, message: '请输入是否施行水务改革', trigger: 'blur' }
        ]
      },
      //查询到的信息对象
      editForm: {},
      //编辑表单的验证规则对象
      editFormRules: {
        organizationName: [
          { required: true, message: '请输入组织机构名称', trigger: 'blur' }
        ],
        organizationCode: [
          { required: false, message: '请输入组织机构代码', trigger: 'blur' }
        ],
        administrativeName: [
          { required: false, message: '请输入行政区划名称', trigger: 'blur'}
        ],
        organizationAbbr: [
          { required: false, message: '请输入机构简称', trigger: 'blur' }
        ],
        legalRepresentative: [
          { required: false, message: '请输入法人代表', trigger: 'blur' }
        ],
        agencySpecifications: [
          { required: false, message: '请输入机构规格', trigger: 'blur' }
        ],
        subordinateRelations: [
          { required: false, message: '请输入隶属关系', trigger: 'blur' }
        ],
        institutionalType: [
          { required: false, message: '请输入机构类型', trigger: 'blur' }
        ],
        mainFunction: [
          { required: false, message: '请输入主要职能', trigger: 'blur' }
        ],
        approveContent: [
          { required: false, message: '请输入主要审判内容', trigger: 'blur' }
        ],
        website: [{ required: false, message: '请输入网站', trigger: 'blur' },
        { pattern: /^(https?|ftp):\/\/[^\s\/$.?#].[^\s]*$/, message: "请输入正确网站格式", trigger: 'blur' }
        ],
        email: [{ required: false, message: '请输入邮箱', trigger: 'blur' },
        { pattern: /^[\w.-]+@[a-zA-Z\d.-]+\.[a-zA-Z]{2,}$/, message: "请输入正确的邮箱格式", trigger: 'blur' }
        ],
        address: [{ required: false, message: '请输入地址', trigger: 'blur' }],
        postalCode: [
          { required: false, message: '请输入邮政编码', trigger: 'blur' },
          { pattern: /^[1-9]\d{5}$/, message: '请输入正确的邮政编码', trigger: 'blur' }
        ],
        officeTelephone: [
          { required: false, message: '请输入办公室电话', trigger: 'blur' }
        ],
        fax: [{ required: false, message: '请输入传真', trigger: 'blur' }],
        staffSize: [
          { required: false, message: '请输入编制人数', trigger: 'blur' },
          { pattern: /^[1-9]\d*$/, message: "请输入正整数", trigger: 'blur' }
        ],
        whetherReform: [
          { required: false, message: '请输入是否施行水务改革', trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    //获取列表信息
    async getOrganizazitonList() {
      if (this.inputWord == null) {
        this.inputWord = ''
      }
      const { data: res } = await this.$http.get('/organization/list', {
        params: {
          currentPage: this.queryinfo.currentPage,
          pageSize: this.queryinfo.pageSize,
          name: this.inputWord
        }
      })
      if (res.code !== 200) {
        return this.$message.console.error('获取数据列表失败')
      }
      this.OrganizationList = res.data.records
      this.total = res.data.total
    },
    //获取编辑数据对话框
    async showEditDialog(id) {
      const { data: res } = await this.$http.get('/organization/info/' + id)
      if (res.code !== 200) {
        return this.$message.error('查询数据信息失败')
      }
      this.editForm = res.data
      this.editDialogVisible = true
    },
    //监听pageSize
    handleSizeChange(newSize) {
      this.queryinfo.pageSize = newSize
      this.getOrganizazitonList()
    },
    //监听currentPage
    handleCurrentChange(newPage) {
      this.queryinfo.currentPage = newPage
      this.getOrganizazitonList()
    },
    //监听新增对话框关闭重置事件
    addDialogClosed() {
      this.$refs.addFormRef.resetFields()
    },
    //监听编辑对话框关闭重置事件
    editDialogClosed() {
      this.$refs.editFormRef.resetFields()
    },
    //新增组织机构
    addAdministrativeDivision() {
      this.$refs.addFormRef.validate(async valid => {
        if (!valid) return
        //发起网络请求
        const { data: res } = await this.$http.post(
          '/organization/save',
          this.addForm
        )
        if (res.code !== 200) {
          this.$message.error('添加失败')
        }
        this.$message.success('添加成功')
        this.addDialogVisible = false
        this.getOrganizazitonList()
      })
    },
    //编辑行政区划信息表单
    editAdministrativeDivision() {
      this.$refs.editFormRef.validate(async valid => {
        if (!valid) return
        const { data: res } = await this.$http.post('/organization/update', {
          id: this.editForm.id,
          organizationName: this.editForm.organizationName,
          organizationCode: this.editForm.organizationCode,
          administrativeName: this.editForm.administrativeName,
          organizationAbbr: this.editForm.organizationAbbr,
          legalRepresentative: this.editForm.legalRepresentative,
          agencySpecifications: this.editForm.agencySpecifications,
          subordinateRelations: this.editForm.subordinateRelations,
          institutionalType: this.editForm.institutionalType,
          mainFunction: this.editForm.mainFunction,
          approveContent: this.editForm.approveContent,
          website: this.editForm.website,
          email: this.editForm.email,
          address: this.editForm.address,
          postalCode: this.editForm.postalCode,
          officeTelephone: this.editForm.officeTelephone,
          fax: this.editForm.fax,
          staffSize: this.editForm.staffSize,
          whetherReform: this.editForm.whetherReform
        })
        if (res.code !== 200) {
          return this.$message.error('更新数据失败')
        }
        this.$message.success('更新数据成功')
        this.editDialogVisible = false
        this.getOrganizazitonList()
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
      const { data: res } = await this.$http.post('/organization/delete/' + id)
      if (res.code !== 200) {
        return this.$message.error('删除失败')
      }
      this.$message.success('删除成功')
      this.getOrganizazitonList()
    }
  },
  created() {
    this.getOrganizazitonList()
  },
  mounted() { }
}
</script>

<style lang="less" scoped>
#div1 {
  height: 100%;
  width: 100%;
}

#div-header {
  height: 60px;
  width: 100%;
  margin-top: 0px;
  display: flex;
  background-color: rgb(253, 242, 228);
}

#div-main {
  height: calc(100% - 120px);
  width: 100%;
}

#div-footer {
  height: 35px;
  width: 100%;
  text-align: center;
  background-color: rgb(245, 237, 230);
}
</style>
