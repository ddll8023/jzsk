<template>
<div>
<el-breadcrumb separator-class="el-icon-arrow-right" class="bread">
  <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
  <el-breadcrumb-item>视频监测</el-breadcrumb-item>
</el-breadcrumb>
</div>
</template>

<script>
export default {
  name: '',
  data () {
    return {}
  },
  methods: {},
  created () {
  },
  mounted () {
  },
}
</script> 

<style scoped>
.bread{
  margin-top: 10px;
  margin-bottom: 10px;
  margin-left: 10px;
}
</style>
