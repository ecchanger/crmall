<template>
  <div class="common-layout">
    <el-container>
      <!-- 侧边栏 -->
      <el-aside :width="isCollapse ? '64px' : '200px'" class="sidebar-container">
        <div class="logo">
          <img v-if="!isCollapse" src="/logo.png" alt="Logo" />
          <img v-else src="/logo-mini.png" alt="Logo" />
        </div>
        <el-menu
          :default-active="activeMenu"
          :collapse="isCollapse"
          :unique-opened="false"
          :collapse-transition="false"
          mode="vertical"
          @select="handleMenuSelect"
        >
          <template v-for="route in routes" :key="route.path">
            <sidebar-item :route="route" :base-path="route.path" />
          </template>
        </el-menu>
      </el-aside>

      <!-- 主体内容 -->
      <el-container>
        <!-- 顶部导航 -->
        <el-header class="navbar">
          <div class="navbar-left">
            <el-icon class="hamburger" @click="toggleSidebar">
              <Expand v-if="isCollapse" />
              <Fold v-else />
            </el-icon>
            <breadcrumb />
          </div>
          <div class="navbar-right">
            <el-dropdown @command="handleCommand">
              <span class="el-dropdown-link">
                <el-avatar :size="36" src="/avatar.png" />
                {{ userStore.userInfo.username || 'Admin' }}
                <el-icon class="el-icon--right">
                  <arrow-down />
                </el-icon>
              </span>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item command="profile">个人中心</el-dropdown-item>
                  <el-dropdown-item command="logout">退出登录</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
        </el-header>

        <!-- 主要内容区域 -->
        <el-main class="app-main">
          <router-view v-slot="{ Component }">
            <transition name="fade-transform" mode="out-in">
              <keep-alive>
                <component :is="Component" />
              </keep-alive>
            </transition>
          </router-view>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { ElMessageBox } from 'element-plus'
import SidebarItem from './components/SidebarItem.vue'
import Breadcrumb from './components/Breadcrumb.vue'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()

const isCollapse = ref(false)

// 获取路由配置
const routes = computed(() => {
  return router.options.routes.filter(route => 
    route.path !== '/login' && 
    route.children && 
    route.children.length > 0
  )
})

// 当前激活的菜单
const activeMenu = computed(() => {
  const { meta, path } = route
  if (meta.activeMenu) {
    return meta.activeMenu
  }
  return path
})

// 切换侧边栏
const toggleSidebar = () => {
  isCollapse.value = !isCollapse.value
}

// 菜单选择
const handleMenuSelect = (key) => {
  router.push(key)
}

// 用户操作
const handleCommand = async (command) => {
  switch (command) {
    case 'profile':
      router.push('/profile')
      break
    case 'logout':
      try {
        await ElMessageBox.confirm('确定要退出登录吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        userStore.logout()
        router.push('/login')
      } catch {
        // 用户取消
      }
      break
  }
}
</script>

<style lang="scss" scoped>
.common-layout {
  height: 100vh;
}

.sidebar-container {
  background-color: #304156;
  transition: width 0.28s;
  overflow: hidden;

  .logo {
    height: 50px;
    display: flex;
    align-items: center;
    justify-content: center;
    background-color: #2b2f3a;

    img {
      height: 32px;
    }
  }

  .el-menu {
    border-right: none;
    height: calc(100vh - 50px);
    overflow-y: auto;
    background-color: #304156;

    .el-menu-item {
      color: #bfcbd9;
      
      &:hover {
        background-color: #263445 !important;
        color: #fff;
      }

      &.is-active {
        background-color: #409eff !important;
        color: #fff;
      }
    }

    .el-submenu__title {
      color: #bfcbd9;
      
      &:hover {
        background-color: #263445 !important;
        color: #fff;
      }
    }
  }
}

.navbar {
  height: 50px;
  line-height: 50px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 20px;
  background-color: #fff;
  box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);

  .navbar-left {
    display: flex;
    align-items: center;

    .hamburger {
      font-size: 20px;
      cursor: pointer;
      margin-right: 15px;
      
      &:hover {
        color: #409eff;
      }
    }
  }

  .navbar-right {
    .el-dropdown-link {
      cursor: pointer;
      display: flex;
      align-items: center;
      
      .el-avatar {
        margin-right: 8px;
      }
    }
  }
}

.app-main {
  min-height: calc(100vh - 50px);
  background-color: #f4f4f5;
}

// 过渡动画
.fade-transform-leave-active,
.fade-transform-enter-active {
  transition: all 0.5s;
}

.fade-transform-enter-from {
  opacity: 0;
  transform: translateX(-30px);
}

.fade-transform-leave-to {
  opacity: 0;
  transform: translateX(30px);
}
</style>