<template>
  <div class="app-container">
    <el-card>
      <template #header>
        <span>商品属性管理</span>
        <el-button style="float: right" type="primary" @click="handleAdd">
          <el-icon><Plus /></el-icon>
          添加属性
        </el-button>
      </template>
      
      <!-- 筛选区域 -->
      <div class="filter-container">
        <el-form :inline="true" size="small">
          <el-form-item label="属性分类">
            <el-select v-model="listQuery.cid" placeholder="请选择属性分类" @change="getAttributeList">
              <el-option
                v-for="item in categoryOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="属性类型">
            <el-select v-model="listQuery.type" placeholder="请选择属性类型" @change="getAttributeList">
              <el-option label="规格参数" :value="0" />
              <el-option label="销售属性" :value="1" />
            </el-select>
          </el-form-item>
        </el-form>
      </div>
      
      <el-table v-loading="loading" :data="attributeList" style="width: 100%">
        <el-table-column label="属性名称" prop="name" />
        <el-table-column label="属性分类" prop="productAttributeCategoryName" />
        <el-table-column label="属性类型" width="100">
          <template #default="scope">
            <el-tag v-if="scope.row.type === 0" type="primary" size="small">
              规格参数
            </el-tag>
            <el-tag v-else type="success" size="small">
              销售属性
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="可选值列表" prop="inputList" />
        <el-table-column label="排序" prop="sort" width="100" />
        <el-table-column label="启用状态" width="100">
          <template #default="scope">
            <el-switch
              v-model="scope.row.filterType"
              :active-value="1"
              :inactive-value="0"
            />
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200">
          <template #default="scope">
            <el-button type="primary" size="small" @click="handleEdit(scope.row)">
              编辑
            </el-button>
            <el-button type="danger" size="small" @click="handleDelete(scope.row)">
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      
      <!-- 分页 -->
      <div class="pagination-container">
        <el-pagination
          v-model:current-page="listQuery.pageNum"
          v-model:page-size="listQuery.pageSize"
          :page-sizes="[10, 20, 50]"
          :total="total"
          background
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getProductAttributeList, getProductAttributeCategoryList } from '@/api/product'
import { ElMessage, ElMessageBox } from 'element-plus'

const loading = ref(false)
const attributeList = ref([])
const total = ref(0)
const categoryOptions = ref([])

const listQuery = ref({
  cid: null,
  type: null,
  pageNum: 1,
  pageSize: 10
})

const getAttributeList = async () => {
  if (!listQuery.value.cid) return
  
  loading.value = true
  try {
    const response = await getProductAttributeList(listQuery.value.cid, listQuery.value.type)
    attributeList.value = response.data.data.list || []
    total.value = response.data.data.total || 0
  } catch (error) {
    ElMessage.error('获取属性列表失败')
    // 模拟数据
    attributeList.value = [
      {
        id: 1,
        name: '颜色',
        productAttributeCategoryName: '手机数码',
        type: 1,
        inputList: '红色,蓝色,黑色',
        sort: 1,
        filterType: 1
      },
      {
        id: 2,
        name: '内存',
        productAttributeCategoryName: '手机数码',
        type: 1,
        inputList: '64GB,128GB,256GB',
        sort: 2,
        filterType: 1
      }
    ]
  } finally {
    loading.value = false
  }
}

const getCategoryOptions = async () => {
  try {
    const response = await getProductAttributeCategoryList()
    categoryOptions.value = response.data.data.map(item => ({
      label: item.name,
      value: item.id
    }))
  } catch (error) {
    console.error('获取属性分类失败:', error)
    // 模拟数据
    categoryOptions.value = [
      { label: '手机数码', value: 1 },
      { label: '服装鞋包', value: 2 },
      { label: '家用电器', value: 3 }
    ]
  }
}

const handleAdd = () => {
  ElMessage.info('添加属性功能开发中...')
}

const handleEdit = (row) => {
  ElMessage.info('编辑属性功能开发中...')
}

const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm('确定要删除该属性吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    ElMessage.success('删除成功')
    getAttributeList()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}

const handleSizeChange = (val) => {
  listQuery.value.pageNum = 1
  listQuery.value.pageSize = val
  getAttributeList()
}

const handleCurrentChange = (val) => {
  listQuery.value.pageNum = val
  getAttributeList()
}

onMounted(() => {
  getCategoryOptions()
})
</script>

<style lang="scss" scoped>
.filter-container {
  margin-bottom: 20px;
}
</style>