<template>
  <div class="app-container">
    <el-card>
      <template #header>
        <span>商品分类管理</span>
        <el-button style="float: right" type="primary" @click="handleAdd">
          <el-icon><Plus /></el-icon>
          添加分类
        </el-button>
      </template>
      
      <el-table
        v-loading="loading"
        :data="categoryList"
        style="width: 100%"
        row-key="id"
        :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
      >
        <el-table-column label="分类名称" prop="name" />
        <el-table-column label="级别" width="100">
          <template #default="scope">
            <el-tag v-if="scope.row.level === 0" type="primary" size="small">
              一级分类
            </el-tag>
            <el-tag v-else-if="scope.row.level === 1" type="success" size="small">
              二级分类
            </el-tag>
            <el-tag v-else type="warning" size="small">
              三级分类
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="商品数量" prop="productCount" width="100" />
        <el-table-column label="排序" prop="sort" width="100" />
        <el-table-column label="显示状态" width="100">
          <template #default="scope">
            <el-switch
              v-model="scope.row.showStatus"
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
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getProductCategoryList } from '@/api/product'
import { ElMessage, ElMessageBox } from 'element-plus'

const loading = ref(false)
const categoryList = ref([])

const getCategoryList = async () => {
  loading.value = true
  try {
    const response = await getProductCategoryList()
    categoryList.value = response.data.data || []
  } catch (error) {
    ElMessage.error('获取分类列表失败')
  } finally {
    loading.value = false
  }
}

const handleAdd = () => {
  ElMessage.info('添加分类功能开发中...')
}

const handleEdit = (row) => {
  ElMessage.info('编辑分类功能开发中...')
}

const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm('确定要删除该分类吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    ElMessage.success('删除成功')
    getCategoryList()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}

onMounted(() => {
  getCategoryList()
})
</script>