<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import { apiClient } from '../lib/api'

type WorkoutItem = {
  workId: number
  workName: string
  userId: string
  createDate?: string
  editDate?: string
}

const router = useRouter()
const isLoading = ref(true)
const errorMessage = ref('')
const workouts = ref<WorkoutItem[]>([])

const loadWorkouts = async () => {
  isLoading.value = true
  errorMessage.value = ''

  try {
    const response = await apiClient.post('/work/works', {})
    const data = response.data
    workouts.value = (data?.list || data?.workouts || []) as WorkoutItem[]
  } catch (error) {
    errorMessage.value = '로그인이 필요합니다. 다시 로그인해주세요.'
    router.push('/login')
  } finally {
    isLoading.value = false
  }
}

onMounted(() => {
  loadWorkouts()
})
</script>

<template>
  <section class="hero-shell fade-rise">
    <div class="card">
      <span class="pill">Workout Hub</span>
      <h1 class="title">오늘의 운동 기록</h1>
      <p class="subtitle">운동 목록을 선택하고, 기록된 세트를 한눈에 확인하세요.</p>

      <div v-if="isLoading" class="status-chip" style="margin-top: 20px">불러오는 중...</div>

      <div v-else-if="errorMessage" class="status-chip" style="margin-top: 20px">
        {{ errorMessage }}
      </div>

      <div v-else class="workout-grid">
        <button v-for="item in workouts" :key="item.workId" class="workout-card" type="button">
          <div class="workout-title">{{ item.workName }}</div>
          <div class="workout-meta">ID: {{ item.workId }}</div>
        </button>

        <div v-if="workouts.length === 0" class="workout-empty">
          등록된 운동이 없습니다. 첫 운동을 추가해보세요.
        </div>
      </div>
    </div>
  </section>
</template>
