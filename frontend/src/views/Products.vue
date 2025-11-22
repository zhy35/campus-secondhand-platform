<template>
  <div class="products-page">
    <NavBar />

    <div class="container">
      <!-- 搜索和筛选 -->
      <div class="filters-section">
        <div class="search-box">
          <input
            v-model="searchKeyword"
            type="text"
            placeholder="搜索商品名称、描述..."
            class="search-input"
            @keyup.enter="handleSearch"
          />
          <button @click="handleSearch" class="search-btn">
            <span class="search-icon">🔍</span>
            搜索
          </button>
        </div>

        <div class="filter-row">
          <div class="filter-group">
            <label>分类：</label>
            <select v-model="filters.category" class="filter-select">
              <option value="">全部分类</option>
              <option value="books">图书教材</option>
              <option value="electronics">数码产品</option>
              <option value="clothing">服装鞋帽</option>
              <option value="daily">生活用品</option>
              <option value="sports">运动户外</option>
              <option value="other">其他</option>
            </select>
          </div>

          <div class="filter-group">
            <label>价格：</label>
            <select v-model="filters.priceRange" class="filter-select">
              <option value="">全部价格</option>
              <option value="0-50">50元以下</option>
              <option value="50-100">50-100元</option>
              <option value="100-200">100-200元</option>
              <option value="200-500">200-500元</option>
              <option value="500-">500元以上</option>
            </select>
          </div>

          <div class="filter-group">
            <label>状态：</label>
            <select v-model="filters.status" class="filter-select">
              <option value="">全部状态</option>
              <option value="selling">出售中</option>
              <option value="sold">已售出</option>
            </select>
          </div>

          <div class="filter-group">
            <label>排序：</label>
            <select v-model="sortBy" class="filter-select">
              <option value="newest">最新发布</option>
              <option value="price-low">价格从低到高</option>
              <option value="price-high">价格从高到低</option>
            </select>
          </div>
        </div>
      </div>

      <!-- 商品列表 -->
      <div class="products-section">
        <div class="section-header">
          <h2>
            <span v-if="searchKeyword">搜索"{{ searchKeyword }}"</span>
            <span v-else>所有商品</span>
            <span class="product-count">(共 {{ filteredProducts.length }} 件)</span>
          </h2>
          <div class="view-options">
            <button
              :class="['view-btn', { active: viewMode === 'grid' }]"
              @click="viewMode = 'grid'"
            >
              ⬜ 网格
            </button>
            <button
              :class="['view-btn', { active: viewMode === 'list' }]"
              @click="viewMode = 'list'"
            >
              📃 列表
            </button>
          </div>
        </div>

        <!-- 加载状态 -->
        <div v-if="loading" class="loading-state">
          <div class="loading-spinner"></div>
          <p>加载中...</p>
        </div>

        <!-- 商品网格视图 -->
        <div v-else-if="viewMode === 'grid'" class="products-grid">
          <div
            v-for="product in filteredProducts"
            :key="product.id"
            class="product-card"
            @click="viewProductDetail(product.id)"
          >
            <div class="product-image">
              <div class="image-placeholder" v-if="!product.image">
                <span class="placeholder-text">📷</span>
              </div>
              <img v-else :src="product.image" :alt="product.name" />
              <div class="product-status" :class="product.status">
                {{ product.status === 'sold' ? '已售出' : '出售中' }}
              </div>
              <div class="product-favorite" @click.stop="toggleFavorite(product)">
                ♡
              </div>
            </div>
            <div class="product-info">
              <h3 class="product-name">{{ product.name }}</h3>
              <p class="product-price">¥{{ product.price }}</p>
              <p class="product-description">{{ product.description }}</p>
              <div class="product-meta">
                <span class="seller">
                  <span class="avatar">👤</span>
                  {{ product.sellerName }}
                </span>
                <span class="time">{{ formatTime(product.createTime) }}</span>
              </div>
              <div class="product-tags">
                <span class="tag category">{{ getCategoryName(product.category) }}</span>
                <span v-if="product.isNegotiable" class="tag negotiable">可议价</span>
              </div>
            </div>
          </div>
        </div>

        <!-- 商品列表视图 -->
        <div v-else class="products-list">
          <div
            v-for="product in filteredProducts"
            :key="product.id"
            class="product-list-item"
            @click="viewProductDetail(product.id)"
          >
            <div class="item-image">
              <div class="image-placeholder" v-if="!product.image">
                <span class="placeholder-text">📷</span>
              </div>
              <img v-else :src="product.image" :alt="product.name" />
            </div>
            <div class="item-info">
              <div class="item-header">
                <h3 class="product-name">{{ product.name }}</h3>
                <p class="product-price">¥{{ product.price }}</p>
              </div>
              <p class="product-description">{{ product.description }}</p>
              <div class="item-footer">
                <div class="product-tags">
                  <span class="tag category">{{ getCategoryName(product.category) }}</span>
                  <span v-if="product.isNegotiable" class="tag negotiable">可议价</span>
                  <span class="tag status" :class="product.status">
                    {{ product.status === 'sold' ? '已售出' : '出售中' }}
                  </span>
                </div>
                <div class="product-meta">
                  <span class="seller">{{ product.sellerName }}</span>
                  <span class="time">{{ formatTime(product.createTime) }}</span>
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- 空状态 -->
        <div v-if="!loading && filteredProducts.length === 0" class="empty-state">
          <div class="empty-icon">📦</div>
          <p v-if="searchKeyword || filters.category || filters.priceRange">没有找到相关商品</p>
          <p v-else>暂无商品，快来发布第一个吧！</p>
          <router-link to="/products/create" class="btn primary">发布商品</router-link>
        </div>

        <!-- 加载更多 -->
        <div v-if="filteredProducts.length > 0 && hasMore" class="load-more">
          <button @click="loadMore" class="btn outline" :disabled="loadingMore">
            {{ loadingMore ? '加载中...' : '加载更多' }}
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
const loading = ref(false)
const loadingMore = ref(false)
const searchKeyword = ref('')
const sortBy = ref('newest')
const viewMode = ref('grid') // grid 或 list
const hasMore = ref(true)

const filters = reactive({
  category: '',
  priceRange: '',
  status: ''
})

// 增强的模拟商品数据
const products = ref([
  {
    id: 1,
    name: 'Java编程思想（第五版）',
    price: 25,
    description: '计算机专业教材，九成新，无笔记划线，适合计算机专业同学',
    image: '',
    category: 'books',
    sellerName: '张同学',
    createTime: '2024-01-15T10:30:00',
    status: 'selling',
    isNegotiable: true,
    isFavorite: false
  },
  {
    id: 2,
    name: 'iPhone 12 透明保护壳',
    price: 15,
    description: '全新未使用，高清透明，防摔保护，完美贴合',
    image: '',
    category: 'electronics',
    sellerName: '李同学',
    createTime: '2024-01-14T16:20:00',
    status: 'selling',
    isNegotiable: false,
    isFavorite: false
  },
  {
    id: 3,
    name: '斯伯丁篮球 7号标准球',
    price: 50,
    description: '使用一学期，保养良好，弹性十足，无破损',
    image: '',
    category: 'sports',
    sellerName: '王同学',
    createTime: '2024-01-13T09:15:00',
    status: 'selling',
    isNegotiable: true,
    isFavorite: false
  },
  {
    id: 4,
    name: '冬季保暖棉服外套',
    price: 80,
    description: 'L码，只穿过几次，几乎全新，保暖性能好',
    image: '',
    category: 'clothing',
    sellerName: '赵同学',
    createTime: '2024-01-12T14:45:00',
    status: 'sold',
    isNegotiable: true,
    isFavorite: false
  },
  {
    id: 5,
    name: 'LED护眼台灯',
    price: 20,
    description: '三档调光，USB充电，适合宿舍使用',
    image: '',
    category: 'daily',
    sellerName: '刘同学',
    createTime: '2024-01-11T11:00:00',
    status: 'selling',
    isNegotiable: false,
    isFavorite: false
  },
  {
    id: 6,
    name: '高等数学教材全套',
    price: 18,
    description: '大一下学期使用，有少量笔记，内容完整',
    image: '',
    category: 'books',
    sellerName: '陈同学',
    createTime: '2024-01-10T13:20:00',
    status: 'selling',
    isNegotiable: true,
    isFavorite: false
  }
])

onMounted(() => {
  loadProducts()
})

const loadProducts = async () => {
  loading.value = true
  try {
    // 模拟API调用延迟
    await new Promise(resolve => setTimeout(resolve, 800))
    // 这里后续可以调用真实API
    // const response = await request.get('/products')
    // products.value = response.data
  } catch (error) {
    console.error('加载商品失败:', error)
  } finally {
    loading.value = false
  }
}

const loadMore = async () => {
  loadingMore.value = true
  try {
    // 模拟加载更多数据
    await new Promise(resolve => setTimeout(resolve, 1000))
    // 这里可以加载更多商品
  } catch (error) {
    console.error('加载更多失败:', error)
  } finally {
    loadingMore.value = false
  }
}

// 计算属性：过滤和排序商品
const filteredProducts = computed(() => {
  let result = [...products.value]

  // 搜索过滤
  if (searchKeyword.value) {
    const keyword = searchKeyword.value.toLowerCase()
    result = result.filter(product =>
      product.name.toLowerCase().includes(keyword) ||
      product.description.toLowerCase().includes(keyword)
    )
  }

  // 分类过滤
  if (filters.category) {
    result = result.filter(product => product.category === filters.category)
  }

  // 价格范围过滤
  if (filters.priceRange) {
    const [min, max] = filters.priceRange.split('-').map(Number)
    result = result.filter(product => {
      if (max) {
        return product.price >= min && product.price <= max
      } else {
        return product.price >= min
      }
    })
  }

  // 状态过滤
  if (filters.status) {
    result = result.filter(product => product.status === filters.status)
  }

  // 排序
  result.sort((a, b) => {
    switch (sortBy.value) {
      case 'price-low':
        return a.price - b.price
      case 'price-high':
        return b.price - a.price
      case 'newest':
      default:
        return new Date(b.createTime) - new Date(a.createTime)
    }
  })

  return result
})

const handleSearch = () => {
  console.log('搜索关键词:', searchKeyword.value)
}

const viewProductDetail = (productId) => {
  router.push(`/product/${productId}`)
}

const toggleFavorite = (product) => {
  product.isFavorite = !product.isFavorite
  // 这里可以调用API更新收藏状态
}

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
.products-page {
  min-height: 100vh;
  background: #f8f9fa;
}

.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 2rem 20px;
}

/* 筛选区域 */
.filters-section {
  background: white;
  padding: 2rem;
  border-radius: 12px;
  margin-bottom: 2rem;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
}

.search-box {
  display: flex;
  gap: 1rem;
  margin-bottom: 1.5rem;
}

.search-input {
  flex: 1;
  padding: 0.75rem 1rem;
  border: 2px solid #e1e5e9;
  border-radius: 8px;
  font-size: 1rem;
  transition: border-color 0.3s;
}

.search-input:focus {
  outline: none;
  border-color: #409eff;
}

.search-btn {
  padding: 0.75rem 1.5rem;
  background: #409eff;
  color: white;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  font-weight: 500;
  display: flex;
  align-items: center;
  gap: 0.5rem;
  transition: background 0.3s;
}

.search-btn:hover {
  background: #66b1ff;
}

.filter-row {
  display: flex;
  gap: 2rem;
  align-items: center;
  flex-wrap: wrap;
}

.filter-group {
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.filter-group label {
  color: #666;
  font-weight: 500;
  white-space: nowrap;
}

.filter-select {
  padding: 0.5rem;
  border: 2px solid #e1e5e9;
  border-radius: 6px;
  background: white;
  min-width: 120px;
  transition: border-color 0.3s;
}

.filter-select:focus {
  outline: none;
  border-color: #409eff;
}

/* 商品列表区域 */
.products-section {
  background: white;
  padding: 2rem;
  border-radius: 12px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 2rem;
  flex-wrap: wrap;
  gap: 1rem;
}

.section-header h2 {
  color: #333;
  margin: 0;
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.product-count {
  font-size: 1rem;
  color: #666;
  font-weight: normal;
}

.view-options {
  display: flex;
  gap: 0.5rem;
  background: #f8f9fa;
  padding: 0.25rem;
  border-radius: 8px;
}

.view-btn {
  padding: 0.5rem 1rem;
  border: none;
  background: transparent;
  border-radius: 6px;
  cursor: pointer;
  transition: all 0.3s;
}

.view-btn.active {
  background: white;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
}

/* 加载状态 */
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

/* 商品网格视图 */
.products-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 1.5rem;
}

.product-card {
  background: white;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  cursor: pointer;
  transition: all 0.3s;
  border: 1px solid #f0f0f0;
}

.product-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 25px rgba(0, 0, 0, 0.15);
}

.product-image {
  height: 200px;
  background: #f8f9fa;
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
  overflow: hidden;
}

.image-placeholder {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.placeholder-text {
  font-size: 3rem;
  opacity: 0.7;
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

.product-favorite {
  position: absolute;
  top: 0.5rem;
  left: 0.5rem;
  padding: 0.5rem;
  background: rgba(255, 255, 255, 0.9);
  border-radius: 50%;
  cursor: pointer;
  transition: all 0.3s;
}

.product-favorite:hover {
  background: white;
  transform: scale(1.1);
}

.product-info {
  padding: 1.25rem;
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
  margin: 0 0 0.5rem 0;
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
  align-items: center;
  font-size: 0.8rem;
  color: #999;
  margin-bottom: 0.75rem;
}

.seller {
  display: flex;
  align-items: center;
  gap: 0.25rem;
}

.avatar {
  font-size: 0.7rem;
}

.product-tags {
  display: flex;
  gap: 0.5rem;
  flex-wrap: wrap;
}

.tag {
  padding: 0.25rem 0.5rem;
  border-radius: 4px;
  font-size: 0.7rem;
  font-weight: 500;
}

.tag.category {
  background: #ecf5ff;
  color: #409eff;
}

.tag.negotiable {
  background: #f0f9ff;
  color: #67c23a;
}

.tag.status.selling {
  background: #f0f9ff;
  color: #67c23a;
}

.tag.status.sold {
  background: #f5f5f5;
  color: #909399;
}

/* 商品列表视图 */
.products-list {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.product-list-item {
  display: flex;
  background: white;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  cursor: pointer;
  transition: all 0.3s;
  border: 1px solid #f0f0f0;
}

.product-list-item:hover {
  transform: translateX(4px);
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.15);
}

.item-image {
  width: 120px;
  background: #f8f9fa;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}

.item-image .image-placeholder {
  width: 100%;
  height: 100%;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.item-info {
  flex: 1;
  padding: 1.25rem;
  display: flex;
  flex-direction: column;
}

.item-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 0.5rem;
}

.item-header .product-name {
  margin: 0;
  flex: 1;
}

.item-header .product-price {
  margin: 0;
  font-size: 1.5rem;
}

.item-footer {
  margin-top: auto;
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-wrap: wrap;
  gap: 1rem;
}

/* 空状态 */
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

.empty-state p {
  margin-bottom: 1.5rem;
  font-size: 1.1rem;
}

/* 加载更多 */
.load-more {
  text-align: center;
  margin-top: 2rem;
  padding-top: 2rem;
  border-top: 1px solid #f0f0f0;
}

/* 按钮样式 */
.btn {
  padding: 0.75rem 1.5rem;
  border-radius: 6px;
  text-decoration: none;
  font-weight: 500;
  transition: all 0.3s;
  border: none;
  cursor: pointer;
  display: inline-block;
  text-align: center;
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
  border: 1px solid #409eff;
}

.btn.outline:hover {
  background: #409eff;
  color: white;
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

  .filters-section {
    padding: 1.5rem;
  }

  .search-box {
    flex-direction: column;
  }

  .filter-row {
    flex-direction: column;
    gap: 1rem;
    align-items: stretch;
  }

  .filter-group {
    justify-content: space-between;
  }

  .section-header {
    flex-direction: column;
    align-items: stretch;
  }

  .view-options {
    align-self: center;
  }

  .products-grid {
    grid-template-columns: 1fr;
  }

  .product-list-item {
    flex-direction: column;
  }

  .item-image {
    width: 100%;
    height: 150px;
  }

  .item-header {
    flex-direction: column;
    gap: 0.5rem;
  }

  .item-footer {
    flex-direction: column;
    align-items: stretch;
    gap: 0.5rem;
  }
}
</style>