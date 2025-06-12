# 前端集成指南

## 📋 概述

本指南帮助前端开发者快速集成商品管理后台的新增功能，提供了详细的API调用示例和UI设计建议。

## 🔧 API 调用示例

### 1. 商品统计概览

```javascript
// 获取商品统计信息
async function getProductStatistics() {
  try {
    const response = await axios.get('/product/statistics');
    const stats = response.data.data;
    
    // 更新UI显示
    updateStatisticsCards({
      total: stats.totalCount,
      published: stats.publishedCount,
      pending: stats.pendingCount,
      lowStock: stats.lowStockCount
    });
  } catch (error) {
    console.error('获取统计信息失败:', error);
  }
}

// 统计卡片组件示例
function updateStatisticsCards(stats) {
  document.getElementById('total-count').textContent = stats.total;
  document.getElementById('published-count').textContent = stats.published;
  document.getElementById('pending-count').textContent = stats.pending;
  document.getElementById('low-stock-count').textContent = stats.lowStock;
}
```

### 2. 高级搜索功能

```javascript
// 高级搜索表单数据
const searchForm = {
  keyword: '',
  brandId: null,
  productCategoryId: null,
  minPrice: null,
  maxPrice: null,
  publishStatus: null,
  verifyStatus: null,
  recommendStatus: null,
  newStatus: null,
  sortField: 'createTime',
  sortOrder: 'desc'
};

// 执行高级搜索
async function performAdvancedSearch(pageNum = 1, pageSize = 10) {
  try {
    const response = await axios.post(
      `/product/enhanced/advancedSearch?pageSize=${pageSize}&pageNum=${pageNum}`,
      searchForm
    );
    
    const result = response.data.data;
    renderProductList(result.list);
    updatePagination(result);
  } catch (error) {
    console.error('搜索失败:', error);
    showErrorMessage('搜索失败，请重试');
  }
}

// 渲染商品列表
function renderProductList(products) {
  const tbody = document.getElementById('product-table-body');
  tbody.innerHTML = products.map(product => `
    <tr>
      <td><input type="checkbox" value="${product.id}"></td>
      <td>${product.name}</td>
      <td>${product.productSn}</td>
      <td>¥${product.price}</td>
      <td>${product.stock}</td>
      <td>${getStatusText(product.publishStatus, 'publish')}</td>
      <td>${getStatusText(product.verifyStatus, 'verify')}</td>
      <td>
        <button onclick="editProduct(${product.id})">编辑</button>
        <button onclick="viewProduct(${product.id})">查看</button>
      </td>
    </tr>
  `).join('');
}
```

### 3. 批量操作功能

```javascript
// 批量操作处理
async function performBatchOperation(operationType, remark = '') {
  const selectedIds = getSelectedProductIds();
  
  if (selectedIds.length === 0) {
    showWarningMessage('请选择要操作的商品');
    return;
  }
  
  const confirmMessage = getBatchOperationConfirmMessage(operationType, selectedIds.length);
  if (!confirm(confirmMessage)) {
    return;
  }
  
  try {
    const response = await axios.post('/product/enhanced/batchOperation', {
      productIds: selectedIds,
      operationType: operationType,
      remark: remark
    });
    
    if (response.data.code === 200) {
      showSuccessMessage(`批量操作成功，影响${response.data.data}个商品`);
      refreshProductList();
    } else {
      showErrorMessage(response.data.message);
    }
  } catch (error) {
    console.error('批量操作失败:', error);
    showErrorMessage('批量操作失败，请重试');
  }
}

// 获取选中的商品ID
function getSelectedProductIds() {
  const checkboxes = document.querySelectorAll('#product-table-body input[type="checkbox"]:checked');
  return Array.from(checkboxes).map(cb => parseInt(cb.value));
}

// 批量操作按钮事件
document.getElementById('batch-publish').addEventListener('click', () => {
  performBatchOperation(1, '批量上架操作');
});

document.getElementById('batch-unpublish').addEventListener('click', () => {
  performBatchOperation(2, '批量下架操作');
});

document.getElementById('batch-delete').addEventListener('click', () => {
  performBatchOperation(3, '批量删除操作');
});
```

### 4. 库存预警功能

```javascript
// 获取库存预警列表
async function getStockWarningList(lowStock = 10) {
  try {
    const response = await axios.get(`/product/stockWarning?lowStock=${lowStock}&pageSize=20&pageNum=1`);
    const warningProducts = response.data.data.list;
    
    renderStockWarningList(warningProducts);
    updateWarningBadge(warningProducts.length);
  } catch (error) {
    console.error('获取库存预警失败:', error);
  }
}

// 渲染库存预警列表
function renderStockWarningList(products) {
  const container = document.getElementById('stock-warning-list');
  container.innerHTML = products.map(product => `
    <div class="warning-item ${product.stock === 0 ? 'out-of-stock' : 'low-stock'}">
      <div class="product-info">
        <img src="${product.pic}" alt="${product.name}" class="product-thumb">
        <div class="product-details">
          <h4>${product.name}</h4>
          <p>货号: ${product.productSn}</p>
          <p class="stock-info">当前库存: <span class="stock-number">${product.stock}</span></p>
        </div>
      </div>
      <div class="actions">
        <button onclick="editProduct(${product.id})" class="btn-primary">补货</button>
        <button onclick="viewProduct(${product.id})" class="btn-secondary">查看</button>
      </div>
    </div>
  `).join('');
}

// 更新预警徽章
function updateWarningBadge(count) {
  const badge = document.getElementById('warning-badge');
  badge.textContent = count;
  badge.style.display = count > 0 ? 'inline' : 'none';
}
```

### 5. 商品复制功能

```javascript
// 复制商品
async function copyProducts(productIds) {
  try {
    const response = await axios.post('/product/copy', `ids=${productIds.join(',')}`);
    
    if (response.data.code === 200) {
      showSuccessMessage(`成功复制${response.data.data}个商品`);
      refreshProductList();
    } else {
      showErrorMessage('复制失败: ' + response.data.message);
    }
  } catch (error) {
    console.error('复制商品失败:', error);
    showErrorMessage('复制失败，请重试');
  }
}

// 复制按钮事件
document.getElementById('copy-selected').addEventListener('click', () => {
  const selectedIds = getSelectedProductIds();
  if (selectedIds.length === 0) {
    showWarningMessage('请选择要复制的商品');
    return;
  }
  
  if (confirm(`确定要复制选中的${selectedIds.length}个商品吗？`)) {
    copyProducts(selectedIds);
  }
});
```

## 🎨 UI 组件建议

### 1. 统计卡片组件

```html
<div class="statistics-cards">
  <div class="stat-card">
    <div class="stat-icon">📦</div>
    <div class="stat-content">
      <h3 id="total-count">0</h3>
      <p>商品总数</p>
    </div>
  </div>
  
  <div class="stat-card">
    <div class="stat-icon">✅</div>
    <div class="stat-content">
      <h3 id="published-count">0</h3>
      <p>已上架</p>
    </div>
  </div>
  
  <div class="stat-card">
    <div class="stat-icon">⏳</div>
    <div class="stat-content">
      <h3 id="pending-count">0</h3>
      <p>待审核</p>
    </div>
  </div>
  
  <div class="stat-card warning">
    <div class="stat-icon">⚠️</div>
    <div class="stat-content">
      <h3 id="low-stock-count">0</h3>
      <p>库存预警</p>
    </div>
  </div>
</div>
```

### 2. 高级搜索表单

```html
<div class="advanced-search-form">
  <div class="search-row">
    <input type="text" placeholder="商品名称" v-model="searchForm.keyword">
    <select v-model="searchForm.brandId">
      <option value="">选择品牌</option>
      <option v-for="brand in brands" :value="brand.id">{{brand.name}}</option>
    </select>
    <select v-model="searchForm.productCategoryId">
      <option value="">选择分类</option>
      <option v-for="category in categories" :value="category.id">{{category.name}}</option>
    </select>
  </div>
  
  <div class="search-row">
    <input type="number" placeholder="最低价格" v-model="searchForm.minPrice">
    <input type="number" placeholder="最高价格" v-model="searchForm.maxPrice">
    <select v-model="searchForm.publishStatus">
      <option value="">上架状态</option>
      <option value="1">已上架</option>
      <option value="0">未上架</option>
    </select>
  </div>
  
  <div class="search-actions">
    <button @click="performAdvancedSearch" class="btn-primary">搜索</button>
    <button @click="resetSearchForm" class="btn-secondary">重置</button>
  </div>
</div>
```

### 3. 批量操作工具栏

```html
<div class="batch-operations">
  <div class="selection-info">
    已选择 <span id="selected-count">0</span> 个商品
  </div>
  
  <div class="batch-buttons">
    <button id="batch-publish" class="btn-success">批量上架</button>
    <button id="batch-unpublish" class="btn-warning">批量下架</button>
    <button id="batch-recommend" class="btn-info">设为推荐</button>
    <button id="copy-selected" class="btn-primary">复制商品</button>
    <button id="batch-delete" class="btn-danger">批量删除</button>
  </div>
</div>
```

## 📱 响应式设计建议

```css
/* 统计卡片响应式 */
.statistics-cards {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 20px;
  margin-bottom: 30px;
}

.stat-card {
  background: white;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
  display: flex;
  align-items: center;
}

.stat-card.warning {
  border-left: 4px solid #ff6b6b;
}

/* 搜索表单响应式 */
.advanced-search-form {
  background: white;
  padding: 20px;
  border-radius: 8px;
  margin-bottom: 20px;
}

.search-row {
  display: flex;
  gap: 15px;
  margin-bottom: 15px;
  flex-wrap: wrap;
}

.search-row input,
.search-row select {
  flex: 1;
  min-width: 150px;
  padding: 8px 12px;
  border: 1px solid #ddd;
  border-radius: 4px;
}

/* 移动端适配 */
@media (max-width: 768px) {
  .search-row {
    flex-direction: column;
  }
  
  .batch-operations {
    flex-direction: column;
    gap: 10px;
  }
  
  .batch-buttons {
    display: flex;
    flex-wrap: wrap;
    gap: 8px;
  }
}
```

## 🔔 用户体验优化

### 1. 加载状态处理

```javascript
// 显示加载状态
function showLoading(message = '加载中...') {
  const loading = document.getElementById('loading-overlay');
  loading.querySelector('.loading-text').textContent = message;
  loading.style.display = 'flex';
}

// 隐藏加载状态
function hideLoading() {
  document.getElementById('loading-overlay').style.display = 'none';
}

// API调用时使用加载状态
async function performAdvancedSearch() {
  showLoading('搜索中...');
  try {
    // API调用
  } finally {
    hideLoading();
  }
}
```

### 2. 消息提示

```javascript
// 消息提示函数
function showMessage(message, type = 'info') {
  const messageEl = document.createElement('div');
  messageEl.className = `message message-${type}`;
  messageEl.textContent = message;
  
  document.body.appendChild(messageEl);
  
  setTimeout(() => {
    messageEl.remove();
  }, 3000);
}

function showSuccessMessage(message) {
  showMessage(message, 'success');
}

function showErrorMessage(message) {
  showMessage(message, 'error');
}

function showWarningMessage(message) {
  showMessage(message, 'warning');
}
```

## 🚀 性能优化建议

1. **分页加载**: 商品列表使用分页，避免一次加载过多数据
2. **防抖搜索**: 搜索输入使用防抖，减少API调用
3. **缓存策略**: 品牌、分类等基础数据可以缓存
4. **懒加载**: 商品图片使用懒加载
5. **批量操作确认**: 大批量操作前进行二次确认

---

**提示**: 以上示例代码可以根据实际使用的前端框架（Vue、React、Angular等）进行相应调整。
