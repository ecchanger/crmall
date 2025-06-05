import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '@/stores/user'
import NProgress from 'nprogress'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/login/Login.vue'),
    meta: { title: '登录' }
  },
  {
    path: '/',
    component: () => import('@/layout/Layout.vue'),
    redirect: '/dashboard',
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('@/views/dashboard/Dashboard.vue'),
        meta: { title: '仪表盘', icon: 'Dashboard' }
      }
    ]
  },
  {
    path: '/product',
    component: () => import('@/layout/Layout.vue'),
    name: 'Product',
    meta: { title: '商品管理', icon: 'Goods' },
    children: [
      {
        path: 'list',
        name: 'ProductList',
        component: () => import('@/views/product/ProductList.vue'),
        meta: { title: '商品列表', icon: 'List' }
      },
      {
        path: 'add',
        name: 'ProductAdd',
        component: () => import('@/views/product/ProductAdd.vue'),
        meta: { title: '添加商品', icon: 'Plus' }
      },
      {
        path: 'edit/:id',
        name: 'ProductEdit',
        component: () => import('@/views/product/ProductEdit.vue'),
        meta: { title: '编辑商品', icon: 'Edit' },
        hidden: true
      },
      {
        path: 'category',
        name: 'ProductCategory',
        component: () => import('@/views/product/ProductCategory.vue'),
        meta: { title: '商品分类', icon: 'Menu' }
      },
      {
        path: 'brand',
        name: 'ProductBrand',
        component: () => import('@/views/product/ProductBrand.vue'),
        meta: { title: '商品品牌', icon: 'Star' }
      },
      {
        path: 'attribute',
        name: 'ProductAttribute',
        component: () => import('@/views/product/ProductAttribute.vue'),
        meta: { title: '商品属性', icon: 'Setting' }
      }
    ]
  },
  {
    path: '/order',
    component: () => import('@/layout/Layout.vue'),
    name: 'Order',
    meta: { title: '订单管理', icon: 'ShoppingCart' },
    children: [
      {
        path: 'list',
        name: 'OrderList',
        component: () => import('@/views/order/OrderList.vue'),
        meta: { title: '订单列表', icon: 'List' }
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由守卫
router.beforeEach(async (to, from, next) => {
  NProgress.start()
  
  const userStore = useUserStore()
  
  if (to.path === '/login') {
    next()
  } else if (userStore.token) {
    next()
  } else {
    next('/login')
  }
})

router.afterEach(() => {
  NProgress.done()
})

export default router