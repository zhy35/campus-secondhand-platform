<template>
  <div class="my-products-page">
    <NavBar />

    <div class="container">
      <div class="page-header">
        <div class="header-content">
          <h1>我的商品</h1>
          <p>管理您发布的闲置物品</p>
        </div>
        <router-link to="/products/create" class="btn primary">
          + 发布新商品
        </router-link>
      </div>

      <!-- 统计卡片 -->
      <div class="stats-cards">
        <div class="stat-card">
          <div class="stat-icon">📦</div>
          <div class="stat-info">
            <div class="stat-number">{{ stats.total }}</div>
            <div class="stat-label">全部商品</div>
          </div>
        </div>
        <div class="stat-card">
          <div class="stat-icon">🛒</div>
          <div class="stat-info">
            <div class="stat-number">{{ stats.selling }}</div>
            <div class="stat-label">出售中</div>
          </div>
        </div>
        <div class="stat-card">
          <div class="stat-icon">💰</div>
          <div class="stat-info">
            <div class="stat-number">{{ stats.sold }}</div>
            <div class="stat-label">已售出</div>
          </div>
        </div>
        <div class="stat-card">
          <div class="stat-icon">👁️</div>
          <div class="stat-info">
            <div class="stat-number">{{ stats.views }}</div>
            <div class="stat-label">总浏览</div>
          </div>
        </div>
      </div>

      <!-- 筛选和搜索 -->
      <div class="filters-section">
        <div class="filter-tabs">
          <button
            v-for="tab in statusTabs"
            :key="tab.value"
            :class="['tab-btn', { active: activeStatus === tab.value }]"
            @click="activeStatus = tab.value"
          >
            {{ tab.label }}
            <span class="tab-count">({{ getStatusCount(tab.value) }})</span>
          </button>
        </div>

        <div class="search-sort">
          <div class="search-box">
            <input
              v-model="searchKeyword"
              type="text"
              placeholder="搜索我的商品..."
              class="search-input"
            />
            <span class="search-icon">🔍</span>
          </div>

          <select v-model="sortBy" class="sort-select">
            <option value="newest">最新发布</option>
            <option value="price-high">价格从高到低</option>
            <option value="price-low">价格从低到高</option>
            <option value="views">浏览最多</option>
          </select>
        </div>
      </div>

      <!-- 商品列表 -->
      <div class="products-section">
        <div v-if="loading" class="loading-state">
          <div class="loading-spinner"></div>
          <p>加载中...</p>
        </div>

        <div v-else-if="filteredProducts.length === 0" class="empty-state">
          <div class="empty-icon">
            <span v-if="activeStatus === 'selling'">🛒</span>
            <span v-else-if="activeStatus === 'sold'">💰</span>
            <span v-else>📦</span>
          </div>
          <h3>{{ getEmptyStateTitle() }}</h3>
          <p>{{ getEmptyStateMessage() }}</p>
          <router-link
            v-if="activeStatus === 'selling'"
            to="/products/create"
            class="btn primary"
          >
            发布第一个商品
          </router-link>
          <button
            v-else
            class="btn outline"
            @click="activeStatus = 'selling'"
          >
            查看出售中的商品
          </button>
        </div>

        <div v-else class="products-grid">
          <div
            v-for="product in filteredProducts"
            :key="product.id"
            class="product-card"
          >
            <div class="product-image">
              <img :src="product.mainImage || '/placeholder-product.jpg'" :alt="product.name" />
              <div class="product-status" :class="product.status">
                {{ product.status === 'sold' ? '已售出' : '出售中' }}
              </div>
              <div class="product-actions">
                <button
                  class="action-btn edit"
                  @click="editProduct(product)"
                  title="编辑商品"
                >
                  ✏️
                </button>
                <button
                  class="action-btn delete"
                  @click="confirmDelete(product)"
                  title="删除商品"
                >
                  🗑️
                </button>
              </div>
            </div>

            <div class="product-info">
              <h3 class="product-name">{{ product.name }}</h3>
              <p class="product-price">¥{{ product.price }}</p>
              <p class="product-description">{{ product.description }}</p>

              <div class="product-meta">
                <div class="meta-item">
                  <span class="meta-icon">👁️</span>
                  <span>{{ product.viewCount }} 浏览</span>
                </div>
                <div class="meta-item">
                  <span class="meta-icon">❤️</span>
                  <span>{{ product.favoriteCount }} 收藏</span>
                </div>
                <div class="meta-item">
                  <span class="meta-icon">💬</span>
                  <span>{{ product.inquiryCount }} 咨询</span>
                </div>
              </div>

              <div class="product-time">
                发布于 {{ formatTime(product.createTime) }}
              </div>

              <div class="product-actions-bottom">
                <button
                  v-if="product.status === 'selling'"
                  class="btn small outline"
                  @click="markAsSold(product)"
                >
                  标记为已售
                </button>
                <button
                  v-else
                  class="btn small outline"
                  @click="relistProduct(product)"
                >
                  重新上架
                </button>
                <button
                  class="btn small primary"
                  @click="viewProductDetail(product.id)"
                >
                  查看详情
                </button>
              </div>
            </div>
          </div>
        </div>

        <!-- 加载更多 -->
        <div v-if="hasMore && !loading" class="load-more">
          <button @click="loadMore" class="btn outline" :disabled="loadingMore">
            {{ loadingMore ? '加载中...' : '加载更多' }}
          </button>
        </div>
      </div>
    </div>

    <!-- 删除确认弹窗 -->
    <div v-if="showDeleteModal" class="modal-overlay">
      <div class="modal-content">
        <div class="modal-header">
          <h2>确认删除</h2>
        </div>
        <div class="modal-body">
          <p>确定要删除商品 "<strong>{{ productToDelete?.name }}</strong>" 吗？此操作不可撤销。</p>
        </div>
        <div class="modal-actions">
          <button class="btn outline" @click="showDeleteModal = false">
            取消
          </button>
          <button class="btn danger" @click="deleteProduct" :disabled="deleting">
            {{ deleting ? '删除中...' : '确认删除' }}
          </button>
        </div>
      </div>
    </div>

    <!-- 标记为已售弹窗 -->
    <div v-if="showSoldModal" class="modal-overlay">
      <div class="modal-content">
        <div class="modal-header">
          <h2>标记为已售出</h2>
        </div>
        <div class="modal-body">
          <p>确定要将商品 "<strong>{{ productToMark?.name }}</strong>" 标记为已售出吗？</p>
          <div class="sold-price" v-if="productToMark">
            <label>实际成交价格 (元)</label>
            <input
              v-model="soldPrice"
              type="number"
              placeholder="请输入实际成交价格"
              min="0"
              step="0.01"
            />
          </div>
        </div>
        <div class="modal-actions">
          <button class="btn outline" @click="showSoldModal = false">
            取消
          </button>
          <button class="btn primary" @click="confirmMarkAsSold" :disabled="markingAsSold">
            {{ markingAsSold ? '处理中...' : '确认售出' }}
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

// 状态标签
const statusTabs = [
  { value: 'all', label: '全部' },
  { value: 'selling', label: '出售中' },
  { value: 'sold', label: '已售出' }
]

// 状态管理
const activeStatus = ref('selling')
const searchKeyword = ref('')
const sortBy = ref('newest')
const loading = ref(false)
const loadingMore = ref(false)
const hasMore = ref(true)

// 弹窗状态
const showDeleteModal = ref(false)
const showSoldModal = ref(false)
const deleting = ref(false)
const markingAsSold = ref(false)
const productToDelete = ref(null)
const productToMark = ref(null)
const soldPrice = ref('')

// 统计数据
const stats = ref({
  total: 0,
  selling: 0,
  sold: 0,
  views: 0
})

// 模拟商品数据
const products = ref([
  {
    id: 1,
    name: 'Java编程思想（第五版）',
    price: 25,
    description: '计算机专业教材，九成新，无笔记划线',
    mainImage: '',
    status: 'selling',
    viewCount: 156,
    favoriteCount: 12,
    inquiryCount: 8,
    createTime: '2024-01-15T10:30:00',
    category: 'books'
  },
  {
    id: 2,
    name: 'iPhone 12 透明保护壳',
    price: 15,
    description: '全新未使用，高清透明，防摔保护',
    mainImage: '',
    status: 'selling',
    viewCount: 89,
    favoriteCount: 5,
    inquiryCount: 3,
    createTime: '2024-01-14T16:20:00',
    category: 'electronics'
  },
  {
    id: 3,
    name: '斯伯丁篮球',
    price: 50,
    description: '使用一学期，保养良好，弹性十足',
    mainImage: '',
    status: 'sold',
    viewCount: 234,
    favoriteCount: 18,
    inquiryCount: 15,
    createTime: '2024-01-13T09:15:00',
    category: 'sports',
    soldPrice: 45,
    soldTime: '2024-01-20T14:30:00'
  },
  {
    id: 4,
    name: '冬季保暖棉服外套',
    price: 80,
    description: 'L码，只穿过几次，几乎全新',
    mainImage: '',
    status: 'sold',
    viewCount: 167,
    favoriteCount: 9,
    inquiryCount: 6,
    createTime: '2024-01-12T14:45:00',
    category: 'clothing',
    soldPrice: 70,
    soldTime: '2024-01-18T11:20:00'
  },
  {
    id: 5,
    name: 'LED护眼台灯',
    price: 20,
    description: '三档调光，USB充电，适合宿舍使用',
    mainImage: '',
    status: 'selling',
    viewCount: 45,
    favoriteCount: 3,
    inquiryCount: 2,
    createTime: '2024-01-11T11:00:00',
    category: 'daily'
  },
  {
    id: 6,
    name: '高等数学教材全套',
    price: 18,
    description: '大一下学期使用，有少量笔记，内容完整',
    mainImage: '',
    status: 'selling',
    viewCount: 78,
    favoriteCount: 6,
    inquiryCount: 4,
    createTime: '2024-01-10T13:20:00',
    category: 'books'
  }
])

onMounted(() => {
  loadProducts()
  calculateStats()
})

// 计算属性：过滤和排序商品
const filteredProducts = computed(() => {
  let result = [...products.value]

  // 状态过滤
  if (activeStatus.value !== 'all') {
    result = result.filter(product => product.status === activeStatus.value)
  }

  // 搜索过滤
  if (searchKeyword.value) {
    const keyword = searchKeyword.value.toLowerCase()
    result = result.filter(product =>
      product.name.toLowerCase().includes(keyword) ||
      product.description.toLowerCase().includes(keyword)
    )
  }

  // 排序
  result.sort((a, b) => {
    switch (sortBy.value) {
      case 'price-high':
        return b.price - a.price
      case 'price-low':
        return a.price - b.price
      case 'views':
        return b.viewCount - a.viewCount
      case 'newest':
      default:
        return new Date(b.createTime) - new Date(a.createTime)
    }
  })

  return result
})

// 加载商品
const loadProducts = async () => {
  loading.value = true
  try {
    // 模拟API调用
    await new Promise(resolve => setTimeout(resolve, 1000))
    // 这里可以调用真实API
    // const response = await request.get('/user/products')
    // products.value = response.data
  } catch (error) {
    console.error('加载商品失败:', error)
  } finally {
    loading.value = false
  }
}

// 加载更多
const loadMore = async () => {
  loadingMore.value = true
  try {
    // 模拟加载更多
    await new Promise(resolve => setTimeout(resolve, 800))
    // 这里可以加载更多商品
  } catch (error) {
    console.error('加载更多失败:', error)
  } finally {
    loadingMore.value = false
  }
}

// 计算统计数据
const calculateStats = () => {
  stats.value.total = products.value.length
  stats.value.selling = products.value.filter(p => p.status === 'selling').length
  stats.value.sold = products.value.filter(p => p.status === 'sold').length
  stats.value.views = products.value.reduce((sum, p) => sum + p.viewCount, 0)
}

// 获取状态数量
const getStatusCount = (status) => {
  if (status === 'all') return stats.value.total
  if (status === 'selling') return stats.value.selling
  if (status === 'sold') return stats.value.sold
  return 0
}

// 获取空状态标题和消息
const getEmptyStateTitle = () => {
  if (activeStatus.value === 'selling') return '暂无出售中的商品'
  if (activeStatus.value === 'sold') return '暂无已售出的商品'
  return '暂无商品'
}

const getEmptyStateMessage = () => {
  if (activeStatus.value === 'selling') return '发布您的第一个闲置物品，开始交易吧！'
  if (activeStatus.value === 'sold') return '您还没有售出任何商品'
  return '您还没有发布任何商品'
}

// 编辑商品
const editProduct = (product) => {
  // 这里可以跳转到编辑页面或显示编辑弹窗
  alert(`编辑商品: ${product.name}`)
}

// 确认删除
const confirmDelete = (product) => {
  productToDelete.value = product
  showDeleteModal.value = true
}

// 删除商品
const deleteProduct = async () => {
  if (!productToDelete.value) return

  deleting.value = true
  try {
    // 模拟API调用
    await new Promise(resolve => setTimeout(resolve, 800))

    // 从列表中移除
    const index = products.value.findIndex(p => p.id === productToDelete.value.id)
    if (index !== -1) {
      products.value.splice(index, 1)
    }

    // 重新计算统计
    calculateStats()

    showDeleteModal.value = false
    productToDelete.value = null

    alert('商品删除成功')
  } catch (error) {
    console.error('删除商品失败:', error)
    alert('删除失败，请重试')
  } finally {
    deleting.value = false
  }
}

// 标记为已售
const markAsSold = (product) => {
  productToMark.value = product
  soldPrice.value = product.price // 默认使用原价
  showSoldModal.value = true
}

// 确认标记为已售
const confirmMarkAsSold = async () => {
  if (!productToMark.value) return

  markingAsSold.value = true
  try {
    // 模拟API调用
    await new Promise(resolve => setTimeout(resolve, 800))

    // 更新商品状态
    const product = products.value.find(p => p.id === productToMark.value.id)
    if (product) {
      product.status = 'sold'
      product.soldPrice = parseFloat(soldPrice.value) || product.price
      product.soldTime = new Date().toISOString()
    }

    // 重新计算统计
    calculateStats()

    showSoldModal.value = false
    productToMark.value = null
    soldPrice.value = ''

    alert('商品已标记为已售出')
  } catch (error) {
    console.error('标记失败:', error)
    alert('操作失败，请重试')
  } finally {
    markingAsSold.value = false
  }
}

// 重新上架
const relistProduct = async (product) => {
  try {
    // 模拟API调用
    await new Promise(resolve => setTimeout(resolve, 500))

    // 更新商品状态
    product.status = 'selling'
    delete product.soldPrice
    delete product.soldTime

    // 重新计算统计
    calculateStats()

    alert('商品已重新上架')
  } catch (error) {
    console.error('重新上架失败:', error)
    alert('操作失败，请重试')
  }
}

// 查看商品详情
const viewProductDetail = (productId) => {
  router.push(`/product/${productId}`)
}

// 格式化时间
const formatTime = (time) => {
  const now = new Date()
  const productTime = new Date(time)
  const diff = now - productTime
  const days = Math.floor(diff / (1000 * 60 * 60 * 24))

  if (days === 0) {
    return '今天'
  } else if (days === 1) {
    return '昨天'
  } else if (days < 7) {
    return `${days}天前`
  } else {
    return productTime.toLocaleDateString('zh-CN', {
      month: 'short',
      day: 'numeric'
    })
  }
}
</script>

<style scoped>
.my-products-page {
  min-height: 100vh;
  background: #f8f9fa;
}

.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 2rem 20px;
}

/* 页面头部 */
.page-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-end;
  margin-bottom: 2rem;
  flex-wrap: wrap;
  gap: 1rem;
}

.header-content h1 {
  color: #333;
  margin: 0 0 0.5rem 0;
  font-size: 2rem;
}

.header-content p {
  color: #666;
  margin: 0;
  font-size: 1.1rem;
}

/* 统计卡片 */
.stats-cards {
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
  transition: transform 0.3s;
}

.stat-card:hover {
  transform: translateY(-2px);
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

/* 筛选区域 */
.filters-section {
  background: white;
  padding: 1.5rem;
  border-radius: 12px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
  margin-bottom: 2rem;
}

.filter-tabs {
  display: flex;
  gap: 0.5rem;
  margin-bottom: 1.5rem;
  border-bottom: 1px solid #f0f0f0;
  padding-bottom: 1rem;
}

.tab-btn {
  padding: 0.75rem 1.5rem;
  border: none;
  background: transparent;
  color: #666;
  cursor: pointer;
  border-radius: 6px;
  transition: all 0.3s;
  font-weight: 500;
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.tab-btn:hover {
  background: #f8f9fa;
  color: #333;
}

.tab-btn.active {
  background: #ecf5ff;
  color: #409eff;
}

.tab-count {
  font-size: 0.8rem;
  opacity: 0.7;
}

.search-sort {
  display: flex;
  gap: 1rem;
  align-items: center;
  flex-wrap: wrap;
}

.search-box {
  position: relative;
  flex: 1;
  min-width: 250px;
}

.search-input {
  width: 100%;
  padding: 0.75rem 1rem 0.75rem 2.5rem;
  border: 2px solid #e1e5e9;
  border-radius: 8px;
  font-size: 1rem;
  transition: border-color 0.3s;
}

.search-input:focus {
  outline: none;
  border-color: #409eff;
}

.search-icon {
  position: absolute;
  left: 0.75rem;
  top: 50%;
  transform: translateY(-50%);
  color: #999;
}

.sort-select {
  padding: 0.75rem 1rem;
  border: 2px solid #e1e5e9;
  border-radius: 8px;
  background: white;
  min-width: 150px;
}

/* 商品列表 */
.products-section {
  background: white;
  padding: 2rem;
  border-radius: 12px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
}

.loading-state {
  text-align: center;
  padding: 3rem;
  color: #666;
}

.loading-spinner {
  width: 40px;
  height: 40px;
  border: 3px solid #f3f3f3;
  border-top: 3px solid #409eff;
  border-radius: 50%;
  animation: spin 1s linear infinite;
  margin: 0 auto 1rem;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

.empty-state {
  text-align: center;
  padding: 4rem 2rem;
  color: #666;
}

.empty-icon {
  font-size: 4rem;
  margin-bottom: 1rem;
  opacity: 0.5;
}

.empty-state h3 {
  margin: 0 0 1rem 0;
  color: #333;
}

.empty-state p {
  margin: 0 0 2rem 0;
  font-size: 1.1rem;
}

.products-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(350px, 1fr));
  gap: 2rem;
}

.product-card {
  background: white;
  border: 1px solid #f0f0f0;
  border-radius: 12px;
  overflow: hidden;
  transition: all 0.3s;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
}

.product-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 25px rgba(0, 0, 0, 0.15);
}

.product-image {
  position: relative;
  height: 200px;
  background: #f8f9fa;
  overflow: hidden;
}

.product-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.product-status {
  position: absolute;
  top: 0.5rem;
  right: 0.5rem;
  padding: 0.25rem 0.75rem;
  border-radius: 20px;
  font-size: 0.8rem;
  font-weight: 500;
}

.product-status.selling {
  background: #67c23a;
  color: white;
}

.product-status.sold {
  background: #909399;
  color: white;
}

.product-actions {
  position: absolute;
  top: 0.5rem;
  left: 0.5rem;
  display: flex;
  gap: 0.25rem;
  opacity: 0;
  transition: opacity 0.3s;
}

.product-card:hover .product-actions {
  opacity: 1;
}

.action-btn {
  width: 32px;
  height: 32px;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  font-size: 0.9rem;
  transition: all 0.3s;
}

.action-btn.edit {
  background: rgba(64, 158, 255, 0.9);
  color: white;
}

.action-btn.edit:hover {
  background: #409eff;
}

.action-btn.delete {
  background: rgba(245, 108, 108, 0.9);
  color: white;
}

.action-btn.delete:hover {
  background: #f56c6c;
}

.product-info {
  padding: 1.5rem;
}

.product-name {
  margin: 0 0 0.5rem 0;
  color: #333;
  font-size: 1.1rem;
  font-weight: 600;
  line-height: 1.4;
}

.product-price {
  font-size: 1.5rem;
  font-weight: bold;
  color: #f56c6c;
  margin: 0 0 0.75rem 0;
}

.product-description {
  color: #666;
  margin: 0 0 1rem 0;
  line-height: 1.5;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.product-meta {
  display: flex;
  justify-content: space-between;
  margin-bottom: 1rem;
  padding: 0.75rem;
  background: #f8f9fa;
  border-radius: 6px;
}

.meta-item {
  display: flex;
  align-items: center;
  gap: 0.25rem;
  font-size: 0.8rem;
  color: #666;
}

.meta-icon {
  font-size: 0.7rem;
}

.product-time {
  font-size: 0.8rem;
  color: #999;
  margin-bottom: 1rem;
  text-align: center;
}

.product-actions-bottom {
  display: flex;
  gap: 0.5rem;
}

.btn.small {
  padding: 0.5rem 1rem;
  font-size: 0.8rem;
  flex: 1;
}

/* 加载更多 */
.load-more {
  text-align: center;
  margin-top: 2rem;
  padding-top: 2rem;
  border-top: 1px solid #f0f0f0;
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

.modal-header {
  margin-bottom: 1.5rem;
}

.modal-header h2 {
  margin: 0;
  color: #333;
  text-align: center;
}

.modal-body {
  margin-bottom: 2rem;
}

.modal-body p {
  color: #666;
  line-height: 1.5;
  margin: 0 0 1rem 0;
}

.sold-price {
  margin-top: 1.5rem;
}

.sold-price label {
  display: block;
  margin-bottom: 0.5rem;
  color: #333;
  font-weight: 500;
}

.sold-price input {
  width: 100%;
  padding: 0.75rem 1rem;
  border: 2px solid #e1e5e9;
  border-radius: 8px;
  font-size: 1rem;
  transition: border-color 0.3s;
}

.sold-price input:focus {
  outline: none;
  border-color: #409eff;
}

.modal-actions {
  display: flex;
  gap: 1rem;
  justify-content: center;
}

/* 按钮样式 */
.btn {
  padding: 0.75rem 1.5rem;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  font-weight: 500;
  transition: all 0.3s;
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

.btn.outline:hover:not(:disabled) {
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

/* 响应式设计 */
@media (max-width: 768px) {
  .container {
    padding: 1rem 15px;
  }

  .page-header {
    flex-direction: column;
    align-items: stretch;
    text-align: center;
  }

  .stats-cards {
    grid-template-columns: repeat(2, 1fr);
  }

  .filter-tabs {
    flex-wrap: wrap;
  }

  .tab-btn {
    flex: 1;
    min-width: 100px;
    justify-content: center;
  }

  .search-sort {
    flex-direction: column;
    align-items: stretch;
  }

  .search-box {
    min-width: auto;
  }

  .products-grid {
    grid-template-columns: 1fr;
  }

  .product-meta {
    flex-direction: column;
    gap: 0.5rem;
    align-items: center;
  }

  .product-actions-bottom {
    flex-direction: column;
  }

  .modal-actions {
    flex-direction: column;
  }
}

@media (max-width: 480px) {
  .stats-cards {
    grid-template-columns: 1fr;
  }

  .products-section {
    padding: 1.5rem;
  }
}
</style>