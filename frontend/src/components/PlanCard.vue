<template>
  <div class="plan-card">
    <div class="plan-card-header">
      <h3>{{ title }}</h3>
      <span class="plan-day">第{{ dayNumber }}天</span>
    </div>
    <div class="plan-card-content">
      <div class="plan-schedule">
        <div v-for="(item, index) in schedule" :key="index" class="schedule-item">
          <div class="time">{{ item.time }}</div>
          <div class="activity">
            <h4>{{ item.place }}</h4>
            <p>{{ item.description }}</p>
          </div>
        </div>
      </div>
    </div>
    <div class="plan-card-footer">
      <div class="transportation">
        <span class="label">交通方式:</span>
        <span>{{ transportation }}</span>
      </div>
      <div class="tips" v-if="tips">
        <span class="label">小贴士:</span>
        <span>{{ tips }}</span>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'PlanCard',
  props: {
    title: {
      type: String,
      required: true
    },
    dayNumber: {
      type: Number,
      required: true
    },
    schedule: {
      type: Array,
      required: true,
      // 每个元素应该包含 time, place, description
      default: () => []
    },
    transportation: {
      type: String,
      default: '步行'
    },
    tips: {
      type: String,
      default: ''
    }
  }
};
</script>

<style scoped>
.plan-card {
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  margin-bottom: 20px;
  overflow: hidden;
}

.plan-card-header {
  background-color: #3498db;
  color: white;
  padding: 15px 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.plan-card-header h3 {
  margin: 0;
  font-size: 1.2rem;
}

.plan-day {
  background-color: rgba(255, 255, 255, 0.2);
  padding: 4px 10px;
  border-radius: 20px;
  font-size: 0.9rem;
}

.plan-card-content {
  padding: 20px;
}

.schedule-item {
  display: flex;
  margin-bottom: 15px;
  position: relative;
}

.schedule-item:not(:last-child)::after {
  content: '';
  position: absolute;
  left: 15px;
  top: 30px;
  bottom: -15px;
  width: 2px;
  background-color: #e0e0e0;
}

.time {
  min-width: 80px;
  font-weight: bold;
  color: #555;
  position: relative;
}

.time::after {
  content: '';
  position: absolute;
  width: 10px;
  height: 10px;
  background-color: #3498db;
  border-radius: 50%;
  right: 15px;
  top: 5px;
}

.activity {
  flex: 1;
  padding-left: 20px;
}

.activity h4 {
  margin: 0 0 5px 0;
  color: #333;
}

.activity p {
  margin: 0;
  color: #666;
  font-size: 0.9rem;
}

.plan-card-footer {
  background-color: #f9f9f9;
  padding: 15px 20px;
  border-top: 1px solid #eee;
  font-size: 0.9rem;
}

.transportation, .tips {
  margin-bottom: 5px;
}

.label {
  font-weight: bold;
  margin-right: 5px;
  color: #555;
}
</style>