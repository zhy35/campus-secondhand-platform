<template>
  <div class="register-container">
    <div class="register-form">
      <h2>注册校园二手平台</h2>
      <form @submit.prevent="handleRegister">
        <div class="form-group">
          <label>用户名 *</label>
          <input
            v-model="form.username"
            type="text"
            placeholder="请输入用户名（3-50个字符）"
            @blur="checkUsername"
            @input="onUsernameInput"
            required
          />
          <span v-if="form.username && form.username.length < 3" class="error">
            ✗ 用户名至少需要3个字符
          </span>
          <span v-else-if="form.username && form.username.length > 50" class="error">
            ✗ 用户名不能超过50个字符
          </span>
          <span v-else-if="usernameStatus === 'available'" class="success">
            ✓ 用户名可用
          </span>
          <span v-else-if="usernameStatus === 'taken'" class="error">
            ✗ 用户名已存在
          </span>
          <span v-else-if="usernameStatus === 'checking'" class="info">
            ⏳ 检查中...
          </span>
        </div>

        <div class="form-group">
          <label>密码 *</label>
          <input
            v-model="form.password"
            type="password"
            placeholder="请输入密码（至少6位）"
            autocomplete="new-password"
            required
          />
          <span v-if="form.password && form.password.length < 6" class="error">
            ✗ 密码至少需要6位
          </span>
        </div>

        <div class="form-group">
          <label>确认密码 *</label>
          <input
            v-model="form.confirmPassword"
            type="password"
            placeholder="请再次输入密码"
            autocomplete="new-password"
            required
          />
          <span v-if="form.password && form.confirmPassword && form.password !== form.confirmPassword" class="error">
            ✗ 两次输入的密码不一致
          </span>
        </div>

        <div class="form-group">
          <label>邮箱 *</label>
          <input
            v-model="form.email"
            type="email"
            placeholder="请输入邮箱"
            @blur="checkEmail"
            required
          />
          <span v-if="emailStatus === 'available'" class="success">
            ✓ 邮箱可用
          </span>
          <span v-else-if="emailStatus === 'taken'" class="error">
            ✗ 邮箱已被注册
          </span>
          <span v-else-if="emailStatus === 'checking'" class="info">
            ⏳ 检查中...
          </span>
        </div>

        <div class="form-group">
          <label>手机号</label>
          <input
            v-model="form.phone"
            type="tel"
            placeholder="请输入手机号"
          />
        </div>

        <div class="form-group">
          <label>真实姓名 *</label>
          <input
            v-model="form.realName"
            type="text"
            placeholder="请输入真实姓名"
            required
          />
        </div>

        <div class="form-group">
          <label>学号 *</label>
          <input
            v-model="form.studentId"
            type="text"
            placeholder="请输入学号"
            required
          />
        </div>

        <button
          type="submit"
          :disabled="!isFormValid || loading"
          :class="['submit-btn', { loading }]"
        >
          {{ loading ? '注册中...' : '立即注册' }}
        </button>

        <div class="login-link">
          已有账号？<router-link to="/login">立即登录</router-link>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, reactive } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const loading = ref(false)
const usernameStatus = ref('')
const emailStatus = ref('')

const form = reactive({
  username: '',
  password: '',
  confirmPassword: '',
  email: '',
  phone: '',
  realName: '',
  studentId: ''
})

// 检查表单是否有效
const isFormValid = computed(() => {
  const basicValid = form.username &&
         form.password &&
         form.confirmPassword &&
         form.email &&
         form.realName &&
         form.studentId &&
         form.password === form.confirmPassword

  const usernameValid = form.username.length >= 3 && form.username.length <= 50
  const passwordValid = form.password.length >= 6

  return basicValid && usernameValid && passwordValid &&
         usernameStatus.value === 'available' &&
         emailStatus.value === 'available'
})

// 用户名输入时实时验证长度
const onUsernameInput = () => {
  if (form.username.length < 3 || form.username.length > 50) {
    usernameStatus.value = 'invalid'
  } else {
    checkUsername()
  }
}

// 检查用户名是否可用
const checkUsername = async () => {
  if (!form.username) {
    usernameStatus.value = ''
    return
  }

  if (form.username.length < 3 || form.username.length > 50) {
    usernameStatus.value = 'invalid'
    return
  }

  usernameStatus.value = 'checking'

  try {
    const response = await fetch(`https://hyaloplasmic-surprisedly-margie.ngrok-free.dev/api/api/user/check-username?username=${encodeURIComponent(form.username)}`)
    if (response.ok) {
      const result = await response.json()
      usernameStatus.value = result.data ? 'taken' : 'available'
    } else {
      usernameStatus.value = 'available'
    }
  } catch (error) {
    console.error('检查用户名失败:', error)
    usernameStatus.value = 'available'
  }
}

// 检查邮箱是否可用
const checkEmail = async () => {
  if (!form.email) {
    emailStatus.value = ''
    return
  }

  emailStatus.value = 'checking'

  try {
    const response = await fetch(`https://hyaloplasmic-surprisedly-margie.ngrok-free.dev/api/api/user/check-email?email=${encodeURIComponent(form.email)}`)
    if (response.ok) {
      const result = await response.json()
      emailStatus.value = result.data ? 'taken' : 'available'
    } else {
      emailStatus.value = 'available'
    }
  } catch (error) {
    console.error('检查邮箱失败:', error)
    emailStatus.value = 'available'
  }
}

// 处理注册
const handleRegister = async () => {
  if (!isFormValid.value) {
    alert('请填写完整且正确的信息')
    return
  }

  loading.value = true

  try {
    console.log('发送注册请求:', form)

    // 发送所有必需的字段
    const requestBody = {
      username: form.username,
      password: form.password,
      email: form.email,
      phone: form.phone,
      realName: form.realName,
      studentId: form.studentId
    }

    const response = await fetch('https://hyaloplasmic-surprisedly-margie.ngrok-free.dev/api/api/user/register', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(requestBody)
    })

    const result = await response.json()
    console.log('注册响应:', result)

    if (result.success) {
      alert('注册成功！请登录')
      // 重置表单
      Object.keys(form).forEach(key => form[key] = '')
      usernameStatus.value = ''
      emailStatus.value = ''
      router.push('/login')
    } else {
      alert('注册失败：' + (result.message || '未知错误'))
    }
  } catch (error) {
    console.error('注册失败:', error)
    alert('注册失败，请检查网络连接')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.register-container {
  min-height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 20px;
}

.register-form {
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

.status-text, span {
  display: block;
  margin-top: 5px;
  font-size: 12px;
}

.success {
  color: #67c23a;
}

.error {
  color: #f56c6c;
}

.info {
  color: #909399;
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

.login-link {
  text-align: center;
  margin-top: 20px;
  color: #666;
}

.login-link a {
  color: #409eff;
  text-decoration: none;
}

.login-link a:hover {
  text-decoration: underline;
}
</style>
