<script setup lang="ts">
export type WorkoutCardItem = {
  workId: number
  workName: string
}

type Props = {
  items: WorkoutCardItem[]
  emptyMessage?: string
}

defineProps<Props>()

defineEmits<{
  (event: 'select', item: WorkoutCardItem): void
}>()
</script>

<template>
  <div class="workout-grid">
    <button
      v-for="item in items"
      :key="item.workId"
      class="workout-card"
      type="button"
      @click="$emit('select', item)"
    >
      <div class="workout-title">{{ item.workName }}</div>
      <div class="workout-meta">ID: {{ item.workId }}</div>
    </button>

    <div v-if="items.length === 0" class="workout-empty">
      {{ emptyMessage || '등록된 운동이 없습니다. 첫 운동을 추가해보세요.' }}
    </div>
  </div>
</template>
