<template>
  <div id="app">
    <nav class="navbar" v-if="$route.path !== '/login' && $route.path !== '/register'">
      <div class="nav-container">
        <router-link to="/" class="nav-brand">校园二手交易平台</router-link>
        <div class="nav-links">
          <router-link to="/">首页</router-link>
          <router-link to="/login" class="login-btn">登录</router-link>
          <router-link to="/register" class="register-btn">注册</router-link>
        </div>
      </div>
    </nav>
    <router-view />
  </div>
</template>

<script setup>
import { useRoute } from 'vue-router'

const $route = useRoute()
</script>

<style>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

body {
  font-family: -apple-system, BlinkMacSystemFont, 'Segue UI', Roboto, sans-serif;
  background-color: #f5f7fa;
}

#app {
  min-height: 100vh;
}

.navbar {
  background: white;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  padding: 0 20px;
}

.nav-container {
  max-width: 1200px;
  margin: 0 auto;
  display: flex;
  justify-content: space-between;
  align-items: center;
  height: 60px;
}

.nav-brand {
  font-size: 20px;
  font-weight: bold;
  color: #409eff;
  text-decoration: none;
}

.nav-links {
  display: flex;
  gap: 20px;
  align-items: center;
}

.nav-links a {
  color: #666;
  text-decoration: none;
  padding: 8px 16px;
  border-radius: 4px;
  transition: all 0.3s;
}

.nav-links a:hover {
  color: #409eff;
  background: #f0f7ff;
}

.login-btn {
  border: 1px solid #409eff;
  color: #409eff !important;
}

.register-btn {
  background: #409eff;
  color: white !important;
}

.register-btn:hover {
  background: #66b1ff !important;
  color: white !important;
}
</style>