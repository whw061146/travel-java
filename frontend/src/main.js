import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import axios from 'axios'
import api from './utils/api'

// 配置axios全局默认值
axios.defaults.baseURL = 'http://localhost:8080/api'
axios.defaults.timeout = 10000

// 将api实例挂载到全局
const app = createApp(App)

app.config.globalProperties.$axios = axios
app.config.globalProperties.$api = api

app.use(router)
app.use(store)
app.mount('#app')