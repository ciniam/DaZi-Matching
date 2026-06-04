<template>
  <van-grid :column-num="1">
    <van-grid-item :text="`您正在和 ${targetUser.username} 聊天`"/>
  </van-grid>

  <div id="Content" ref="contentRef" style="overflow-y: scroll; padding: 8px;">
    <div v-if="connecting" style="text-align: center; color: #999; padding: 20px;">
      正在连接...
    </div>
  </div>

  <div id="input">
    <textarea v-model="text" type="text" placeholder="在此输入内容..." id="input_text"></textarea>
    <div style="clear:both"></div>
    <div>
      <van-button block type="primary" id="submit" @click="sendMessage" :disabled="!connected">
        {{ connected ? '发送' : '未连接' }}
      </van-button>
    </div>
  </div>
</template>

<script setup>
import {useRoute} from "vue-router";
import {onMounted, onUnmounted, ref, nextTick} from "vue";
import {getCurrentUser} from "../services/user";
import {currentID} from "../states/currentID";
import {Toast} from "vant";

const route = useRoute();
const targetUser = ref({
  userId: route.query.userId || '',
  avatarUrl: route.query.avatarUrl || '/default-avatar.svg',
  username: route.query.username || '未知用户',
});
const text = ref('');
const contentRef = ref(null);
const connecting = ref(true);
const connected = ref(false);

let ws = null;

onMounted(() => {
  connectWebSocket();
});

onUnmounted(() => {
  if (ws) {
    ws.close();
  }
});

const connectWebSocket = () => {
  const uid = currentID.value;
  if (!uid) {
    Toast.fail('请先登录');
    return;
  }

  ws = new WebSocket(`ws://localhost:8080/api/chat?userId=${uid}`);

  ws.onopen = () => {
    connecting.value = false;
    connected.value = true;
    appendSystemMsg('连接成功，开始聊天');
  };

  ws.onmessage = (ev) => {
    const data = JSON.parse(ev.data);
    if (data.system) {
      appendSystemMsg(data.message);
    } else {
      appendMessage(data.fromName, data.message, targetUser.value.avatarUrl, false);
    }
  };

  ws.onclose = () => {
    connected.value = false;
    appendSystemMsg('连接已断开');
  };

  ws.onerror = () => {
    Toast.fail('连接失败，请刷新重试');
  };
};

const sendMessage = () => {
  const msg = text.value.trim();
  if (!msg || !ws || ws.readyState !== WebSocket.OPEN) {
    return;
  }

  getCurrentUser().then(currentUser => {
    if (!currentUser) {
      Toast.fail('用户信息获取失败');
      return;
    }

    const payload = {
      toUserId: Number(targetUser.value.userId),
      message: msg,
    };
    ws.send(JSON.stringify(payload));

    // 在聊天区域显示自己发送的消息
    appendMessage('我', msg, currentUser.avatarUrl || '/default-avatar.svg', true);

    text.value = '';
    scrollToBottom();
  });
};

const appendMessage = (sender, msg, avatarUrl, isSelf) => {
  const container = contentRef.value;
  if (!container) return;

  const wrapper = document.createElement('div');
  wrapper.style.cssText = `display: flex; align-items: flex-start; margin: 8px 0; ${isSelf ? 'flex-direction: row-reverse;' : ''}`;

  const img = document.createElement('img');
  img.src = avatarUrl || '/default-avatar.svg';
  img.style.cssText = 'width: 40px; height: 40px; border-radius: 50%; object-fit: cover; flex-shrink: 0;';

  const bubble = document.createElement('div');
  bubble.style.cssText = `max-width: 60%; padding: 8px 12px; border-radius: 8px; font-size: 14px; word-break: break-all;
    ${isSelf ? 'background: #1989fa; color: #fff; margin-right: 8px;' : 'background: #e5e5ea; color: #333; margin-left: 8px;'}`;
  bubble.textContent = msg;

  wrapper.appendChild(img);
  wrapper.appendChild(bubble);
  container.appendChild(wrapper);

  scrollToBottom();
};

const appendSystemMsg = (msg) => {
  const container = contentRef.value;
  if (!container) return;

  const div = document.createElement('div');
  div.style.cssText = 'text-align: center; color: #999; font-size: 12px; padding: 8px;';
  div.textContent = msg;
  container.appendChild(div);

  scrollToBottom();
};

const scrollToBottom = () => {
  nextTick(() => {
    if (contentRef.value) {
      contentRef.value.scrollTop = contentRef.value.scrollHeight;
    }
  });
};
</script>

<style scoped>
#Content {
  width: 100%;
  height: 420px;
  background-color: #f0f0f0;
}

#input textarea {
  width: 100%;
  height: 80px;
  border-style: solid none none none;
  resize: none;
  padding: 8px;
  box-sizing: border-box;
}

#input {
  width: 100%;
}

#submit {
  width: 100px;
  float: right;
  margin: 8px;
}
</style>
