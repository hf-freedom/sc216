import { createApp } from 'vue'
import { createRouter, createWebHistory } from 'vue-router'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import App from './App.vue'
import CourseList from './components/CourseList.vue'
import CreateCourse from './components/CreateCourse.vue'
import MemberList from './components/MemberList.vue'
import Statistics from './components/Statistics.vue'
import BookingList from './components/BookingList.vue'

const routes = [
  { path: '/', component: CourseList },
  { path: '/create-course', component: CreateCourse },
  { path: '/members', component: MemberList },
  { path: '/statistics', component: Statistics },
  { path: '/bookings', component: BookingList }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

const app = createApp(App)
app.use(router)
app.use(ElementPlus)
app.mount('#app')
