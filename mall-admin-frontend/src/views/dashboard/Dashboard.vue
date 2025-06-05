<template>
  <div class="app-container">
    <!-- 数据统计卡片 -->
    <el-row :gutter="20" class="mb-20">
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-item">
            <div class="stat-icon" style="background: #409eff">
              <el-icon><Goods /></el-icon>
            </div>
            <div class="stat-content">
              <div class="stat-number">{{ stats.totalProducts }}</div>
              <div class="stat-label">商品总数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-item">
            <div class="stat-icon" style="background: #67c23a">
              <el-icon><Upload /></el-icon>
            </div>
            <div class="stat-content">
              <div class="stat-number">{{ stats.publishedProducts }}</div>
              <div class="stat-label">已上架商品</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-item">
            <div class="stat-icon" style="background: #e6a23c">
              <el-icon><Warning /></el-icon>
            </div>
            <div class="stat-content">
              <div class="stat-number">{{ stats.lowStockProducts }}</div>
              <div class="stat-label">库存不足</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-item">
            <div class="stat-icon" style="background: #f56c6c">
              <el-icon><ShoppingCart /></el-icon>
            </div>
            <div class="stat-content">
              <div class="stat-number">{{ stats.todayOrders }}</div>
              <div class="stat-label">今日订单</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 图表区域 -->
    <el-row :gutter="20">
      <el-col :span="12">
        <el-card class="chart-card">
          <template #header>
            <span>商品销售趋势</span>
          </template>
          <div id="salesChart" style="height: 300px"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card class="chart-card">
          <template #header>
            <span>商品分类占比</span>
          </template>
          <div id="categoryChart" style="height: 300px"></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 最新商品和热销商品 -->
    <el-row :gutter="20" class="mt-20">
      <el-col :span="12">
        <el-card>
          <template #header>
            <span>最新商品</span>
            <el-button style="float: right" type="text" @click="$router.push('/product/list')">
              查看更多
            </el-button>
          </template>
          <el-table :data="latestProducts" style="width: 100%">
            <el-table-column label="商品图片" width="80">
              <template #default="scope">
                <el-image
                  :src="scope.row.pic"
                  style="width: 60px; height: 60px"
                  fit="cover"
                />
              </template>
            </el-table-column>
            <el-table-column label="商品名称" prop="name" />
            <el-table-column label="价格" width="100">
              <template #default="scope">
                <span style="color: #f56c6c">¥{{ scope.row.price }}</span>
              </template>
            </el-table-column>
            <el-table-column label="状态" width="80">
              <template #default="scope">
                <el-tag v-if="scope.row.publishStatus === 1" type="success" size="small">
                  已上架
                </el-tag>
                <el-tag v-else type="danger" size="small">
                  已下架
                </el-tag>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card>
          <template #header>
            <span>热销商品</span>
            <el-button style="float: right" type="text" @click="$router.push('/product/list')">
              查看更多
            </el-button>
          </template>
          <el-table :data="hotProducts" style="width: 100%">
            <el-table-column label="商品图片" width="80">
              <template #default="scope">
                <el-image
                  :src="scope.row.pic"
                  style="width: 60px; height: 60px"
                  fit="cover"
                />
              </template>
            </el-table-column>
            <el-table-column label="商品名称" prop="name" />
            <el-table-column label="销量" prop="sale" width="80" />
            <el-table-column label="库存" width="80">
              <template #default="scope">
                <el-tag v-if="scope.row.stock > 0" type="success" size="small">
                  {{ scope.row.stock }}
                </el-tag>
                <el-tag v-else type="danger" size="small">
                  缺货
                </el-tag>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted, nextTick } from 'vue'
import * as echarts from 'echarts'
import { getProductList } from '@/api/product'

const stats = ref({
  totalProducts: 0,
  publishedProducts: 0,
  lowStockProducts: 0,
  todayOrders: 0
})

const latestProducts = ref([])
const hotProducts = ref([])

// 初始化销售趋势图表
const initSalesChart = () => {
  const chartDom = document.getElementById('salesChart')
  const myChart = echarts.init(chartDom)
  
  const option = {
    tooltip: {
      trigger: 'axis'
    },
    legend: {
      data: ['销售额', '订单量']
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      containLabel: true
    },
    xAxis: {
      type: 'category',
      boundaryGap: false,
      data: ['周一', '周二', '周三', '周四', '周五', '周六', '周日']
    },
    yAxis: {
      type: 'value'
    },
    series: [
      {
        name: '销售额',
        type: 'line',
        stack: 'Total',
        data: [1200, 1320, 1010, 1340, 1290, 1330, 1320]
      },
      {
        name: '订单量',
        type: 'line',
        stack: 'Total',
        data: [220, 180, 191, 234, 290, 330, 310]
      }
    ]
  }
  
  myChart.setOption(option)
}

// 初始化分类占比图表
const initCategoryChart = () => {
  const chartDom = document.getElementById('categoryChart')
  const myChart = echarts.init(chartDom)
  
  const option = {
    tooltip: {
      trigger: 'item'
    },
    legend: {
      orient: 'vertical',
      left: 'left'
    },
    series: [
      {
        name: '商品分类',
        type: 'pie',
        radius: '50%',
        data: [
          { value: 335, name: '手机数码' },
          { value: 310, name: '服装鞋包' },
          { value: 234, name: '家用电器' },
          { value: 135, name: '食品饮料' },
          { value: 148, name: '美妆护肤' }
        ],
        emphasis: {
          itemStyle: {
            shadowBlur: 10,
            shadowOffsetX: 0,
            shadowColor: 'rgba(0, 0, 0, 0.5)'
          }
        }
      }
    ]
  }
  
  myChart.setOption(option)
}

// 获取统计数据
const getStats = async () => {
  try {
    // 模拟数据，实际应该调用相应的API
    stats.value = {
      totalProducts: 1268,
      publishedProducts: 1089,
      lowStockProducts: 15,
      todayOrders: 89
    }
  } catch (error) {
    console.error('获取统计数据失败:', error)
  }
}

// 获取最新商品
const getLatestProducts = async () => {
  try {
    const response = await getProductList({ pageNum: 1, pageSize: 5 })
    latestProducts.value = response.data.data.list || []
  } catch (error) {
    console.error('获取最新商品失败:', error)
    // 模拟数据
    latestProducts.value = [
      {
        id: 1,
        name: 'iPhone 14 Pro',
        pic: 'https://via.placeholder.com/60',
        price: 7999,
        publishStatus: 1
      },
      {
        id: 2,
        name: 'MacBook Air',
        pic: 'https://via.placeholder.com/60',
        price: 8999,
        publishStatus: 1
      }
    ]
  }
}

// 获取热销商品
const getHotProducts = async () => {
  try {
    // 模拟数据，实际应该调用相应的API
    hotProducts.value = [
      {
        id: 1,
        name: 'iPhone 14',
        pic: 'https://via.placeholder.com/60',
        sale: 1250,
        stock: 89
      },
      {
        id: 2,
        name: '小米13',
        pic: 'https://via.placeholder.com/60',
        sale: 980,
        stock: 156
      },
      {
        id: 3,
        name: 'AirPods Pro',
        pic: 'https://via.placeholder.com/60',
        sale: 750,
        stock: 0
      }
    ]
  } catch (error) {
    console.error('获取热销商品失败:', error)
  }
}

onMounted(async () => {
  await getStats()
  await getLatestProducts()
  await getHotProducts()
  
  nextTick(() => {
    initSalesChart()
    initCategoryChart()
  })
})
</script>

<style lang="scss" scoped>
.mb-20 {
  margin-bottom: 20px;
}

.mt-20 {
  margin-top: 20px;
}

.stat-card {
  .stat-item {
    display: flex;
    align-items: center;
    
    .stat-icon {
      width: 60px;
      height: 60px;
      border-radius: 8px;
      display: flex;
      align-items: center;
      justify-content: center;
      margin-right: 16px;
      
      .el-icon {
        font-size: 24px;
        color: white;
      }
    }
    
    .stat-content {
      flex: 1;
      
      .stat-number {
        font-size: 28px;
        font-weight: bold;
        color: #303133;
        line-height: 1;
      }
      
      .stat-label {
        font-size: 14px;
        color: #909399;
        margin-top: 4px;
      }
    }
  }
}

.chart-card {
  height: 400px;
}
</style>