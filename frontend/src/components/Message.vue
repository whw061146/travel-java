<template>
  <transition name="message-fade">
    <div v-if="visible" class="message" :class="typeClass">
      <i class="message-icon" :class="iconClass"></i>
      <span class="message-content">{{ content }}</span>
      <i v-if="showClose" class="message-close" @click="close">×</i>
    </div>
  </transition>
</template>

<script setup>
import { ref, onMounted, onBeforeUnmount, defineProps, defineExpose } from 'vue';

const props = defineProps({
  type: {
    type: String,
    default: 'info',
    validator: (value) => ['success', 'warning', 'error', 'info'].includes(value)
  },
  content: {
    type: String,
    required: true
  },
  duration: {
    type: Number,
    default: 3000
  },
  showClose: {
    type: Boolean,
    default: true
  },
  onClose: {
    type: Function,
    default: () => {}
  }
});

const visible = ref(true);
let timer = null;

const typeClass = `message-${props.type}`;

const iconClass = {
  success: 'icon-success',
  warning: 'icon-warning',
  error: 'icon-error',
  info: 'icon-info'
}[props.type];

const close = () => {
  visible.value = false;
  props.onClose();
};

onMounted(() => {
  if (props.duration > 0) {
    timer = setTimeout(() => {
      close();
    }, props.duration);
  }
});

onBeforeUnmount(() => {
  if (timer) {
    clearTimeout(timer);
    timer = null;
  }
});

// 提供一个创建消息的工厂函数，方便在其他组件中使用
// 这部分可以放在单独的文件中作为插件使用
const createMessage = (options) => {
  // 在实际项目中，这里可以动态创建组件实例并挂载到DOM
  // 这里仅作为示例，实际使用时需要结合Vue的动态组件创建机制
  console.log('创建消息:', options);
};

// 导出工厂函数供其他组件使用
defineExpose({
  close,
  createMessage
});
</script>

<style scoped>
.message {
  position: fixed;
  top: 20px;
  left: 50%;
  transform: translateX(-50%);
  min-width: 300px;
  padding: 10px 20px;
  border-radius: 4px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  display: flex;
  align-items: center;
  z-index: 10000;
  background-color: #fff;
  transition: opacity 0.3s, transform 0.3s;
}

.message-success {
  background-color: #f0f9eb;
  border: 1px solid #e1f3d8;
  color: #67c23a;
}

.message-warning {
  background-color: #fdf6ec;
  border: 1px solid #faecd8;
  color: #e6a23c;
}

.message-error {
  background-color: #fef0f0;
  border: 1px solid #fde2e2;
  color: #f56c6c;
}

.message-info {
  background-color: #edf2fc;
  border: 1px solid #ebeef5;
  color: #909399;
}

.message-icon {
  margin-right: 10px;
  font-size: 16px;
}

.message-content {
  flex: 1;
  font-size: 14px;
}

.message-close {
  margin-left: 10px;
  font-size: 16px;
  cursor: pointer;
  color: #c0c4cc;
}

.message-close:hover {
  color: #909399;
}

/* 简单的图标样式，实际项目中可以使用图标库 */
.icon-success:before { content: '✓'; }
.icon-warning:before { content: '⚠'; }
.icon-error:before { content: '✗'; }
.icon-info:before { content: 'i'; }

/* 过渡动画 */
.message-fade-enter-active,
.message-fade-leave-active {
  transition: opacity 0.3s, transform 0.3s;
}

.message-fade-enter-from,
.message-fade-leave-to {
  opacity: 0;
  transform: translate(-50%, -20px);
}
</style>