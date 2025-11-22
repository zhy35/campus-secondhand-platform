<template>
  <div class="about-page">
    <NavBar />

    <div class="container">
      <div class="hero-section">
        <h1>关于校园二手平台</h1>
        <p>连接校园，共享资源，让闲置物品找到新主人</p>
      </div>

      <div class="content-section">
        <div class="about-card">
          <h2>🎯 平台使命</h2>
          <p>我们致力于为在校学生提供一个安全、便捷的二手交易平台，促进资源循环利用，降低生活成本，培养环保意识。</p>
        </div>

        <div class="about-card">
          <h2>🌟 平台特色</h2>
          <div class="features-grid">
            <div class="feature-item">
              <div class="feature-icon">🎓</div>
              <h3>校园认证</h3>
              <p>严格的学生身份验证，确保交易安全</p>
            </div>
            <div class="feature-item">
              <div class="feature-icon">💰</div>
              <h3>经济实惠</h3>
              <p>低价购买优质物品，节省生活开支</p>
            </div>
            <div class="feature-item">
              <div class="feature-icon">🌱</div>
              <h3>环保共享</h3>
              <p>促进资源循环利用，共建绿色校园</p>
            </div>
            <div class="feature-item">
              <div class="feature-icon">🔒</div>
              <h3>安全交易</h3>
              <p>多重保障机制，交易更放心</p>
            </div>
          </div>
        </div>

        <div class="about-card">
          <h2>📞 联系我们</h2>
          <div class="contact-info">
            <div class="contact-item">
              <span class="contact-icon">📧</span>
              <div>
                <h3>邮箱</h3>
                <p>contact@campus-secondhand.com</p>
              </div>
            </div>
            <div class="contact-item">
              <span class="contact-icon">📱</span>
              <div>
                <h3>客服微信</h3>
                <p>CampusSecondhand</p>
              </div>
            </div>
            <div class="contact-item">
              <span class="contact-icon">🏢</span>
              <div>
                <h3>地址</h3>
                <p>某某大学学生活动中心</p>
              </div>
            </div>
          </div>
        </div>

        <div class="about-card">
          <h2>❓ 常见问题</h2>
          <div class="faq-list">
            <div class="faq-item">
              <h3>如何发布商品？</h3>
              <p>登录后点击"发布商品"，填写商品信息并上传图片即可。</p>
            </div>
            <div class="faq-item">
              <h3>交易是否安全？</h3>
              <p>我们采用学生实名认证，建议选择面交并在公共场所完成交易。</p>
            </div>
            <div class="faq-item">
              <h3>商品可以议价吗？</h3>
              <p>如果卖家标注"可议价"，您可以通过联系卖家进行价格协商。</p>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import NavBar from '../components/NavBar.vue'
</script>

<style scoped>
.about-page {
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.container {
  max-width: 1000px;
  margin: 0 auto;
  padding: 2rem 20px;
}

.hero-section {
  text-align: center;
  color: white;
  margin-bottom: 3rem;
  padding: 4rem 0;
}

.hero-section h1 {
  font-size: 3rem;
  margin-bottom: 1rem;
  font-weight: bold;
}

.hero-section p {
  font-size: 1.3rem;
  opacity: 0.9;
}

.content-section {
  display: flex;
  flex-direction: column;
  gap: 2rem;
}

.about-card {
  background: white;
  padding: 2.5rem;
  border-radius: 16px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
}

.about-card h2 {
  color: #333;
  margin-bottom: 1.5rem;
  font-size: 1.8rem;
  text-align: center;
}

.about-card p {
  color: #666;
  line-height: 1.7;
  font-size: 1.1rem;
  text-align: center;
  max-width: 800px;
  margin: 0 auto;
}

.features-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 2rem;
  margin-top: 2rem;
}

.feature-item {
  text-align: center;
  padding: 1.5rem;
}

.feature-icon {
  font-size: 3rem;
  margin-bottom: 1rem;
}

.feature-item h3 {
  color: #333;
  margin-bottom: 0.5rem;
  font-size: 1.2rem;
}

.feature-item p {
  color: #666;
  font-size: 0.95rem;
  margin: 0;
}

.contact-info {
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
  max-width: 500px;
  margin: 0 auto;
}

.contact-item {
  display: flex;
  align-items: center;
  gap: 1rem;
  padding: 1rem;
  background: #f8f9fa;
  border-radius: 8px;
}

.contact-icon {
  font-size: 2rem;
}

.contact-item h3 {
  margin: 0 0 0.25rem 0;
  color: #333;
  font-size: 1.1rem;
}

.contact-item p {
  margin: 0;
  color: #666;
  text-align: left;
}

.faq-list {
  max-width: 800px;
  margin: 0 auto;
}

.faq-item {
  margin-bottom: 2rem;
  padding-bottom: 2rem;
  border-bottom: 1px solid #f0f0f0;
}

.faq-item:last-child {
  margin-bottom: 0;
  padding-bottom: 0;
  border-bottom: none;
}

.faq-item h3 {
  color: #333;
  margin-bottom: 0.75rem;
  font-size: 1.2rem;
}

.faq-item p {
  color: #666;
  text-align: left;
  margin: 0;
}

@media (max-width: 768px) {
  .container {
    padding: 1rem 15px;
  }

  .hero-section {
    padding: 2rem 0;
  }

  .hero-section h1 {
    font-size: 2rem;
  }

  .hero-section p {
    font-size: 1.1rem;
  }

  .about-card {
    padding: 1.5rem;
  }

  .features-grid {
    grid-template-columns: 1fr;
    gap: 1.5rem;
  }

  .contact-item {
    flex-direction: column;
    text-align: center;
  }

  .contact-item p {
    text-align: center;
  }
}
</style>