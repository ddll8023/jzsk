<template>
    <div id="div1">
        <div id="bread">
            <el-breadcrumb separator-class="el-icon-arrow-right" style="padding-top: 5px; padding-left: 10px">
                <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
                <el-breadcrumb-item>工程信息服务</el-breadcrumb-item>
                
                <el-breadcrumb-item :to="{ path: '/home/line' }">管道</el-breadcrumb-item>
            </el-breadcrumb>
        </div>

        <div id="div-header1">
            <div style="align-self:center;margin-left:10px">
                <el-input placeholder="请输入管道名称搜索" v-model="name" class="input-with-select" clearable
                    @clear="getLineList">
                    <el-button slot="append" icon="el-icon-search" @click="getLineList"></el-button>
                </el-input>
            </div>

            <span style="align-self:center; margin-left:20px;font-size:14px">管道类型：</span>
            <el-select v-model="type" clearable placeholder="请选择" style="align-self:center;" @change="getLineList"
                @clear="getLineList">
                <el-option v-for="item in typeList" :key="item.value" :label="item.label" :value="item.value">
                </el-option>
            </el-select>

            <el-button type="primary" round icon="el-icon-plus"
                style="margin-left:auto; margin-right:10px; align-self: center;"
                @click="addDialogVisible = true">新增</el-button>

            <el-button type="primary" round icon="iconfont icon-icon-test" style="align-self:center;margin-right:10px;"
                @click="importDialogVisible = true">导入</el-button>

            <el-button type="primary" round icon="iconfont icon-icon-test" style="align-self:center;margin-right:10px"
                @click="exportExcel">导出</el-button>

        </div>

        <div id="div-main">
            <el-table :data="LineList" border stripe height="100%" style="width: 100%"
                :header-cell-style="{ background: '#cfe2f3', color: '#606266' }">
                <el-table-column type="index" label="序号" width="60px" align="center" :index="table_index">
                </el-table-column>
                <el-table-column prop="name" label="管道名称" align="center" width="120px">
                </el-table-column>
                <el-table-column prop="type" label="管道类型" align="center" width="120px">
                </el-table-column>
                <el-table-column prop="note" label="备注" align="center">
                </el-table-column>
                <el-table-column fixed="right" label="操作" align="center" width="180">
                    <template slot-scope="scope">
                        <el-button size="mini" @click="showEditDialog(scope.row.id)">编辑</el-button>
                        <el-button size="mini" type="danger" @click="removeById(scope.row.id)">删除</el-button>
                    </template>
                </el-table-column>
            </el-table>
        </div>

        <div id="div-footer">
            <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
                :current-page="queryinfo.currentPage" :page-sizes="[10, 20, 30, 50]" :page-size="queryinfo.pageSize"
                layout="total, sizes, prev, pager, next, jumper" :total="total" style="padding-left:40%">
                >
            </el-pagination>
        </div>

        <!-- 新增对话框 -->
        <el-dialog title="新增管道" :visible.sync="addDialogVisible" width="800px" @close="addDialogClosed" center
            :close-on-press-escape="false" :close-on-click-modal="false">
            <!-- label-width="170px" -->
            <el-form :model="addForm" :rules="addFormRules" ref="addFormRef" label-width="150px" size="small">
                <el-row :gutter="20">
                    <el-col :span="12">
                        <el-form-item label="管道名称:" prop="name">
                            <el-input v-model="addForm.name"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="管道类型:" prop="type">
                            <el-select v-model="addForm.type" placeholder="请选择管道类型">
                                <el-option v-for="type in typeList" :key="type.value" :label="type.label"
                                    :value="type.value"></el-option>
                            </el-select>
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
                <el-row :gutter="20">
                    <el-col :span="24">
                        <el-form-item label="管道坐标:" prop="points">
                            <el-input type="textarea" v-model="addForm.points" :autosize="{ minRows: 4, maxRows: 10 }">
                            </el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
            </el-form>
            <!-- 底部按钮 -->
            <span slot="footer" class="dialog-footer">
                <el-button type="primary" @click="addLine">确 定</el-button>
                <el-button @click="addDialogVisible = false">取 消</el-button>
            </span>
        </el-dialog>

        <!-- 编辑对话框 -->
        <el-dialog title="编辑管道信息" :visible.sync="editDialogVisible" width="850px" @close="editDialogClosed" center
            :close-on-press-escape="false" :close-on-click-modal="false">
            <el-form :model="editForm" :rules="editFormRules" ref="editFormRef" label-width="170px"
                class="demo-ruleForm" size="small ">
                <el-row :gutter="20">
                    <el-col :span="12">
                        <el-form-item label="管道名称:" prop="name">
                            <el-input v-model="editForm.name"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="类型:" prop="type">
                            <el-select v-model="editForm.type" placeholder="请选择管道类型">
                                <el-option v-for="type in typeList" :key="type.value" :label="type.label"
                                    :value="type.value"></el-option>
                            </el-select>
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
                <el-button type="primary" @click="editLine">确 定</el-button>
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
            <el-upload class="upload-demo" drag ref="upload" accept=".xlsx,.xls" name="file" action="/line/import-excel"
                :file-list="fileList" :auto-upload="false" :on-change="fileChange" :on-remove="handleRemove"
                :before-upload="beforeUpload" :http-request="httpRequest" multiple>
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
        <el-table id="table" :data="AllLineList" border stripe height="100%" style="width: 100%;display:none;"
            :header-cell-style="{ background: '#cfe2f3', color: '#606266' }">
            <el-table-column type="index" label="序号" width="60px" align="center" :index="table_index">
            </el-table-column>
            <el-table-column prop="name" label="管道名称" align="center" width="120px">
            </el-table-column>
            <el-table-column prop="type" label="管道类型" align="center" width="120px">
            </el-table-column>
            <el-table-column prop="note" label="备注" align="center" width="120px">
            </el-table-column>
        </el-table>

        <!-- 导出数据模板 -->
        <el-table id="tableFormwork" :data="ExampleR" border stripe height="100%" style="width: 100%;display:none;"
            :header-cell-style="{ background: '#cfe2f3', color: '#606266' }">
            <el-table-column type="index" label="序号" width="60px" align="center" :index="table_index">
            </el-table-column>
            <el-table-column prop="name" label="管道名称" align="center" width="120px">
            </el-table-column>
            <el-table-column prop="type" label="管道类型" align="center" width="120px">
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
        return {
            file: '',
            fileList: [],
            name: '',
            type: '',
            types: '',
            typeList: [],
            //获取表格的参数对象
            queryinfo: {
                //当前页数
                currentPage: 1,
                //当前每页显示条数
                pageSize: 10
            },
            //数据列表
            ExampleR: [
                {
                    name: '管道1',
                    type: '管道类型',
                    note: '起点XX村,终点XX村',
                    points: '[{31.6460040,113.3757260},{31.6358930,113.3798260}]',
                }
            ],
            LineList: [],
            AllLineList: [],
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
                type: '',
                note: '',
                points: ''
            },
            //添加表单的验证规则对象
            addFormRules: {
                name: [
                    { required: true, message: '请输入管道名称', trigger: 'blur' }
                ],
                type: [
                    { required: true, message: '请输入管道类型', trigger: 'blur' }
                ],
                note: [
                    { required: false, message: '请输入备注信息', trigger: 'blur' }
                ],
                points: [
                    { required: true, message: '请输入管道坐标', trigger: 'blur' },
                ]
            },
            //查询到的信息对象
            editForm: {},
            //编辑表单的验证规则对象
            editFormRules: {
                name: [
                    { required: true, message: '请输入管道名称', trigger: 'blur' }
                ],
                type: [
                    { required: true, message: '请输入管道类型', trigger: 'blur' }
                ],
                note: [
                    { required: false, message: '请输入备注信息', trigger: 'blur' }
                ],
            }
        }
    },
    methods: {
        //获取数据列表
        async getLineList() {
            const { data: res } = await this.$http.get('/line/list', {
                params: {
                    name: this.name,
                    type: this.type,
                    currentPage: this.queryinfo.currentPage,
                    pageSize: this.queryinfo.pageSize
                }
            })
            if (res.code !== 200) {
                return this.$message.error('获取数据列表失败')
            }
            this.LineList = res.data.records
            this.total = res.data.total
        },
        //获取不分页数据列表
        async getAllLineList() {
            const { data: res } = await this.$http.get('/line/export-excel')
            if (res.code !== 200) {
                return this.$message.error('获取数据列表失败')
            }
            this.AllLineList = res.data
        },
        //获取编辑数据对话框
        async showEditDialog(id) {
            const { data: res } = await this.$http.get('/line/info/' + id)
            if (res.code !== 200) {
                return this.$message.error('查询数据信息失败')
            }
            this.editForm = res.data
            this.editDialogVisible = true
        },
        //得到所有预警状态
        async getAllTypes() {
            const { data: res } = await this.$http.get('/dict/kinds', {
                params: {
                    name: '管道类型'
                }
            })
            if (res.code !== 200) {
                return this.$message.error('查询数据信息失败')
            }
            this.types = res.data
            this.typeList = this.types.map(type => ({
                value: type,
                label: type
            }))
        },
        //监听pageSize
        handleSizeChange(newSize) {
            this.queryinfo.pageSize = newSize
            this.getLineList()
        },
        //监听currentPage
        handleCurrentChange(newPage) {
            this.queryinfo.currentPage = newPage
            this.getLineList()
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
        addLine() {
            this.$refs.addFormRef.validate(async valid => {
                if (!valid) return
                //发起网络请求
                const { data: res } = await this.$http.post(
                    '/line/save',
                    this.addForm
                )
                console.log(res)
                if (res.code !== 200) {
                    this.$message.error(res.message)
                }
                else {
                    this.$message.success('添加成功')
                    this.addDialogVisible = false
                    this.getLineList()
                    this.getAllLineList()
                }
            })
        },
        //编辑角色信息表单
        editLine() {
            this.$refs.editFormRef.validate(async valid => {
                if (!valid) return
                const { data: res } = await this.$http.post('/line/update', {
                    id: this.editForm.id,
                    name: this.editForm.name,
                    type: this.editForm.type,
                    note: this.editForm.note,
                    points: this.editForm.points
                })
                if (res.code !== 200) {
                    return this.$message.error('更新数据失败')
                }
                this.$message.success('更新数据成功')
                this.editDialogVisible = false
                this.getLineList()
                this.getAllLineList()
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
            if (confirmResult !== 'confirm') return
            const { data: res } = await this.$http.post('/line/delete/' + id)
            if (res.code !== 200) {
                return this.$message.error('删除失败')
            }
            this.$message.success('删除成功')
            this.getLineList()
            this.getAllLineList()
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
                    '管道数据列表报告.xlsx'
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
                    '管道数据列表模板.xlsx'
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
            //关闭dialog
            this.importDialogVisible = false
            this.$refs.upload.submit()
            setTimeout(() => {
                this.getLineList()
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
            let url = '/line/import-excel'
            let config = {
                headers: {
                    'Content-Type': 'multipart/form-data'
                }
            }
            const { data: res } = await this.$http.post(url, formData, config)
            if (res.code === 200) {
                this.$message.success('导入成功')
                this.getAllLineList()
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
        this.getLineList()
        this.getAllLineList()
        this.getAllTypes()
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
    background-color: rgb(245, 237, 230);
}

.upload-demo {
    margin: 10px 200px;
}
</style>