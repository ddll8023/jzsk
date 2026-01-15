<template>
  <div style="display: none;"></div>
</template>

<script>
// 如果项目中axios未全局挂载到Vue实例 (this.$http)，请取消以下注释导入axios
// import axios from 'axios'; 

export default {
  name: 'DsTokenManager',
  data() {
    return {
      refreshTimer: null,
      loginPayload: {
        username: "JCSK",
        password: "003eb5f1d8298580ea38cb8af2acd223d397eb12977502110b1c5634041a9384",
        // !! 注意：pictureCode 和 uuid 在实际登录流程中通常是动态的（例如，与验证码绑定）。
        // !! 硬编码这些值可能会导致登录失败，如果后端要求它们是动态的。
        pictureCode: "7006", 
        uuid: "6623d2fd-10e9-4834-b1f0-0d9f567d8d1f" 
      },
      loginHeaders: {
        "Language": "zh-CN",
        "Industry-Code": "DZ",
        "Content-Type": "application/json"
      }
    };
  },
  methods: {
    async fetchAndStoreToken() {
      console.log('Attempting to fetch and store token...');
      try {
        // 使用this.$http (axios) 发送POST请求，确保axios已全局挂载或在此处导入
        const response = await this.$http.post(
          "http://111.4.68.108:30080/api/manager/login",
          this.loginPayload,
          { headers: this.loginHeaders }
        );
        const result = response.data; // axios 会将响应数据包装在 .data 属性中

        if (result && result.code === 200 && result.data && result.data.token) {
          const newToken = result.data.token;
          localStorage.setItem('token', newToken); // 将Token存储到localStorage
          console.log('Token successfully fetched and stored in localStorage.');
        } else {
          console.error('Failed to get token:', result.message || 'Unknown error');
          localStorage.removeItem('token'); // 清除无效Token
          // 根据实际需求处理Token获取失败的情况，例如：
          // 如果是登录凭据问题，可能需要重定向到登录页：this.$router.push('/login');
        }
      } catch (error) {
        console.error('Error during token fetch:', error);
        localStorage.removeItem('token'); // 在网络错误或其他异常时清除Token
        // 根据实际需求处理网络错误，例如：
        // this.$router.push('/login'); // 重定向到登录页
      }
    },
    startTokenRefresh() {
      // Token有效期为2小时，我们设定每90分钟（1.5小时）刷新一次，留有足够的缓冲时间
      const refreshInterval = 90 * 60 * 1000; 
      
      this.stopTokenRefresh(); // 确保在启动新定时器之前清除任何现有的定时器

      this.refreshTimer = setInterval(() => {
        this.fetchAndStoreToken();
      }, refreshInterval);
      console.log(`Token refresh scheduled every ${refreshInterval / 1000 / 60} minutes.`);
    },
    stopTokenRefresh() {
      if (this.refreshTimer) {
        clearInterval(this.refreshTimer);
        this.refreshTimer = null;
        console.log('Token refresh stopped.');
      }
    }
  },
  created() {
    // 组件创建时立即尝试获取Token
    this.fetchAndStoreToken(); 
  },
  mounted() {
    // 组件挂载后开始定时刷新Token
    this.startTokenRefresh(); 
  },
  beforeDestroy() {
    // 组件销毁前停止定时器，防止内存泄漏
    this.stopTokenRefresh(); 
  }
};
</script>

<style scoped>
/* 这是一个无UI组件，无需任何样式 */
</style>
