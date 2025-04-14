import axios from 'axios';
import router from '../router';
import { Message } from './message';

// 创建axios实例
const api = axios.create({
  baseURL: 'http://localhost:8080/api', // 后端API基础URL
  timeout: 10000, // 请求超时时间
  headers: {
    'Content-Type': 'application/json'
  }
});

// 请求拦截器
api.interceptors.request.use(
  config => {
    // 从localStorage获取token
    const token = localStorage.getItem('token');
    // 如果有token，则添加到请求头
    if (token) {
      config.headers['Authorization'] = `Bearer ${token}`;
    }
    return config;
  },
  error => {
    // 请求错误处理
    console.error('Request error:', error);
    return Promise.reject(error);
  }
);

// 响应拦截器
api.interceptors.response.use(
  response => {
    // 统一处理响应数据格式
    const res = response.data;
    
    // 如果后端返回的数据符合约定格式
    if (res.code !== undefined) {
      // 如果请求成功
      if (res.code === 0) {
        return res.data;
      } else {
        // 业务逻辑错误
        const errorMsg = res.message || '请求失败';
        // 使用消息提示组件显示错误
        Message({
          content: errorMsg,
          type: 'error'
        });
        return Promise.reject(new Error(errorMsg));
      }
    }
    
    // 如果后端返回的不是约定格式，直接返回数据
    return response.data;
  },
  error => {
    // 处理HTTP错误状态
    if (error.response) {
      const status = error.response.status;
      
      // 处理401未授权错误（token过期或无效）
      if (status === 401) {
        // 清除本地token
        localStorage.removeItem('token');
        // 跳转到登录页
        router.push('/login');
        Message({
          content: '登录已过期，请重新登录',
          type: 'warning'
        });
      } else if (status === 403) {
        Message({
          content: '没有权限访问该资源',
          type: 'error'
        });
      } else if (status === 404) {
        Message({
          content: '请求的资源不存在',
          type: 'error'
        });
      } else if (status === 500) {
        Message({
          content: '服务器内部错误',
          type: 'error'
        });
      } else {
        Message({
          content: `请求错误: ${error.message}`,
          type: 'error'
        });
      }
    } else if (error.request) {
      // 请求发出但没有收到响应
      Message({
        content: '网络错误，服务器未响应',
        type: 'error'
      });
    } else {
      // 请求配置出错
      Message({
        content: `请求配置错误: ${error.message}`,
        type: 'error'
      });
    }
    
    return Promise.reject(error);
  }
);

export default api;