<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import WorkoutCardList from '../components/WorkoutCardList.vue'
import type { WorkoutCardItem } from '../components/WorkoutCardList.vue'
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
const toastMessage = ref('')
const toastProgress = ref(0)
const toastType = ref<'success' | 'error'>('success')
const toastOffsetX = ref(0)
const isToastDragging = ref(false)
let toastStartX = 0
let toastTimer: number | undefined
let toastProgressTimer: number | undefined

const handleSelectWorkout = (item: WorkoutCardItem) => {
  router.push(`/workouts/${item.workId}`)
}

const showToast = (message: string, type: 'success' | 'error') => {
  toastMessage.value = message
  toastType.value = type
  toastProgress.value = 100

  if (toastTimer) {
    window.clearTimeout(toastTimer)
  }

  if (toastProgressTimer) {
    window.clearTimeout(toastProgressTimer)
  }

  toastProgressTimer = window.setTimeout(() => {
    toastProgress.value = 0
  }, 50)

  toastTimer = window.setTimeout(() => {
    dismissToast()
  }, 3200)
}

const dismissToast = () => {
  if (toastTimer) {
    window.clearTimeout(toastTimer)
  }

  if (toastProgressTimer) {
    window.clearTimeout(toastProgressTimer)
  }

  toastMessage.value = ''
  toastProgress.value = 0
  toastOffsetX.value = 0
  isToastDragging.value = false
}

const handleToastPointerDown = (event: PointerEvent) => {
  if (!toastMessage.value) return
  isToastDragging.value = true
  toastStartX = event.clientX
}

const handleToastPointerMove = (event: PointerEvent) => {
  if (!isToastDragging.value) return
  toastOffsetX.value = event.clientX - toastStartX
}

const handleToastPointerUp = () => {
  if (!isToastDragging.value) return
  const distance = Math.abs(toastOffsetX.value)
  isToastDragging.value = false

  if (distance > 80) {
    dismissToast()
    return
  }

  toastOffsetX.value = 0
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
    const response = await apiClient.post('/work/addList', {
      workName: trimmedName,
    })
    const data = response.data
    if (data?.status === '200') {
      showToast(data?.message || '운동이 추가되었습니다.', 'success')
      newWorkoutName.value = ''
      await loadWorkouts()
    } else {
      const message = data?.error || '운동 추가에 실패했습니다. 잠시 후 다시 시도해주세요.'
      errorMessage.value = message
      showToast(message, 'error')
    }
  } catch (error) {
    errorMessage.value = '운동 추가에 실패했습니다. 잠시 후 다시 시도해주세요.'
    showToast(errorMessage.value, 'error')
  } finally {
    isSubmitting.value = false
  }
}

const deleteWorkout = async (item: WorkoutCardItem) => {
  if (isSubmitting.value) return
  const confirmed = window.confirm('정말 삭제할까요? 삭제된 운동은 복구할 수 없습니다.')
  if (!confirmed) return

  isSubmitting.value = true
  deletingId.value = item.workId
  errorMessage.value = ''

  try {
    const response = await apiClient.post('/work/deleteList', {
      workId: item.workId,
    })
    const data = response.data
    if (data?.status === '200') {
      const detailCount = data?.detailDeleteCount ?? 0
      showToast(`운동이 삭제되었습니다. (상세 ${detailCount}건)`, 'success')
      await loadWorkouts()
    } else {
      const message = data?.error || '운동 삭제에 실패했습니다. 잠시 후 다시 시도해주세요.'
      errorMessage.value = message
      showToast(message, 'error')
    }
  } catch (error) {
    errorMessage.value = '운동 삭제에 실패했습니다. 잠시 후 다시 시도해주세요.'
    showToast(errorMessage.value, 'error')
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
    showToast(errorMessage.value, 'error')
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

      <WorkoutCardList
        v-else
        :items="workouts"
        :deleting-id="deletingId"
        @select="handleSelectWorkout"
        @delete="deleteWorkout"
      />

      <div
        v-if="toastMessage"
        class="toast"
        :class="toastType"
        role="button"
        tabindex="0"
        @click="dismissToast"
        @keydown.enter="dismissToast"
        @keydown.space.prevent="dismissToast"
        @pointerdown="handleToastPointerDown"
        @pointermove="handleToastPointerMove"
        @pointerup="handleToastPointerUp"
        @pointercancel="handleToastPointerUp"
        :style="{
          transform: `translateX(${toastOffsetX}px)`,
        }"
        :data-dragging="isToastDragging"
      >
        <span class="toast-icon" aria-hidden="true"></span>
        <span class="toast-text">{{ toastMessage }}</span>
        <span class="toast-progress" :style="{ width: `${toastProgress}%` }"></span>
      </div>
    </div>
  </section>
</template>
