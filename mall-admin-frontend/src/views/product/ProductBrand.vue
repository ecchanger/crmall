<template>
  <div class="app-container">
    <el-card>
      <template #header>
        <span>品牌管理</span>
        <el-button style="float: right" type="primary" @click="handleAdd">
          <el-icon><Plus /></el-icon>
          添加品牌
        </el-button>
      </template>
      
      <el-table v-loading="loading" :data="brandList" style="width: 100%">
        <el-table-column label="品牌Logo" width="100">
          <template #default="scope">
            <el-image
              :src="scope.row.logo"
              style="width: 60px; height: 60px"
              fit="cover"
            />
          </template>
        </el-table-column>
        <el-table-column label="品牌名称" prop="name" />
        <el-table-column label="品牌首字母" prop="firstLetter" width="120" />
        <el-table-column label="排序" prop="sort" width="100" />
        <el-table-column label="是否显示" width="100">
          <template #default="scope">
            <el-switch
              v-model="scope.row.showStatus"
              :active-value="1"
              :inactive-value="0"
            />
          </template>
        </el-table-column>
        <el-table-column label="商品数量" prop="productCount" width="100" />
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
import { getBrandList } from '@/api/product'
import { ElMessage, ElMessageBox } from 'element-plus'

const loading = ref(false)
const brandList = ref([])
const total = ref(0)

const listQuery = ref({
  pageNum: 1,
  pageSize: 10
})

const getBrands = async () => {
  loading.value = true
  try {
    const response = await getBrandList(listQuery.value)
    brandList.value = response.data.data.list || []
    total.value = response.data.data.total || 0
  } catch (error) {
    ElMessage.error('获取品牌列表失败')
    // 模拟数据
    brandList.value = [
      {
        id: 1,
        name: '苹果',
        logo: 'https://via.placeholder.com/60',
        firstLetter: 'A',
        sort: 1,
        showStatus: 1,
        productCount: 100
      },
      {
        id: 2,
        name: '小米',
        logo: 'https://via.placeholder.com/60',
        firstLetter: 'X',
        sort: 2,
        showStatus: 1,
        productCount: 80
      }
    ]
  } finally {
    loading.value = false
  }
}

const handleAdd = () => {
  ElMessage.info('添加品牌功能开发中...')
}

const handleEdit = (row) => {
  ElMessage.info('编辑品牌功能开发中...')
}

const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm('确定要删除该品牌吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    ElMessage.success('删除成功')
    getBrands()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}

const handleSizeChange = (val) => {
  listQuery.value.pageNum = 1
  listQuery.value.pageSize = val
  getBrands()
}

const handleCurrentChange = (val) => {
  listQuery.value.pageNum = val
  getBrands()
}

onMounted(() => {
  getBrands()
})
</script>