<template>
  <div>
    <h2 style="margin-bottom: 20px">发布新课程</h2>
    
    <el-card style="max-width: 600px">
      <el-form :model="form" label-width="100px" @submit.prevent="submit">
        <el-form-item label="课程名称" required>
          <el-input v-model="form.name" placeholder="请输入课程名称" />
        </el-form-item>
        <el-form-item label="教练姓名" required>
          <el-input v-model="form.coach" placeholder="请输入教练姓名" />
        </el-form-item>
        <el-form-item label="开始时间" required>
          <el-date-picker
            v-model="form.startTime"
            type="datetime"
            placeholder="选择开始时间"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="结束时间" required>
          <el-date-picker
            v-model="form.endTime"
            type="datetime"
            placeholder="选择结束时间"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="总名额" required>
          <el-input-number v-model="form.totalCapacity" :min="1" :max="100" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submit">创建课程</el-button>
          <el-button @click="reset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { ElMessage } from 'element-plus'
import { useRouter } from 'vue-router'
import { courseApi } from '../api'

const router = useRouter()

const form = ref({
  name: '',
  coach: '',
  startTime: null,
  endTime: null,
  totalCapacity: 20
})

const submit = async () => {
  if (!form.value.name) {
    ElMessage.warning('请输入课程名称')
    return
  }
  if (!form.value.coach) {
    ElMessage.warning('请输入教练姓名')
    return
  }
  if (!form.value.startTime) {
    ElMessage.warning('请选择开始时间')
    return
  }
  if (!form.value.endTime) {
    ElMessage.warning('请选择结束时间')
    return
  }
  if (form.value.startTime >= form.value.endTime) {
    ElMessage.warning('结束时间必须晚于开始时间')
    return
  }
  
  await courseApi.create(form.value)
  ElMessage.success('课程创建成功')
  router.push('/')
}

const reset = () => {
  form.value = {
    name: '',
    coach: '',
    startTime: null,
    endTime: null,
    totalCapacity: 20
  }
}
</script>
