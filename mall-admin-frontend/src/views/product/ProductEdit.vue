<template>
  <div class="app-container">
    <el-card>
      <template #header>
        <span>编辑商品</span>
      </template>
      <div v-if="loading" class="loading">
        <el-skeleton :rows="8" />
      </div>
      <div v-else class="form-container">
        <el-form
          ref="productFormRef"
          :model="productForm"
          :rules="productRules"
          label-width="120px"
        >
          <el-form-item label="商品名称" prop="name">
            <el-input v-model="productForm.name" placeholder="请输入商品名称" />
          </el-form-item>
          
          <el-form-item label="商品副标题" prop="subTitle">
            <el-input v-model="productForm.subTitle" placeholder="请输入商品副标题" />
          </el-form-item>
          
          <el-form-item label="商品货号" prop="productSn">
            <el-input v-model="productForm.productSn" placeholder="请输入商品货号" />
          </el-form-item>
          
          <el-form-item label="商品价格" prop="price">
            <el-input-number v-model="productForm.price" :precision="2" :min="0" />
          </el-form-item>
          
          <el-form-item label="商品库存" prop="stock">
            <el-input-number v-model="productForm.stock" :min="0" />
          </el-form-item>
          
          <el-form-item label="商品分类" prop="productCategoryId">
            <el-select v-model="productForm.productCategoryId" placeholder="请选择商品分类">
              <el-option
                v-for="item in categoryOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
          
          <el-form-item label="商品品牌" prop="brandId">
            <el-select v-model="productForm.brandId" placeholder="请选择商品品牌">
              <el-option
                v-for="item in brandOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
          
          <el-form-item label="商品描述" prop="description">
            <el-input
              v-model="productForm.description"
              type="textarea"
              :rows="4"
              placeholder="请输入商品描述"
            />
          </el-form-item>
          
          <el-form-item>
            <el-button type="primary" @click="handleSubmit" :loading="submitting">
              更新商品
            </el-button>
            <el-button @click="handleCancel">取消</el-button>
          </el-form-item>
        </el-form>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { updateProduct, getProductUpdateInfo, getProductCategoryList, getBrandList } from '@/api/product'
import { ElMessage } from 'element-plus'

const router = useRouter()
const route = useRoute()

const productFormRef = ref()
const loading = ref(false)
const submitting = ref(false)

const productForm = ref({
  name: '',
  subTitle: '',
  productSn: '',
  price: 0,
  stock: 0,
  productCategoryId: null,
  brandId: null,
  description: ''
})

const productRules = {
  name: [{ required: true, message: '请输入商品名称', trigger: 'blur' }],
  productSn: [{ required: true, message: '请输入商品货号', trigger: 'blur' }],
  price: [{ required: true, message: '请输入商品价格', trigger: 'blur' }],
  stock: [{ required: true, message: '请输入商品库存', trigger: 'blur' }],
  productCategoryId: [{ required: true, message: '请选择商品分类', trigger: 'change' }],
  brandId: [{ required: true, message: '请选择商品品牌', trigger: 'change' }]
}

const categoryOptions = ref([])
const brandOptions = ref([])

const getProductInfo = async () => {
  loading.value = true
  try {
    const response = await getProductUpdateInfo(route.params.id)
    const product = response.data.data
    
    productForm.value = {
      name: product.name || '',
      subTitle: product.subTitle || '',
      productSn: product.productSn || '',
      price: product.price || 0,
      stock: product.stock || 0,
      productCategoryId: product.productCategoryId,
      brandId: product.brandId,
      description: product.description || ''
    }
  } catch (error) {
    ElMessage.error('获取商品信息失败')
  } finally {
    loading.value = false
  }
}

const handleSubmit = () => {
  productFormRef.value.validate(async (valid) => {
    if (valid) {
      submitting.value = true
      try {
        await updateProduct(route.params.id, productForm.value)
        ElMessage.success('商品更新成功')
        router.push('/product/list')
      } catch (error) {
        ElMessage.error('商品更新失败')
      } finally {
        submitting.value = false
      }
    }
  })
}

const handleCancel = () => {
  router.push('/product/list')
}

const getOptions = async () => {
  try {
    // 获取分类选项
    const categoryResponse = await getProductCategoryList({ parentId: 0 })
    categoryOptions.value = categoryResponse.data.data.map(item => ({
      label: item.name,
      value: item.id
    }))

    // 获取品牌选项
    const brandResponse = await getBrandList()
    brandOptions.value = brandResponse.data.data.map(item => ({
      label: item.name,
      value: item.id
    }))
  } catch (error) {
    console.error('获取选项失败:', error)
  }
}

onMounted(async () => {
  await getOptions()
  await getProductInfo()
})
</script>

<style lang="scss" scoped>
.form-container {
  max-width: 600px;
}

.loading {
  padding: 20px;
}
</style>