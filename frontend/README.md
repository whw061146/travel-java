# 智能旅游规划器前端项目

## 与后端交互

前端通过axios发起HTTP请求。在`main.js`中配置axios默认`baseURL`指向后端API（如`http://localhost:8080/api`）。

### JWT认证

项目使用JWT认证机制，具体实现如下：

- 在`utils/api.js`中配置axios请求拦截器，在有token时为每个请求添加`Authorization: Bearer <token>`头部
- 响应拦截器处理token过期的情况（如遇401未授权，自动清除token并跳转登录页面）
- 登录成功后，后端返回的token存储在localStorage中，并更新Vuex store状态
- 用户登出时，清除localStorage中的token和用户信息

### 数据格式处理

前端统一处理后端返回的数据格式。约定后端所有成功响应包装为 `{ code:0, data: ..., message: "OK" }` 形式，这样前端可以统一判断`code`是否为0来确定请求是否成功。若失败（`code != 0`），统一显示`message`错误信息。

在`utils/api.js`中的响应拦截器实现了这一逻辑：

```javascript
api.interceptors.response.use(
  response => {
    const res = response.data;
    if (res.code !== undefined) {
      if (res.code === 0) {
        return res.data;
      } else {
        const errorMsg = res.message || '请求失败';
        console.error(errorMsg);
        return Promise.reject(new Error(errorMsg));
      }
    }
    return response.data;
  },
  // 错误处理...
);
```

### 错误处理

前端实现了完善的错误处理机制：

- **表单验证提示**：在各表单组件中实现必填项验证、格式验证等，并高亮错误字段
- **网络错误处理**：在api.js的响应拦截器中统一处理各类HTTP错误状态
  - 401：未授权，清除token并跳转登录页
  - 403：无权限访问
  - 404：资源不存在
  - 500：服务器内部错误
  - 网络连接问题：显示"网络错误，服务器未响应"

### 使用示例

在Vue组件中使用封装的API：

```javascript
// 登录
async login() {
  try {
    const response = await this.$api.post('/login', {
      username: this.username,
      password: this.password
    });
    // 登录成功，存储token和用户信息
    this.$store.dispatch('login', {
      token: response.token,
      user: response.user
    });
    this.$router.push('/');
  } catch (error) {
    // 错误已在拦截器中处理
  }
}

// 获取数据
async fetchData() {
  try {
    this.loading = true;
    const data = await this.$api.get('/some-endpoint');
    this.items = data;
  } catch (error) {
    // 错误已在拦截器中处理
  } finally {
    this.loading = false;
  }
}
```