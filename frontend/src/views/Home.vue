<template>
  <div class="home">
    <div class="hero">
      <div class="container">
        <h1>智能旅游规划器</h1>
        <p>使用AI技术，为您量身定制完美旅行计划</p>
        <router-link to="/plan" class="btn btn-primary">开始规划</router-link>
      </div>
    </div>
    
    <div class="features container">
      <h2 class="text-center">我们的特色</h2>
      <div class="feature-grid">
        <div class="feature-card">
          <div class="feature-icon">🤖</div>
          <h3>AI智能推荐</h3>
          <p>基于Kimi AI技术，根据您的偏好智能推荐最适合的旅游路线</p>
        </div>
        <div class="feature-card">
          <div class="feature-icon">📅</div>
          <h3>个性化行程</h3>
          <p>根据您的时间、预算和兴趣，定制专属旅游计划</p>
        </div>
        <div class="feature-card">
          <div class="feature-icon">📍</div>
          <h3>景点详情</h3>
          <p>提供详细的景点介绍、参观建议和交通指南</p>
        </div>
        <div class="feature-card">
          <div class="feature-icon">📝</div>
          <h3>历史记录</h3>
          <p>保存您的历史规划，随时查看和修改</p>
        </div>
      </div>
    </div>
    
    <div class="how-it-works container">
      <h2 class="text-center">如何使用</h2>
      <div class="steps">
        <div class="step">
          <div class="step-number">1</div>
          <div class="step-content">
            <h3>注册账号</h3>
            <p>创建您的个人账号，开始使用我们的服务</p>
          </div>
        </div>
        <div class="step">
          <div class="step-number">2</div>
          <div class="step-content">
            <h3>填写需求</h3>
            <p>告诉我们您的目的地、旅行天数和个人偏好</p>
          </div>
        </div>
        <div class="step">
          <div class="step-number">3</div>
          <div class="step-content">
            <h3>获取规划</h3>
            <p>AI将为您生成详细的旅游行程规划</p>
          </div>
        </div>
        <div class="step">
          <div class="step-number">4</div>
          <div class="step-content">
            <h3>出发旅行</h3>
            <p>按照规划开始您的完美旅程</p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Home'
}
</script>

<style scoped>
.hero {
  background-image: linear-gradient(rgba(0, 0, 0, 0.5), rgba(0, 0, 0, 0.5)), url('https://source.unsplash.com/random/1600x900/?travel');
  background-size: cover;
  background-position: center;
  color: white;
  padding: 100px 0;
  text-align: center;
}

.hero h1 {
  font-size: 3rem;
  margin-bottom: 20px;
}

.hero p {
  font-size: 1.5rem;
  margin-bottom: 30px;
}

.features {
  padding: 60px 0;
}

.feature-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 30px;
  margin-top: 40px;
}

.feature-card {
  background-color: white;
  border-radius: 8px;
  padding: 30px;
  text-align: center;
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
  transition: transform 0.3s;
}

.feature-card:hover {
  transform: translateY(-10px);
}

.feature-icon {
  font-size: 3rem;
  margin-bottom: 20px;
}

.how-it-works {
  padding: 60px 0;
  background-color: #f9f9f9;
}

.steps {
  margin-top: 40px;
}

.step {
  display: flex;
  align-items: center;
  margin-bottom: 30px;
}

.step-number {
  width: 50px;
  height: 50px;
  background-color: var(--primary-color);
  color: white;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 1.5rem;
  font-weight: bold;
  margin-right: 20px;
  flex-shrink: 0;
}

.step-content {
  flex-grow: 1;
}

.step-content h3 {
  margin-top: 0;
}
</style>