<template>
<div class="div1">
  <el-tabs type="border-card" style="height:99.8%">
    <el-tab-pane label="视频查看">
      <el-tabs v-model="activeName" @tab-click="handleClick">
        <el-tab-pane label="实时监控" name="first"></el-tab-pane>
        <el-tab-pane label="监控回放" name="second"></el-tab-pane>
      </el-tabs>
    </el-tab-pane>

    <el-tab-pane label="视频配置">
      <div id="div3">
        <div id="div3-aside">
          <el-menu :default-openeds="['1', '3']">
            <el-submenu index="1">
              <template slot="title"
                ><i class="el-icon-setting"></i>摄像头目录</template
              >
              <el-menu-item-group>
                <template slot="title">分组一</template>
                <el-menu-item index="1-1">选项1</el-menu-item>
                <el-menu-item index="1-2">选项2</el-menu-item>
              </el-menu-item-group>
              <el-menu-item-group title="分组2">
                <el-menu-item index="1-3">选项3</el-menu-item>
              </el-menu-item-group>
              <el-submenu index="1-4">
                <template slot="title">选项4</template>
                <el-menu-item index="1-4-1">选项4-1</el-menu-item>
              </el-submenu>
            </el-submenu>
          </el-menu>
        </div>

        <div id="div3-main">
          <div id="div3-main-header">
            <el-input
              v-model="input"
              placeholder="请输入"
              style="align-self:center;width:140px"
            ></el-input>
            <el-button type="primary" icon="el-icon-search">搜索</el-button>
          </div>

          <div id="div3-main-body">
            <el-table
              :data="PIList"
              border
              stripe
              style="width: 100%"
              height="100%"
              v-loading="loading"
              :header-cell-style="{ background: '#cfe2f3', color: '#606266' }"
            >
              <el-table-column prop="date" label="序号" width="140">
              </el-table-column>
              <el-table-column prop="name" label="名称" width="120">
              </el-table-column>
              <el-table-column prop="address" label="类型"> </el-table-column>
              <el-table-column prop="address" label="IP地址"> </el-table-column>
              <el-table-column prop="address" label="端口号"> </el-table-column>
              <el-table-column prop="address" label="用户名"> </el-table-column>
              <el-table-column prop="address" label="密码"> </el-table-column>
              <el-table-column prop="address" label="操作"> </el-table-column>
            </el-table>
          </div>
        </div>
      </div>

        
    </el-tab-pane>

    <el-tab-pane label="在线统计" id="monitor"></el-tab-pane>
  </el-tabs>
  </div>
</template>

<script>
export default {
  name: '',
  data() {
    return {
      activeName: 'first',
      input: '',
      tableData: {}
    }
  },
  methods: {
    handleClick(tab, event) {
      console.log(tab, event)
    }
  },
  created() {},
  mounted() {}
}
</script>

<style lang="less" scoped>
.div1{
  height: 100%;
  width: 100%;
}
#div3{
  height: 100%;
  width: 100%;
  display: flex;
  border: 1px solid rgb(192, 190, 190);
}
#div3-aside{
  height: 100%;
  width: 200px;
  background-color: rgb(238, 241, 246);
}
#div3-main{
  height: 100%;
  width: 100%;
}
#div3-main-header{
  height: 10%;
  width: 100%;
  display: flex;
  margin: 10px;
}
</style>
