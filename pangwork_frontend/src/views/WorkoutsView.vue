<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import WorkoutCardList from '../components/WorkoutCardList.vue'
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
const newWorkoutName = ref('')
const isSubmitting = ref(false)
const deletingId = ref<number | null>(null)

const handleSelectWorkout = (item: WorkoutItem) => {
  router.push(`/workouts/${item.workId}`)
}

const addWorkout = async () => {
  if (isSubmitting.value) return

  const trimmedName = newWorkoutName.value.trim()
  if (!trimmedName) {
    errorMessage.value = '운동 이름을 입력해주세요.'
    return
  }

  isSubmitting.value = true
  errorMessage.value = ''

  try {
    await apiClient.post('/work/addList', {
      workName: trimmedName,
    })
    newWorkoutName.value = ''
    await loadWorkouts()
  } catch (error) {
    errorMessage.value = '운동 추가에 실패했습니다. 잠시 후 다시 시도해주세요.'
  } finally {
    isSubmitting.value = false
  }
}

const deleteWorkout = async (item: WorkoutItem) => {
  if (isSubmitting.value) return
  const confirmed = window.confirm('정말 삭제할까요? 삭제된 운동은 복구할 수 없습니다.')
  if (!confirmed) return

  isSubmitting.value = true
  deletingId.value = item.workId
  errorMessage.value = ''

  try {
    await apiClient.post('/work/deleteList', {
      workId: item.workId,
    })
    await loadWorkouts()
  } catch (error) {
    errorMessage.value = '운동 삭제에 실패했습니다. 잠시 후 다시 시도해주세요.'
  } finally {
    isSubmitting.value = false
    deletingId.value = null
  }
}

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

      <div class="workout-actions" style="margin-top: 20px">
        <input
          v-model="newWorkoutName"
          class="workout-input"
          type="text"
          placeholder="새 운동 이름"
          :disabled="isLoading || isSubmitting"
          @keydown.enter.prevent="addWorkout"
        />
        <button
          class="btn btn-primary"
          type="button"
          :disabled="isLoading || isSubmitting"
          @click="addWorkout"
        >
          운동 추가
        </button>
      </div>

      <div v-if="isLoading" class="status-chip" style="margin-top: 20px">불러오는 중...</div>

      <div v-else-if="errorMessage" class="status-chip" style="margin-top: 20px">
        {{ errorMessage }}
      </div>

      <WorkoutCardList
        v-else
        :items="workouts"
        :deleting-id="deletingId"
        @select="handleSelectWorkout"
        @delete="deleteWorkout"
      />
    </div>
  </section>
</template>
