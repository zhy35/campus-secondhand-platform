<template>
  <div class="product-detail-page">
    <NavBar />

    <div class="container" v-if="product">
      <!-- 面包屑导航 -->
      <nav class="breadcrumb">
        <router-link to="/">首页</router-link>
        <span class="separator">/</span>
        <router-link to="/products">所有商品</router-link>
        <span class="separator">/</span>
        <span class="current">商品详情</span>
      </nav>

      <div class="product-detail">
        <!-- 商品图片区域 -->
        <div class="product-gallery">
          <div class="main-image">
            <img :src="product.mainImage || '/placeholder-product.jpg'" :alt="product.name" />
          </div>
          <div class="image-thumbnails" v-if="product.images && product.images.length > 0">
            <div
              v-for="(image, index) in product.images"
              :key="index"
              class="thumbnail"
              :class="{ active: currentImageIndex === index }"
              @click="currentImageIndex = index"
            >
              <img :src="image" :alt="`${product.name} ${index + 1}`" />
            </div>
          </div>
        </div>

        <!-- 商品信息区域 -->
        <div class="product-info">
          <div class="product-header">
            <h1 class="product-title">{{ product.name }}</h1>
            <div class="product-status" :class="product.status">
              {{ product.status === 'sold' ? '已售出' : '出售中' }}
            </div>
          </div>

          <div class="product-price-section">
            <div class="price">¥{{ product.price }}</div>
            <div class="original-price" v-if="product.originalPrice">
              原价: ¥{{ product.originalPrice }}
            </div>
            <div class="price-tag" v-if="product.isNegotiable">可议价</div>
          </div>

          <div class="product-meta">
            <div class="meta-item">
              <span class="label">分类:</span>
              <span class="value">{{ getCategoryName(product.category) }}</span>
            </div>
            <div class="meta-item">
              <span class="label">发布时间:</span>
              <span class="value">{{ formatTime(product.createTime) }}</span>
            </div>
            <div class="meta-item">
              <span class="label">浏览量:</span>
              <span class="value">{{ product.viewCount || 0 }}</span>
            </div>
          </div>

          <div class="product-description">
            <h3>商品描述</h3>
            <p>{{ product.description }}</p>
          </div>

          <div class="product-specs" v-if="product.specifications">
            <h3>商品规格</h3>
            <div class="specs-grid">
              <div
                v-for="(value, key) in product.specifications"
                :key="key"
                class="spec-item"
              >
                <span class="spec-label">{{ key }}:</span>
                <span class="spec-value">{{ value }}</span>
              </div>
            </div>
          </div>

          <!-- 卖家信息 -->
          <div class="seller-info">
            <h3>卖家信息</h3>
            <div class="seller-card">
              <div class="seller-avatar">
                <img :src="product.sellerAvatar || '/default-avatar.jpg'" alt="卖家头像" />
              </div>
              <div class="seller-details">
                <div class="seller-name">{{ product.sellerName }}</div>
                <div class="seller-stats">
                  <span class="stat">商品 {{ product.sellerProductCount || 0 }}</span>
                  <span class="stat">好评率 {{ product.sellerRating || '100%' }}</span>
                </div>
                <div class="seller-trust">
                  <span class="trust-badge">🎓 学生认证</span>
                  <span class="trust-badge" v-if="product.sellerIsVerified">✅ 实名认证</span>
                </div>
              </div>
              <button class="contact-btn" @click="contactSeller">
                💬 联系卖家
              </button>
            </div>
          </div>

          <!-- 操作按钮 -->
          <div class="action-buttons">
            <button
              class="btn favorite-btn"
              :class="{ active: product.isFavorite }"
              @click="toggleFavorite"
            >
              {{ product.isFavorite ? '❤️ 已收藏' : '♡ 收藏' }}
            </button>
            <button
              class="btn share-btn"
              @click="shareProduct"
            >
              📤 分享
            </button>
            <button
              class="btn primary buy-btn"
              :disabled="product.status === 'sold'"
              @click="handleBuy"
            >
              {{ product.status === 'sold' ? '已售出' : '立即购买' }}
            </button>
          </div>
        </div>
      </div>

      <!-- 相关商品推荐 -->
      <div class="related-products" v-if="relatedProducts.length > 0">
        <h2>相关推荐</h2>
        <div class="related-grid">
          <div
            v-for="relatedProduct in relatedProducts"
            :key="relatedProduct.id"
            class="related-card"
            @click="viewProductDetail(relatedProduct.id)"
          >
            <div class="related-image">
              <img :src="relatedProduct.image || '/placeholder-product.jpg'" :alt="relatedProduct.name" />
            </div>
            <div class="related-info">
              <h4>{{ relatedProduct.name }}</h4>
              <p class="related-price">¥{{ relatedProduct.price }}</p>
            </div>
          </div>
        </div>
      </div>
    </div>

    <div v-else-if="loading" class="loading-container">
      <div class="loading-spinner"></div>
      <p>加载中...</p>
    </div>

    <div v-else class="error-container">
      <div class="error-icon">❌</div>
      <h2>商品不存在</h2>
      <p>该商品可能已被删除或下架</p>
      <router-link to="/products" class="btn primary">返回商品列表</router-link>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import NavBar from '../components/NavBar.vue'

const route = useRoute()
const router = useRouter()
const product = ref(null)
const loading = ref(true)
const currentImageIndex = ref(0)

// 模拟商品详情数据
const mockProduct = {
  id: 1,
  name: 'Java编程思想（第五版）',
  price: 25,
  originalPrice: 68,
  description: '计算机专业经典教材，九成新，无笔记划线，页面干净整洁。适合计算机专业同学学习使用，内容全面深入，是学习Java编程的必备书籍。',
  mainImage: '',
  images: [],
  category: 'books',
  sellerName: '张同学',
  sellerAvatar: '',
  sellerProductCount: 12,
  sellerRating: '98%',
  sellerIsVerified: true,
  createTime: '2024-01-15T10:30:00',
  status: 'selling',
  isNegotiable: true,
  isFavorite: false,
  viewCount: 156,
  specifications: {
    '书籍类型': '教材',
    '适用专业': '计算机科学',
    '新旧程度': '九成新',
    '出版年份': '2022年',
    '出版社': '机械工业出版社'
  }
}

// 模拟相关商品
const relatedProducts = ref([
  {
    id: 2,
    name: 'Python编程从入门到实践',
    price: 30,
    image: ''
  },
  {
    id: 3,
    name: '数据结构与算法分析',
    price: 35,
    image: ''
  },
  {
    id: 4,
    name: '计算机网络教程',
    price: 28,
    image: ''
  },
  {
    id: 5,
    name: '操作系统概念',
    price: 32,
    image: ''
  }
])

onMounted(() => {
  loadProductDetail()
})

const loadProductDetail = async () => {
  loading.value = true
  try {
    // 模拟API调用
    await new Promise(resolve => setTimeout(resolve, 1000))

    const productId = parseInt(route.params.id)
    // 这里后续可以调用真实API
    // const response = await request.get(`/products/${productId}`)
    // product.value = response.data

    // 暂时使用模拟数据
    product.value = { ...mockProduct, id: productId }

    // 增加浏览量
    product.value.viewCount++

  } catch (error) {
    console.error('加载商品详情失败:', error)
  } finally {
    loading.value = false
  }
}

const toggleFavorite = () => {
  if (product.value) {
    product.value.isFavorite = !product.value.isFavorite
    // 这里可以调用API更新收藏状态
    console.log(product.value.isFavorite ? '已收藏' : '取消收藏')
  }
}

const contactSeller = () => {
  if (product.value) {
    // 这里可以实现联系卖家的功能
    alert(`联系卖家: ${product.value.sellerName}`)
  }
}

const handleBuy = () => {
  if (product.value && product.value.status === 'selling') {
    // 跳转到购买页面或显示购买对话框
    alert(`购买商品: ${product.value.name}`)
  }
}

const shareProduct = () => {
  if (product.value) {
    // 这里可以实现分享功能
    const shareUrl = `${window.location.origin}/product/${product.value.id}`
    navigator.clipboard.writeText(shareUrl).then(() => {
      alert('商品链接已复制到剪贴板！')
    })
  }
}

const viewProductDetail = (productId) => {
  router.push(`/product/${productId}`)
}

const formatTime = (time) => {
  return new Date(time).toLocaleDateString('zh-CN', {
    year: 'numeric',
    month: 'long',
    day: 'numeric',
    hour: '2-digit',
    minute: '2-digit'
  })
}

const getCategoryName = (category) => {
  const categoryMap = {
    books: '图书教材',
    electronics: '数码产品',
    clothing: '服装鞋帽',
    daily: '生活用品',
    sports: '运动户外',
    other: '其他'
  }
  return categoryMap[category] || '其他'
}
</script>

<style scoped>
.product-detail-page {
  min-height: 100vh;
  background: #f8f9fa;
}

.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 2rem 20px;
}

/* 面包屑导航 */
.breadcrumb {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  margin-bottom: 2rem;
  font-size: 0.9rem;
  color: #666;
}

.breadcrumb a {
  color: #409eff;
  text-decoration: none;
}

.breadcrumb a:hover {
  text-decoration: underline;
}

.breadcrumb .separator {
  color: #ccc;
}

.breadcrumb .current {
  color: #333;
}

/* 商品详情布局 */
.product-detail {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 3rem;
  margin-bottom: 3rem;
}

/* 商品图片区域 */
.product-gallery {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.main-image {
  background: white;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
}

.main-image img {
  width: 100%;
  height: 400px;
  object-fit: cover;
}

.image-thumbnails {
  display: flex;
  gap: 0.5rem;
  overflow-x: auto;
}

.thumbnail {
  width: 80px;
  height: 80px;
  border-radius: 8px;
  overflow: hidden;
  cursor: pointer;
  border: 2px solid transparent;
  transition: border-color 0.3s;
  flex-shrink: 0;
}

.thumbnail.active {
  border-color: #409eff;
}

.thumbnail img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

/* 商品信息区域 */
.product-info {
  background: white;
  padding: 2rem;
  border-radius: 12px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
}

.product-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 1.5rem;
}

.product-title {
  margin: 0;
  color: #333;
  font-size: 1.5rem;
  font-weight: 600;
  line-height: 1.4;
  flex: 1;
}

.product-status {
  padding: 0.25rem 0.75rem;
  border-radius: 20px;
  font-size: 0.8rem;
  font-weight: 500;
  white-space: nowrap;
}

.product-status.selling {
  background: #67c23a;
  color: white;
}

.product-status.sold {
  background: #909399;
  color: white;
}

/* 价格区域 */
.product-price-section {
  margin-bottom: 1.5rem;
  padding-bottom: 1.5rem;
  border-bottom: 1px solid #f0f0f0;
}

.price {
  font-size: 2rem;
  font-weight: bold;
  color: #f56c6c;
  margin-bottom: 0.5rem;
}

.original-price {
  font-size: 1rem;
  color: #999;
  text-decoration: line-through;
  margin-bottom: 0.5rem;
}

.price-tag {
  display: inline-block;
  padding: 0.25rem 0.75rem;
  background: #f0f9ff;
  color: #67c23a;
  border-radius: 4px;
  font-size: 0.8rem;
  font-weight: 500;
}

/* 商品元信息 */
.product-meta {
  margin-bottom: 1.5rem;
  padding-bottom: 1.5rem;
  border-bottom: 1px solid #f0f0f0;
}

.meta-item {
  display: flex;
  justify-content: space-between;
  margin-bottom: 0.5rem;
}

.meta-item .label {
  color: #666;
}

.meta-item .value {
  color: #333;
  font-weight: 500;
}

/* 商品描述 */
.product-description {
  margin-bottom: 1.5rem;
  padding-bottom: 1.5rem;
  border-bottom: 1px solid #f0f0f0;
}

.product-description h3 {
  margin: 0 0 1rem 0;
  color: #333;
  font-size: 1.1rem;
}

.product-description p {
  color: #666;
  line-height: 1.6;
  margin: 0;
}

/* 商品规格 */
.product-specs {
  margin-bottom: 1.5rem;
  padding-bottom: 1.5rem;
  border-bottom: 1px solid #f0f0f0;
}

.product-specs h3 {
  margin: 0 0 1rem 0;
  color: #333;
  font-size: 1.1rem;
}

.specs-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 0.75rem;
}

.spec-item {
  display: flex;
  justify-content: space-between;
  padding: 0.5rem;
  background: #f8f9fa;
  border-radius: 6px;
}

.spec-label {
  color: #666;
  font-weight: 500;
}

.spec-value {
  color: #333;
}

/* 卖家信息 */
.seller-info {
  margin-bottom: 2rem;
}

.seller-info h3 {
  margin: 0 0 1rem 0;
  color: #333;
  font-size: 1.1rem;
}

.seller-card {
  display: flex;
  align-items: center;
  gap: 1rem;
  padding: 1rem;
  background: #f8f9fa;
  border-radius: 8px;
}

.seller-avatar {
  width: 50px;
  height: 50px;
  border-radius: 50%;
  overflow: hidden;
  flex-shrink: 0;
}

.seller-avatar img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.seller-details {
  flex: 1;
}

.seller-name {
  font-weight: 600;
  color: #333;
  margin-bottom: 0.25rem;
}

.seller-stats {
  display: flex;
  gap: 1rem;
  margin-bottom: 0.5rem;
  font-size: 0.8rem;
  color: #666;
}

.seller-trust {
  display: flex;
  gap: 0.5rem;
}

.trust-badge {
  font-size: 0.7rem;
  padding: 0.2rem 0.5rem;
  background: white;
  border-radius: 4px;
  color: #666;
}

.contact-btn {
  padding: 0.5rem 1rem;
  background: #409eff;
  color: white;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  font-size: 0.9rem;
  white-space: nowrap;
}

.contact-btn:hover {
  background: #66b1ff;
}

/* 操作按钮 */
.action-buttons {
  display: flex;
  gap: 1rem;
}

.btn {
  padding: 0.75rem 1.5rem;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  font-weight: 500;
  transition: all 0.3s;
  flex: 1;
}

.favorite-btn {
  background: #f8f9fa;
  color: #666;
}

.favorite-btn.active {
  background: #fef0f0;
  color: #f56c6c;
}

.favorite-btn:hover {
  background: #e9ecef;
}

.share-btn {
  background: #f8f9fa;
  color: #666;
}

.share-btn:hover {
  background: #e9ecef;
}

.buy-btn {
  background: #409eff;
  color: white;
}

.buy-btn:hover:not(:disabled) {
  background: #66b1ff;
}

.buy-btn:disabled {
  background: #c0c4cc;
  cursor: not-allowed;
}

/* 相关商品 */
.related-products {
  background: white;
  padding: 2rem;
  border-radius: 12px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
}

.related-products h2 {
  margin: 0 0 1.5rem 0;
  color: #333;
}

.related-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 1.5rem;
}

.related-card {
  background: #f8f9fa;
  border-radius: 8px;
  overflow: hidden;
  cursor: pointer;
  transition: transform 0.3s;
}

.related-card:hover {
  transform: translateY(-2px);
}

.related-image {
  height: 120px;
  background: #e1e5e9;
}

.related-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.related-info {
  padding: 1rem;
}

.related-info h4 {
  margin: 0 0 0.5rem 0;
  color: #333;
  font-size: 0.9rem;
  line-height: 1.4;
}

.related-price {
  margin: 0;
  color: #f56c6c;
  font-weight: 600;
  font-size: 1rem;
}

/* 加载状态 */
.loading-container {
  text-align: center;
  padding: 4rem 2rem;
}

.loading-spinner {
  width: 50px;
  height: 50px;
  border: 4px solid #f3f3f3;
  border-top: 4px solid #409eff;
  border-radius: 50%;
  animation: spin 1s linear infinite;
  margin: 0 auto 1rem;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

/* 错误状态 */
.error-container {
  text-align: center;
  padding: 4rem 2rem;
  color: #666;
}

.error-icon {
  font-size: 4rem;
  margin-bottom: 1rem;
}

.error-container h2 {
  margin: 0 0 1rem 0;
  color: #333;
}

.error-container p {
  margin: 0 0 2rem 0;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .container {
    padding: 1rem 15px;
  }

  .product-detail {
    grid-template-columns: 1fr;
    gap: 2rem;
  }

  .main-image img {
    height: 300px;
  }

  .action-buttons {
    flex-direction: column;
  }

  .seller-card {
    flex-direction: column;
    text-align: center;
  }

  .specs-grid {
    grid-template-columns: 1fr;
  }

  .related-grid {
    grid-template-columns: repeat(auto-fit, minmax(150px, 1fr));
  }
}
</style>