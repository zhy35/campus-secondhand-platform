<template>
  <header class="navbar">
    <div class="nav-container">
      <div class="logo">
        <router-link to="/">
          <h1>校园二手平台</h1>
        </router-link>
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
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const user = ref(null)

onMounted(() => {
  const userData = localStorage.getItem('user')
  if (userData) {
    user.value = JSON.parse(userData)
  }
})

const handleLogout = () => {
  localStorage.removeItem('token')
  localStorage.removeItem('user')
  user.value = null
  router.push('/')
}
</script>

<style scoped>
.navbar {
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

.logo a {
  text-decoration: none;
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
}
</style>