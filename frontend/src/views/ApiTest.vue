<template>
  <div class="api-test-container">
    <h1>API路径测试</h1>
    
    <div class="test-section">
      <h3>当前环境信息</h3>
      <p><strong>当前URL:</strong> {{ currentUrl }}</p>
      <p><strong>主机名:</strong> {{ hostname }}</p>
      <p><strong>协议:</strong> {{ protocol }}</p>
      <p><strong>端口:</strong> {{ port }}</p>
    </div>

    <div class="test-section">
      <h3>测试不同的登录接口路径</h3>
      <button @click="testPath('http://111.4.68.108:8081/login')">测试 /login</button>
      <button @click="testPath('http://111.4.68.108:8081/api/login')">测试 /api/login</button>
      <button @click="testPath('http://111.4.68.108:8081/auth/login')">测试 /auth/login</button>
      <button @click="testPath('http://111.4.68.108:8081/user/login')">测试 /user/login</button>
      <button @click="testPath('http://111.4.68.108:8081/system/login')">测试 /system/login</button>
      <div v-html="pathResult"></div>
    </div>

    <div class="test-section">
      <h3>测试健康检查接口</h3>
      <button @click="testHealth('http://111.4.68.108:8081/actuator/health')">测试 /actuator/health</button>
      <button @click="testHealth('http://111.4.68.108:8081/health')">测试 /health</button>
      <button @click="testHealth('http://111.4.68.108:8081/')">测试根路径 /</button>
      <div v-html="healthResult"></div>
    </div>

    <div class="test-section">
      <h3>测试登录请求（POST）</h3>
      <button @click="testLoginPost('http://111.4.68.108:8081/login')">POST /login</button>
      <button @click="testLoginPost('http://111.4.68.108:8081/api/login')">POST /api/login</button>
      <button @click="testLoginPost('http://111.4.68.108:8081/auth/login')">POST /auth/login</button>
      <div v-html="loginPostResult"></div>
    </div>

    <div class="test-section">
      <h3>测试内网连接</h3>
      <button @click="testPath('http://192.168.20.3:8081/login')">测试内网 /login</button>
      <button @click="testPath('http://192.168.20.3:8081/api/login')">测试内网 /api/login</button>
      <div v-html="intranetResult"></div>
    </div>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  name: 'ApiTest',
  data() {
    return {
      currentUrl: window.location.href,
      hostname: window.location.hostname,
      protocol: window.location.protocol,
      port: window.location.port,
      pathResult: '',
      healthResult: '',
      loginPostResult: '',
      intranetResult: ''
    }
  },
  methods: {
    // 测试API路径
    async testPath(url) {
      this.pathResult = '<p>测试中...</p>'
      
      try {
        console.log(`测试路径: ${url}`)
        
        const response = await axios.get(url, {
          timeout: 5000
        })
        
        this.pathResult = '<div class="success">' +
          '<h4>✅ 路径测试结果</h4>' +
          '<p><strong>URL:</strong> ' + url + '</p>' +
          '<p><strong>状态:</strong> ' + response.status + ' ' + response.statusText + '</p>' +
          '<p><strong>响应:</strong></p>' +
          '<pre>' + JSON.stringify(response.data, null, 2) + '</pre>' +
          '</div>'
      } catch (error) {
        this.pathResult = '<div class="error">' +
          '<h4>❌ 路径测试错误</h4>' +
          '<p><strong>URL:</strong> ' + url + '</p>' +
          '<p><strong>错误:</strong> ' + error.message + '</p>' +
          '<p><strong>状态码:</strong> ' + (error.response && error.response.status ? error.response.status : 'N/A') + '</p>' +
          '</div>'
      }
    },

    // 测试健康检查
    async testHealth(url) {
      this.healthResult = '<p>测试中...</p>'
      
      try {
        console.log(`测试健康检查: ${url}`)
        
        const response = await axios.get(url, {
          timeout: 5000
        })
        
        this.healthResult = '<div class="success">' +
          '<h4>✅ 健康检查结果</h4>' +
          '<p><strong>URL:</strong> ' + url + '</p>' +
          '<p><strong>状态:</strong> ' + response.status + ' ' + response.statusText + '</p>' +
          '<p><strong>响应:</strong></p>' +
          '<pre>' + JSON.stringify(response.data, null, 2) + '</pre>' +
          '</div>'
      } catch (error) {
        this.healthResult = '<div class="error">' +
          '<h4>❌ 健康检查错误</h4>' +
          '<p><strong>URL:</strong> ' + url + '</p>' +
          '<p><strong>错误:</strong> ' + error.message + '</p>' +
          '<p><strong>状态码:</strong> ' + (error.response && error.response.status ? error.response.status : 'N/A') + '</p>' +
          '</div>'
      }
    },

    // 测试POST登录
    async testLoginPost(url) {
      this.loginPostResult = '<p>测试中...</p>'
      
      try {
        console.log(`测试POST登录: ${url}`)
        
        const loginData = new URLSearchParams({
          username: 'admin01',
          password: '123456'
        })
        
        const response = await axios.post(url, loginData, {
          timeout: 5000,
          headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
          }
        })
        
        this.loginPostResult = '<div class="success">' +
          '<h4>✅ POST登录结果</h4>' +
          '<p><strong>URL:</strong> ' + url + '</p>' +
          '<p><strong>状态:</strong> ' + response.status + ' ' + response.statusText + '</p>' +
          '<p><strong>响应:</strong></p>' +
          '<pre>' + JSON.stringify(response.data, null, 2) + '</pre>' +
          '</div>'
      } catch (error) {
        this.loginPostResult = '<div class="error">' +
          '<h4>❌ POST登录错误</h4>' +
          '<p><strong>URL:</strong> ' + url + '</p>' +
          '<p><strong>错误:</strong> ' + error.message + '</p>' +
          '<p><strong>状态码:</strong> ' + (error.response && error.response.status ? error.response.status : 'N/A') + '</p>' +
          '</div>'
      }
    },

    // 测试内网连接
    async testIntranet(url) {
      this.intranetResult = '<p>测试中...</p>'
      
      try {
        console.log(`测试内网连接: ${url}`)
        
        const response = await axios.get(url, {
          timeout: 5000
        })
        
        this.intranetResult = '<div class="success">' +
          '<h4>✅ 内网连接成功</h4>' +
          '<p><strong>URL:</strong> ' + url + '</p>' +
          '<p><strong>状态:</strong> ' + response.status + ' ' + response.statusText + '</p>' +
          '<p><strong>响应:</strong></p>' +
          '<pre>' + JSON.stringify(response.data, null, 2) + '</pre>' +
          '</div>'
      } catch (error) {
        this.intranetResult = '<div class="error">' +
          '<h4>❌ 内网连接错误</h4>' +
          '<p><strong>URL:</strong> ' + url + '</p>' +
          '<p><strong>错误:</strong> ' + error.message + '</p>' +
          '<p><strong>状态码:</strong> ' + (error.response && error.response.status ? error.response.status : 'N/A') + '</p>' +
          '</div>'
      }
    }
  }
}
</script>

<style scoped>
.api-test-container {
  padding: 20px;
  font-family: Arial, sans-serif;
}

.test-section {
  margin: 20px 0;
  padding: 15px;
  border: 1px solid #ddd;
  border-radius: 5px;
}

.success {
  background-color: #d4edda;
  border-color: #c3e6cb;
  padding: 10px;
  border-radius: 3px;
  margin-top: 10px;
}

.error {
  background-color: #f8d7da;
  border-color: #f5c6cb;
  padding: 10px;
  border-radius: 3px;
  margin-top: 10px;
}

button {
  padding: 10px 20px;
  margin: 5px;
  cursor: pointer;
  background-color: #007bff;
  color: white;
  border: none;
  border-radius: 3px;
}

button:hover {
  background-color: #0056b3;
}

pre {
  background: #f8f9fa;
  padding: 10px;
  border-radius: 3px;
  overflow-x: auto;
  white-space: pre-wrap;
}
</style> 