<template>
  <div>
    <h2 style="margin-bottom: 20px">课程列表</h2>
    
    <el-table :data="courses" border stripe>
      <el-table-column prop="name" label="课程名称" width="150" />
      <el-table-column prop="coach" label="教练" width="120" />
      <el-table-column prop="startTime" label="开始时间" width="180">
        <template #default="{ row }">
          {{ formatTime(row.startTime) }}
        </template>
      </el-table-column>
      <el-table-column prop="endTime" label="结束时间" width="180">
        <template #default="{ row }">
          {{ formatTime(row.endTime) }}
        </template>
      </el-table-column>
      <el-table-column prop="totalCapacity" label="总名额" width="80" />
      <el-table-column prop="bookedCount" label="已预约" width="80" />
      <el-table-column prop="waitlistCount" label="候补人数" width="80" />
      <el-table-column prop="status" label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="getStatusType(row.status)">
            {{ getStatusText(row.status) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="400">
        <template #default="{ row }">
          <el-button size="small" v-if="row.status === 'DRAFT'" @click="publishCourse(row.id)">
            发布
          </el-button>
          <el-button size="small" type="primary" @click="openBookingDialog(row)" v-if="canBook(row)">
            预约
          </el-button>
          <el-button size="small" type="success" @click="openCheckinDialog(row)" v-if="row.status === 'IN_PROGRESS'">
            签到
          </el-button>
          <el-button size="small" type="danger" @click="cancelCourse(row.id)" v-if="row.status !== 'COMPLETED' && row.status !== 'CANCELLED'">
            取消课程
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="bookingDialogVisible" title="预约课程" width="400">
      <el-form :model="bookingForm" label-width="80px">
        <el-form-item label="选择会员">
          <el-select v-model="bookingForm.memberId" placeholder="请选择会员">
            <el-option v-for="member in members" :key="member.id" :label="member.name" :value="member.id" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="bookingDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="confirmBooking">确认预约</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="checkinDialogVisible" title="会员签到" width="400">
      <el-form :model="checkinForm" label-width="80px">
        <el-form-item label="选择会员">
          <el-select v-model="checkinForm.memberId" placeholder="请选择会员">
            <el-option v-for="member in bookedMembers" :key="member.id" :label="member.name" :value="member.id" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="checkinDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="confirmCheckin">确认签到</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { courseApi, memberApi, bookingApi } from '../api'

const courses = ref([])
const members = ref([])
const bookedMembers = ref([])
const bookingDialogVisible = ref(false)
const checkinDialogVisible = ref(false)
const currentCourse = ref(null)
const bookingForm = ref({ memberId: '' })
const checkinForm = ref({ memberId: '' })

const loadCourses = async () => {
  const res = await courseApi.getAll()
  courses.value = res.data
}

const loadMembers = async () => {
  const res = await memberApi.getAll()
  members.value = res.data
}

const publishCourse = async (id) => {
  await courseApi.publish(id)
  ElMessage.success('课程发布成功')
  loadCourses()
}

const cancelCourse = async (id) => {
  await courseApi.cancel(id)
  ElMessage.success('课程已取消，名额已释放')
  loadCourses()
}

const openBookingDialog = (course) => {
  currentCourse.value = course
  bookingForm.value.memberId = ''
  bookingDialogVisible.value = true
}

const openCheckinDialog = async (course) => {
  currentCourse.value = course
  const res = await bookingApi.getByCourse(course.id)
  const confirmedBookings = res.data.filter(b => b.status === 'CONFIRMED')
  bookedMembers.value = members.value.filter(m => confirmedBookings.some(b => b.memberId === m.id))
  checkinForm.value.memberId = ''
  checkinDialogVisible.value = true
}

const confirmBooking = async () => {
  if (!bookingForm.value.memberId) {
    ElMessage.warning('请选择会员')
    return
  }
  const res = await bookingApi.lock({
    courseId: currentCourse.value.id,
    memberId: bookingForm.value.memberId
  })
  await bookingApi.confirm(res.data.id)
  ElMessage.success('预约成功')
  bookingDialogVisible.value = false
  loadCourses()
}

const confirmCheckin = async () => {
  if (!checkinForm.value.memberId) {
    ElMessage.warning('请选择会员')
    return
  }
  await bookingApi.checkIn({
    courseId: currentCourse.value.id,
    memberId: checkinForm.value.memberId
  })
  ElMessage.success('签到成功')
  checkinDialogVisible.value = false
  loadCourses()
}

const canBook = (course) => {
  return course.status === 'PUBLISHED' || course.status === 'FULL'
}

const getStatusType = (status) => {
  const map = {
    DRAFT: 'info',
    PUBLISHED: 'success',
    FULL: 'warning',
    IN_PROGRESS: 'primary',
    COMPLETED: 'info',
    CANCELLED: 'danger'
  }
  return map[status] || 'info'
}

const getStatusText = (status) => {
  const map = {
    DRAFT: '草稿',
    PUBLISHED: '已发布',
    FULL: '已满员',
    IN_PROGRESS: '进行中',
    COMPLETED: '已完成',
    CANCELLED: '已取消'
  }
  return map[status] || status
}

const formatTime = (time) => {
  if (!time) return ''
  return new Date(time).toLocaleString('zh-CN')
}

onMounted(() => {
  loadCourses()
  loadMembers()
})
</script>
