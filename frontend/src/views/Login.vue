<template>
  <div class="login-container">
    <div class="login-form">
      <h2>登录校园二手平台</h2>
      <form @submit.prevent="handleLogin">
        <div class="form-group">
          <label>用户名</label>
          <input
            v-model="form.username"
            type="text"
            placeholder="请输入用户名"
            required
          />
        </div>

        <div class="form-group">
          <label>密码</label>
          <input
            v-model="form.password"
            type="password"
            placeholder="请输入密码"
            required
          />
        </div>

        <button
          type="submit"
          :disabled="loading"
          :class="['submit-btn', { loading }]"
        >
          {{ loading ? '登录中...' : '登录' }}
        </button>

        <div class="register-link">
          还没有账号？<router-link to="/register">立即注册</router-link>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const loading = ref(false)

const form = reactive({
  username: '',
  password: ''
})

const handleLogin = async () => {
  // 表单验证
  if (!form.username.trim() || !form.password.trim()) {
    alert('请输入用户名和密码')
    return
  }

  loading.value = true

  try {
    const response = await fetch('https://hyaloplasmic-surprisedly-margie.ngrok-free.dev/api/api/auth/login', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({
        username: form.username,
        password: form.password
      })
    })

    const result = await response.json()

    if (result.success) {
      // 保存用户信息和 token
      localStorage.setItem('token', result.data.token)
      localStorage.setItem('user', JSON.stringify(result.data.user))

      // 显示成功消息
      alert('登录成功！')

      // 跳转到首页
      router.push('/')
    } else {
      alert('登录失败: ' + result.message)
    }
  } catch (error) {
    console.error('登录失败:', error)
    alert('网络错误，请稍后重试')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.login-container {
  min-height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 20px;
}

.login-form {
  background: white;
  padding: 40px;
  border-radius: 10px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.2);
  width: 100%;
  max-width: 400px;
}

h2 {
  text-align: center;
  margin-bottom: 30px;
  color: #333;
  font-size: 24px;
}

.form-group {
  margin-bottom: 20px;
}

label {
  display: block;
  margin-bottom: 5px;
  color: #555;
  font-weight: 500;
}

input {
  width: 100%;
  padding: 12px;
  border: 2px solid #e1e5e9;
  border-radius: 6px;
  font-size: 14px;
  transition: border-color 0.3s;
}

input:focus {
  outline: none;
  border-color: #409eff;
}

.submit-btn {
  width: 100%;
  padding: 12px;
  background: #409eff;
  color: white;
  border: none;
  border-radius: 6px;
  font-size: 16px;
  cursor: pointer;
  transition: background 0.3s;
}

.submit-btn:hover:not(:disabled) {
  background: #66b1ff;
}

.submit-btn:disabled {
  background: #c0c4cc;
  cursor: not-allowed;
}

.submit-btn.loading {
  background: #a0cfff;
}

.register-link {
  text-align: center;
  margin-top: 20px;
  color: #666;
}

.register-link a {
  color: #409eff;
  text-decoration: none;
}

.register-link a:hover {
  text-decoration: underline;
}
</style>
