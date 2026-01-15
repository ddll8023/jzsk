<template>
    <div id="div1">
        <div id="bread">
            <el-breadcrumb separator-class="el-icon-arrow-right" style="padding-top: 5px; padding-left: 10px">
                <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
                <el-breadcrumb-item>工程信息服务</el-breadcrumb-item>
                
                <el-breadcrumb-item :to="{ path: '/home/herbread' }">消毒药材</el-breadcrumb-item>
            </el-breadcrumb>
        </div>

        <div id="div-header1">
            <div style="align-self:center;margin-left:10px">
                <el-input placeholder="请输入消毒药材名称搜索" v-model="input" class="input-with-select" clearable
                    @clear="getHerbList">
                    <el-button slot="append" icon="el-icon-search" @click="searchHerb"></el-button>
                </el-input>
            </div>

            <el-button type="primary" round icon="iconfont icon-icon-test" style="align-self:center;margin-right:10px;margin-left:auto"
                @click="exportExcel">导出</el-button>
        </div>


        <div id="div-main">
            <el-table :data="HerbList" border stripe height="100%" style="width: 100%"
                :header-cell-style="{ background: '#cfe2f3', color: '#606266' }">
                <el-table-column type="index" label="序号" width="60px" align="center" :index="table_index">
                </el-table-column>
                <el-table-column prop="name" label="名称" align="center" min-width="100px">
                </el-table-column>
                <el-table-column prop="storageConditions" label="存储条件" align="center" min-width="100px">
                </el-table-column>
                <el-table-column prop="productionDate" label="生产日期" align="center" min-width="100px">
                </el-table-column>
                <el-table-column prop="expiryDate" label="有效期" align="center" min-width="100px">
                </el-table-column>
                <el-table-column prop="note" label="备注" align="center" min-width="100px">
                </el-table-column>
            </el-table>
        </div>

        <div id="div-footer">
            <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
                :current-page="queryinfo.currentPage" :page-sizes="[10, 20, 30, 50]" :page-size="queryinfo.pageSize"
                layout="total, sizes, prev, pager, next, jumper" :total="total">
            </el-pagination>
        </div>

        <!-- 新增对话框 -->
        <el-dialog title="新增消毒药材" :visible.sync="addDialogVisible" width="800px" @close="addDialogClosed" center
            :close-on-press-escape="false" :close-on-click-modal="false">
            <el-form :model="addForm" :rules="addFormRules" ref="addFormRef" label-width="150px" size="small">
                <el-row :gutter="20">
                    <el-col :span="12">
                        <el-form-item label="名称:" prop="name">
                            <el-input v-model="addForm.name"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="存储条件:" prop="storageConditions">
                            <el-input v-model="addForm.storageConditions"></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row :gutter="20">
                    <el-col :span="12">
                        <el-form-item label="生产日期:" prop="productionDate">
                            <el-date-picker v-model="addForm.productionDate" type="date" placeholder="选择日期"
                                value-format="yyyy-MM-dd">
                            </el-date-picker>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="有效期:" prop="expiryDate">
                            <el-input v-model="addForm.expiryDate"></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row :gutter="20">
                    <el-col :span="24">
                        <el-form-item label="备注:" prop="note">
                            <el-input v-model="addForm.note"></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
            </el-form>
            <!-- 底部按钮 -->
            <span slot="footer" class="dialog-footer">
                <el-button type="primary" @click="addHerb">确 定</el-button>
                <el-button @click="addDialogVisible = false">取 消</el-button>
            </span>
        </el-dialog>

        <!-- 编辑对话框 -->
        <el-dialog title="编辑消毒药材信息" :visible.sync="editDialogVisible" width="800px" @close="editDialogClosed" center
            :close-on-press-escape="false" :close-on-click-modal="false">
            <el-form :model="editForm" :rules="editFormRules" ref="editFormRef" label-width="150px" size="small">
                <el-row :gutter="20">
                    <el-col :span="12">
                        <el-form-item label="名称:" prop="name">
                            <el-input v-model="editForm.name"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="存储条件:" prop="storageConditions">
                            <el-input v-model="editForm.storageConditions"></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row :gutter="20">
                    <el-col :span="12">
                        <el-form-item label="生产日期:" prop="productionDate">
                            <el-date-picker v-model="editForm.productionDate" type="date" placeholder="选择日期"
                                value-format="yyyy-MM-dd">
                            </el-date-picker>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="有效期:" prop="expiryDate">
                            <el-input v-model="editForm.expiryDate"></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row :gutter="20">
                    <el-col :span="24">
                        <el-form-item label="备注:" prop="note">
                            <el-input v-model="editForm.note"></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
            </el-form>
            <!-- 底部按钮 -->
            <span slot="footer">
                <el-button type="primary" @click="editHerb">确 定</el-button>
                <el-button @click="editDialogVisible = false">取 消</el-button>
            </span>
        </el-dialog>

        <!-- 导入框 -->
        <el-dialog :visible.sync="importDialogVisible" width="800px" :modal="false" :close-on-press-escape="false"
            :close-on-click-modal="false">
            <template slot="title">
                <div style="border-bottom: 1px solid #ebebeb;font-size:20px;padding-bottom:10px">
                    导入Excel
                </div>
            </template>
            <el-upload class="upload-demo" drag ref="upload" accept=".xlsx,.xls" name="file"
                action="/herb/import-excel" :file-list="fileList" :auto-upload="false"
                :on-change="fileChange" :on-remove="handleRemove" :before-upload="beforeUpload"
                :http-request="httpRequest" multiple style="margin: 10px 200px;">
                <i class="el-icon-upload"></i>
                <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
                <div class="el-upload__tip" slot="tip">
                    只能上传xls/xlsx文件
                </div>
            </el-upload>
            <span slot="footer">
                <el-button type="info" icon="el-icon-download" style="margin-right:450px"
                    @click="exportFormwork">下载模板</el-button>
                <el-button type="success" @click="submitUpload">上 传</el-button>
                <el-button @click="importDialogVisible = false">取 消</el-button>
            </span>
        </el-dialog>

        <!-- 导出表格 -->
        <el-table id="table" :data="AllHerbList" border stripe height="100%" style="width: 100%;display:none;"
            :header-cell-style="{ background: '#cfe2f3', color: '#606266' }">
            <el-table-column type="index" label="序号" width="60px" align="center" :index="table_index">
            </el-table-column>
            <el-table-column prop="name" label="名称" align="center">
            </el-table-column>
            <el-table-column prop="storageConditions" label="存储条件" align="center">
            </el-table-column>
            <el-table-column prop="productionDate" label="生产日期" align="center">
            </el-table-column>
            <el-table-column prop="expiryDate" label="有效期" align="center">
            </el-table-column>
            <el-table-column prop="note" label="备注" align="center">
            </el-table-column>   
        </el-table>

        <!-- 导出数据模板 -->
        <el-table id="tableFormwork" :data="ExampleHerb" border stripe height="100%"
            style="width: 100%;display:none;" :header-cell-style="{ background: '#cfe2f3', color: '#606266' }">
            <el-table-column type="index" label="序号" width="60px" align="center" :index="table_index">
            </el-table-column>
            <el-table-column prop="name" label="名称" align="center">
            </el-table-column>
            <el-table-column prop="storageConditions" label="存储条件" align="center">
            </el-table-column>
            <el-table-column prop="productionDate" label="生产日期" align="center">
            </el-table-column>
            <el-table-column prop="expiryDate" label="有效期" align="center">
            </el-table-column>
            <el-table-column prop="note" label="备注" align="center">
            </el-table-column>
        </el-table>
    </div>
</template>

<script>
import FileSaver from 'file-saver'
import XLSX from 'xlsx'

export default {
    name: '',
    data() {
        //自定义表单验证规则
        return {
            file: '',
            fileList: [],
            input: '',
            //获取表格的参数对象
            queryinfo: {
                //当前页数
                currentPage: 1,
                //当前每页显示条数
                pageSize: 10
            },
            //数据列表
            ExampleHerb: [{
                name: '氮气',
                storageConditions: '干燥',
                productionDate: '2020-09-10',
                expiryDate: '12个月',
                note: '备注'
            }],
            HerbList: [],
            AllHerbList: [],
            total: 0,
            //控制新增对话框
            addDialogVisible: false,
            //控制编辑对话框
            editDialogVisible: false,
            //控制导入框
            importDialogVisible: false,
            //添加表单数据
            addForm: {
                name: '',
                storageConditions: '',
                productionDate: '',
                expiryDate: '',
                note: ''
            },
            //添加表单的验证规则对象
            addFormRules: {
                name: [
                    { required: true, message: '请输入消毒药材名称', trigger: 'blur' }
                ],
                storageConditions: [
                    { required: true, message: '请输入存储条件', trigger: 'blur' }
                ],
                productionDate: [
                    { required: true, message: '请输入生产日期', trigger: 'blur' }
                ],
                expiryDate: [
                    { required: true, message: '请输入有效期', trigger: 'blur' }
                    
                ],
                note: [
                    { required: false, message: '请输入备注信息', trigger: 'blur' }
                ]
            },
            //查询到的信息对象
            editForm: {},
            //编辑表单的验证规则对象
            editFormRules: {
                name: [
                    { required: true, message: '请输入消毒药材名称', trigger: 'blur' }
                ],
                storageConditions: [
                    { required: true, message: '请输入存储条件', trigger: 'blur' }
                ],
                productionDate: [
                    { required: true, message: '请输入生产日期', trigger: 'blur' }
                ],
                expiryDate: [
                    { required: true, message: '请输入有效期', trigger: 'blur' }
                    
                ],
                note: [
                    { required: false, message: '请输入备注信息', trigger: 'blur' }
                ]
            }
        }
    },
    methods: {
        //获取数据列表
        async getHerbList() {
            const { data: res } = await this.$http.get('/herb/list', {
                params: this.queryinfo
            })
            if (res.code !== 200) {
                return this.$message.error('获取数据列表失败')
            }
            this.HerbList = res.data.records
            this.total = res.data.total
        },
        //获取不分页数据列表
        async getAllHerbList() {
            const { data: res } = await this.$http.get('/herb/export-excel')
            if (res.code !== 200) {
                return this.$message.error('获取数据列表失败')
            }
            this.AllHerbList = res.data
        },
        //获取编辑数据对话框
        async showEditDialog(id) {
            const { data: res } = await this.$http.get('/herb/info/' + id)
            if (res.code !== 200) {
                return this.$message.error('查询数据信息失败')
            }
            this.editForm = res.data
            this.editDialogVisible = true
        },
        //监听pageSize
        handleSizeChange(newSize) {
            this.queryinfo.pageSize = newSize
            this.getHerbList()
        },
        //监听currentPage
        handleCurrentChange(newPage) {
            this.queryinfo.currentPage = newPage
            this.getHerbList()
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
        //新增枢纽供水工程
        addHerb() {
            this.$refs.addFormRef.validate(async valid => {
                if (!valid) return
                //发起网络请求
                const { data: res } = await this.$http.post(
                    '/herb/save',
                    this.addForm
                )
                if (res.code !== 200) {
                    this.$message.error('添加失败')
                }
                this.$message.success('添加成功')
                this.addDialogVisible = false
                this.getHerbList()
                this.getAllHerbList()
            })
        },
        //编辑角色信息表单
        editHerb() {
            this.$refs.editFormRef.validate(async valid => {
                if (!valid) return
                const { data: res } = await this.$http.post('/herb/update', {
                    id: this.editForm.id,
                    name: this.editForm.name,
                    storageConditions: this.editForm.storageConditions,
                    productionDate: this.editForm.productionDate,
                    expiryDate: this.editForm.expiryDate,
                    note: this.editForm.note
                })
                if (res.code !== 200) {
                    return this.$message.error('更新数据失败')
                }
                this.$message.success('更新数据成功')
                this.editDialogVisible = false
                this.getHerbList()
                this.getAllHerbList()
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
            const { data: res } = await this.$http.post('/herb/delete/' + id)
            if (res.code !== 200) {
                return this.$message.error('删除失败')
            }
            this.$message.success('删除成功')
            this.getHerbList()
            this.getAllHerbList()
        },
        //搜索
        async searchHerb() {
            const { data: res } = await this.$http.get('/herb/name', {
                params: {
                    name: this.input,
                    currentPage: this.queryinfo.currentPage,
                    pageSize: this.queryinfo.pageSize
                }
            })
            if (res.code !== 200) {
                return this.$message.error('搜索数据错误')
            }
            this.HerbList = res.data.records
            this.total = res.data.total
        },
        //导出Excel
        exportExcel() {
            var xlsxParam = { raw: true }
            var wb = XLSX.utils.table_to_book(
                document.querySelector('#table'),
                xlsxParam
            )
            var wbout = XLSX.write(wb, {
                bookType: 'xlsx',
                bookSST: true,
                type: 'array'
            })
            try {
                FileSaver.saveAs(
                    new Blob([wbout], { type: 'application/octet-stream' }),
                    '消毒药材数据列表报告.xlsx'
                )
            } catch (e) {
                if (typeof console !== 'undefined') {
                    console.log(e, wbout)
                }
            }
            return wbout
        },
        //导出模板
        exportFormwork() {
            var xlsxParam = { raw: true }
            var wb = XLSX.utils.table_to_book(
                document.querySelector('#tableFormwork'),
                xlsxParam
            )
            var wbout = XLSX.write(wb, {
                bookType: 'xlsx',
                bookSST: true,
                type: 'array'
            })
            try {
                FileSaver.saveAs(
                    new Blob([wbout], { type: 'application/octet-stream' }),
                    '消毒药材数据列表模板.xlsx'
                )
            } catch (e) {
                if (typeof console !== 'undefined') {
                    console.log(e, wbout)
                }
            }
            return wbout
        },
        //导入Excel
        submitUpload() {
            this.importDialogVisible = false
            this.$refs.upload.submit()
            setTimeout(() => {
                this.getHerbList()
            }, 1000)
        },
        fileChange(file, fileList) {
            this.fileList = fileList
        },
        handleRemove(file, fileList) {
            this.fileList = fileList
        },
        async httpRequest(param) {
            let fileObj = param.file
            let formData = new FormData()
            formData.append('file', fileObj)
            let url = '/herb/import-excel'
            let config = {
                headers: {
                    'Content-Type': 'multipart/form-data'
                }
            }
            const { data: res } = await this.$http.post(url, formData, config)
            if (res.code === 200) {
                this.$message.success('导入成功')
                this.getAllHerbList()
            }
            this.submitForm()
        },
        beforeUpload(file) {
            // 允许上传的文件格式列表
            let acceptList = ['xlsx', 'xls']
            // 根据文件名获取文件的后缀名
            let fileType = file.name
                .split('.')
                .pop()
                .toLowerCase()
            // 判断文件格式是否符合要求
            if (acceptList.indexOf(fileType) === -1) {
                this.$message.error('只能上传 xlsx/xls 格式的文件 !')
                return false
            }
        }
    },
    created() {
        this.getHerbList()
        this.getAllHerbList()
    },
    mounted() { }
}
</script>

<style scoped>
#div1 {
    height: 100%;
    width: 100%;
}

#bread {
    height: 25px;
    width: 100%;
}

#div-header1 {
    height: 60px;
    width: 100%;
    margin-top: 0px;
    display: flex;
    background-color: rgb(253, 242, 228);
}

#div-header2 {
    height: 6%;
    width: 100%;
    margin-top: 0px;
    display: flex;
}

#div-main {
    height: calc(100% - 120px);
    width: 100%;
}

#div-footer {
    height: 35px;
    width: 100%;
    float: left;
    text-align: center;
    background-color: rgb(245, 237, 230);
}

.upload-demo {
    margin: 10px 200px;
}
</style>