<template>
  <div class="home">
    <!-- 导航栏 -->
    <header class="header">
      <div class="nav-container">
        <div class="logo">
          <h1>校园二手平台</h1>
        </div>
        <nav class="nav-links">
          <router-link to="/" class="nav-link">首页</router-link>
          <router-link to="/products" class="nav-link">浏览商品</router-link>
          <router-link to="/about" class="nav-link">关于我们</router-link>
        </nav>
        <div class="user-actions">
          <template v-if="user">
            <span class="welcome-text">欢迎，{{ user.username }}</span>
            <router-link to="/profile" class="btn outline">个人中心</router-link>
            <button @click="handleLogout" class="btn secondary">退出登录</button>
          </template>
          <template v-else>
            <router-link to="/login" class="btn outline">登录</router-link>
            <router-link to="/register" class="btn primary">注册</router-link>
          </template>
        </div>
      </div>
    </header>

    <!-- 主要内容 -->
    <main>
      <!-- 英雄区域 -->
      <section class="hero-section">
        <div class="hero-content">
          <h1 v-if="user">欢迎回来，{{ user.username }}！</h1>
          <h1 v-else>欢迎来到校园二手交易平台</h1>
          <p>买卖闲置物品，让校园生活更环保、更经济</p>
          <div class="cta-buttons">
            <template v-if="user">
              <router-link to="/products" class="btn primary large">浏览商品</router-link>
              <router-link to="/products/create" class="btn secondary large">发布商品</router-link>
            </template>
            <template v-else>
              <router-link to="/register" class="btn primary large">开始使用</router-link>
              <router-link to="/login" class="btn secondary large">立即登录</router-link>
            </template>
          </div>
        </div>
        <div class="hero-stats" v-if="user">
          <div class="stat-item">
            <div class="stat-number">12</div>
            <div class="stat-label">我的商品</div>
          </div>
          <div class="stat-item">
            <div class="stat-number">5</div>
            <div class="stat-label">待处理订单</div>
          </div>
          <div class="stat-item">
            <div class="stat-number">3</div>
            <div class="stat-label">新消息</div>
          </div>
        </div>
      </section>

      <!-- 功能特色 -->
      <section class="features-section">
        <div class="container">
          <h2>平台特色</h2>
          <div class="features-grid">
            <div class="feature-card">
              <div class="feature-icon">🛒</div>
              <h3>便捷交易</h3>
              <p>简单快捷的买卖流程，让闲置物品流通起来</p>
            </div>
            <div class="feature-card">
              <div class="feature-icon">🎓</div>
              <h3>校园认证</h3>
              <p>实名学生认证，交易更安全可靠</p>
            </div>
            <div class="feature-card">
              <div class="feature-icon">💰</div>
              <h3>经济实惠</h3>
              <p>低价购买优质物品，节省生活开支</p>
            </div>
            <div class="feature-card">
              <div class="feature-icon">🌱</div>
              <h3>环保共享</h3>
              <p>促进资源循环利用，共建绿色校园</p>
            </div>
          </div>
        </div>
      </section>

      <!-- 热门商品预览（登录后显示） -->
      <section class="products-preview" v-if="user">
        <div class="container">
          <div class="section-header">
            <h2>推荐商品</h2>
            <router-link to="/products" class="view-all">查看全部 →</router-link>
          </div>
          <div class="products-grid">
            <!-- 这里可以后续添加真实的商品数据 -->
            <div class="product-card placeholder">
              <div class="product-image"></div>
              <div class="product-info">
                <h3>商品名称</h3>
                <p class="price">¥99</p>
                <p class="description">商品描述信息...</p>
              </div>
            </div>
            <div class="product-card placeholder">
              <div class="product-image"></div>
              <div class="product-info">
                <h3>商品名称</h3>
                <p class="price">¥199</p>
                <p class="description">商品描述信息...</p>
              </div>
            </div>
            <div class="product-card placeholder">
              <div class="product-image"></div>
              <div class="product-info">
                <h3>商品名称</h3>
                <p class="price">¥299</p>
                <p class="description">商品描述信息...</p>
              </div>
            </div>
          </div>
        </div>
      </section>
    </main>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const user = ref(null)

onMounted(() => {
  // 从 localStorage 获取用户信息
  const userData = localStorage.getItem('user')
  if (userData) {
    user.value = JSON.parse(userData)
  }
})

const handleLogout = () => {
  localStorage.removeItem('token')
  localStorage.removeItem('user')
  user.value = null
  // 可以添加退出成功的提示
  alert('已成功退出登录')
}
</script>

<style scoped>
.home {
  min-height: 100vh;
}

/* 导航栏样式 */
.header {
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
  border-bottom: 1px solid #e1e5e9;
  position: sticky;
  top: 0;
  z-index: 100;
}

.nav-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  height: 70px;
}

.logo h1 {
  color: #333;
  margin: 0;
  font-size: 1.5rem;
}

.nav-links {
  display: flex;
  gap: 2rem;
}

.nav-link {
  color: #333;
  text-decoration: none;
  font-weight: 500;
  transition: color 0.3s;
}

.nav-link:hover,
.nav-link.router-link-active {
  color: #409eff;
}

.user-actions {
  display: flex;
  align-items: center;
  gap: 1rem;
}

.welcome-text {
  color: #666;
  font-size: 0.9rem;
}

/* 按钮样式 */
.btn {
  padding: 8px 16px;
  border-radius: 6px;
  text-decoration: none;
  font-weight: 500;
  transition: all 0.3s;
  border: none;
  cursor: pointer;
  font-size: 0.9rem;
}

.btn.outline {
  background: transparent;
  color: #409eff;
  border: 1px solid #409eff;
}

.btn.outline:hover {
  background: #409eff;
  color: white;
}

.btn.primary {
  background: #409eff;
  color: white;
}

.btn.primary:hover {
  background: #66b1ff;
}

.btn.secondary {
  background: #f56c6c;
  color: white;
}

.btn.secondary:hover {
  background: #f78989;
}

.btn.large {
  padding: 12px 30px;
  font-size: 1rem;
}

/* 英雄区域 */
.hero-section {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  padding: 80px 20px;
  position: relative;
}

.hero-content {
  max-width: 1200px;
  margin: 0 auto;
  text-align: center;
}

.hero-section h1 {
  font-size: 3rem;
  margin-bottom: 20px;
  font-weight: bold;
}

.hero-section p {
  font-size: 1.2rem;
  margin-bottom: 40px;
  opacity: 0.9;
}

.cta-buttons {
  display: flex;
  gap: 20px;
  justify-content: center;
  flex-wrap: wrap;
}

.hero-stats {
  display: flex;
  justify-content: center;
  gap: 3rem;
  margin-top: 3rem;
  padding-top: 3rem;
  border-top: 1px solid rgba(255, 255, 255, 0.2);
}

.stat-item {
  text-align: center;
}

.stat-number {
  font-size: 2.5rem;
  font-weight: bold;
  margin-bottom: 0.5rem;
}

.stat-label {
  font-size: 0.9rem;
  opacity: 0.8;
}

/* 功能特色区域 */
.features-section {
  padding: 80px 20px;
  background: white;
}

.container {
  max-width: 1200px;
  margin: 0 auto;
}

.features-section h2 {
  text-align: center;
  font-size: 2.5rem;
  margin-bottom: 60px;
  color: #333;
}

.features-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 40px;
}

.feature-card {
  text-align: center;
  padding: 40px 20px;
  border-radius: 10px;
  background: #f8f9fa;
  transition: transform 0.3s;
}

.feature-card:hover {
  transform: translateY(-5px);
}

.feature-icon {
  font-size: 3rem;
  margin-bottom: 20px;
}

.feature-card h3 {
  font-size: 1.5rem;
  margin-bottom: 15px;
  color: #333;
}

.feature-card p {
  color: #666;
  line-height: 1.6;
}

/* 商品预览区域 */
.products-preview {
  padding: 80px 20px;
  background: #f8f9fa;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 40px;
}

.section-header h2 {
  color: #333;
  margin: 0;
}

.view-all {
  color: #409eff;
  text-decoration: none;
  font-weight: 500;
}

.view-all:hover {
  text-decoration: underline;
}

.products-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
  gap: 30px;
}

.product-card.placeholder {
  background: white;
  border-radius: 10px;
  padding: 20px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

.product-image {
  height: 200px;
  background: #e1e5e9;
  border-radius: 8px;
  margin-bottom: 15px;
}

.product-info h3 {
  margin: 0 0 10px 0;
  color: #333;
}

.price {
  font-size: 1.2rem;
  font-weight: bold;
  color: #f56c6c;
  margin: 0 0 10px 0;
}

.description {
  color: #666;
  margin: 0;
  font-size: 0.9rem;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .nav-container {
    flex-direction: column;
    height: auto;
    padding: 1rem 20px;
  }

  .nav-links {
    margin: 1rem 0;
  }

  .user-actions {
    margin-top: 1rem;
  }

  .hero-section h1 {
    font-size: 2rem;
  }

  .hero-section p {
    font-size: 1rem;
  }

  .cta-buttons {
    flex-direction: column;
    align-items: center;
  }

  .btn.large {
    width: 200px;
  }

  .hero-stats {
    flex-direction: column;
    gap: 1rem;
  }

  .section-header {
    flex-direction: column;
    gap: 1rem;
    text-align: center;
  }
}
</style>