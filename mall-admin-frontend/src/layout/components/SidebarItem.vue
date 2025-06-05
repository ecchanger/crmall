<template>
  <div v-if="!route.hidden">
    <!-- 单个菜单项 -->
    <template v-if="hasOneShowingChild(route.children, route) && (!onlyOneChild.children || onlyOneChild.noShowingChildren) && !route.alwaysShow">
      <app-link v-if="onlyOneChild.meta" :to="resolvePath(onlyOneChild.path)">
        <el-menu-item :index="resolvePath(onlyOneChild.path)" :class="{'submenu-title-noDropdown': !isNest}">
          <el-icon v-if="onlyOneChild.meta.icon">
            <component :is="onlyOneChild.meta.icon" />
          </el-icon>
          <template #title>
            <span>{{ onlyOneChild.meta.title }}</span>
          </template>
        </el-menu-item>
      </app-link>
    </template>

    <!-- 多级菜单 -->
    <el-submenu v-else ref="subMenu" :index="resolvePath(route.path)" popper-append-to-body>
      <template #title>
        <el-icon v-if="route.meta && route.meta.icon">
          <component :is="route.meta.icon" />
        </el-icon>
        <span>{{ route.meta.title }}</span>
      </template>
      <sidebar-item
        v-for="child in route.children"
        :key="child.path"
        :is-nest="true"
        :route="child"
        :base-path="resolvePath(child.path)"
        class="nest-menu"
      />
    </el-submenu>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import path from 'path-browserify'
import AppLink from './AppLink.vue'

const props = defineProps({
  route: {
    type: Object,
    required: true
  },
  isNest: {
    type: Boolean,
    default: false
  },
  basePath: {
    type: String,
    default: ''
  }
})

const onlyOneChild = ref({})

// 检查是否只有一个显示的子菜单
const hasOneShowingChild = (children = [], parent) => {
  const showingChildren = children.filter(item => {
    if (item.hidden) {
      return false
    } else {
      // 临时设置为显示
      onlyOneChild.value = item
      return true
    }
  })

  // 当只有一个子路由时，子路由会被显示
  if (showingChildren.length === 1) {
    return true
  }

  // 如果没有子路由显示，显示父路由
  if (showingChildren.length === 0) {
    onlyOneChild.value = { ...parent, path: '', noShowingChildren: true }
    return true
  }

  return false
}

// 解析路径
const resolvePath = (routePath) => {
  if (path.isAbsolute(routePath)) {
    return routePath
  }
  if (path.isAbsolute(props.basePath)) {
    return path.resolve(props.basePath, routePath)
  }
  return path.resolve(props.basePath, routePath)
}
</script>

<style lang="scss" scoped>
.nest-menu .el-submenu > .el-submenu__title,
.el-submenu .el-menu-item {
  background-color: #1f2d3d !important;

  &:hover {
    background-color: #001528 !important;
  }
}
</style>