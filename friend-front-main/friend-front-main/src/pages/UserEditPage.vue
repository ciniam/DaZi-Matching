<template>
  <van-form @submit="onSubmit">
    <van-field v-if="editUser.editKey!=='avatarUrl'"
               v-model="editUser.currentValue"
               :name="editUser.editKey"
               :label="editUser.editName"
               :placeholder="`请输入${editUser.editName}`"
    />
    <div v-if="editUser.editKey==='avatarUrl'">
      <van-uploader 
        v-model="fileList" 
        :max-count="1"
        :before-read="beforeRead" 
        :after-read="afterRead"
        :deletable="true"
        @delete="onDelete"
      >
        <van-button icon="plus" type="primary">上传图片</van-button>
      </van-uploader>
      <div v-if="uploading" style="margin-top: 10px; color: #1989fa;">
        正在上传...
      </div>
    </div>

    <div style="margin: 16px;">
      <van-button round block type="primary" native-type="submit" :loading="submitting">
        提交
      </van-button>
    </div>
  </van-form>
</template>

<script setup lang="ts">
import {useRoute, useRouter} from "vue-router";
import {onMounted, ref} from "vue";
import myAxios from "../plugins/myAxios";
import {Toast} from "vant";
import {getCurrentUser} from "../services/user";

const route = useRoute();
const router = useRouter();

const editUser = ref({
  editKey: route.query.editKey,
  currentValue: route.query.currentValue,
  editName: route.query.editName,
});

const fileList = ref<any[]>([]);
const uploading = ref(false);
const submitting = ref(false);
const uploadedAvatarUrl = ref(''); // 存储上传成功后的完整URL

// 初始化文件列表
onMounted(() => {
  const currentValue = editUser.value.currentValue as string;
  if (editUser.value.editKey === 'avatarUrl' && currentValue) {
    fileList.value = [{ url: currentValue, isImage: true }];
    uploadedAvatarUrl.value = currentValue;
  }
  
  // 性别字段转换
  if (editUser.value.editKey === 'gender') {
    if (editUser.value.currentValue === '1') {
      editUser.value.currentValue = '男';
    } else if (editUser.value.currentValue === '0') {
      editUser.value.currentValue = '女';
    }
  }
});

// 删除图片
const onDelete = () => {
  uploadedAvatarUrl.value = '';
  fileList.value = [];
};

const beforeRead = (file: any) => {
  // 检查文件类型
  const allowedTypes = ['image/jpeg', 'image/jpg', 'image/png', 'image/gif', 'image/bmp', 'image/webp'];
  if (!allowedTypes.includes(file.type)) {
    Toast.fail('请上传 jpg、png、gif、bmp 或 webp 格式的图片');
    return false;
  }
  
  // 检查文件大小（5MB）
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
    
    // 创建 FormData
    const formData = new FormData();
    formData.append('file', file.file);
    
    // 上传到七牛云
    const res = await myAxios.post('/upload/img', formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
    });
    
    console.log('上传响应:', res);
    
    if (res.code === 0 && res.data) {
      // 后端返回完整的URL
      uploadedAvatarUrl.value = res.data;
      editUser.value.currentValue = res.data;
      Toast.success('图片上传成功');
      
      // 更新文件列表显示
      fileList.value = [{ url: res.data, isImage: true }];
    } else {
      Toast.fail(res.message || '图片上传失败');
      // 上传失败，清空文件列表
      fileList.value = [];
    }
  } catch (error) {
    console.error('上传失败:', error);
    Toast.fail('图片上传失败，请重试');
    fileList.value = [];
  } finally {
    uploading.value = false;
  }
};

const onSubmit = async () => {
  const currentUser = await getCurrentUser();
  
  if (!currentUser) {
    Toast.fail('用户未登录');
    return;
  }
  
  // 如果是头像上传，检查是否已上传成功
  if (editUser.value.editKey === 'avatarUrl') {
    if (!uploadedAvatarUrl.value) {
      Toast.fail('请先上传图片');
      return;
    }
    editUser.value.currentValue = uploadedAvatarUrl.value;
  }
  
  // 性别字段转换
  if (editUser.value.editKey === 'gender') {
    if (editUser.value.currentValue === '男') {
      editUser.value.currentValue = '1';
    } else if (editUser.value.currentValue === '女') {
      editUser.value.currentValue = '0';
    }
  }
  
  // 手机号验证
  if (editUser.value.editKey === 'phone') {
    if (editUser.value.currentValue?.length !== 11) {
      Toast.fail('电话号码长度应为11位');
      return;
    }
  }
  
  try {
    submitting.value = true;
    
    const res = await myAxios.post('/user/update', {
      'id': currentUser.id,
      [editUser.value.editKey as string]: editUser.value.currentValue,
    });
    
    console.log('更新响应:', res);
    
    if (res.code === 0 && res.data > 0) {
      Toast.success('修改成功');
      router.back();
    } else {
      Toast.fail(res.message || '修改失败');
    }
  } catch (error) {
    console.error('更新失败:', error);
    Toast.fail('修改失败，请重试');
  } finally {
    submitting.value = false;
  }
};
</script>

<style scoped>

</style>
