import { createRouter, createWebHistory } from 'vue-router'
import Home from '../views/Home.vue'
import Login from '../views/Login.vue'
import Register from '../views/Register.vue'
import Plan from '../views/Plan.vue'
import History from '../views/History.vue'
import Feedback from '../views/Feedback.vue'

const routes = [
  {
    path: '/',
    name: 'Home',
    component: Home
  },
  {
    path: '/login',
    name: 'Login',
    component: Login
  },
  {
    path: '/register',
    name: 'Register',
    component: Register
  },
  {
    path: '/plan',
    name: 'Plan',
    component: Plan,
    meta: { requiresAuth: true }
  },
  {
    path: '/history',
    name: 'History',
    component: History,
    meta: { requiresAuth: true }
  },
  {
    path: '/feedback',
    name: 'Feedback',
    component: Feedback,
    meta: { requiresAuth: true }
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由守卫：检查用户是否已登录
router.beforeEach((to, from, next) => {
  // 检查路由是否需要认证
  if (to.matched.some(record => record.meta.requiresAuth)) {
    // 检查用户是否已登录（通过localStorage中的token或userInfo判断）
    const isLoggedIn = localStorage.getItem('token') || localStorage.getItem('userInfo')
    
    if (!isLoggedIn) {
      // 未登录，重定向到登录页
      next({
        path: '/login',
        query: { redirect: to.fullPath } // 保存原目标路径，登录后可跳回
      })
    } else {
      // 已登录，允许访问
      next()
    }
  } else {
    // 不需要认证的路由，直接访问
    next()
  }
})

export default router