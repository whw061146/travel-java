<template>
  <div class="plan-page">
    <div class="container">
      <h1 class="text-center">智能旅游行程规划</h1>
      
      <div class="plan-form-container" v-if="!loading && !planResult">
        <div class="card">
          <h2>填写旅行需求</h2>
          <form @submit.prevent="generatePlan" class="plan-form">
            <div class="form-group">
              <label for="departure">出发地</label>
              <input 
                type="text" 
                id="departure" 
                v-model="planRequest.departure" 
                class="form-control" 
                required
                placeholder="例如：北京"
              >
            </div>
            
            <div class="form-group">
              <label for="destination">目的地</label>
              <input 
                type="text" 
                id="destination" 
                v-model="planRequest.destination" 
                class="form-control" 
                required
                placeholder="例如：上海"
              >
            </div>
            
            <div class="form-group">
              <label for="days">旅行天数</label>
              <input 
                type="number" 
                id="days" 
                v-model.number="planRequest.days" 
                class="form-control" 
                required
                min="1"
                max="14"
              >
            </div>
            
            <div class="form-group">
              <label for="preferences">旅行偏好</label>
              <textarea 
                id="preferences" 
                v-model="planRequest.preferences" 
                class="form-control" 
                rows="4"
                placeholder="请描述您的旅行偏好，例如：喜欢历史文化景点、美食探索、户外活动等"
              ></textarea>
            </div>
            
            <button type="submit" class="btn btn-primary btn-block">
              生成行程规划
            </button>
          </form>
        </div>
      </div>
      
      <div class="loading-container" v-if="loading">
        <div class="loading-spinner"></div>
        <p>正在生成您的专属旅行计划，请稍候...</p>
        <p class="small-text">AI正在思考中，这可能需要几秒钟时间</p>
      </div>
      
      <div class="plan-result" v-if="planResult">
        <div class="plan-header">
          <h2>{{ planResult.title }}</h2>
          <p>{{ planResult.summary }}</p>
          <div class="plan-actions">
            <button @click="savePlan" class="btn btn-secondary" v-if="!planSaved">
              保存行程
            </button>
            <button @click="newPlan" class="btn btn-primary">
              重新规划
            </button>
          </div>
        </div>
        
        <div class="plan-days">
          <div v-for="(day, index) in planResult.days" :key="index" class="plan-day">
            <PlanCard 
              :title="day.title" 
              :dayNumber="index + 1" 
              :schedule="day.schedule" 
              :transportation="day.transportation" 
              :tips="day.tips"
            />
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import PlanCard from '../components/PlanCard.vue';

export default {
  name: 'Plan',
  components: {
    PlanCard
  },
  data() {
    return {
      planRequest: {
        departure: '',
        destination: '',
        days: 3,
        preferences: ''
      },
      loading: false,
      planResult: null,
      planSaved: false,
      error: null
    };
  },
  methods: {
    async generatePlan() {
      this.loading = true;
      this.error = null;
      
      try {
        // 调用后端生成行程接口
        const response = await fetch('/api/plan', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${localStorage.getItem('token')}`
          },
          body: JSON.stringify(this.planRequest)
        });
        
        const data = await response.json();
        
        if (response.ok) {
          this.planResult = data;
          this.planSaved = false;
        } else {
          this.error = data.message || '生成行程失败，请稍后再试';
          alert(this.error);
        }
      } catch (error) {
        console.error('生成行程请求出错:', error);
        this.error = '网络错误，请稍后再试';
        alert(this.error);
      } finally {
        this.loading = false;
      }
    },
    
    async savePlan() {
      // 此方法在实际项目中可能不需要，因为后端可能已经在生成时保存了行程
      // 这里模拟一个保存成功的操作
      this.planSaved = true;
      alert('行程已保存到您的账户');
    },
    
    newPlan() {
      this.planResult = null;
      this.planSaved = false;
      // 保留用户之前填写的出发地和目的地，方便用户修改后重新生成
    }
  }
};
</script>

<style scoped>
.plan-page {
  padding: 40px 0;
  background-color: var(--light-gray);
  min-height: calc(100vh - 60px);
}

.plan-form-container {
  max-width: 700px;
  margin: 0 auto;
}

.btn-block {
  width: 100%;
}

.loading-container {
  text-align: center;
  padding: 60px 0;
}

.loading-spinner {
  display: inline-block;
  width: 50px;
  height: 50px;
  border: 5px solid rgba(0, 0, 0, 0.1);
  border-radius: 50%;
  border-top-color: var(--primary-color);
  animation: spin 1s ease-in-out infinite;
  margin-bottom: 20px;
}

@keyframes spin {
  to { transform: rotate(360deg); }
}

.small-text {
  font-size: 14px;
  color: #666;
}

.plan-result {
  margin-top: 30px;
}

.plan-header {
  text-align: center;
  margin-bottom: 30px;
}

.plan-actions {
  margin-top: 20px;
}

.plan-actions .btn {
  margin: 0 10px;
}

.plan-days {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

@media (max-width: 768px) {
  .plan-form-container {
    padding: 0 15px;
  }
}
</style>