<template>
  <div class="profile-page">
    <NavBar />

    <div class="container">
      <div class="profile-header">
        <h1>个人中心</h1>
        <p>管理您的个人信息和账户设置</p>
      </div>

      <div class="profile-layout">
        <!-- 侧边栏菜单 -->
        <div class="sidebar">
          <nav class="sidebar-nav">
            <button
              v-for="tab in tabs"
              :key="tab.id"
              :class="['nav-item', { active: activeTab === tab.id }]"
              @click="activeTab = tab.id"
            >
              <span class="nav-icon">{{ tab.icon }}</span>
              <span class="nav-text">{{ tab.name }}</span>
            </button>
          </nav>
        </div>

        <!-- 主要内容区域 -->
        <div class="main-content">
          <!-- 基本信息 -->
          <div v-if="activeTab === 'basic'" class="tab-content">
            <div class="section-card">
              <h2>基本信息</h2>

              <div class="avatar-section">
                <div class="avatar-upload">
                  <div class="avatar-preview">
                    <img :src="user.avatar || '/default-avatar.jpg'" alt="用户头像" />
                    <button class="change-avatar-btn" @click="triggerAvatarUpload">
                      📷 更换
                    </button>
                  </div>
                  <input
                    ref="avatarInput"
                    type="file"
                    accept="image/*"
                    @change="handleAvatarUpload"
                    style="display: none"
                  />
                </div>

                <div class="avatar-info">
                  <h3>{{ user.username }}</h3>
                  <p class="user-role">🎓 认证学生</p>
                  <p class="join-date">注册于 {{ formatJoinDate(user.createTime) }}</p>
                </div>
              </div>

              <form @submit.prevent="updateBasicInfo" class="info-form">
                <div class="form-row">
                  <div class="form-group">
                    <label>用户名</label>
                    <input
                      v-model="basicForm.username"
                      type="text"
                      placeholder="请输入用户名"
                    />
                  </div>

                  <div class="form-group">
                    <label>真实姓名</label>
                    <input
                      v-model="basicForm.realName"
                      type="text"
                      placeholder="请输入真实姓名"
                    />
                  </div>
                </div>

                <div class="form-row">
                  <div class="form-group">
                    <label>学号</label>
                    <input
                      v-model="basicForm.studentId"
                      type="text"
                      placeholder="请输入学号"
                    />
                  </div>

                  <div class="form-group">
                    <label>手机号</label>
                    <input
                      v-model="basicForm.phone"
                      type="tel"
                      placeholder="请输入手机号"
                    />
                  </div>
                </div>

                <div class="form-group">
                  <label>邮箱</label>
                  <input
                    v-model="basicForm.email"
                    type="email"
                    placeholder="请输入邮箱"
                  />
                </div>

                <div class="form-group">
                  <label>个人简介</label>
                  <textarea
                    v-model="basicForm.bio"
                    placeholder="介绍一下自己吧..."
                    rows="3"
                    maxlength="200"
                  ></textarea>
                  <div class="char-count">{{ basicForm.bio.length }}/200</div>
                </div>

                <div class="form-actions">
                  <button
                    type="submit"
                    class="btn primary"
                    :disabled="!isBasicFormChanged || updatingBasic"
                  >
                    {{ updatingBasic ? '保存中...' : '保存修改' }}
                  </button>
                </div>
              </form>
            </div>
          </div>

          <!-- 账户安全 -->
          <div v-else-if="activeTab === 'security'" class="tab-content">
            <div class="section-card">
              <h2>账户安全</h2>

              <div class="security-items">
                <div class="security-item">
                  <div class="security-info">
                    <h3>登录密码</h3>
                    <p>定期更换密码有助于保护账户安全</p>
                  </div>
                  <button class="btn outline" @click="showChangePassword = true">
                    修改密码
                  </button>
                </div>

                <div class="security-item">
                  <div class="security-info">
                    <h3>登录设备</h3>
                    <p>当前设备：{{ currentDevice }}</p>
                  </div>
                  <button class="btn outline" @click="viewLoginDevices">
                    查看设备
                  </button>
                </div>

                <div class="security-item">
                  <div class="security-info">
                    <h3>账户注销</h3>
                    <p>永久删除账户及所有数据</p>
                  </div>
                  <button class="btn danger" @click="showDeleteAccount = true">
                    注销账户
                  </button>
                </div>
              </div>
            </div>
          </div>

          <!-- 我的数据 -->
          <div v-else-if="activeTab === 'stats'" class="tab-content">
            <div class="stats-grid">
              <div class="stat-card">
                <div class="stat-icon">📦</div>
                <div class="stat-info">
                  <div class="stat-number">{{ userStats.totalProducts }}</div>
                  <div class="stat-label">发布商品</div>
                </div>
              </div>

              <div class="stat-card">
                <div class="stat-icon">💰</div>
                <div class="stat-info">
                  <div class="stat-number">{{ userStats.soldProducts }}</div>
                  <div class="stat-label">已售出</div>
                </div>
              </div>

              <div class="stat-card">
                <div class="stat-icon">❤️</div>
                <div class="stat-info">
                  <div class="stat-number">{{ userStats.favorites }}</div>
                  <div class="stat-label">收藏数</div>
                </div>
              </div>

              <div class="stat-card">
                <div class="stat-icon">⭐</div>
                <div class="stat-info">
                  <div class="stat-number">{{ userStats.rating }}</div>
                  <div class="stat-label">信誉评分</div>
                </div>
              </div>
            </div>

            <div class="section-card">
              <h2>最近活动</h2>
              <div class="activity-list">
                <div
                  v-for="activity in recentActivities"
                  :key="activity.id"
                  class="activity-item"
                >
                  <div class="activity-icon">{{ activity.icon }}</div>
                  <div class="activity-content">
                    <p>{{ activity.message }}</p>
                    <span class="activity-time">{{ activity.time }}</span>
                  </div>
                </div>
              </div>
            </div>
          </div>

          <!-- 通知设置 -->
          <div v-else-if="activeTab === 'notifications'" class="tab-content">
            <div class="section-card">
              <h2>通知设置</h2>

              <div class="notification-settings">
                <div class="setting-item">
                  <div class="setting-info">
                    <h3>商品消息</h3>
                    <p>有人购买、咨询或评论您的商品时通知</p>
                  </div>
                  <label class="switch">
                    <input
                      v-model="notificationSettings.productMessages"
                      type="checkbox"
                    />
                    <span class="slider"></span>
                  </label>
                </div>

                <div class="setting-item">
                  <div class="setting-info">
                    <h3>系统通知</h3>
                    <p>平台更新、活动通知等重要信息</p>
                  </div>
                  <label class="switch">
                    <input
                      v-model="notificationSettings.systemNotifications"
                      type="checkbox"
                    />
                    <span class="slider"></span>
                  </label>
                </div>

                <div class="setting-item">
                  <div class="setting-info">
                    <h3>营销信息</h3>
                    <p>优惠活动、推荐商品等信息</p>
                  </div>
                  <label class="switch">
                    <input
                      v-model="notificationSettings.marketingMessages"
                      type="checkbox"
                    />
                    <span class="slider"></span>
                  </label>
                </div>

                <div class="setting-item">
                  <div class="setting-info">
                    <h3>邮件通知</h3>
                    <p>通过邮件接收重要通知</p>
                  </div>
                  <label class="switch">
                    <input
                      v-model="notificationSettings.emailNotifications"
                      type="checkbox"
                    />
                    <span class="slider"></span>
                  </label>
                </div>
              </div>

              <div class="form-actions">
                <button
                  class="btn primary"
                  @click="saveNotificationSettings"
                  :disabled="savingNotifications"
                >
                  {{ savingNotifications ? '保存中...' : '保存设置' }}
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 修改密码弹窗 -->
    <div v-if="showChangePassword" class="modal-overlay">
      <div class="modal-content">
        <h2>修改密码</h2>
        <form @submit.prevent="changePassword" class="password-form">
          <div class="form-group">
            <label>当前密码</label>
            <input
              v-model="passwordForm.currentPassword"
              type="password"
              placeholder="请输入当前密码"
              required
            />
          </div>

          <div class="form-group">
            <label>新密码</label>
            <input
              v-model="passwordForm.newPassword"
              type="password"
              placeholder="请输入新密码"
              required
            />
          </div>

          <div class="form-group">
            <label>确认新密码</label>
            <input
              v-model="passwordForm.confirmPassword"
              type="password"
              placeholder="请再次输入新密码"
              required
            />
          </div>

          <div class="modal-actions">
            <button
              type="button"
              class="btn outline"
              @click="showChangePassword = false"
            >
              取消
            </button>
            <button
              type="submit"
              class="btn primary"
              :disabled="changingPassword"
            >
              {{ changingPassword ? '修改中...' : '确认修改' }}
            </button>
          </div>
        </form>
      </div>
    </div>

    <!-- 注销账户确认弹窗 -->
    <div v-if="showDeleteAccount" class="modal-overlay">
      <div class="modal-content">
        <div class="warning-icon">⚠️</div>
        <h2>确认注销账户</h2>
        <p class="warning-text">
          此操作将永久删除您的账户和所有相关数据，包括发布的商品、收藏记录等。此操作不可撤销！
        </p>

        <div class="delete-confirm">
          <label>
            <input
              v-model="deleteConfirm"
              type="checkbox"
            />
            我理解此操作的后果，确认注销账户
          </label>
        </div>

        <div class="modal-actions">
          <button
            class="btn outline"
            @click="showDeleteAccount = false"
          >
            取消
          </button>
          <button
            class="btn danger"
            @click="deleteAccount"
            :disabled="!deleteConfirm || deletingAccount"
          >
            {{ deletingAccount ? '注销中...' : '确认注销' }}
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import NavBar from '../components/NavBar.vue'

const router = useRouter()
const avatarInput = ref(null)

// 标签页配置
const tabs = [
  { id: 'basic', name: '基本信息', icon: '👤' },
  { id: 'security', name: '账户安全', icon: '🔒' },
  { id: 'stats', name: '我的数据', icon: '📊' },
  { id: 'notifications', name: '通知设置', icon: '🔔' }
]

const activeTab = ref('basic')

// 用户信息
const user = ref({
  id: 1,
  username: '张同学',
  realName: '张三',
  studentId: '20240001',
  email: 'zhang@example.com',
  phone: '13800138000',
  bio: '热爱编程的计算机专业学生，喜欢分享技术心得。',
  avatar: '',
  createTime: '2024-01-01T00:00:00'
})

// 基本信息表单
const basicForm = reactive({ ...user.value })
const updatingBasic = ref(false)

// 安全相关
const showChangePassword = ref(false)
const changingPassword = ref(false)
const showDeleteAccount = ref(false)
const deleteConfirm = ref(false)
const deletingAccount = ref(false)

const passwordForm = reactive({
  currentPassword: '',
  newPassword: '',
  confirmPassword: ''
})

// 用户数据统计
const userStats = ref({
  totalProducts: 12,
  soldProducts: 8,
  favorites: 23,
  rating: '4.8'
})

// 最近活动
const recentActivities = ref([
  {
    id: 1,
    icon: '📦',
    message: '发布了新商品 "Java编程思想"',
    time: '2小时前'
  },
  {
    id: 2,
    icon: '💰',
    message: '商品 "iPhone保护壳" 已售出',
    time: '1天前'
  },
  {
    id: 3,
    icon: '❤️',
    message: '收藏了 "篮球"',
    time: '2天前'
  },
  {
    id: 4,
    icon: '⭐',
    message: '获得新的信誉评分',
    time: '3天前'
  }
])

// 通知设置
const notificationSettings = reactive({
  productMessages: true,
  systemNotifications: true,
  marketingMessages: false,
  emailNotifications: true
})
const savingNotifications = ref(false)

// 当前设备信息
const currentDevice = ref(`${navigator.platform} · ${navigator.userAgent.split(' ')[0]}`)

onMounted(() => {
  loadUserData()
})

const loadUserData = async () => {
  // 这里可以加载用户数据
  // const response = await request.get('/user/profile')
  // user.value = response.data
}

// 计算属性
const isBasicFormChanged = computed(() => {
  return Object.keys(basicForm).some(key => basicForm[key] !== user.value[key])
})

// 触发头像上传
const triggerAvatarUpload = () => {
  avatarInput.value?.click()
}

// 处理头像上传
const handleAvatarUpload = (event) => {
  const file = event.target.files[0]
  if (file && file.type.startsWith('image/')) {
    const reader = new FileReader()
    reader.onload = (e) => {
      user.value.avatar = e.target.result
      // 这里可以调用API更新头像
      console.log('上传头像:', file)
    }
    reader.readAsDataURL(file)
  }
}

// 更新基本信息
const updateBasicInfo = async () => {
  updatingBasic.value = true
  try {
    // 模拟API调用
    await new Promise(resolve => setTimeout(resolve, 1000))

    // 更新用户信息
    Object.assign(user.value, basicForm)

    // 这里可以调用真实API
    // await request.put('/user/profile', basicForm)

    alert('基本信息更新成功！')
  } catch (error) {
    console.error('更新失败:', error)
    alert('更新失败，请重试')
  } finally {
    updatingBasic.value = false
  }
}

// 修改密码
const changePassword = async () => {
  if (passwordForm.newPassword !== passwordForm.confirmPassword) {
    alert('两次输入的密码不一致')
    return
  }

  changingPassword.value = true
  try {
    // 模拟API调用
    await new Promise(resolve => setTimeout(resolve, 1000))

    // 这里可以调用真实API
    // await request.put('/user/password', passwordForm)

    alert('密码修改成功！')
    showChangePassword.value = false
    // 清空表单
    Object.keys(passwordForm).forEach(key => passwordForm[key] = '')
  } catch (error) {
    console.error('修改密码失败:', error)
    alert('修改密码失败，请重试')
  } finally {
    changingPassword.value = false
  }
}

// 查看登录设备
const viewLoginDevices = () => {
  alert('登录设备管理功能开发中...')
}

// 注销账户
const deleteAccount = async () => {
  deletingAccount.value = true
  try {
    // 模拟API调用
    await new Promise(resolve => setTimeout(resolve, 2000))

    // 这里可以调用真实API
    // await request.delete('/user/account')

    // 清除本地存储
    localStorage.removeItem('token')
    localStorage.removeItem('user')

    alert('账户已成功注销')
    router.push('/')
  } catch (error) {
    console.error('注销账户失败:', error)
    alert('注销账户失败，请重试')
  } finally {
    deletingAccount.value = false
  }
}

// 保存通知设置
const saveNotificationSettings = async () => {
  savingNotifications.value = true
  try {
    // 模拟API调用
    await new Promise(resolve => setTimeout(resolve, 500))

    // 这里可以调用真实API
    // await request.put('/user/notifications', notificationSettings)

    alert('通知设置已保存')
  } catch (error) {
    console.error('保存设置失败:', error)
    alert('保存失败，请重试')
  } finally {
    savingNotifications.value = false
  }
}

// 工具函数
const formatJoinDate = (dateString) => {
  return new Date(dateString).toLocaleDateString('zh-CN', {
    year: 'numeric',
    month: 'long',
    day: 'numeric'
  })
}
</script>

<style scoped>
.profile-page {
  min-height: 100vh;
  background: #f8f9fa;
}

.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 2rem 20px;
}

.profile-header {
  text-align: center;
  margin-bottom: 3rem;
}

.profile-header h1 {
  color: #333;
  margin: 0 0 0.5rem 0;
  font-size: 2rem;
}

.profile-header p {
  color: #666;
  margin: 0;
  font-size: 1.1rem;
}

.profile-layout {
  display: grid;
  grid-template-columns: 250px 1fr;
  gap: 2rem;
}

/* 侧边栏 */
.sidebar {
  background: white;
  border-radius: 12px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
  padding: 1.5rem 0;
  height: fit-content;
  position: sticky;
  top: 2rem;
}

.sidebar-nav {
  display: flex;
  flex-direction: column;
}

.nav-item {
  display: flex;
  align-items: center;
  gap: 0.75rem;
  padding: 1rem 1.5rem;
  border: none;
  background: transparent;
  cursor: pointer;
  transition: all 0.3s;
  text-align: left;
  color: #666;
}

.nav-item:hover {
  background: #f8f9fa;
  color: #333;
}

.nav-item.active {
  background: #ecf5ff;
  color: #409eff;
  border-right: 3px solid #409eff;
}

.nav-icon {
  font-size: 1.2rem;
  width: 24px;
  text-align: center;
}

.nav-text {
  font-weight: 500;
}

/* 主要内容 */
.main-content {
  min-height: 600px;
}

.tab-content {
  animation: fadeIn 0.3s ease-in-out;
}

@keyframes fadeIn {
  from { opacity: 0; transform: translateY(10px); }
  to { opacity: 1; transform: translateY(0); }
}

.section-card {
  background: white;
  padding: 2rem;
  border-radius: 12px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
  margin-bottom: 2rem;
}

.section-card h2 {
  margin: 0 0 1.5rem 0;
  color: #333;
  font-size: 1.5rem;
  font-weight: 600;
}

/* 头像区域 */
.avatar-section {
  display: flex;
  gap: 2rem;
  align-items: flex-start;
  margin-bottom: 2rem;
  padding-bottom: 2rem;
  border-bottom: 1px solid #f0f0f0;
}

.avatar-upload {
  position: relative;
}

.avatar-preview {
  width: 100px;
  height: 100px;
  border-radius: 50%;
  overflow: hidden;
  position: relative;
  border: 3px solid #e1e5e9;
}

.avatar-preview img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.change-avatar-btn {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  background: rgba(0, 0, 0, 0.7);
  color: white;
  border: none;
  padding: 0.25rem;
  font-size: 0.8rem;
  cursor: pointer;
}

.avatar-info h3 {
  margin: 0 0 0.5rem 0;
  color: #333;
  font-size: 1.3rem;
}

.user-role {
  color: #409eff;
  margin: 0 0 0.5rem 0;
  font-weight: 500;
}

.join-date {
  color: #666;
  margin: 0;
  font-size: 0.9rem;
}

/* 表单样式 */
.info-form {
  max-width: 600px;
}

.form-row {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 1.5rem;
}

.form-group {
  margin-bottom: 1.5rem;
}

.form-group label {
  display: block;
  margin-bottom: 0.5rem;
  color: #333;
  font-weight: 500;
}

.form-group input,
.form-group textarea,
.form-group select {
  width: 100%;
  padding: 0.75rem 1rem;
  border: 2px solid #e1e5e9;
  border-radius: 8px;
  font-size: 1rem;
  transition: border-color 0.3s;
  box-sizing: border-box;
}

.form-group input:focus,
.form-group textarea:focus,
.form-group select:focus {
  outline: none;
  border-color: #409eff;
}

.form-group textarea {
  resize: vertical;
  min-height: 80px;
  font-family: inherit;
}

.char-count {
  text-align: right;
  font-size: 0.8rem;
  color: #999;
  margin-top: 0.25rem;
}

.form-actions {
  text-align: center;
  margin-top: 2rem;
}

/* 安全项目 */
.security-items {
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
}

.security-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 1.5rem;
  background: #f8f9fa;
  border-radius: 8px;
}

.security-info h3 {
  margin: 0 0 0.5rem 0;
  color: #333;
}

.security-info p {
  margin: 0;
  color: #666;
  font-size: 0.9rem;
}

/* 数据统计 */
.stats-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 1.5rem;
  margin-bottom: 2rem;
}

.stat-card {
  background: white;
  padding: 1.5rem;
  border-radius: 12px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
  display: flex;
  align-items: center;
  gap: 1rem;
}

.stat-icon {
  font-size: 2.5rem;
  opacity: 0.7;
}

.stat-number {
  font-size: 2rem;
  font-weight: bold;
  color: #333;
  margin-bottom: 0.25rem;
}

.stat-label {
  color: #666;
  font-size: 0.9rem;
}

/* 活动列表 */
.activity-list {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.activity-item {
  display: flex;
  align-items: flex-start;
  gap: 1rem;
  padding: 1rem;
  background: #f8f9fa;
  border-radius: 8px;
}

.activity-icon {
  font-size: 1.2rem;
  margin-top: 0.2rem;
}

.activity-content p {
  margin: 0 0 0.25rem 0;
  color: #333;
}

.activity-time {
  color: #999;
  font-size: 0.8rem;
}

/* 通知设置 */
.notification-settings {
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
}

.setting-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 1.5rem;
  background: #f8f9fa;
  border-radius: 8px;
}

.setting-info h3 {
  margin: 0 0 0.5rem 0;
  color: #333;
}

.setting-info p {
  margin: 0;
  color: #666;
  font-size: 0.9rem;
}

/* 开关样式 */
.switch {
  position: relative;
  display: inline-block;
  width: 50px;
  height: 24px;
}

.switch input {
  opacity: 0;
  width: 0;
  height: 0;
}

.slider {
  position: absolute;
  cursor: pointer;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: #ccc;
  transition: .4s;
  border-radius: 24px;
}

.slider:before {
  position: absolute;
  content: "";
  height: 16px;
  width: 16px;
  left: 4px;
  bottom: 4px;
  background-color: white;
  transition: .4s;
  border-radius: 50%;
}

input:checked + .slider {
  background-color: #409eff;
}

input:checked + .slider:before {
  transform: translateX(26px);
}

/* 按钮样式 */
.btn {
  padding: 0.75rem 1.5rem;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  font-weight: 500;
  transition: all 0.3s;
  font-size: 0.9rem;
  text-decoration: none;
  display: inline-block;
  text-align: center;
}

.btn.primary {
  background: #409eff;
  color: white;
}

.btn.primary:hover:not(:disabled) {
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

.btn.danger {
  background: #f56c6c;
  color: white;
}

.btn.danger:hover:not(:disabled) {
  background: #f78989;
}

.btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

/* 弹窗样式 */
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.modal-content {
  background: white;
  padding: 2rem;
  border-radius: 12px;
  max-width: 500px;
  width: 90%;
  max-height: 90vh;
  overflow-y: auto;
}

.modal-content h2 {
  margin: 0 0 1.5rem 0;
  color: #333;
  text-align: center;
}

.warning-icon {
  font-size: 3rem;
  text-align: center;
  margin-bottom: 1rem;
}

.warning-text {
  color: #666;
  text-align: center;
  margin-bottom: 1.5rem;
  line-height: 1.5;
}

.password-form {
  max-width: 400px;
  margin: 0 auto;
}

.delete-confirm {
  margin: 1.5rem 0;
  padding: 1rem;
  background: #fef0f0;
  border-radius: 8px;
  border: 1px solid #fbc4c4;
}

.delete-confirm label {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  color: #f56c6c;
  font-weight: 500;
  cursor: pointer;
}

.delete-confirm input {
  width: auto;
}

.modal-actions {
  display: flex;
  gap: 1rem;
  justify-content: center;
  margin-top: 2rem;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .container {
    padding: 1rem 15px;
  }

  .profile-layout {
    grid-template-columns: 1fr;
  }

  .sidebar {
    position: static;
  }

  .sidebar-nav {
    flex-direction: row;
    overflow-x: auto;
  }

  .nav-item {
    white-space: nowrap;
    border-right: none;
    border-bottom: 3px solid transparent;
  }

  .nav-item.active {
    border-right: none;
    border-bottom: 3px solid #409eff;
  }

  .avatar-section {
    flex-direction: column;
    text-align: center;
  }

  .form-row {
    grid-template-columns: 1fr;
    gap: 1rem;
  }

  .stats-grid {
    grid-template-columns: repeat(2, 1fr);
  }

  .security-item,
  .setting-item {
    flex-direction: column;
    align-items: stretch;
    gap: 1rem;
    text-align: center;
  }

  .modal-actions {
    flex-direction: column;
  }
}

@media (max-width: 480px) {
  .stats-grid {
    grid-template-columns: 1fr;
  }

  .section-card {
    padding: 1.5rem;
  }
}
</style>