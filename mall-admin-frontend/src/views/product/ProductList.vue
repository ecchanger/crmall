<template>
  <div class="app-container">
    <!-- 搜索筛选区域 -->
    <el-card class="filter-container">
      <el-form :model="listQuery" size="small" :inline="true">
        <el-form-item label="商品名称">
          <el-input
            v-model="listQuery.keyword"
            placeholder="商品名称/副标题/货号"
            style="width: 200px"
            clearable
          />
        </el-form-item>
        <el-form-item label="商品货号">
          <el-input
            v-model="listQuery.productSn"
            placeholder="商品货号"
            style="width: 200px"
            clearable
          />
        </el-form-item>
        <el-form-item label="商品分类">
          <el-select
            v-model="listQuery.productCategoryId"
            placeholder="请选择商品分类"
            style="width: 200px"
            clearable
          >
            <el-option
              v-for="item in productCategoryOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="商品品牌">
          <el-select
            v-model="listQuery.brandId"
            placeholder="请选择商品品牌"
            style="width: 200px"
            clearable
          >
            <el-option
              v-for="item in brandOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="上架状态">
          <el-select
            v-model="listQuery.publishStatus"
            placeholder="请选择上架状态"
            style="width: 150px"
            clearable
          >
            <el-option label="上架" :value="1" />
            <el-option label="下架" :value="0" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearchList">
            <el-icon><Search /></el-icon>
            搜索
          </el-button>
          <el-button @click="handleResetSearch">
            <el-icon><Refresh /></el-icon>
            重置
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 操作按钮区域 -->
    <el-card class="operate-container">
      <el-button type="primary" @click="handleAdd">
        <el-icon><Plus /></el-icon>
        添加商品
      </el-button>
      <el-button
        type="danger"
        :disabled="multipleSelection.length === 0"
        @click="handleBatchDelete"
      >
        <el-icon><Delete /></el-icon>
        批量删除
      </el-button>
      <el-button
        type="success"
        :disabled="multipleSelection.length === 0"
        @click="handleBatchPublish"
      >
        <el-icon><Upload /></el-icon>
        批量上架
      </el-button>
      <el-button
        type="warning"
        :disabled="multipleSelection.length === 0"
        @click="handleBatchUnpublish"
      >
        <el-icon><Download /></el-icon>
        批量下架
      </el-button>
    </el-card>

    <!-- 商品列表表格 -->
    <el-card>
      <el-table
        v-loading="listLoading"
        :data="list"
        style="width: 100%"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column label="商品图片" width="120">
          <template #default="scope">
            <el-image
              :src="scope.row.pic"
              :preview-src-list="[scope.row.pic]"
              style="width: 80px; height: 80px"
              fit="cover"
            />
          </template>
        </el-table-column>
        <el-table-column label="商品名称" width="200">
          <template #default="scope">
            <div>{{ scope.row.name }}</div>
            <div style="color: #999; font-size: 12px">货号: {{ scope.row.productSn }}</div>
          </template>
        </el-table-column>
        <el-table-column label="价格/货号" width="150">
          <template #default="scope">
            <div style="color: #f56c6c; font-weight: bold">
              ¥{{ scope.row.price }}
            </div>
            <div style="color: #999; font-size: 12px">
              {{ scope.row.productSn }}
            </div>
          </template>
        </el-table-column>
        <el-table-column label="标签" width="120">
          <template #default="scope">
            <el-tag v-if="scope.row.verifyStatus === 1" type="success" size="small">
              审核通过
            </el-tag>
            <el-tag v-else type="danger" size="small">
              未审核
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="排序" width="100" prop="sort" />
        <el-table-column label="SKU库存" width="100">
          <template #default="scope">
            <el-tag v-if="scope.row.stock > 0" type="success" size="small">
              {{ scope.row.stock }}
            </el-tag>
            <el-tag v-else type="danger" size="small">
              无库存
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="销量" width="100" prop="sale" />
        <el-table-column label="状态" width="100">
          <template #default="scope">
            <el-switch
              v-model="scope.row.publishStatus"
              :active-value="1"
              :inactive-value="0"
              @change="handlePublishStatusChange(scope.$index, scope.row)"
            />
          </template>
        </el-table-column>
        <el-table-column label="操作" fixed="right" width="200">
          <template #default="scope">
            <el-button
              type="primary"
              size="small"
              @click="handleView(scope.$index, scope.row)"
            >
              查看
            </el-button>
            <el-button
              type="primary"
              size="small"
              @click="handleEdit(scope.$index, scope.row)"
            >
              编辑
            </el-button>
            <el-button
              type="danger"
              size="small"
              @click="handleDelete(scope.$index, scope.row)"
            >
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 分页 -->
    <div class="pagination-container">
      <el-pagination
        v-model:current-page="listQuery.pageNum"
        v-model:page-size="listQuery.pageSize"
        :page-sizes="[5, 10, 15]"
        :total="total"
        background
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import {
  getProductList,
  updatePublishStatus,
  updateDeleteStatus,
  getProductCategoryList,
  getBrandList
} from '@/api/product'
import { ElMessage, ElMessageBox } from 'element-plus'

const router = useRouter()

const list = ref([])
const total = ref(0)
const listLoading = ref(false)
const multipleSelection = ref([])

const listQuery = ref({
  keyword: null,
  productSn: null,
  productCategoryId: null,
  brandId: null,
  publishStatus: null,
  verifyStatus: null,
  pageNum: 1,
  pageSize: 10
})

const productCategoryOptions = ref([])
const brandOptions = ref([])

// 获取商品列表
const getList = async () => {
  listLoading.value = true
  try {
    const response = await getProductList(listQuery.value)
    list.value = response.data.data.list
    total.value = response.data.data.total
  } catch (error) {
    ElMessage.error('获取商品列表失败')
  } finally {
    listLoading.value = false
  }
}

// 获取商品分类选项
const getProductCategoryOptions = async () => {
  try {
    const response = await getProductCategoryList({ parentId: 0 })
    productCategoryOptions.value = response.data.data.map(item => ({
      label: item.name,
      value: item.id
    }))
  } catch (error) {
    console.error('获取商品分类失败:', error)
  }
}

// 获取品牌选项
const getBrandOptions = async () => {
  try {
    const response = await getBrandList()
    brandOptions.value = response.data.data.map(item => ({
      label: item.name,
      value: item.id
    }))
  } catch (error) {
    console.error('获取品牌列表失败:', error)
  }
}

// 搜索
const handleSearchList = () => {
  listQuery.value.pageNum = 1
  getList()
}

// 重置搜索
const handleResetSearch = () => {
  listQuery.value = {
    keyword: null,
    productSn: null,
    productCategoryId: null,
    brandId: null,
    publishStatus: null,
    verifyStatus: null,
    pageNum: 1,
    pageSize: 10
  }
  getList()
}

// 多选变化
const handleSelectionChange = (val) => {
  multipleSelection.value = val
}

// 分页大小变化
const handleSizeChange = (val) => {
  listQuery.value.pageNum = 1
  listQuery.value.pageSize = val
  getList()
}

// 当前页变化
const handleCurrentChange = (val) => {
  listQuery.value.pageNum = val
  getList()
}

// 添加商品
const handleAdd = () => {
  router.push('/product/add')
}

// 批量删除
const handleBatchDelete = async () => {
  try {
    await ElMessageBox.confirm('确定要删除选中的商品吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    const ids = multipleSelection.value.map(item => item.id)
    await updateDeleteStatus({ ids, deleteStatus: 1 })
    ElMessage.success('删除成功')
    getList()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}

// 批量上架
const handleBatchPublish = async () => {
  try {
    const ids = multipleSelection.value.map(item => item.id)
    await updatePublishStatus({ ids, publishStatus: 1 })
    ElMessage.success('上架成功')
    getList()
  } catch (error) {
    ElMessage.error('上架失败')
  }
}

// 批量下架
const handleBatchUnpublish = async () => {
  try {
    const ids = multipleSelection.value.map(item => item.id)
    await updatePublishStatus({ ids, publishStatus: 0 })
    ElMessage.success('下架成功')
    getList()
  } catch (error) {
    ElMessage.error('下架失败')
  }
}

// 查看商品
const handleView = (index, row) => {
  router.push(`/product/detail/${row.id}`)
}

// 编辑商品
const handleEdit = (index, row) => {
  router.push(`/product/edit/${row.id}`)
}

// 删除商品
const handleDelete = async (index, row) => {
  try {
    await ElMessageBox.confirm('确定要删除该商品吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    await updateDeleteStatus({ ids: [row.id], deleteStatus: 1 })
    ElMessage.success('删除成功')
    getList()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}

// 上架状态变化
const handlePublishStatusChange = async (index, row) => {
  try {
    await updatePublishStatus({ 
      ids: [row.id], 
      publishStatus: row.publishStatus 
    })
    ElMessage.success(row.publishStatus === 1 ? '上架成功' : '下架成功')
  } catch (error) {
    ElMessage.error('操作失败')
    // 回滚状态
    row.publishStatus = row.publishStatus === 1 ? 0 : 1
  }
}

onMounted(() => {
  getList()
  getProductCategoryOptions()
  getBrandOptions()
})
</script>

<style lang="scss" scoped>
.app-container {
  .filter-container {
    margin-bottom: 20px;
  }
  
  .operate-container {
    margin-bottom: 20px;
    
    .el-button {
      margin-right: 10px;
    }
  }
}
</style>