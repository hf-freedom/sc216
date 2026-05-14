<template>
  <div>
    <h2 style="margin-bottom: 20px">数据统计</h2>
    
    <el-row :gutter="20" style="margin-bottom: 30px">
      <el-col :span="6">
        <el-card shadow="hover">
          <div style="text-align: center">
            <div style="font-size: 32px; color: #409EFF; font-weight: bold">{{ totalCourses }}</div>
            <div style="color: #909399; margin-top: 10px">总课程数</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <div style="text-align: center">
            <div style="font-size: 32px; color: #67C23A; font-weight: bold">{{ totalBookings }}</div>
            <div style="color: #909399; margin-top: 10px">总预约数</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <div style="text-align: center">
            <div style="font-size: 32px; color: #E6A23C; font-weight: bold">{{ avgBookingRate }}%</div>
            <div style="color: #909399; margin-top: 10px">平均预约率</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <div style="text-align: center">
            <div style="font-size: 32px; color: #909399; font-weight: bold">{{ avgCheckInRate }}%</div>
            <div style="color: #909399; margin-top: 10px">平均签到率</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <div style="text-align: center">
            <div style="font-size: 32px; color: #F56C6C; font-weight: bold">{{ avgNoShowRate }}%</div>
            <div style="color: #909399; margin-top: 10px">平均爽约率</div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <h3 style="margin: 20px 0">课程统计详情</h3>
    <el-table :data="statistics" border stripe>
      <el-table-column prop="courseName" label="课程名称" width="150" />
      <el-table-column prop="totalBooked" label="预约人数" width="100" />
      <el-table-column prop="totalCheckedIn" label="签到人数" width="100" />
      <el-table-column prop="totalNoShow" label="爽约人数" width="100" />
      <el-table-column prop="bookingRate" label="预约率" width="100">
        <template #default="{ row }">
          {{ row.bookingRate.toFixed(1) }}%
        </template>
      </el-table-column>
      <el-table-column prop="checkInRate" label="签到率" width="100">
        <template #default="{ row }">
          {{ row.checkInRate.toFixed(1) }}%
        </template>
      </el-table-column>
      <el-table-column prop="noShowRate" label="爽约率" width="100">
        <template #default="{ row }">
          {{ row.noShowRate.toFixed(1) }}%
        </template>
      </el-table-column>
    </el-table>

    <h3 style="margin: 30px 0 20px 0">热门课程排行</h3>
    <el-table :data="popularCourses" border stripe>
      <el-table-column label="排名" width="80" type="index" />
      <el-table-column prop="name" label="课程名称" width="200" />
      <el-table-column prop="coach" label="教练" width="120" />
      <el-table-column prop="bookedCount" label="预约人数" width="120" />
      <el-table-column prop="totalCapacity" label="总名额" width="100" />
    </el-table>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { courseApi, bookingApi } from '../api'

const statistics = ref([])
const popularCourses = ref([])
const allBookings = ref([])

const totalCourses = computed(() => statistics.value.length)
const totalBookings = computed(() => statistics.value.reduce((sum, s) => sum + s.totalBooked, 0))
const avgBookingRate = computed(() => {
  if (statistics.value.length === 0) return 0
  return (statistics.value.reduce((sum, s) => sum + s.bookingRate, 0) / statistics.value.length).toFixed(1)
})
const avgCheckInRate = computed(() => {
  if (statistics.value.length === 0) return 0
  return (statistics.value.reduce((sum, s) => sum + s.checkInRate, 0) / statistics.value.length).toFixed(1)
})
const avgNoShowRate = computed(() => {
  if (statistics.value.length === 0) return 0
  return (statistics.value.reduce((sum, s) => sum + s.noShowRate, 0) / statistics.value.length).toFixed(1)
})

const loadStatistics = async () => {
  const res = await courseApi.getStatistics()
  statistics.value = res.data
}

const loadPopularCourses = async () => {
  const res = await courseApi.getPopular()
  popularCourses.value = res.data
}

onMounted(() => {
  loadStatistics()
  loadPopularCourses()
})
</script>
