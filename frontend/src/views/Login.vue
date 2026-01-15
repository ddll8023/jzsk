<template>
  <div class="login-container">
    <!-- API地址切换组件 - 隐藏 -->
    <!-- <ApiSwitcher /> -->
    
    <div class="login-box">
      <h2 id="h2">智慧荆竹水库管理平台</h2>
      <el-form label-width="0px" class="login_form" :model="loginForm" :rules="loginFormRules" ref="loginFormRef" @submit.native.prevent="login">
        <!-- 用户名 -->
        <el-form-item prop="username">
          <el-input v-model="loginForm.username" prefix-icon="iconfont icon-yonghu" placeholder="用户名"></el-input>
        </el-form-item>
        <!-- 密码 -->
        <el-form-item prop="password">
          <el-input v-model="loginForm.password" prefix-icon="iconfont icon-lock" type="password"
            placeholder="密码"></el-input>
        </el-form-item>
        <!-- 按钮区域 -->
        <el-form-item class="btns">
          <el-button type="primary" native-type="submit">登录</el-button>
          <el-button type="info" @click="resetLoginForm">重置</el-button>
        </el-form-item>
      </el-form>
    </div>
    <!-- 新增的提示信息 -->
    <!-- <div class="download-plugin">
      <a href="http://localhost:8081/shipin/HCWebSDKPlugin.exe" target="_blank">请在此处下载视频插件并安装</a>
    </div> -->
  </div>
</template>

<script>
import ApiSwitcher from '@/components/ApiSwitcher.vue'

export default {
  name: 'Login',
  components: {
    ApiSwitcher
  },
  data() {
    return {
      //登陆表单的数据绑定对象
      loginForm: {
        username: '',
        password: ''
      },
      //表单的验证规则
      loginFormRules: {
        username: [
          { required: true, message: '请输入用户名', trigger: 'blur' },
          {
            min: 3,
            max: 9,
            message: '用户名长度在 3 到 9 个字符',
            trigger: 'blur'
          }
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' },
          {
            min: 3,
            max: 15,
            message: '密码长度在 3 到 15 个字符',
            trigger: 'blur'
          }
        ]
      }
    }
  },
  methods: {
    //重置
    resetLoginForm() {
      this.$refs.loginFormRef.resetFields()
    },
    //登录
    login() {
      console.log('开始登录流程...');
      console.log('登录表单数据:', this.loginForm);
      console.log('当前axios baseURL:', this.$http.defaults.baseURL);
      
      this.$refs.loginFormRef.validate(async valid => {
        if (valid != true) {
          console.log('表单验证失败');
          return;
        }
        
        console.log('表单验证通过，准备发送登录请求...');
        const loginUrl = '/login';
        const loginData = this.$qs.stringify(this.loginForm);
        
        console.log('登录URL:', loginUrl);
        console.log('登录数据:', loginData);
        console.log('完整请求URL:', this.$http.defaults.baseURL + loginUrl);
        
        // 添加详细的调试信息
        console.log('=== 调试信息 ===');
        console.log('用户名:', this.loginForm.username);
        console.log('密码:', this.loginForm.password);
        console.log('密码长度:', this.loginForm.password.length);
        console.log('密码字符编码:', Array.from(this.loginForm.password).map(c => c.charCodeAt(0)));
        console.log('URL编码后的数据:', loginData);
        console.log('================');
        
        try {
          // 确保使用表单格式发送请求
          const { data: res } = await this.$http.post(loginUrl, loginData, {
            headers: {
              'Content-Type': 'application/x-www-form-urlencoded'
            }
          });
          console.log('登录响应:', res);
          
          if (res.code !== 200) {
            console.log('登录失败，错误码:', res.code);
            console.log('错误信息:', res.message || res.msg || '未知错误');
            return this.$message.error('账号或密码错误');
          }
          
          console.log('登录成功，保存token...');
          window.sessionStorage.setItem('token', res.data.token);
          this.$router.push('/home');
        } catch (error) {
          console.error('登录请求失败:', error);
          this.$message.error('登录失败，请检查网络连接');
        }
      })
    }
  }
}
</script>

<style lang="less" scoped>
.login-container {
  height: 100%;
  width: 100%;
  font-family: sans-serif;
  background-image: url(../assets/img/sea.jpg);
  background-size: cover;
  position: relative; /* 确保可以使用绝对定位 */
}

#h2 {
  margin: 30px 0 30px;
  padding: 0;
  color: #fff;
  text-align: center;
}

.login-box {
  width: 450px;
  height: 300px;
  background-color: rgba(0, 0, 0, 0.8);
  border-radius: 10px;
  position: absolute;
  left: 50%;
  top: 50%;
  transform: translate(-50%, -50%);
  box-sizing: border-box;
  box-shadow: 0 15px 25px rgba(0, 0, 0, 0.8);
}

.btns {
  display: flex;
  justify-content: flex-end;
}

.login_form {
  position: absolute;
  bottom: 0px;
  width: 100%;
  padding: 0 20px;
  box-sizing: border-box;
  border: none;
  outline: none;
}

/* 新增的样式 */
.download-plugin {
  position: absolute;
  bottom: 20px; /* 距离底部20px */
  left: 50%;
  transform: translateX(-50%);
  color: #fff;
  font-size: 16px;
  text-align: center;
}

.download-plugin a {
  color: #409eff; /* 链接颜色 */
  text-decoration: none; /* 去掉下划线 */
}

.download-plugin a:hover {
  text-decoration: underline; /* 鼠标悬停时显示下划线 */
}
</style>