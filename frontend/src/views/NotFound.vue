<template>
  <div class="not-found-page">
    <NavBar />

    <div class="container">
      <div class="error-content">
        <div class="error-icon">404</div>
        <h1>页面未找到</h1>
        <p>抱歉，您访问的页面不存在或已被移除</p>
        <div class="action-buttons">
          <router-link to="/" class="btn primary">返回首页</router-link>
          <router-link to="/products" class="btn outline">浏览商品</router-link>
          <button @click="goBack" class="btn outline">返回上页</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'
import NavBar from '../components/NavBar.vue'

const router = useRouter()

const goBack = () => {
  router.back()
}
</script>

<style scoped>
.not-found-page {
  min-height: 100vh;
  background: #f8f9fa;
}

.container {
  max-width: 600px;
  margin: 0 auto;
  padding: 2rem 20px;
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: calc(100vh - 140px);
}

.error-content {
  text-align: center;
  padding: 3rem 2rem;
}

.error-icon {
  font-size: 8rem;
  font-weight: bold;
  color: #409eff;
  margin-bottom: 1rem;
  opacity: 0.8;
}

.error-content h1 {
  color: #333;
  margin-bottom: 1rem;
  font-size: 2rem;
}

.error-content p {
  color: #666;
  font-size: 1.1rem;
  margin-bottom: 2rem;
}

.action-buttons {
  display: flex;
  gap: 1rem;
  justify-content: center;
  flex-wrap: wrap;
}

.btn {
  padding: 0.75rem 1.5rem;
  border-radius: 6px;
  text-decoration: none;
  font-weight: 500;
  transition: all 0.3s;
  border: none;
  cursor: pointer;
  font-size: 1rem;
}

.btn.primary {
  background: #409eff;
  color: white;
}

.btn.primary:hover {
  background: #66b1ff;
}

.btn.outline {
  background: transparent;
  color: #409eff;
  border: 2px solid #409eff;
}

.btn.outline:hover {
  background: #409eff;
  color: white;
}

@media (max-width: 768px) {
  .container {
    padding: 1rem 15px;
  }

  .error-icon {
    font-size: 6rem;
  }

  .error-content h1 {
    font-size: 1.5rem;
  }

  .action-buttons {
    flex-direction: column;
    align-items: center;
  }

  .btn {
    width: 200px;
  }
}
</style>