import request from './request'

// 获取商品列表
export const getProductList = (params) => {
  return request({
    url: '/product/list',
    method: 'get',
    params
  })
}

// 创建商品
export const createProduct = (data) => {
  return request({
    url: '/product/create',
    method: 'post',
    data
  })
}

// 更新商品
export const updateProduct = (id, data) => {
  return request({
    url: `/product/update/${id}`,
    method: 'post',
    data
  })
}

// 获取商品编辑信息
export const getProductUpdateInfo = (id) => {
  return request({
    url: `/product/updateInfo/${id}`,
    method: 'get'
  })
}

// 简单商品列表查询
export const getSimpleProductList = (keyword) => {
  return request({
    url: '/product/simpleList',
    method: 'get',
    params: { keyword }
  })
}

// 批量修改审核状态
export const updateVerifyStatus = (data) => {
  return request({
    url: '/product/update/verifyStatus',
    method: 'post',
    data
  })
}

// 批量上下架商品
export const updatePublishStatus = (data) => {
  return request({
    url: '/product/update/publishStatus',
    method: 'post',
    data
  })
}

// 批量推荐商品
export const updateRecommendStatus = (data) => {
  return request({
    url: '/product/update/recommendStatus',
    method: 'post',
    data
  })
}

// 批量设为新品
export const updateNewStatus = (data) => {
  return request({
    url: '/product/update/newStatus',
    method: 'post',
    data
  })
}

// 批量修改删除状态
export const updateDeleteStatus = (data) => {
  return request({
    url: '/product/update/deleteStatus',
    method: 'post',
    data
  })
}

// 获取商品分类列表
export const getProductCategoryList = (params) => {
  return request({
    url: '/productCategory/list',
    method: 'get',
    params
  })
}

// 获取品牌列表
export const getBrandList = (params) => {
  return request({
    url: '/brand/listAll',
    method: 'get',
    params
  })
}

// 获取商品属性分类列表
export const getProductAttributeCategoryList = () => {
  return request({
    url: '/productAttribute/category/list',
    method: 'get'
  })
}

// 根据分类获取商品属性列表
export const getProductAttributeList = (cid, type) => {
  return request({
    url: '/productAttribute/list/' + cid,
    method: 'get',
    params: { type }
  })
}