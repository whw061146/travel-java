import { createVNode, render } from 'vue';
import MessageComponent from '../components/Message.vue';

let messageInstance = null;
let container = null;

// 创建DOM容器
const createContainer = () => {
  container = document.createElement('div');
  container.className = 'message-container';
  document.body.appendChild(container);
  return container;
};

// 关闭消息并清理DOM
const closeMessage = () => {
  if (messageInstance) {
    render(null, container);
    messageInstance = null;
    if (container) {
      document.body.removeChild(container);
      container = null;
    }
  }
};

// 创建消息
const Message = (options) => {
  if (typeof options === 'string') {
    options = {
      content: options,
      type: 'info'
    };
  }
  
  // 如果已有消息实例，先关闭
  if (messageInstance) {
    closeMessage();
  }
  
  // 创建容器
  if (!container) {
    container = createContainer();
  }
  
  // 创建VNode
  const vnode = createVNode(MessageComponent, {
    ...options,
    onClose: () => {
      if (options.onClose) options.onClose();
      setTimeout(() => {
        closeMessage();
      }, 300); // 等待动画结束
    }
  });
  
  // 渲染到DOM
  render(vnode, container);
  messageInstance = vnode;
  
  return {
    close: () => {
      if (messageInstance?.component?.exposed) {
        messageInstance.component.exposed.close();
      }
    }
  };
};

// 便捷方法
Message.success = (content, options = {}) => {
  return Message({
    type: 'success',
    content,
    ...options
  });
};

Message.warning = (content, options = {}) => {
  return Message({
    type: 'warning',
    content,
    ...options
  });
};

Message.error = (content, options = {}) => {
  return Message({
    type: 'error',
    content,
    ...options
  });
};

Message.info = (content, options = {}) => {
  return Message({
    type: 'info',
    content,
    ...options
  });
};

export default Message;