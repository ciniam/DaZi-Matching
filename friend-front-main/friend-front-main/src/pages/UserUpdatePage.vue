<template>
  <template v-if="user">
    <!-- 顶部头像区域 -->
    <div class="avatar-section">
      <div class="avatar-wrapper" @click="triggerUpload">
        <img class="avatar-img" :src="user.avatarUrl || '/default-avatar.svg'" alt="头像" />
        <div class="avatar-overlay">
          <span>修改头像</span>
        </div>
      </div>
      <div v-if="uploading" class="uploading-hint">上传中...</div>
    </div>

    <!-- 隐藏的上传组件 -->
    <van-uploader
      ref="uploaderRef"
      :before-read="beforeRead"
      :after-read="afterRead"
      :max-count="1"
      style="display: none;"
    />

    <!-- 信息列表 -->
    <van-cell title="昵称" is-link :value="user.username"
              @click="toEdit('username', '昵称', user.username)"/>
    <van-cell title="账号" :value="user.userAccount"/>
    <van-cell title="性别" is-link :value="user.gender=== 0 ? '女': '男'"
              @click="toEdit('gender', '性别', user.gender)"/>
    <van-cell title="电话" is-link :value="user.phone" @click="toEdit('phone', '电话', user.phone)"/>
    <van-cell title="邮箱" is-link :value="user.email" @click="toEdit('email', '邮箱', user.email)"/>
    <van-cell title="个人介绍" is-link :value="user.profile"
              @click="toEdit('profile', '个人介绍', user.profile)"/>
    <van-cell title="标签" is-link to="/user/edit/tag" :value="user.tags" @click="toUpdateTag"/>
    <van-cell title="用户编号" :value="user.planetCode"/>
    <van-cell title="注册时间" :value="user.createTime"/>
  </template>
</template>

<script setup lang="ts">
import {useRouter} from "vue-router";
import {onMounted, ref} from "vue";
import myAxios from "../plugins/myAxios";
import {Toast} from "vant";
import {getCurrentUser} from "../services/user";
import {currentID} from "../states/currentID";

const user = ref();
const uploaderRef = ref();
const uploading = ref(false);
const router = useRouter();

onMounted(async () => {
  const res = await myAxios.get("/user/getNewUserInfo", {
    params: {
      id: currentID.value,
    }
  });
  user.value = res.data
})

const triggerUpload = () => {
  uploaderRef.value?.chooseFile();
};

const beforeRead = (file: any) => {
  const allowedTypes = ['image/jpeg', 'image/jpg', 'image/png', 'image/gif', 'image/bmp', 'image/webp'];
  if (!allowedTypes.includes(file.type)) {
    Toast.fail('请上传 jpg、png、gif、bmp 或 webp 格式的图片');
    return false;
  }
  const maxSize = 5 * 1024 * 1024;
  if (file.size > maxSize) {
    Toast.fail('图片大小不能超过5MB');
    return false;
  }
  return true;
};

const afterRead = async (file: any) => {
  try {
    uploading.value = true;
    const formData = new FormData();
    formData.append('file', file.file);

    const res = await myAxios.post('/upload/img', formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
    });

    if (res.code === 0 && res.data) {
      const avatarUrl = res.data;
      // 更新用户头像到后端
      const currentUser = await getCurrentUser();
      const updateRes = await myAxios.post('/user/update', {
        id: currentUser.id,
        avatarUrl,
      });
      if (updateRes.code === 0 && updateRes.data > 0) {
        user.value.avatarUrl = avatarUrl;
        Toast.success('头像修改成功');
      } else {
        Toast.fail(updateRes.message || '头像保存失败');
      }
    } else {
      Toast.fail(res.message || '图片上传失败');
    }
  } catch (error) {
    console.error('上传失败:', error);
    Toast.fail('图片上传失败，请重试');
  } finally {
    uploading.value = false;
  }
};

const toEdit = (editKey: string, editName: string, currentValue: string) => {
  router.push({
    path: '/user/edit',
    query: {
      editKey,
      editName,
      currentValue: currentValue || '',
    }
  })
}

const toUpdateTag = () => {
  router.push({
    path: '/user/edit/tag'
  })
}
</script>

<style scoped>
.avatar-section {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 24px 0;
  background: #fff;
}

.avatar-wrapper {
  position: relative;
  width: 80px;
  height: 80px;
  border-radius: 50%;
  overflow: hidden;
  cursor: pointer;
}

.avatar-img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.avatar-overlay {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  height: 28px;
  background: rgba(0, 0, 0, 0.45);
  display: flex;
  align-items: center;
  justify-content: center;
  opacity: 0;
  transition: opacity 0.2s;
}

.avatar-overlay span {
  color: #fff;
  font-size: 10px;
}

.avatar-wrapper:hover .avatar-overlay {
  opacity: 1;
}

.uploading-hint {
  margin-top: 8px;
  font-size: 12px;
  color: #1989fa;
}
</style>
