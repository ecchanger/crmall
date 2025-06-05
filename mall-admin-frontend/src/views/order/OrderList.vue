<template>
  <div class="app-container">
    <el-card>
      <template #header>
        <span>订单管理</span>
      </template>
      
      <!-- 筛选区域 -->
      <div class="filter-container">
        <el-form :inline="true" size="small">
          <el-form-item label="订单号">
            <el-input
              v-model="listQuery.orderSn"
              placeholder="请输入订单号"
              style="width: 200px"
              clearable
            />
          </el-form-item>
          <el-form-item label="订单状态">
            <el-select v-model="listQuery.status" placeholder="请选择订单状态" clearable>
              <el-option label="待付款" :value="0" />
              <el-option label="待发货" :value="1" />
              <el-option label="已发货" :value="2" />
              <el-option label="已完成" :value="3" />
              <el-option label="已关闭" :value="4" />
            </el-select>
          </el-form-item>
          <el-form-item label="订单类型">
            <el-select v-model="listQuery.orderType" placeholder="请选择订单类型" clearable>
              <el-option label="正常订单" :value="0" />
              <el-option label="秒杀订单" :value="1" />
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
      </div>
      
      <el-table v-loading="loading" :data="orderList" style="width: 100%">
        <el-table-column label="订单号" prop="orderSn" width="180" />
        <el-table-column label="提交时间" width="180">
          <template #default="scope">
            {{ formatDate(scope.row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column label="用户账号" prop="memberUsername" width="120" />
        <el-table-column label="订单金额" width="120">
          <template #default="scope">
            <span style="color: #f56c6c">¥{{ scope.row.totalAmount }}</span>
          </template>
        </el-table-column>
        <el-table-column label="支付方式" width="120">
          <template #default="scope">
            <span v-if="scope.row.payType === 0">未支付</span>
            <span v-else-if="scope.row.payType === 1">支付宝</span>
            <span v-else-if="scope.row.payType === 2">微信</span>
            <span v-else>其他</span>
          </template>
        </el-table-column>
        <el-table-column label="订单来源" width="100">
          <template #default="scope">
            <span v-if="scope.row.sourceType === 0">PC订单</span>
            <span v-else-if="scope.row.sourceType === 1">APP订单</span>
            <span v-else>其他</span>
          </template>
        </el-table-column>
        <el-table-column label="订单状态" width="120">
          <template #default="scope">
            <el-tag v-if="scope.row.status === 0" type="warning" size="small">
              待付款
            </el-tag>
            <el-tag v-else-if="scope.row.status === 1" type="primary" size="small">
              待发货
            </el-tag>
            <el-tag v-else-if="scope.row.status === 2" type="success" size="small">
              已发货
            </el-tag>
            <el-tag v-else-if="scope.row.status === 3" type="success" size="small">
              已完成
            </el-tag>
            <el-tag v-else type="danger" size="small">
              已关闭
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" fixed="right" width="200">
          <template #default="scope">
            <el-button type="primary" size="small" @click="handleView(scope.row)">
              查看
            </el-button>
            <el-button
              v-if="scope.row.status === 1"
              type="success"
              size="small"
              @click="handleDeliver(scope.row)"
            >
              发货
            </el-button>
            <el-button
              v-if="scope.row.status === 0"
              type="danger"
              size="small"
              @click="handleClose(scope.row)"
            >
              关闭
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
import { ElMessage, ElMessageBox } from 'element-plus'
import dayjs from 'dayjs'

const loading = ref(false)
const orderList = ref([])
const total = ref(0)

const listQuery = ref({
  orderSn: '',
  status: null,
  orderType: null,
  pageNum: 1,
  pageSize: 10
})

const getOrderList = async () => {
  loading.value = true
  try {
    // 模拟API调用
    await new Promise(resolve => setTimeout(resolve, 1000))
    
    // 模拟数据
    orderList.value = [
      {
        id: 1,
        orderSn: '202312010001',
        createTime: new Date(),
        memberUsername: 'test@example.com',
        totalAmount: 299.00,
        payType: 1,
        sourceType: 1,
        status: 1
      },
      {
        id: 2,
        orderSn: '202312010002',
        createTime: new Date(Date.now() - 3600000),
        memberUsername: 'user@example.com',
        totalAmount: 599.00,
        payType: 2,
        sourceType: 0,
        status: 2
      },
      {
        id: 3,
        orderSn: '202312010003',
        createTime: new Date(Date.now() - 7200000),
        memberUsername: 'admin@example.com',
        totalAmount: 199.00,
        payType: 0,
        sourceType: 1,
        status: 0
      }
    ]
    total.value = 3
  } catch (error) {
    ElMessage.error('获取订单列表失败')
  } finally {
    loading.value = false
  }
}

const formatDate = (date) => {
  return dayjs(date).format('YYYY-MM-DD HH:mm:ss')
}

const handleSearchList = () => {
  listQuery.value.pageNum = 1
  getOrderList()
}

const handleResetSearch = () => {
  listQuery.value = {
    orderSn: '',
    status: null,
    orderType: null,
    pageNum: 1,
    pageSize: 10
  }
  getOrderList()
}

const handleView = (row) => {
  ElMessage.info('查看订单详情功能开发中...')
}

const handleDeliver = async (row) => {
  try {
    await ElMessageBox.confirm('确定要发货吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    ElMessage.success('发货成功')
    getOrderList()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('发货失败')
    }
  }
}

const handleClose = async (row) => {
  try {
    await ElMessageBox.confirm('确定要关闭订单吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    ElMessage.success('关闭订单成功')
    getOrderList()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('关闭订单失败')
    }
  }
}

const handleSizeChange = (val) => {
  listQuery.value.pageNum = 1
  listQuery.value.pageSize = val
  getOrderList()
}

const handleCurrentChange = (val) => {
  listQuery.value.pageNum = val
  getOrderList()
}

onMounted(() => {
  getOrderList()
})
</script>

<style lang="scss" scoped>
.filter-container {
  margin-bottom: 20px;
}
</style>