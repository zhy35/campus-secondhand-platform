<template>
  <div class="create-product-page">
    <NavBar />

    <div class="container">
      <div class="page-header">
        <h1>发布商品</h1>
        <p>分享你的闲置物品，让资源循环利用</p>
      </div>

      <div class="create-form">
        <form @submit.prevent="handleSubmit">
          <!-- 商品基本信息 -->
          <div class="form-section">
            <h2>商品信息</h2>

            <div class="form-group">
              <label class="required">商品名称</label>
              <input
                v-model="form.name"
                type="text"
                placeholder="请输入商品名称"
                maxlength="50"
                required
              />
              <div class="char-count">{{ form.name.length }}/50</div>
            </div>

            <div class="form-group">
              <label class="required">商品描述</label>
              <textarea
                v-model="form.description"
                placeholder="请详细描述商品的新旧程度、使用情况、瑕疵等"
                rows="4"
                maxlength="500"
                required
              ></textarea>
              <div class="char-count">{{ form.description.length }}/500</div>
            </div>

            <div class="form-row">
              <div class="form-group">
                <label class="required">商品分类</label>
                <select v-model="form.category" required>
                  <option value="">请选择分类</option>
                  <option value="books">图书教材</option>
                  <option value="electronics">数码产品</option>
                  <option value="clothing">服装鞋帽</option>
                  <option value="daily">生活用品</option>
                  <option value="sports">运动户外</option>
                  <option value="other">其他</option>
                </select>
              </div>

              <div class="form-group">
                <label class="required">商品状态</label>
                <select v-model="form.condition" required>
                  <option value="">请选择状态</option>
                  <option value="new">全新</option>
                  <option value="like-new">几乎全新</option>
                  <option value="good">轻微使用</option>
                  <option value="fair">明显使用</option>
                </select>
              </div>
            </div>
          </div>

          <!-- 价格信息 -->
          <div class="form-section">
            <h2>价格信息</h2>

            <div class="form-row">
              <div class="form-group">
                <label class="required">出售价格 (元)</label>
                <input
                  v-model="form.price"
                  type="number"
                  placeholder="0.00"
                  min="0"
                  step="0.01"
                  required
                />
              </div>

              <div class="form-group">
                <label>原价 (元)</label>
                <input
                  v-model="form.originalPrice"
                  type="number"
                  placeholder="0.00"
                  min="0"
                  step="0.01"
                />
              </div>
            </div>

            <div class="form-group">
              <label class="checkbox-label">
                <input
                  v-model="form.isNegotiable"
                  type="checkbox"
                />
                <span class="checkmark"></span>
                价格可议
              </label>
            </div>
          </div>

          <!-- 图片上传 -->
          <div class="form-section">
            <h2>商品图片</h2>
            <p class="section-description">最多上传6张图片，第一张将作为主图</p>

            <div class="image-upload">
              <div
                v-for="(image, index) in form.images"
                :key="index"
                class="image-preview"
              >
                <img :src="image.url" :alt="`商品图片 ${index + 1}`" />
                <button
                  type="button"
                  class="remove-image"
                  @click="removeImage(index)"
                >
                  ×
                </button>
                <div
                  v-if="index === 0"
                  class="main-image-badge"
                >
                  主图
                </div>
              </div>

              <div
                v-if="form.images.length < 6"
                class="image-upload-area"
                @click="triggerFileInput"
                @drop="handleDrop"
                @dragover="handleDragOver"
              >
                <div class="upload-placeholder">
                  <div class="upload-icon">📷</div>
                  <p>点击或拖拽图片到这里</p>
                  <p class="upload-hint">支持 JPG、PNG 格式，每张不超过 5MB</p>
                </div>
                <input
                  ref="fileInput"
                  type="file"
                  multiple
                  accept="image/*"
                  @change="handleFileSelect"
                  style="display: none"
                />
              </div>
            </div>
          </div>

          <!-- 商品规格 -->
          <div class="form-section">
            <h2>商品规格</h2>

            <div class="specs-form">
              <div
                v-for="(spec, index) in form.specifications"
                :key="index"
                class="spec-row"
              >
                <input
                  v-model="spec.key"
                  type="text"
                  placeholder="规格名称（如：颜色、尺寸）"
                  class="spec-input"
                />
                <span class="spec-separator">:</span>
                <input
                  v-model="spec.value"
                  type="text"
                  placeholder="规格值"
                  class="spec-input"
                />
                <button
                  type="button"
                  class="remove-spec"
                  @click="removeSpecification(index)"
                >
                  删除
                </button>
              </div>

              <button
                type="button"
                class="add-spec-btn"
                @click="addSpecification"
              >
                + 添加规格
              </button>
            </div>
          </div>

          <!-- 联系信息 -->
          <div class="form-section">
            <h2>联系信息</h2>

            <div class="form-group">
              <label class="required">联系方式</label>
              <select v-model="form.contactMethod" required>
                <option value="">请选择联系方式</option>
                <option value="wechat">微信</option>
                <option value="phone">手机</option>
                <option value="qq">QQ</option>
              </select>
            </div>

            <div class="form-group" v-if="form.contactMethod">
              <label class="required">联系账号</label>
              <input
                v-model="form.contactInfo"
                type="text"
                :placeholder="getContactPlaceholder(form.contactMethod)"
                required
              />
            </div>

            <div class="form-group">
              <label>交易地点</label>
              <input
                v-model="form.location"
                type="text"
                placeholder="如：学生宿舍区、图书馆前、教学楼等"
              />
            </div>
          </div>

          <!-- 提交按钮 -->
          <div class="form-actions">
            <button
              type="button"
              class="btn outline"
              @click="handleCancel"
            >
              取消
            </button>
            <button
              type="submit"
              class="btn primary"
              :disabled="!isFormValid || submitting"
            >
              {{ submitting ? '发布中...' : '发布商品' }}
            </button>
          </div>
        </form>
      </div>
    </div>

    <!-- 发布成功弹窗 -->
    <div v-if="showSuccessModal" class="modal-overlay">
      <div class="modal-content">
        <div class="success-icon">✅</div>
        <h2>发布成功！</h2>
        <p>您的商品已成功发布，正在等待审核</p>
        <div class="modal-actions">
          <button
            class="btn outline"
            @click="goToProducts"
          >
            查看我的商品
          </button>
          <button
            class="btn primary"
            @click="publishAnother"
          >
            继续发布
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
const fileInput = ref(null)
const submitting = ref(false)
const showSuccessModal = ref(false)

const form = reactive({
  name: '',
  description: '',
  category: '',
  condition: '',
  price: '',
  originalPrice: '',
  isNegotiable: false,
  images: [],
  specifications: [
    { key: '', value: '' }
  ],
  contactMethod: '',
  contactInfo: '',
  location: ''
})

onMounted(() => {
  // 可以在这里加载用户默认信息
})

// 表单验证
const isFormValid = computed(() => {
  return (
    form.name.trim() &&
    form.description.trim() &&
    form.category &&
    form.condition &&
    form.price &&
    form.contactMethod &&
    form.contactInfo.trim() &&
    form.images.length > 0
  )
})

// 触发文件选择
const triggerFileInput = () => {
  fileInput.value?.click()
}

// 处理文件选择
const handleFileSelect = (event) => {
  const files = Array.from(event.target.files)
  if (files.length + form.images.length > 6) {
    alert('最多只能上传6张图片')
    return
  }

  files.forEach(file => {
    if (!file.type.startsWith('image/')) {
      alert('请选择图片文件')
      return
    }

    if (file.size > 5 * 1024 * 1024) {
      alert('图片大小不能超过5MB')
      return
    }

    const reader = new FileReader()
    reader.onload = (e) => {
      form.images.push({
        file: file,
        url: e.target.result
      })
    }
    reader.readAsDataURL(file)
  })

  // 清空input，允许重复选择相同文件
  event.target.value = ''
}

// 处理拖拽
const handleDragOver = (event) => {
  event.preventDefault()
}

const handleDrop = (event) => {
  event.preventDefault()
  const files = Array.from(event.dataTransfer.files)
  if (files.length > 0) {
    const fileInputEvent = { target: { files } }
    handleFileSelect(fileInputEvent)
  }
}

// 删除图片
const removeImage = (index) => {
  form.images.splice(index, 1)
}

// 添加规格
const addSpecification = () => {
  form.specifications.push({ key: '', value: '' })
}

// 删除规格
const removeSpecification = (index) => {
  if (form.specifications.length > 1) {
    form.specifications.splice(index, 1)
  }
}

// 获取联系方式占位符
const getContactPlaceholder = (method) => {
  const placeholders = {
    wechat: '请输入微信号',
    phone: '请输入手机号码',
    qq: '请输入QQ号'
  }
  return placeholders[method] || '请输入联系账号'
}

// 处理表单提交
const handleSubmit = async () => {
  if (!isFormValid.value) {
    alert('请填写完整信息')
    return
  }

  submitting.value = true

  try {
    // 模拟API调用
    await new Promise(resolve => setTimeout(resolve, 2000))

    // 构建提交数据
    const submitData = {
      ...form,
      price: parseFloat(form.price),
      originalPrice: form.originalPrice ? parseFloat(form.originalPrice) : null,
      // 过滤空规格
      specifications: form.specifications.filter(spec =>
        spec.key.trim() && spec.value.trim()
      )
    }

    console.log('提交数据:', submitData)

    // 这里可以调用真实API
    // const response = await request.post('/products', submitData)

    // 显示成功弹窗
    showSuccessModal.value = true

  } catch (error) {
    console.error('发布失败:', error)
    alert('发布失败，请重试')
  } finally {
    submitting.value = false
  }
}

// 处理取消
const handleCancel = () => {
  if (form.name || form.description || form.images.length > 0) {
    if (confirm('确定要取消发布吗？已填写的内容将不会保存。')) {
      router.push('/products')
    }
  } else {
    router.push('/products')
  }
}

// 跳转到我的商品
const goToProducts = () => {
  showSuccessModal.value = false
  router.push('/my-products')
}

// 继续发布
const publishAnother = () => {
  showSuccessModal.value = false
  // 重置表单
  Object.keys(form).forEach(key => {
    if (key === 'specifications') {
      form[key] = [{ key: '', value: '' }]
    } else if (key === 'images') {
      form[key] = []
    } else if (typeof form[key] === 'boolean') {
      form[key] = false
    } else {
      form[key] = ''
    }
  })
}
</script>

<style scoped>
.create-product-page {
  min-height: 100vh;
  background: #f8f9fa;
}

.container {
  max-width: 800px;
  margin: 0 auto;
  padding: 2rem 20px;
}

.page-header {
  text-align: center;
  margin-bottom: 3rem;
}

.page-header h1 {
  color: #333;
  margin: 0 0 0.5rem 0;
  font-size: 2rem;
}

.page-header p {
  color: #666;
  margin: 0;
  font-size: 1.1rem;
}

.create-form {
  background: white;
  padding: 2rem;
  border-radius: 12px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
}

.form-section {
  margin-bottom: 3rem;
  padding-bottom: 2rem;
  border-bottom: 1px solid #f0f0f0;
}

.form-section:last-of-type {
  border-bottom: none;
  margin-bottom: 2rem;
}

.form-section h2 {
  color: #333;
  margin: 0 0 1.5rem 0;
  font-size: 1.3rem;
  font-weight: 600;
}

.section-description {
  color: #666;
  margin: -1rem 0 1.5rem 0;
  font-size: 0.9rem;
}

.form-group {
  margin-bottom: 1.5rem;
  position: relative;
}

.form-row {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 1.5rem;
}

label {
  display: block;
  margin-bottom: 0.5rem;
  color: #333;
  font-weight: 500;
}

label.required::after {
  content: " *";
  color: #f56c6c;
}

input, select, textarea {
  width: 100%;
  padding: 0.75rem 1rem;
  border: 2px solid #e1e5e9;
  border-radius: 8px;
  font-size: 1rem;
  transition: border-color 0.3s;
  box-sizing: border-box;
}

input:focus, select:focus, textarea:focus {
  outline: none;
  border-color: #409eff;
}

textarea {
  resize: vertical;
  min-height: 100px;
  font-family: inherit;
}

.char-count {
  text-align: right;
  font-size: 0.8rem;
  color: #999;
  margin-top: 0.25rem;
}

/* 复选框样式 */
.checkbox-label {
  display: flex;
  align-items: center;
  cursor: pointer;
  margin-bottom: 0;
}

.checkbox-label input {
  width: auto;
  margin-right: 0.5rem;
}

/* 图片上传 */
.image-upload {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(120px, 1fr));
  gap: 1rem;
}

.image-preview {
  position: relative;
  aspect-ratio: 1;
  border-radius: 8px;
  overflow: hidden;
  border: 2px solid #e1e5e9;
}

.image-preview img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.remove-image {
  position: absolute;
  top: 0.25rem;
  right: 0.25rem;
  width: 24px;
  height: 24px;
  background: rgba(0, 0, 0, 0.7);
  color: white;
  border: none;
  border-radius: 50%;
  cursor: pointer;
  font-size: 1rem;
  line-height: 1;
}

.main-image-badge {
  position: absolute;
  bottom: 0.25rem;
  left: 0.25rem;
  background: #409eff;
  color: white;
  padding: 0.2rem 0.5rem;
  border-radius: 4px;
  font-size: 0.7rem;
  font-weight: 500;
}

.image-upload-area {
  aspect-ratio: 1;
  border: 2px dashed #e1e5e9;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: border-color 0.3s;
}

.image-upload-area:hover {
  border-color: #409eff;
}

.upload-placeholder {
  text-align: center;
  color: #999;
}

.upload-icon {
  font-size: 2rem;
  margin-bottom: 0.5rem;
}

.upload-hint {
  font-size: 0.8rem;
  margin: 0.5rem 0 0 0;
  color: #ccc;
}

/* 规格表单 */
.specs-form {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.spec-row {
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.spec-input {
  flex: 1;
}

.spec-separator {
  color: #666;
  font-weight: 500;
}

.remove-spec {
  padding: 0.5rem 1rem;
  background: #f56c6c;
  color: white;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  font-size: 0.8rem;
  white-space: nowrap;
}

.remove-spec:hover {
  background: #f78989;
}

.add-spec-btn {
  padding: 0.75rem 1rem;
  background: #f8f9fa;
  color: #666;
  border: 2px dashed #e1e5e9;
  border-radius: 8px;
  cursor: pointer;
  font-size: 0.9rem;
  transition: all 0.3s;
}

.add-spec-btn:hover {
  border-color: #409eff;
  color: #409eff;
}

/* 表单操作 */
.form-actions {
  display: flex;
  gap: 1rem;
  justify-content: center;
  padding-top: 2rem;
  border-top: 1px solid #f0f0f0;
}

.btn {
  padding: 0.75rem 2rem;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  font-weight: 500;
  transition: all 0.3s;
  font-size: 1rem;
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

.btn.primary {
  background: #409eff;
  color: white;
}

.btn.primary:hover:not(:disabled) {
  background: #66b1ff;
}

.btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

/* 成功弹窗 */
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
  padding: 3rem;
  border-radius: 12px;
  text-align: center;
  max-width: 400px;
  width: 90%;
}

.success-icon {
  font-size: 4rem;
  margin-bottom: 1rem;
}

.modal-content h2 {
  margin: 0 0 1rem 0;
  color: #333;
}

.modal-content p {
  color: #666;
  margin: 0 0 2rem 0;
}

.modal-actions {
  display: flex;
  gap: 1rem;
  justify-content: center;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .container {
    padding: 1rem 15px;
  }

  .create-form {
    padding: 1.5rem;
  }

  .form-row {
    grid-template-columns: 1fr;
    gap: 1rem;
  }

  .image-upload {
    grid-template-columns: repeat(auto-fill, minmax(100px, 1fr));
  }

  .spec-row {
    flex-direction: column;
    align-items: stretch;
  }

  .spec-separator {
    display: none;
  }

  .form-actions {
    flex-direction: column;
  }

  .modal-actions {
    flex-direction: column;
  }

  .modal-content {
    padding: 2rem;
  }
}
</style>