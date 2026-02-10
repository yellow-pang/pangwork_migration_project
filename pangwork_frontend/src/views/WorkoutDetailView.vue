<script setup lang="ts">
import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'

const route = useRoute()
const router = useRouter()

const workId = computed(() => String(route.params.workId || ''))
</script>

<template>
  <section class="hero-shell fade-rise">
    <div class="card">
      <span class="pill">Workout Detail</span>
      <h1 class="title">운동 상세</h1>
      <p class="subtitle">선택한 운동의 상세 기록을 준비 중입니다.</p>

      <div class="status-chip" style="margin-top: 20px">선택된 운동 ID: {{ workId }}</div>

      <div class="cta-row">
        <button class="btn btn-secondary" type="button" @click="router.push('/workouts')">
          목록으로
        </button>
      </div>
    </div>
  </section>
</template>
