<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import { apiClient, getOAuth2LoginUrl } from '../lib/api'

const router = useRouter()
const statusLabel = ref('Checking session')
const statusTone = ref<'ok' | 'fail' | 'idle'>('idle')
const apiBaseUrl = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080'

const checkSession = async () => {
  try {
    await apiClient.post('/work/test')
    statusLabel.value = 'Logged in'
    statusTone.value = 'ok'
  } catch (error) {
    statusLabel.value = 'Not logged in'
    statusTone.value = 'fail'
  }
}

const startLogin = () => {
  window.location.href = getOAuth2LoginUrl()
}

const goLogin = () => {
  router.push('/login')
}

onMounted(() => {
  checkSession()
})
</script>

<template>
  <section class="hero-shell fade-rise">
    <div class="card grid-two">
      <div>
        <span class="pill">Social Login</span>
        <h1 class="title">Welcome to Pangwork</h1>
        <p class="subtitle">
          Secure OAuth2 login with Naver and a JWT session. Jump in and keep your workouts synced.
        </p>
        <div class="cta-row">
          <button class="btn btn-primary" type="button" @click="startLogin">
            Start Naver Login
          </button>
          <button class="btn btn-secondary" type="button" @click="goLogin">
            View Login Screen
          </button>
        </div>
        <div class="footer-note">API base: {{ apiBaseUrl }}</div>
      </div>
      <div class="naver-card">
        <div class="naver-pill">Session status</div>
        <div class="status-chip">
          <span
            class="status-dot"
            :class="{ ok: statusTone === 'ok', fail: statusTone === 'fail' }"
          ></span>
          <span>{{ statusLabel }}</span>
        </div>
        <div>
          <div class="panel-title">How it works</div>
          <div class="panel-list">
            <div>1. OAuth2 redirect to Naver</div>
            <div>2. Backend issues JWT cookie</div>
            <div>3. Vue app calls secured APIs via axios</div>
          </div>
        </div>
      </div>
    </div>
  </section>
</template>
