<template>
  <div>
    <h2 style="margin-bottom: 20px">预约管理</h2>
    
    <el-table :data="bookings" border stripe>
      <el-table-column label="课程" width="150">
        <template #default="{ row }">
          {{ getCourseName(row.courseId) }}
        </template>
      </el-table-column>
      <el-table-column label="会员" width="120">
        <template #default="{ row }">
          {{ getMemberName(row.memberId) }}
        </template>
      </el-table-column>
      <el-table-column prop="bookingTime" label="预约时间" width="180">
        <template #default="{ row }">
          {{ formatTime(row.bookingTime) }}
        </template>
      </el-table-column>
      <el-table-column prop="checkInTime" label="签到时间" width="180">
        <template #default="{ row }">
          {{ row.checkInTime ? formatTime(row.checkInTime) : '-' }}
        </template>
      </el-table-column>
      <el-table-column prop="status" label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="getStatusType(row.status)" size="small">
            {{ getStatusText(row.status) }}
          </el-tag>
          <el-tag v-if="row.waitlist" type="warning" size="small" style="margin-left: 5px">候补</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="150">
        <template #default="{ row }">
          <el-button size="small" type="success" @click="confirmBooking(row.id)" v-if="row.status === 'LOCKED'">
            确认
          </el-button>
          <el-button size="small" type="danger" @click="cancelBooking(row.id)" v-if="row.status !== 'CANCELLED' && row.status !== 'CHECKED_IN' && row.status !== 'NO_SHOW'">
            取消
          </el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { bookingApi, courseApi, memberApi } from '../api'

const bookings = ref([])
const courses = ref([])
const members = ref([])

const loadBookings = async () => {
  const res = await bookingApi.getAll()
  bookings.value = res.data
}

const loadCourses = async () => {
  const res = await courseApi.getAll()
  courses.value = res.data
}

const loadMembers = async () => {
  const res = await memberApi.getAll()
  members.value = res.data
}

const getCourseName = (courseId) => {
  const course = courses.value.find(c => c.id === courseId)
  return course ? course.name : courseId
}

const getMemberName = (memberId) => {
  const member = members.value.find(m => m.id === memberId)
  return member ? member.name : memberId
}

const confirmBooking = async (id) => {
  await bookingApi.confirm(id)
  ElMessage.success('预约已确认')
  loadBookings()
}

const cancelBooking = async (id) => {
  await bookingApi.cancel(id)
  ElMessage.success('预约已取消')
  loadBookings()
}

const getStatusType = (status) => {
  const map = {
    LOCKED: 'warning',
    CONFIRMED: 'success',
    WAITLIST: 'info',
    CHECKED_IN: 'primary',
    NO_SHOW: 'danger',
    CANCELLED: 'info'
  }
  return map[status] || 'info'
}

const getStatusText = (status) => {
  const map = {
    LOCKED: '锁定中',
    CONFIRMED: '已确认',
    WAITLIST: '候补中',
    CHECKED_IN: '已签到',
    NO_SHOW: '爽约',
    CANCELLED: '已取消'
  }
  return map[status] || status
}

const formatTime = (time) => {
  if (!time) return ''
  return new Date(time).toLocaleString('zh-CN')
}

onMounted(() => {
  loadBookings()
  loadCourses()
  loadMembers()
})
</script>
