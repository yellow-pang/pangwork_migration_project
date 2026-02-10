<script setup lang="ts">
export type WorkoutCardItem = {
  workId: number
  workName: string
}

type Props = {
  items: WorkoutCardItem[]
  emptyMessage?: string
  showDelete?: boolean
  deletingId?: number | null
}

defineProps<Props>()

defineEmits<{
  (event: 'select', item: WorkoutCardItem): void
  (event: 'delete', item: WorkoutCardItem): void
}>()
</script>

<template>
  <div class="workout-grid">
    <div
      v-for="item in items"
      :key="item.workId"
      class="workout-card"
      role="button"
      tabindex="0"
      @click="$emit('select', item)"
      @keydown.enter="$emit('select', item)"
      @keydown.space.prevent="$emit('select', item)"
    >
      <div class="workout-card-header">
        <div class="workout-title">{{ item.workName }}</div>
        <button
          v-if="showDelete !== false"
          class="workout-card-delete"
          type="button"
          :disabled="deletingId === item.workId"
          @click.stop="$emit('delete', item)"
        >
          삭제
        </button>
      </div>
      <div class="workout-meta">ID: {{ item.workId }}</div>
    </div>

    <div v-if="items.length === 0" class="workout-empty">
      {{ emptyMessage || '등록된 운동이 없습니다. 첫 운동을 추가해보세요.' }}
    </div>
  </div>
</template>
