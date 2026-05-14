import axios from 'axios'

const api = axios.create({
  baseURL: '/api',
  timeout: 10000
})

export const courseApi = {
  getAll: () => api.get('/courses'),
  getById: (id) => api.get(`/courses/${id}`),
  create: (data) => api.post('/courses', data),
  publish: (id) => api.post(`/courses/${id}/publish`),
  cancel: (id) => api.post(`/courses/${id}/cancel`),
  getStatistics: () => api.get('/courses/statistics'),
  getPopular: () => api.get('/courses/popular')
}

export const memberApi = {
  getAll: () => api.get('/members'),
  getById: (id) => api.get(`/members/${id}`),
  create: (data) => api.post('/members', data),
  isBanned: (id) => api.get(`/members/${id}/banned`),
  resetBan: (id) => api.post(`/members/${id}/reset-ban`)
}

export const bookingApi = {
  getAll: () => api.get('/bookings'),
  getById: (id) => api.get(`/bookings/${id}`),
  lock: (data) => api.post('/bookings/lock', data),
  confirm: (id) => api.post(`/bookings/${id}/confirm`),
  cancel: (id) => api.post(`/bookings/${id}/cancel`),
  checkIn: (data) => api.post('/bookings/checkin', data),
  getByMember: (memberId) => api.get(`/bookings/member/${memberId}`),
  getByCourse: (courseId) => api.get(`/bookings/course/${courseId}`)
}

export default api
