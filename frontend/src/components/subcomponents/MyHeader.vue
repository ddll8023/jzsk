<template>
      <el-menu
      :default-active="activeIndex"
      class="el-menu-demo"
      mode="horizontal"
      @select="handleSelect"
      background-color="#545c64"
      text-color="#fff"
      active-text-color="#ffd04b">

      <el-submenu index="3">
        <template slot="title">预警管理</template>
        <el-menu-item index="3-1">
          <router-link to="/home/prewarninginformation">预警信息</router-link>
        </el-menu-item>
        <el-menu-item index="3-2">
          <router-link to="/home/prewarningindex">预警统计</router-link>
        </el-menu-item>
      </el-submenu>

      <el-submenu index="4">
        <template slot="title">工程巡检</template>
        <el-menu-item index="4-1">
          <router-link to="/home/pollingrecord">巡检记录</router-link>
        </el-menu-item>
        <el-menu-item index="4-2">
          <router-link to="/home/eventstudy">事件中心</router-link>
        </el-menu-item>
        <el-menu-item index="4-3">
          <router-link to="/home/maintencerecord">工程维护记录</router-link>
        </el-menu-item>
      </el-submenu>

      <el-submenu index="5">
        <template slot="title">管理信息服务</template>
        <el-menu-item index="5-1">
          <router-link to="/home/manageorganization">管理机构信息</router-link>
        </el-menu-item>
        <el-menu-item index="5-2">
          <router-link to="/home/manageperson">管理人员信息</router-link>
        </el-menu-item>
      </el-submenu>

      <el-submenu index="6">
        <template slot="title">工程信息服务</template>
        <el-menu-item index="6-1">
          <router-link to="/home/watersupplyengineering">枢纽供水工程信息</router-link>
        </el-menu-item>
        <el-menu-item index="6-2">
          <router-link to="/home/enginerringinformation">工程基础信息</router-link>
        </el-menu-item>
        <el-menu-item index="6-3">
          <router-link to="/home/monitorsite">监测站点</router-link>
        </el-menu-item>
      </el-submenu>

      <el-submenu index="7">
        <template slot="title">系统管理</template>
        <el-menu-item index="7-1">
          <router-link to="/home/administrativedivision">行政区划</router-link>
        </el-menu-item>
        <el-menu-item index="7-2">
          <router-link to="/home/oraganization">组织机构</router-link>
        </el-menu-item>
        <el-menu-item index="7-3">
          <router-link to="/home/charactermanage">角色管理</router-link>
        </el-menu-item>
        <el-menu-item index="7-4">
          <router-link to="/home/resourcemanage">资源管理</router-link>
        </el-menu-item>
        <el-menu-item index="7-5">
          <router-link to="/home/usermanage">用户管理</router-link>
        </el-menu-item>
      </el-submenu>

      <el-submenu index="8" style="float:right">
        <template slot="title" >个人</template>
        <el-menu-item index="8-1" @click="dialogFormVisible = true">个人信息</el-menu-item>
        <el-menu-item index="8-2" @click="laterPassword">修改密码</el-menu-item>
        <el-menu-item index="8-3" @click="logout">退出</el-menu-item>
      </el-submenu>
    </el-menu>
</template>


<script>
  export default {
    data() {
      return {
        activeIndex: '2',
        dialogFormVisible: false,
        form: {
          name: '',
          region: '',
          date1: '',
          date2: '',
          delivery: false,
          type: [],
          resource: '',
          desc: ''
        },
        formLabelWidth: '120px'
      };
    },
    methods: {
      handleSelect(key, keyPath) {
        console.log(key, keyPath);
      },
      laterPassword(){
      },
      logout(){
        sessionStorage.removeItem('token')
        this.$router.push('/login')
      },
      handleClose(done) {
        this.$confirm('确认关闭?')
          .then(_ => {
            done();
          })
          .catch(_ => {});
      }
    }
  }
</script>

<style lang="less"scoped>
.router-link-active {
  text-decoration: none;
  color: white;
}
a{
  text-decoration: none;
  color: white;
}
</style>