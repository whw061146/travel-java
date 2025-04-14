<template>
  <div class="feedback-page">
    <div class="container">
      <div class="card feedback-card">
        <h2 class="text-center">用户反馈</h2>
        <div v-if="message" :class="['alert', messageType === 'success' ? 'alert-success' : 'alert-danger']">
          {{ message }}
        </div>
        <form @submit.prevent="submitFeedback" class="feedback-form">
          <div class="form-group">
            <label for="feedbackContent">反馈内容</label>
            <textarea 
              id="feedbackContent" 
              v-model="feedbackForm.content" 
              class="form-control" 
              rows="5" 
              required
              placeholder="请输入您的反馈内容（200字以内）"
              maxlength="200"
            ></textarea>
            <div class="char-count">{{ feedbackForm.content.length }}/200</div>
          </div>
          
          <div class="form-group">
            <label>满意度评分</label>
            <div class="rating">
              <div class="rating-container">
                <span 
                  v-for="star in 5" 
                  :key="star" 
                  class="star" 
                  :class="{ 'active': star <= feedbackForm.rating }"
                  @click="feedbackForm.rating = star"
                >
                  ★
                </span>
              </div>
              <span class="rating-text">{{ getRatingText() }}</span>
            </div>
          </div>
          
          <div class="form-group">
            <label for="feedbackType">反馈类型</label>
            <select id="feedbackType" v-model="feedbackForm.type" class="form-control">
              <option value="">请选择反馈类型</option>
              <option value="suggestion">功能建议</option>
              <option value="bug">问题反馈</option>
              <option value="experience">体验反馈</option>
              <option value="other">其他</option>
            </select>
          </div>
          
          <button type="submit" class="btn btn-primary btn-block" :disabled="loading || !isFormValid">
            {{ loading ? '提交中...' : '提交反馈' }}
          </button>
        </form>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: 'Feedback',
  data() {
    return {
      feedbackForm: {
        content: '',
        rating: 0,
        type: ''
      },
      message: '',
      messageType: '',
      loading: false
    };
  },
  computed: {
    isFormValid() {
      return this.feedbackForm.content.trim() !== '' && 
             this.feedbackForm.rating > 0 &&
             this.feedbackForm.type !== '';
    }
  },
  methods: {
    getRatingText() {
      const ratings = ['', '非常不满意', '不满意', '一般', '满意', '非常满意'];
      return this.feedbackForm.rating > 0 ? ratings[this.feedbackForm.rating] : '请选择评分';
    },
    async submitFeedback() {
      if (!this.isFormValid) {
        this.message = '请填写完整的反馈信息';
        this.messageType = 'error';
        return;
      }
      
      this.loading = true;
      try {
        // 获取用户ID（如果已登录）
        let userId = null;
        const userInfo = localStorage.getItem('userInfo');
        if (userInfo) {
          userId = JSON.parse(userInfo).id;
        }
        
        // 准备提交的数据
        const feedbackData = {
          content: this.feedbackForm.content,
          rating: this.feedbackForm.rating,
          type: this.feedbackForm.type,
          userId: userId // 如果未登录，userId为null，后端会处理为匿名反馈
        };
        
        // 调用后端反馈接口
        const response = await axios.post('/api/feedback', feedbackData);
        
        if (response.status === 200 || response.status === 201) {
          this.message = '感谢您的反馈！我们会认真考虑您的意见。';
          this.messageType = 'success';
          
          // 重置表单
          this.feedbackForm.content = '';
          this.feedbackForm.rating = 0;
          this.feedbackForm.type = '';
          
          // 3秒后清除成功消息
          setTimeout(() => {
            if (this.messageType === 'success') {
              this.message = '';
            }
          }, 3000);
        }
      } catch (error) {
        console.error('提交反馈失败:', error);
        this.message = '提交失败，请稍后再试';
        this.messageType = 'error';
      } finally {
        this.loading = false;
      }
    }
  }
};
</script>

<style scoped>
.feedback-page {
  padding: 40px 0;
  min-height: calc(100vh - 60px);
  display: flex;
  align-items: center;
  justify-content: center;
}

.feedback-card {
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  padding: 30px;
  width: 100%;
  max-width: 600px;
  margin: 0 auto;
}

.text-center {
  text-align: center;
}

.form-control {
  width: 100%;
  padding: 12px;
  border: 1px solid var(--border-color);
  border-radius: 4px;
  font-size: 16px;
  transition: border-color 0.3s;
}

textarea.form-control {
  resize: vertical;
  min-height: 120px;
}

.form-control:focus {
  border-color: var(--primary-color);
  outline: none;
}

.char-count {
  text-align: right;
  font-size: 14px;
  color: #666;
  margin-top: 5px;
}

.alert {
  padding: 12px;
  border-radius: 4px;
  margin-bottom: 20px;
}

.alert-success {
  background-color: #d4edda;
  color: #155724;
  border: 1px solid #c3e6cb;
}

.alert-danger {
  background-color: #f8d7da;
  color: #721c24;
  border: 1px solid #f5c6cb;
}

.rating {
  display: flex;
  align-items: center;
  margin-top: 10px;
}

.rating-container {
  display: flex;
  margin-right: 15px;
}

.star {
  font-size: 30px;
  color: #ddd;
  cursor: pointer;
  transition: color 0.2s;
  margin-right: 5px;
}

.star:hover,
.star.active {
  color: var(--accent-color);
}

.rating-text {
  font-size: 16px;
  color: #666;
}

.btn-block {
  width: 100%;
  margin-top: 20px;
}

.btn:disabled {
  background-color: #cccccc;
  cursor: not-allowed;
}
</style>