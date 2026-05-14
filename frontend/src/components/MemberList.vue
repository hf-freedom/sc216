<template>
  <div>
    <h2 style="margin-bottom: 20px">会员管理</h2>
    
    <el-button type="primary" @click="dialogVisible = true" style="margin-bottom: 20px">
      添加会员
    </el-button>

    <el-table :data="members" border stripe>
      <el-table-column prop="name" label="姓名" width="150" />
      <el-table-column prop="phone" label="手机号" width="150" />
      <el-table-column prop="noShowCount" label="爽约次数" width="100" />
      <el-table-column prop="bannedUntil" label="封禁状态" width="150">
        <template #default="{ row }">
          <el-tag v-if="row.bannedUntil && isBanned(row)" type="danger">
            封禁至 {{ formatDate(row.bannedUntil) }}
          </el-tag>
          <el-tag v-else type="success">正常</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="120">
        <template #default="{ row }">
          <el-button size="small" type="warning" @click="resetBan(row.id)" v-if="row.bannedUntil && isBanned(row)">
            解除封禁
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="dialogVisible" title="添加会员" width="400">
      <el-form :model="form" label-width="80px">
        <el-form-item label="姓名" required>
          <el-input v-model="form.name" placeholder="请输入姓名" />
        </el-form-item>
        <el-form-item label="手机号" required>
          <el-input v-model="form.phone" placeholder="请输入手机号" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="addMember">添加</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { memberApi } from '../api'

const members = ref([])
const dialogVisible = ref(false)
const form = ref({ name: '', phone: '' })

const loadMembers = async () => {
  const res = await memberApi.getAll()
  members.value = res.data
}

const isBanned = (member) => {
  if (!member.bannedUntil) return false
  return new Date(member.bannedUntil) > new Date()
}

const resetBan = async (id) => {
  await memberApi.resetBan(id)
  ElMessage.success('已解除封禁')
  loadMembers()
}

const addMember = async () => {
  if (!form.value.name) {
    ElMessage.warning('请输入姓名')
    return
  }
  if (!form.value.phone) {
    ElMessage.warning('请输入手机号')
    return
  }
  await memberApi.create(form.value)
  ElMessage.success('会员添加成功')
  dialogVisible.value = false
  form.value = { name: '', phone: '' }
  loadMembers()
}

const formatDate = (date) => {
  if (!date) return ''
  return new Date(date).toLocaleDateString('zh-CN')
}

onMounted(loadMembers)
</script>
