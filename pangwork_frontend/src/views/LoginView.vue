<script setup lang="ts">
import { ref } from 'vue'
import { apiClient, getOAuth2LoginUrl } from '../lib/api'

const statusMessage = ref('Ready to connect')
const isChecking = ref(false)

const startLogin = () => {
  window.location.href = getOAuth2LoginUrl()
}

const checkSession = async () => {
  isChecking.value = true
  statusMessage.value = 'Checking session'

  try {
    await apiClient.post('/work/test')
    statusMessage.value = 'Session active: JWT is valid'
  } catch (error) {
    statusMessage.value = 'No session yet. Please login with Naver.'
  } finally {
    isChecking.value = false
  }
}
</script>

<template>
  <section class="hero-shell fade-rise">
    <div class="card grid-two">
      <div>
        <span class="pill">Naver OAuth2</span>
        <h1 class="title">Sign in with Naver</h1>
        <p class="subtitle">
          One tap takes you to Naver. After approval, the backend stores your JWT in an HttpOnly
          cookie and sends you back here.
        </p>
        <div class="cta-row">
          <button class="naver-btn" type="button" @click="startLogin">
            <span class="naver-icon">N</span>
            Continue with Naver
          </button>
          <button
            class="btn btn-secondary"
            type="button"
            @click="checkSession"
            :disabled="isChecking"
          >
            {{ isChecking ? 'Checking...' : 'Check Session' }}
          </button>
        </div>
        <div class="footer-note">{{ statusMessage }}</div>
      </div>
      <div class="naver-card">
        <div class="naver-pill">Tips</div>
        <div class="panel-list">
          <div>Keep cookies enabled for HttpOnly JWT storage.</div>
          <div>Use the same domain or configure CORS credentials.</div>
          <div>Login endpoint: /oauth2/authorization/naver</div>
        </div>
      </div>
    </div>
  </section>
</template>
