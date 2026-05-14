<template>
  <el-container style="height: 100vh">
    <el-header style="background: #409EFF; color: white; display: flex; align-items: center; font-size: 20px; font-weight: bold">
      健身房课程预约系统
    </el-header>
    <el-container>
      <el-aside width="200px" style="background: #f5f7fa">
        <el-menu
          :default-active="activeMenu"
          router
          background-color="#f5f7fa"
          text-color="#333"
          active-text-color="#409EFF"
        >
          <el-menu-item index="/">
            <el-icon><Calendar /></el-icon>
            <span>课程列表</span>
          </el-menu-item>
          <el-menu-item index="/create-course">
            <el-icon><Plus /></el-icon>
            <span>发布课程</span>
          </el-menu-item>
          <el-menu-item index="/members">
            <el-icon><User /></el-icon>
            <span>会员管理</span>
          </el-menu-item>
          <el-menu-item index="/bookings">
            <el-icon><Tickets /></el-icon>
            <span>预约管理</span>
          </el-menu-item>
          <el-menu-item index="/statistics">
            <el-icon><DataAnalysis /></el-icon>
            <span>数据统计</span>
          </el-menu-item>
        </el-menu>
      </el-aside>
      <el-main>
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute } from 'vue-router'
import { Calendar, Plus, User, Tickets, DataAnalysis } from '@element-plus/icons-vue'

const route = useRoute()
const activeMenu = computed(() => route.path)
</script>

<style>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

.el-header {
  padding: 0 20px !important;
}
</style>
