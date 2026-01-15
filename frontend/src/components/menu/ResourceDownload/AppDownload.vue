<template>
  <div id="download-page" class="download-container">
    <div class="breadcrumb">
      <el-breadcrumb separator-class="el-icon-arrow-right">
        <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
        <el-breadcrumb-item>资源下载</el-breadcrumb-item>
        <el-breadcrumb-item :to="{ path: '/home/administrativedivision' }">巡检app下载</el-breadcrumb-item>
      </el-breadcrumb>
    </div>

    <div class="qrcode-section">
      <img src="../../../icon/houduanapp.png" alt="QR Code" class="qrcode-image" />
      <p class="qrcode-text">请扫描二维码下载巡检app</p>
    </div>
  </div>
</template>

<script>
export default {
  // 组件的逻辑部分，如果需要的话
};
</script>

<style scoped>
#download-page {
  padding: 20px;
  text-align: center;
}

.breadcrumb {
  margin-bottom: 20px;
}

.qrcode-section {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-top: 20px;
}

.qrcode-image {
  width: 200px; /* 根据需要调整图片大小 */
  height: auto;
  margin-bottom: 10px;
}

.qrcode-text {
  font-size: 16px;
  color: #333;
}
</style>