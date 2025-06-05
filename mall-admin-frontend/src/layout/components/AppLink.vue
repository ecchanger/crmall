<template>
  <component :is="type" v-bind="linkProps(to)">
    <slot />
  </component>
</template>

<script setup>
import { computed } from 'vue'

const props = defineProps({
  to: {
    type: String,
    required: true
  }
})

// 判断是否为外部链接
const isExternal = (path) => {
  return /^(https?:|mailto:|tel:)/.test(path)
}

const type = computed(() => {
  if (isExternal(props.to)) {
    return 'a'
  }
  return 'router-link'
})

const linkProps = (to) => {
  if (isExternal(to)) {
    return {
      href: to,
      target: '_blank',
      rel: 'noopener'
    }
  }
  return {
    to: to
  }
}
</script>