# 商品管理后台 API 文档

## 📋 概述

本文档描述了商品管理后台系统的所有API接口，包括原有功能和新增的增强功能。

## 🔗 基础商品管理接口

### 1. 商品CRUD操作

#### 创建商品
```http
POST /product/create
Content-Type: application/json

{
  "name": "商品名称",
  "productSn": "商品货号",
  "brandId": 1,
  "productCategoryId": 1,
  "price": 999.99,
  "originalPrice": 1299.99,
  "stock": 100,
  "description": "商品描述",
  "pic": "商品主图URL",
  "albumPics": "商品相册图片",
  "publishStatus": 1,
  "newStatus": 1,
  "recommandStatus": 1
}
```

#### 更新商品
```http
POST /product/update/{id}
Content-Type: application/json
```

#### 获取商品详情
```http
GET /product/{id}
```

#### 获取商品编辑信息
```http
GET /product/updateInfo/{id}
```

#### 查询商品列表
```http
GET /product/list?pageSize=10&pageNum=1&keyword=手机&brandId=1&productCategoryId=1
```

#### 简单商品查询
```http
GET /product/simpleList?keyword=手机
```

### 2. 批量状态管理

#### 批量审核
```http
POST /product/update/verifyStatus
Content-Type: application/x-www-form-urlencoded

ids=1,2,3&verifyStatus=1&detail=审核通过
```

#### 批量上下架
```http
POST /product/update/publishStatus
Content-Type: application/x-www-form-urlencoded

ids=1,2,3&publishStatus=1
```

#### 批量推荐设置
```http
POST /product/update/recommendStatus
Content-Type: application/x-www-form-urlencoded

ids=1,2,3&recommendStatus=1
```

#### 批量新品设置
```http
POST /product/update/newStatus
Content-Type: application/x-www-form-urlencoded

ids=1,2,3&newStatus=1
```

#### 批量删除
```http
POST /product/update/deleteStatus
Content-Type: application/x-www-form-urlencoded

ids=1,2,3&deleteStatus=1
```

## 🚀 新增增强功能接口

### 3. 商品管理增强功能

#### 批量复制商品
```http
POST /product/copy
Content-Type: application/x-www-form-urlencoded

ids=1,2,3
```

#### 获取库存预警列表
```http
GET /product/stockWarning?lowStock=10&pageSize=20&pageNum=1
```

#### 批量更新价格
```http
POST /product/update/price
Content-Type: application/x-www-form-urlencoded

ids=1,2,3&price=999.99&priceType=0
```

#### 获取商品统计信息
```http
GET /product/statistics
```

#### 按分类查询商品
```http
GET /product/category/{categoryId}?pageSize=10&pageNum=1
```

#### 按品牌查询商品
```http
GET /product/brand/{brandId}?pageSize=10&pageNum=1
```

#### 高级搜索商品
```http
GET /product/advancedSearch?name=手机&brandId=1&minPrice=1000&maxPrice=5000&publishStatus=1&pageSize=10&pageNum=1
```

## 📊 商品统计接口

### 4. 统计分析功能

#### 获取商品统计概览
```http
GET /product/statistics/overview
```

**响应示例:**
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "totalCount": 1250,
    "publishedCount": 980,
    "pendingCount": 45,
    "lowStockCount": 23
  }
}
```

#### 获取分类统计（待实现）
```http
GET /product/statistics/category
```

#### 获取品牌统计（待实现）
```http
GET /product/statistics/brand
```

#### 获取价格分布统计（待实现）
```http
GET /product/statistics/price
```

#### 获取库存统计（待实现）
```http
GET /product/statistics/stock
```

## 📥📤 导入导出接口

### 5. 批量导入导出功能

#### 下载导入模板（待实现）
```http
GET /product/importExport/template
```

#### 批量导入商品（待实现）
```http
POST /product/importExport/import
Content-Type: multipart/form-data

file: [Excel文件]
```

#### 导出商品数据（待实现）
```http
GET /product/importExport/export?format=xlsx&keyword=手机&brandId=1
```

#### 获取导入历史（待实现）
```http
GET /product/importExport/importHistory?pageSize=10&pageNum=1
```

#### 获取导出历史（待实现）
```http
GET /product/importExport/exportHistory?pageSize=10&pageNum=1
```

## 🔧 增强功能接口

### 6. 智能化管理功能

#### 高级搜索（POST方式）
```http
POST /product/enhanced/advancedSearch?pageSize=10&pageNum=1
Content-Type: application/json

{
  "keyword": "手机",
  "brandId": 1,
  "productCategoryId": 2,
  "minPrice": 1000,
  "maxPrice": 5000,
  "minStock": 10,
  "maxStock": 1000,
  "publishStatus": 1,
  "verifyStatus": 1,
  "recommendStatus": 1,
  "newStatus": 1,
  "createTimeStart": "2024-01-01T00:00:00",
  "createTimeEnd": "2024-12-31T23:59:59",
  "sortField": "createTime",
  "sortOrder": "desc"
}
```

#### 批量操作
```http
POST /product/enhanced/batchOperation
Content-Type: application/json

{
  "productIds": [1, 2, 3, 4, 5],
  "operationType": 1,
  "remark": "批量上架操作",
  "verifyDetail": "审核通过",
  "priceAdjustment": 999.99,
  "priceType": 0,
  "adjustmentType": 0,
  "adjustmentRatio": 0.1
}
```

**操作类型说明:**
- 1: 上架
- 2: 下架  
- 3: 删除
- 4: 审核通过
- 5: 审核拒绝
- 6: 设为推荐
- 7: 取消推荐
- 8: 设为新品
- 9: 取消新品
- 10: 价格调整

#### 智能分类推荐（待实现）
```http
POST /product/enhanced/recommendCategory
Content-Type: application/x-www-form-urlencoded

productName=iPhone 15 Pro Max
```

#### 价格建议（待实现）
```http
GET /product/enhanced/priceSuggestion?categoryId=1&brandId=2
```

#### 库存优化建议（待实现）
```http
GET /product/enhanced/stockOptimization?productId=1
```

#### 销售趋势分析（待实现）
```http
GET /product/enhanced/salesTrend?productId=1&days=30
```

#### 相似商品推荐（待实现）
```http
GET /product/enhanced/similarProducts?productId=1&limit=10
```

## 📝 通用响应格式

### 成功响应
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    // 具体数据内容
  }
}
```

### 分页响应
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "pageNum": 1,
    "pageSize": 10,
    "totalPage": 5,
    "total": 50,
    "list": [
      // 数据列表
    ]
  }
}
```

### 错误响应
```json
{
  "code": 500,
  "message": "操作失败",
  "data": null
}
```

## 🔒 权限说明

所有接口都需要相应的权限验证，请确保：

1. 请求头包含有效的JWT Token
2. 用户具有相应的操作权限
3. 批量操作建议限制单次处理数量

## 📋 状态码说明

- **200**: 操作成功
- **400**: 请求参数错误
- **401**: 未授权访问
- **403**: 权限不足
- **404**: 资源不存在
- **500**: 服务器内部错误

## 🚀 使用建议

1. **批量操作**: 建议单次处理商品数量不超过1000个
2. **搜索优化**: 使用高级搜索时建议添加适当的筛选条件
3. **统计查询**: 统计接口可能耗时较长，建议异步调用
4. **文件上传**: 导入文件大小限制为10MB

---

**注意**: 标记为"待实现"的接口目前返回占位符响应，后续版本将提供完整功能。
