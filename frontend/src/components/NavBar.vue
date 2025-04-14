<template>
  <nav class="navbar">
    <div class="navbar-brand">
      <router-link to="/" class="navbar-logo">智能旅游规划器</router-link>
    </div>
    <div class="navbar-menu">
      <router-link to="/" class="navbar-item">首页</router-link>
      <router-link to="/plan" class="navbar-item">行程规划</router-link>
      <router-link to="/history" class="navbar-item">历史记录</router-link>
      <router-link to="/feedback" class="navbar-item">用户反馈</router-link>
    </div>
    <div class="navbar-auth">
      <template v-if="isLoggedIn">
        <span class="user-name">{{ userName }}</span>
        <button @click="logout" class="logout-btn">退出</button>
      </template>
      <template v-else>
        <router-link to="/login" class="login-btn">登录</router-link>
        <router-link to="/register" class="register-btn">注册</router-link>
      </template>
    </div>
  </nav>
</template>

<script>
export default {
  name: 'NavBar',
  data() {
    return {
      isLoggedIn: false,
      userName: ''
    };
  },
  created() {
    // 检查本地存储中是否有用户信息
    const userInfo = localStorage.getItem('userInfo');
    if (userInfo) {
      const user = JSON.parse(userInfo);
      this.isLoggedIn = true;
      this.userName = user.username;
    }
  },
  methods: {
    logout() {
      // 清除本地存储的用户信息
      localStorage.removeItem('userInfo');
      localStorage.removeItem('token');
      this.isLoggedIn = false;
      this.userName = '';
      // 跳转到登录页
      this.$router.push('/login');
    }
  }
};
</script>

<style scoped>
.navbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background-color: #3498db;
  color: white;
  padding: 0 20px;
  height: 60px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.navbar-brand {
  font-size: 1.5rem;
  font-weight: bold;
}

.navbar-logo {
  color: white;
  text-decoration: none;
}

.navbar-menu {
  display: flex;
  gap: 20px;
}

.navbar-item {
  color: white;
  text-decoration: none;
  padding: 5px 10px;
  border-radius: 4px;
  transition: background-color 0.3s;
}

.navbar-item:hover {
  background-color: rgba(255, 255, 255, 0.2);
}

.navbar-auth {
  display: flex;
  align-items: center;
  gap: 10px;
}

.user-name {
  margin-right: 10px;
}

.login-btn, .register-btn, .logout-btn {
  padding: 6px 12px;
  border-radius: 4px;
  text-decoration: none;
  font-weight: bold;
}

.login-btn, .logout-btn {
  background-color: transparent;
  border: 1px solid white;
  color: white;
}

.register-btn {
  background-color: white;
  color: #3498db;
  border: none;
}

.logout-btn {
  cursor: pointer;
}
</style>