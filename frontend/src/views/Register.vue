<template>
  <div class="register-page">
    <div class="container">
      <div class="card register-card">
        <h2 class="text-center">用户注册</h2>
        <div v-if="message" :class="['alert', messageType === 'success' ? 'alert-success' : 'alert-danger']">
          {{ message }}
        </div>
        <form @submit.prevent="register" class="register-form">
          <div class="form-group">
            <label for="username">用户名</label>
            <input 
              type="text" 
              id="username" 
              v-model="user.username" 
              class="form-control" 
              required
              placeholder="请输入用户名"
            >
          </div>
          <div class="form-group">
            <label for="email">邮箱</label>
            <input 
              type="email" 
              id="email" 
              v-model="user.email" 
              class="form-control" 
              required
              placeholder="请输入邮箱"
            >
          </div>
          <div class="form-group">
            <label for="password">密码</label>
            <input 
              type="password" 
              id="password" 
              v-model="user.password" 
              class="form-control" 
              required
              placeholder="请输入密码"
            >
          </div>
          <div class="form-group">
            <label for="confirmPassword">确认密码</label>
            <input 
              type="password" 
              id="confirmPassword" 
              v-model="confirmPassword" 
              class="form-control" 
              required
              placeholder="请再次输入密码"
            >
          </div>
          <button type="submit" class="btn btn-primary btn-block" :disabled="loading">
            {{ loading ? '注册中...' : '注册' }}
          </button>
        </form>
        <div class="text-center mt-3">
          已有账号？ <router-link to="/login">立即登录</router-link>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: 'Register',
  data() {
    return {
      user: {
        username: '',
        email: '',
        password: ''
      },
      confirmPassword: '',
      message: '',
      messageType: '',
      loading: false
    };
  },
  methods: {
    validateForm() {
      // 验证用户名
      if (this.user.username.length < 3) {
        this.message = '用户名长度不能少于3个字符';
        this.messageType = 'error';
        return false;
      }
      
      // 验证邮箱格式
      const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
      if (!emailRegex.test(this.user.email)) {
        this.message = '请输入有效的邮箱地址';
        this.messageType = 'error';
        return false;
      }
      
      // 验证密码强度
      if (this.user.password.length < 6) {
        this.message = '密码长度不能少于6个字符';
        this.messageType = 'error';
        return false;
      }
      
      // 验证两次密码是否一致
      if (this.user.password !== this.confirmPassword) {
        this.message = '两次输入的密码不一致';
        this.messageType = 'error';
        return false;
      }
      
      return true;
    },
    async register() {
      // 表单验证
      if (!this.validateForm()) {
        return;
      }
      
      this.loading = true;
      try {
        // 调用后端注册接口
        const response = await axios.post('/api/register', this.user);
        
        const data = response.data;
        
        if (response.status === 200) {
          this.message = '注册成功！正在跳转到登录页...';
          this.messageType = 'success';
          
          // 3秒后跳转到登录页
          setTimeout(() => {
            this.$router.push('/login');
          }, 3000);
        } else {
          this.message = data.message || '注册失败，请稍后再试';
          this.messageType = 'error';
        }
      } catch (error) {
        console.error('注册请求出错:', error);
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
.register-page {
  padding: 60px 0;
  background-color: var(--light-gray);
  min-height: calc(100vh - 60px);
}

.register-card {
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