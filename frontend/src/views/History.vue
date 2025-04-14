<template>
  <div class="history-page">
    <div class="container">
      <h1 class="text-center">我的历史行程</h1>
      
      <div v-if="loading" class="loading-container">
        <div class="loading-spinner"></div>
        <p>正在加载历史记录...</p>
      </div>
      
      <div v-else-if="error" class="alert alert-danger">
        {{ error }}
      </div>
      
      <div v-else-if="historyList.length === 0" class="empty-history">
        <div class="card">
          <p>您还没有任何行程规划记录</p>
          <router-link to="/plan" class="btn btn-primary">开始规划行程</router-link>
        </div>
      </div>
      
      <div v-else class="history-list">
        <div v-for="(item, index) in historyList" :key="item.id" class="history-item card">
          <div class="history-item-header" @click="toggleDetails(index)">
            <div class="history-item-info">
              <h3>{{ item.destination }} 之旅</h3>
              <div class="history-item-meta">
                <span>出发地: {{ item.departure }}</span>
                <span>天数: {{ item.days }}天</span>
                <span>创建时间: {{ formatDate(item.createTime) }}</span>
              </div>
            </div>
            <div class="history-item-actions">
              <button class="btn btn-secondary" @click.stop="viewDetails(item)">
                {{ expandedIndex === index ? '收起详情' : '查看详情' }}
              </button>
              <button class="btn btn-primary" @click.stop="replan(item)">
                重新规划
              </button>
            </div>
          </div>
          
          <div v-if="expandedIndex === index" class="history-item-details">
            <div v-if="selectedPlan && selectedPlan.id === item.id" class="plan-details">
              <div class="plan-header">
                <h2>{{ selectedPlan.title || `${selectedPlan.destination}${selectedPlan.days}日游` }}</h2>
                <p>{{ selectedPlan.summary || `从${selectedPlan.departure}到${selectedPlan.destination}的${selectedPlan.days}天行程` }}</p>
              </div>
              
              <div class="plan-days" v-if="selectedPlan.days && selectedPlan.days.length > 0">
                <div v-for="(day, dayIndex) in selectedPlan.days" :key="dayIndex" class="plan-day">
                  <PlanCard 
                    :title="day.title" 
                    :dayNumber="dayIndex + 1" 
                    :schedule="day.schedule" 
                    :transportation="day.transportation" 
                    :tips="day.tips"
                  />
                </div>
              </div>
              <div v-else class="plan-content">
                <pre>{{ selectedPlan.content }}</pre>
              </div>
            </div>
            <div v-else class="loading-container">
              <div class="loading-spinner"></div>
              <p>正在加载行程详情...</p>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import PlanCard from '../components/PlanCard.vue';

export default {
  name: 'History',
  components: {
    PlanCard
  },
  data() {
    return {
      historyList: [],
      loading: true,
      error: null,
      expandedIndex: -1,
      selectedPlan: null
    };
  },
  created() {
    this.fetchHistoryList();
  },
  methods: {
    async fetchHistoryList() {
      this.loading = true;
      this.error = null;
      
      try {
        const response = await fetch('/api/history', {
          method: 'GET',
          headers: {
            'Authorization': `Bearer ${localStorage.getItem('token')}`
          }
        });
        
        const data = await response.json();
        
        if (response.ok) {
          this.historyList = data;
        } else {
          this.error = data.message || '获取历史记录失败';
        }
      } catch (error) {
        console.error('获取历史记录出错:', error);
        this.error = '网络错误，请稍后再试';
      } finally {
        this.loading = false;
      }
    },
    
    toggleDetails(index) {
      if (this.expandedIndex === index) {
        this.expandedIndex = -1;
        this.selectedPlan = null;
      } else {
        this.expandedIndex = index;
        this.viewDetails(this.historyList[index]);
      }
    },
    
    async viewDetails(item) {
      if (this.selectedPlan && this.selectedPlan.id === item.id) {
        return; // 已经加载了该行程的详情
      }
      
      this.selectedPlan = null;
      
      try {
        const response = await fetch(`/api/plan/${item.id}`, {
          method: 'GET',
          headers: {
            'Authorization': `Bearer ${localStorage.getItem('token')}`
          }
        });
        
        const data = await response.json();
        
        if (response.ok) {
          this.selectedPlan = data;
        } else {
          alert(data.message || '获取行程详情失败');
        }
      } catch (error) {
        console.error('获取行程详情出错:', error);
        alert('网络错误，请稍后再试');
      }
    },
    
    replan(item) {
      // 将行程信息传递给Plan组件进行重新规划
      this.$router.push({
        path: '/plan',
        query: {
          departure: item.departure,
          destination: item.destination,
          days: item.days,
          preferences: item.preferences
        }
      });
    },
    
    formatDate(dateString) {
      if (!dateString) return '';
      
      const date = new Date(dateString);
      return date.toLocaleString('zh-CN', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit',
        hour: '2-digit',
        minute: '2-digit'
      });
    }
  }
};
</script>

<style scoped>
.history-page {
  padding: 40px 0;
  min-height: calc(100vh - 60px);
}

.empty-history {
  text-align: center;
  padding: 40px 0;
}

.empty-history p {
  margin-bottom: 20px;
  font-size: 18px;
  color: #666;
}

.history-list {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.history-item {
  transition: all 0.3s ease;
}

.history-item-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px 20px;
  cursor: pointer;
}

.history-item-info h3 {
  margin: 0 0 10px 0;
  color: var(--primary-color);
}

.history-item-meta {
  display: flex;
  flex-wrap: wrap;
  gap: 15px;
  font-size: 14px;
  color: #666;
}

.history-item-actions {
  display: flex;
  gap: 10px;
}

.history-item-details {
  padding: 0 20px 20px 20px;
  border-top: 1px solid var(--border-color);
}

.loading-container {
  text-align: center;
  padding: 40px 0;
}

.loading-spinner {
  display: inline-block;
  width: 40px;
  height: 40px;
  border: 4px solid rgba(0, 0, 0, 0.1);
  border-radius: 50%;
  border-top-color: var(--primary-color);
  animation: spin 1s ease-in-out infinite;
  margin-bottom: 15px;
}

@keyframes spin {
  to { transform: rotate(360deg); }
}

.plan-header {
  text-align: center;
  margin-bottom: 30px;
  padding-top: 20px;
}

.plan-days {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.plan-content {
  background-color: #f8f9fa;
  padding: 15px;
  border-radius: 5px;
  white-space: pre-wrap;
  font-family: inherit;
  line-height: 1.6;
}

@media (max-width: 768px) {
  .history-item-header {
    flex-direction: column;
    align-items: flex-start;
  }
  
  .history-item-actions {
    margin-top: 15px;
    width: 100%;
    justify-content: flex-end;
  }
}
</style>