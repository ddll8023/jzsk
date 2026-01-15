<template>
    <div id="div1">
        <div id="bread">
            <el-breadcrumb separator-class="el-icon-arrow-right" style="padding-top:5px; padding-left:10px">
                <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
                <el-breadcrumb-item>系统管理</el-breadcrumb-item>
                <el-breadcrumb-item :to="{ path: '/home/shouyeweihu' }">首页维护</el-breadcrumb-item>
            </el-breadcrumb>
        </div>

        <div id="main">
            <div id="main-body">
                <!-- 搜索区域 -->
                <div id="search-section">
                    <el-button 
                        id="text-edit-btn" 
                        size="medium" 
                        type="primary"
                        @click="showEditDialog(1)">
                        更新首页文字
                    </el-button>
                </div>

                <!-- 文本编辑对话框 -->
                <el-dialog 
                    title="编辑文字" 
                    :visible.sync="dialogVisible" 
                    width="50%" 
                    @close="editDialogClosed"
                    :close-on-press-escape="false" 
                    :close-on-click-modal="false">
                    <el-form :model="editForm">
                        <el-form-item prop="text">
                            <el-input 
                                type="textarea" 
                                v-model="editForm.text" 
                                :rows="13"
                                @input="inputEvent"
                                placeholder="请输入展示内容（最多450字）">
                            </el-input>
                            <div class="char-count">{{ characterCount }}/450</div>
                        </el-form-item>
                    </el-form>
                    <span slot="footer" class="dialog-footer">
                        <el-button @click="dialogVisible = false">取消</el-button>
                        <el-button type="primary" @click="updateText">确认更新</el-button>
                    </span>
                </el-dialog>
            </div>
        </div>
    </div>
</template>

<script>
export default {
    name: 'HomeMaintenance',
    data() {
        return {
            projectInfo: [],
            dialogVisible: false,
            editForm: { text: '' },
            characterCount: 0,
            maxCharacters: 450
        }
    },
    methods: {
        // 初始化获取文本内容
        async getProjectInfo() {
            try {
                const { data: res } = await this.$http.get('/icon/getByName')
                if (res.code !== 200) {
                    this.$message.error('获取文本信息失败')
                    return
                }
                this.projectInfo = res.data
                this.editForm.text = this.projectInfo.text || ''
                this.characterCount = this.editForm.text.length
            } catch (error) {
                this.$message.error('服务器请求异常')
            }
        },

        // 打开编辑对话框
        async showEditDialog(id) {
            try {
                const { data: res } = await this.$http.get(`/icon/info/${id}`)
                if (res.code !== 200) {
                    this.$message.error('获取编辑内容失败')
                    return
                }
                this.editForm = res.data
                this.dialogVisible = true
            } catch (error) {
                this.$message.error('服务器请求异常')
            }
        },

        // 文本输入处理
        inputEvent() {
            this.characterCount = this.editForm.text.length
            if (this.characterCount > this.maxCharacters) {
                this.editForm.text = this.editForm.text.substring(0, this.maxCharacters)
                this.$message.warning('已达到最大字数限制')
            }
        },

        // 提交更新
        async updateText() {
            if (this.characterCount > this.maxCharacters) {
                this.$message.error('文字超过最大限制')
                return
            }
            
            try {
                const { data: res } = await this.$http.post('/icon/update', this.editForm)
                if (res.code === 200) {
                    this.$message.success('更新成功')
                    await this.getProjectInfo()
                    this.dialogVisible = false
                } else {
                    this.$message.error(res.msg || '更新失败')
                }
            } catch (error) {
                this.$message.error('更新请求失败')
            }
        },

        // 关闭对话框重置
        editDialogClosed() {
            this.editForm.text = this.projectInfo.text || ''
            this.characterCount = this.editForm.text.length
        }
    },
    mounted() {
        this.getProjectInfo()
    }
}
</script>

<style lang="less" scoped>
#div1 {
    height: 100vh;
    display: flex;
    flex-direction: column;
}

#bread {
    height: 40px;
    background: #fff;
    box-shadow: 0 2px 12px 0 rgba(0,0,0,.1);
}

#main {
    flex: 1;
    margin: 20px;
    padding: 20px;
    background: #fff;
    border-radius: 4px;
    box-shadow: 0 2px 12px 0 rgba(0,0,0,.1);
}

#search-section {
    display: flex;
    justify-content: center;
    margin-bottom: 20px;
}

#text-edit-btn {
    width: 200px;
    font-size: 16px;
}

.char-count {
    text-align: right;
    color: #909399;
    font-size: 12px;
    margin-top: 8px;
}

.dialog-footer {
    display: flex;
    justify-content: flex-end;
    padding: 10px 20px 0;
    border-top: 1px solid #ebeef5;
}
</style>