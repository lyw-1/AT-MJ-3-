<template>
  <Transition name="page-transition">
    <RouterView />
  </Transition>
</template>

<script setup lang="ts"></script>

<style>
/* 全局样式由 styles/theme.scss 提供 */

/* 页面过渡动画 */
.page-transition {
  &-enter-active,
  &-leave-active {
    transition: all 0.3s ease;
  }

  &-enter-from {
    opacity: 0;
    transform: translateX(20px);
  }

  &-leave-to {
    opacity: 0;
    transform: translateX(-20px);
  }

  &-enter-to,
  &-leave-from {
    opacity: 1;
    transform: translateX(0);
  }
}

/* 尊重用户的动画偏好设置 */
@media (prefers-reduced-motion: reduce) {
  .page-transition {
    &-enter-active,
    &-leave-active {
      transition: none;
    }

    &-enter-from,
    &-leave-to {
      opacity: 1;
      transform: none;
    }
  }
}
</style>
