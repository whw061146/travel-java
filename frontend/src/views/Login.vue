<template>
  <div class="login-page">
    <div class="container">
      <div class="card login-card">
        <h2 class="text-center">用户登录</h2>
        <div v-if="message" :class="['alert', messageType === 'success' ? 'alert-success' : 'alert-danger']">
          {{ message }}
        </div>
        <form @submit.prevent="login" class="login-form">
          <div class="form-group">
            <label for="username">用户名</label>
            <input 
              type="text" 
              id="username" 
              v-model="loginForm.username" 
              class="form-control" 
              required
              placeholder="请输入用户名"
            >
          </div>
          <div class="form-group">
            <label for="password">密码</label>
            <input 
              type="password" 
              id="password" 
              v-model="loginForm.password" 
              class="form-control" 
              required
              placeholder="请输入密码"
            >
          </div>
          <button type="submit" class="btn btn-primary btn-block" :disabled="loading">
            {{ loading ? '登录中...' : '登录' }}
          </button>
        </form>
        <div class="text-center mt-3">
          没有账号？ <router-link to="/register">立即注册</router-link>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: 'Login',
  data() {
    return {
      loginForm: {
        username: '',
        password: ''
      },
      message: '',
      messageType: '',
      loading: false
    };
  },
  methods: {
    validateForm() {
      // 验证用户名不能为空
      if (!this.loginForm.username.trim()) {
        this.message = '请输入用户名';
        this.messageType = 'error';
        return false;
      }
      
      // 验证密码不能为空
      if (!this.loginForm.password) {
        this.message = '请输入密码';
        this.messageType = 'error';
        return false;
      }
      
      return true;
    },
    async login() {
      // 表单验证
      if (!this.validateForm()) {
        return;
      }
      
      this.loading = true;
      try {
        // 调用后端登录接口
        const response = await axios.post('/api/login', this.loginForm);
        
        const data = response.data;
        
        if (response.status === 200) {
          // 登录成功，保存token和用户信息
          localStorage.setItem('token', data.token);
          localStorage.setItem('userInfo', JSON.stringify(data.user));
          
          // 更新导航栏状态（通过事件总线或Vuex可实现）
          // 这里简单处理，依赖于NavBar组件的created钩子检测localStorage
          
          this.message = '登录成功！正在跳转...';
          this.messageType = 'success';
          
          // 跳转到首页或之前的页面
          setTimeout(() => {
            const redirectPath = this.$route.query.redirect || '/';
            this.$router.push(redirectPath);
          }, 1500);
        } else {
          this.message = data.message || '用户名或密码错误';
          this.messageType = 'error';
        }
      } catch (error) {
        console.error('登录请求出错:', error);
        this.message = '网络错误，请稍后再试';
        this.messageType = 'error';
      } finally {
        this.loading = false;
      }
    }
  }
};
</script>

<style scoped>
.login-page {
  padding: 60px 0;
  background-color: var(--light-gray);
  min-height: calc(100vh - 60px);
}

.login-card {
  max-width: 500px;
  margin: 0 auto;
  padding: 30px;
}

.btn-block {
  width: 100%;
  margin-top: 20px;
}

.mt-3 {
  margin-top: 15px;
}
</style>