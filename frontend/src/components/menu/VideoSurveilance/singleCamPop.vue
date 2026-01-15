<template>
  <el-dialog title="视频弹窗" :visible.sync="singleDialog" width="500px" @close="handleClose">
    <div>
      <p><b>摄像头IP：</b> {{ cameraInfo.serveraddr }}</p>
      <p><b>用户名：</b> {{ cameraInfo.username }}</p>
      <p><b>密码：</b> {{ cameraInfo.password }}</p>
      <!-- 这里可以集成摄像头播放控件 -->
    </div>
    <span slot="footer" class="dialog-footer">
      <el-button @click="handleClose">关闭</el-button>
    </span>
  </el-dialog>
</template>
<script>
export default {
  name: 'singleCamPop',
  props: {
    singleDialog: {
      type: Boolean,
      required: true
    },
    cameraInfo: {
      type: Object,
      default: () => ({})
    }
  },
  methods: {
    handleClose() {
      this.$emit('update:singleDialog', false);
    }
  }
}
</script>
<style scoped>
.dialog-footer {
  text-align: right;
}
</style> 